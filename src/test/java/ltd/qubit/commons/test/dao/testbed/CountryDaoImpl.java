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

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.test.model.Country;

import org.springframework.dao.DataAccessException;

public class CountryDaoImpl extends DaoImplBase<Country> implements CountryDao {

  public CountryDaoImpl() {
    super(Country.class);
  }

  @Override
  protected String makeNameKey(final Country entity) {
    return entity.getName();
  }

  @Override
  protected String makeDatabaseNameKey(final Country entity) {
    return entity.getName();
  }

  @Override
  public boolean existNonDeletedCode(final String code)
      throws DataAccessException {
    logger.debug("Test the existence of a non-deleted {} by code: {}",
        modelInfo.getName(), code);
    final Country obj = codeMap.get(code);
    return (obj != null) && (! obj.isDeleted());
  }

  @Override
  public boolean existName(final String name) throws DataAccessException {
    logger.debug("Test the existence of a {} by name: {}", modelInfo.getName(), name);
    return nameMap.containsKey(name);
  }

  @Override
  public Country getByName(final String name) throws DataAccessException {
    final Country entity = nameMap.get(name);
    if (entity != null) {
      logger.debug("Get a {} by name {}: {}", modelInfo.getName(), name, entity);
      return entity.clone();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "name", name);
    }
  }

  @Override
  public synchronized Instant updateName(final Long id, final String newName)
      throws DataAccessException {
    logger.debug("Update a country's name by ID: id = {}, newName = {}", id, newName);
    final Country existing = idMap.get(id);
    if (existing == null || existing.getDeleteTime() != null) {
      throw new DataNotExistException(Country.class, "id", id);
    }
    final Country country = new Country(existing);
    country.setName(newName);
    return updateImpl(existing, country);
  }

  @Override
  public synchronized Instant updateNameByCode(final String code, final String newName)
      throws DataAccessException {
    logger.debug("Update a country's name by code: code = {}, newName = {}",
        code, newName);
    final Country existing = codeMap.get(code);
    if (existing == null || existing.getDeleteTime() != null) {
      throw new DataNotExistException(Country.class, "code", code);
    }
    final Country country = new Country(existing);
    country.setName(newName);
    return updateImpl(existing, country);
  }

  @Override
  public Instant deleteByCode(final String code) throws DataAccessException {
    final Country entity = codeMap.get(code);
    if (entity == null || entity.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "code", code);
    }
    logger.debug("Deleted a {} by code: code = {}", modelInfo.getName(), code);
    final Instant deleteTime = clock.now();
    entity.setDeleteTime(deleteTime);
    return deleteTime;
  }

  @Override
  public Instant deleteByName(final String name) throws DataAccessException {
    final Country entity = nameMap.get(name);
    if (entity == null || entity.getDeleteTime() != null) {
      throw new DataNotExistException(modelInfo.getType(), "name", name);
    }
    logger.debug("Deleted a {} by code: name = {}", modelInfo.getName(), name);
    final Instant deleteTime = clock.now();
    entity.setDeleteTime(deleteTime);
    return deleteTime;
  }

  @Override
  public void restoreByCode(final String code) throws DataAccessException {
    final Country entity = codeMap.get(code);
    if (entity == null || entity.getDeleteTime() == null) {
      throw new DataNotExistException(modelInfo.getType(), "code", code);
    }
    logger.debug("Restore a deleted {}: code = {}", modelInfo.getName(), code);
    entity.setDeleteTime(null);
  }

  @Override
  public void purgeByCode(final String code) throws DataAccessException {
    final Country entity = codeMap.get(code);
    if (entity == null || entity.getDeleteTime() == null) {
      throw new DataNotExistException(modelInfo.getType(), "code", code);
    }
    logger.debug("Purge a deleted {}: code = {}", modelInfo.getName(), code);
    idMap.remove(entity.getId());
    codeMap.remove(entity.getCode());
    nameMap.remove(makeNameKey(entity));
  }

  @Override
  public void restoreByName(final String name) throws DataAccessException {
    final Country entity = nameMap.get(name);
    if (entity == null || entity.getDeleteTime() == null) {
      throw new DataNotExistException(modelInfo.getType(), "name", name);
    }
    logger.debug("Restore a deleted {}: name = {}", modelInfo.getName(), name);
    entity.setDeleteTime(null);
  }

  @Override
  public void purgeByName(final String name) throws DataAccessException {
    final Country entity = nameMap.get(name);
    if (entity == null || entity.getDeleteTime() == null) {
      throw new DataNotExistException(modelInfo.getType(), "name", name);
    }
    logger.debug("Purge a deleted {}: name = {}", modelInfo.getName(), name);
    idMap.remove(entity.getId());
    codeMap.remove(entity.getCode());
    nameMap.remove(makeNameKey(entity));
  }

  @Override
  public void eraseByCode(final String code) throws DataAccessException {
    final Country entity = codeMap.get(code);
    if (entity == null) {
      throw new DataNotExistException(modelInfo.getType(), "code", code);
    }
    logger.debug("Erase a {}: code = {}", modelInfo.getName(), code);
    idMap.remove(entity.getId());
    codeMap.remove(entity.getCode());
    nameMap.remove(makeNameKey(entity));
  }
}
