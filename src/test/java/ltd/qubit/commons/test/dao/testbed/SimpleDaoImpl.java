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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.error.FieldTooLongException;
import ltd.qubit.commons.error.NullFieldException;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.CloneableEx;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.test.model.Identifiable;
import ltd.qubit.commons.test.model.WithInfo;
import ltd.qubit.commons.util.clock.MockClock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import static ltd.qubit.commons.test.dao.DaoTestUtils.getColumnName;

public class SimpleDaoImpl<T extends Identifiable & WithInfo>
    implements GettableDao<T>, AddableDao<T>, UpdatableDao<T>, ErasableDao<T> {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  protected final AtomicLong idGenerator = new AtomicLong(1);
  protected final MockClock clock = new MockClock();
  protected final Map<Long, T> idMap = new ConcurrentHashMap<>();
  protected final BeanInfo modelInfo;

  protected SimpleDaoImpl(final Class<T> entityType) {
    modelInfo = BeanInfo.of(entityType);
  }

  @Override
  public boolean exist(final Long id) throws DataAccessException {
    logger.debug("Test the existence of a {} by ID: {}", modelInfo.getName(), id);
    return idMap.containsKey(id);
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
  public Instant add(final T entity) throws DataAccessException {
    logger.debug("Add a {}: {}", modelInfo.getName(), entity);
    return addImpl(entity);
  }

  @SuppressWarnings("unchecked")
  protected Instant addImpl(final T entity) {
    final Instant createTime = clock.now();
    entity.setId(idGenerator.incrementAndGet());
    validateBeforeAdding(entity);
    final T copy = ((CloneableEx<T>) entity).clone();
    idMap.put(entity.getId(), copy);
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
  }

  @Override
  public Instant update(final T entity) throws DataAccessException {
    logger.debug("Update a {} by ID: {}", modelInfo.getName(), entity);
    final T existing = idMap.get(entity.getId());
    if (existing == null) {
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
    ((Assignable<T>) existing).assign(entity);
    return modifyTime;
  }

  protected void validateBeforeUpdating(final T existing, final T entity) {
    for (final Property property : modelInfo.getProperties()) {
      final String propName = property.getName();
      if ("id".equals(propName)) {
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
  }

  @Override
  public void erase(final Long id) throws DataAccessException {
    final T entity = idMap.get(id);
    if (entity == null) {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
    logger.debug("Erase a {}: {}", modelInfo.getName(), id);
    idMap.remove(entity.getId());
  }
}
