////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.test.model.Category;
import ltd.qubit.commons.test.model.InfoWithEntity;

import org.springframework.dao.DataAccessException;

public class CategoryDaoImpl extends DaoImplBase<Category> implements CategoryDao {

  public CategoryDaoImpl() {
    super(Category.class);
  }

  protected String makeNameKey(final Category category) {
    return makeNameKey(category.getEntity(), category.getName());
  }

  @Override
  protected String makeDatabaseNameKey(final Category entity) {
    return makeNameKey(entity);
  }

  protected String makeNameKey(final String entity, final String name) {
    if (entity == null) {
      return "-" + name;
    } else {
      return entity + "-" +  name;
    }
  }

  @Override
  public boolean existName(final String entity, final String name)
      throws DataAccessException {
    return nameMap.containsKey(makeNameKey(entity, name));
  }

  @Override
  public Category getByName(final String entity, final String name)
      throws DataAccessException {
    final Category category = nameMap.get(makeNameKey(entity, name));
    if (category != null) {
      logger.debug("Get a {} by name {}: {}", modelInfo.getName(), name, entity);
      return category.clone();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "name", name);
    }
  }

  @Override
  public InfoWithEntity getInfo(final Long id) throws DataAccessException {
    final Category category = idMap.get(id);
    if (category != null) {
      logger.debug("Get the info of a {} by ID {}: {}", modelInfo.getName(), id, category);
      return category.getInfo();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "id", id);
    }
  }

  @Override
  public InfoWithEntity getInfoByCode(final String code)
      throws DataAccessException {
    final Category category = codeMap.get(code);
    if (category != null) {
      logger.debug("Get the info of a {} by code {}: {}", modelInfo.getName(),
          code, category);
      return category.getInfo();
    } else {
      throw new DataNotExistException(modelInfo.getType(), "code", code);
    }
  }

  @Override
  public InfoWithEntity getInfoByName(final String entity,
      final String name) throws DataAccessException {
    final Category category = nameMap.get(makeNameKey(entity, name));
    if (category != null) {
      logger.debug("Get the info of a category by name {} - {}: {}", entity,
          name, category);
      return category.getInfo();
    } else {
      throw new DataNotExistException(Category.class, "name", name);
    }
  }
}
