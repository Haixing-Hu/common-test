////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.error.DuplicateKeyException;
import ltd.qubit.commons.error.FieldTooLongException;
import ltd.qubit.commons.error.NullFieldException;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.CloneableEx;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.sql.Criterion;
import ltd.qubit.commons.sql.SortRequest;
import ltd.qubit.commons.test.model.Creatable;
import ltd.qubit.commons.test.model.Deletable;
import ltd.qubit.commons.test.model.Info;
import ltd.qubit.commons.test.model.Modifiable;
import ltd.qubit.commons.test.model.WithInfo;
import ltd.qubit.commons.util.clock.MockClock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import static ltd.qubit.commons.test.dao.DaoTestUtils.fixMySqlValueLength;
import static ltd.qubit.commons.test.dao.DaoTestUtils.getColumnName;

public abstract class DaoImplBase<T extends WithInfo & Creatable & Modifiable & Deletable>
    implements ListableDao<T>, GettableWithInfoDao<T>,
    AddableDao<T>, UpdatableDao<T>, UpdatableWithCodeDao<T>, UpdatableWithNameDao<T>,
    AddableUpdatableWithCodeDao<T>, AddableUpdatableWithNameDao<T>, DeletableDao<T>,
    ErasableDao<T> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  protected final AtomicLong idGenerator = new AtomicLong(1);
  protected final MockClock clock = new MockClock();
  protected final Map<Long, T> idMap = new ConcurrentHashMap<>();
  protected final Map<String, T> codeMap = new ConcurrentHashMap<>();
  protected final Map<String, T> nameMap = new ConcurrentHashMap<>();
  protected final BeanInfo modelInfo;

  DaoImplBase(final Class<T> entityType) {
    modelInfo = BeanInfo.of(entityType);
  }

  protected abstract String makeNameKey(T entity);

  protected abstract String makeDatabaseNameKey(T entity);

  @Override
  public long count(@Nullable final Criterion<T> filter)
      throws DataAccessException {
    logger.debug("Count the number of specified {}: filter = {}",
        modelInfo.getName(), filter);
    if (filter == null) {
      return idMap.size();
    } else {
      return idMap.values().stream().filter(filter::accept).count();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<T> list(@Nullable final Criterion<T> filter,
      @Nullable final SortRequest<T> sortRequest, @Nullable final Integer limit,
      @Nullable final Long offset) throws DataAccessException {
    logger.debug("List the specified {}: limit = {}, offset = {}, "
        + "filter = {}, sort = {}", modelInfo.getName(), limit, offset, filter,
        sortRequest);
    final List<T> result;
    if (filter == null) {
      if (sortRequest == null) {
        result = new ArrayList<>(idMap.values());
      } else {
        result = idMap.values()
            .stream()
            .sorted(sortRequest.getComparator())
            .map((e) -> ((CloneableEx<T>) e).clone())
            .collect(Collectors.toList());
      }
    } else if (sortRequest == null) {
      result = idMap.values()
          .stream()
          .filter(filter::accept)
          .map((e) -> ((CloneableEx<T>) e).clone())
          .collect(Collectors.toList());
    } else {
      result = idMap.values()
          .stream()
          .filter(filter::accept)
          .sorted(sortRequest.getComparator())
          .map((e) -> ((CloneableEx<T>) e).clone())
          .collect(Collectors.toList());
    }
    final int fromIndex = (offset == null ? 0 : offset.intValue());
    if (fromIndex >= result.size()) {
      return Collections.emptyList();
    } else {
      final int count = (limit == null ? result.size() : limit);
      final int toIndex = Math.min(result.size(), fromIndex  + count);
      return result.subList(fromIndex, toIndex);
    }
  }

  @Override
  public boolean exist(final Long id) throws DataAccessException {
    logger.debug("Test the existence of a {} by ID: {}", modelInfo.getName(), id);
    return idMap.containsKey(id);
  }

  @Override
  public boolean existNonDeleted(final Long id) throws DataAccessException {
    logger.debug("Test the existence of a non-deleted {} by ID: {}",
        modelInfo.getName(), id);
    final T obj = idMap.get(id);
    return (obj != null) && (! obj.isDeleted());
  }

  @SuppressWarnings("unchecked")
  @Override
  public T get(final Long id) throws DataAccessException {
    final T entity = idMap.get(id);
    if (entity != null) {
      logger.debug("Get a {} by ID {}: {}", modelInfo.getName(), id, entity);
      return ((CloneableEx<T>) entity).clone();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
  }

  @Override
  public boolean existCode(final String code) throws DataAccessException {
    logger.debug("Test the existence of a {} by code: {}",
        modelInfo.getName(), code);
    return codeMap.containsKey(code);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T getByCode(final String code) throws DataAccessException {
    final T entity = codeMap.get(code);
    if (entity != null) {
      logger.debug("Get a {} by code {}: {}",
          modelInfo.getName(), code, entity);
      return ((CloneableEx<T>) entity).clone();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "code", code);
    }
  }

  @Override
  public Info getInfo(final Long id) throws DataAccessException {
    final T entity = idMap.get(id);
    if (entity != null) {
      logger.debug("Get the info of a {} by ID {}: {}",
          modelInfo.getName(), id, entity);
      return entity.getInfo();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
  }

  @Override
  public Info getInfoByCode(final String code)
      throws DataAccessException {
    final T entity = codeMap.get(code);
    if (entity != null) {
      logger.debug("Get the info of a {} by code {}: {}",
          modelInfo.getName(), code, entity);
      return entity.getInfo();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "code",
          code);
    }
  }

  @Override
  public synchronized Instant add(final T entity)
      throws DataAccessException {
    logger.debug("Add a {}: {}", modelInfo.getName(), entity);
    return addImpl(entity);
  }

  protected Instant addImpl(final T entity) {
    final Instant createTime = clock.now();
    entity.setId(idGenerator.incrementAndGet());
    entity.setCreateTime(createTime);
    entity.setModifyTime(null);
    entity.setDeleteTime(null);
    validateBeforeAdding(entity);
    @SuppressWarnings("unchecked")
    final T copy = ((CloneableEx<T>) entity).clone();
    idMap.put(entity.getId(), copy);
    codeMap.put(entity.getCode(), copy);
    nameMap.put(makeNameKey(entity), copy);
    return createTime;
  }

  protected void validateBeforeAdding(final T entity) {
    for (final Property property : modelInfo.getProperties()) {
      if (! property.isNullable()) {
        if (property.getValue(entity) == null) {
          throw new NullFieldException(getColumnName(property));
        }
      }
      if ((property.getSizeRange() != null)
          && (property.getSizeRange().getMax() != null)
          && (property.getType() == String.class)) {
        final int maxSize =  property.getSizeRange().getMax();
        final String value = (String) property.getValue(entity);
        if (value != null && value.length() > maxSize) {
          throw new FieldTooLongException(getColumnName(property));
        }
      }
    }
    if (codeMap.containsKey(entity.getCode())) {
      throw new DuplicateKeyException("code", fixMySqlValueLength(entity.getCode()));
    } else if (nameMap.containsKey(makeNameKey(entity))) {
      final String key = makeDatabaseNameKey(entity);
      throw new DuplicateKeyException("name", fixMySqlValueLength(key));
    }
  }

  @Override
  public synchronized Instant update(final T entity)
      throws DataAccessException {
    logger.debug("Update a {} by ID: {}", modelInfo.getName(), entity);
    final T existing = idMap.get(entity.getId());
    if (existing == null || existing.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "id", entity.getId());
    }
    return updateImpl(existing, entity);
  }

  @SuppressWarnings("unchecked")
  protected Instant updateImpl(final T existing, final T entity) {
    validateBeforeUpdating(existing, entity);
    final Instant modifyTime = clock.now();
    entity.setId(existing.getId());
    entity.setCode(existing.getCode());
    entity.setCreateTime(existing.getCreateTime());
    entity.setModifyTime(modifyTime);
    entity.setDeleteTime(existing.getDeleteTime());
    ((Assignable<T>) existing).assign(entity);
    return modifyTime;
  }

  protected void validateBeforeUpdating(final T existing, final T entity) {
    for (final Property property : modelInfo.getProperties()) {
      final String propName = property.getName();
      if ("id".equals(propName)
          || "createTime".equals(propName)
          || "deleteTime".equals(propName)
          || "deleted".equals(propName)) {
        continue;
      }
      if (! property.isNullable()) {
        if (property.getValue(entity) == null) {
          throw new NullFieldException(getColumnName(property));
        }
      }
      if ((property.getSizeRange() != null)
          && (property.getSizeRange().getMax() != null)
          && (property.getType() == String.class)) {
        final int maxSize =  property.getSizeRange().getMax();
        final String value = (String) property.getValue(entity);
        if (value != null && value.length() > maxSize) {
          throw new FieldTooLongException(getColumnName(property));
        }
      }
    }
    final String existingNameKey = makeNameKey(existing);
    final String newNameKey = makeNameKey(entity);
    if (!Equality.equals(existingNameKey, newNameKey)
        && nameMap.containsKey(newNameKey)) {
      final String key = makeDatabaseNameKey(entity);
      throw new DuplicateKeyException("name", fixMySqlValueLength(key));
    }
  }

  @Override
  public synchronized Instant updateByCode(final T entity)
      throws DataAccessException {
    logger.debug("Update a {} by its code: {}", modelInfo.getName(), entity);
    final T existing = codeMap.get(entity.getCode());
    if (existing == null || existing.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "code", entity.getCode());
    }
    return updateImpl(existing, entity);
  }

  @Override
  public synchronized Instant updateByName(final T entity)
      throws DataAccessException {
    logger.debug("Update a {} by its name: {}", modelInfo.getName(), entity);
    final String nameKey = makeNameKey(entity);
    final T existing = nameMap.get(nameKey);
    if (existing == null || existing.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "name", entity.getName());
    }
    return updateImpl(existing, entity);
  }

  @Override
  public synchronized Instant addOrUpdateByCode(final T entity)
      throws DataAccessException {
    logger.debug("Add or update a {} by code: {}", modelInfo.getName(), entity);
    final T existing = codeMap.get(entity.getCode());
    if (existing == null) {
      return addImpl(entity);
    } else if (existing.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "code", entity.getCode());
    } else {
      return updateImpl(existing, entity);
    }
  }

  @Override
  public synchronized Instant addOrUpdateByName(final T entity)
      throws DataAccessException {
    logger.debug("Add or update a {} by name: {}", modelInfo.getName(), entity);
    final T existing = nameMap.get(makeNameKey(entity));
    if (existing == null) {
      return addImpl(entity);
    } else if (existing.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "name",
          entity.getName());
    } else {
      return updateImpl(existing, entity);
    }
  }

  @Override
  public Instant delete(final Long id) throws DataAccessException {
    final T entity = idMap.get(id);
    if (entity == null || entity.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
    logger.debug("Deleted a {}: {}", modelInfo.getName(), id);
    final Instant deleteTime = clock.now();
    entity.setDeleteTime(deleteTime);
    return deleteTime;
  }

  @Override
  public void restore(final Long id) throws DataAccessException {
    final T entity = idMap.get(id);
    if (entity == null || entity.getDeleteTime() == null) {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
    logger.debug("Restore a deleted {}: {}", modelInfo.getName(), id);
    entity.setDeleteTime(null);
  }

  @Override
  public void purge(final Long id) throws DataAccessException {
    final T entity = idMap.get(id);
    if (entity == null || entity.getDeleteTime() == null) {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
    logger.debug("Purge a deleted {}: {}", modelInfo.getName(), id);
    idMap.remove(entity.getId());
    codeMap.remove(entity.getCode());
    nameMap.remove(makeNameKey(entity));
  }

  @Override
  public long purgeAll() throws DataAccessException {
    final List<Long> deletedIds = new ArrayList<>();
    for (final T entity : idMap.values()) {
      if (entity.getDeleteTime() != null) {
        deletedIds.add(entity.getId());
        idMap.remove(entity.getId());
        codeMap.remove(entity.getCode());
        nameMap.remove(makeNameKey(entity));
      }
    }
    logger.debug("Purge all {} deleted {}.", modelInfo.getName(),
        deletedIds.size());
    return deletedIds.size();
  }

  @Override
  public void erase(final Long id) throws DataAccessException {
    final T entity = idMap.get(id);
    if (entity == null) {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
    logger.debug("Erase a {}: {}", modelInfo.getName(), id);
    idMap.remove(entity.getId());
    codeMap.remove(entity.getCode());
    nameMap.remove(makeNameKey(entity));
  }

  @Override
  public long clear() throws DataAccessException {
    final long size = idMap.size();
    logger.debug("Clear {} {}.", size, modelInfo.getName());
    idMap.clear();
    codeMap.clear();
    nameMap.clear();
    return size;
  }
}
