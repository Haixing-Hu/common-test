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

/**
 * 存取{@link Category}对象的DAO的接口。
 *
 * @author 胡海星
 */
public interface CategoryDao extends ListableDao<Category>,
    GettableWithInfoDao<Category>,
    AddableDao<Category>, UpdatableDao<Category>, UpdatableWithCodeDao<Category>,
    UpdatableWithNameDao<Category>, AddableUpdatableWithCodeDao<Category>,
    AddableUpdatableWithNameDao<Category>, DeletableDao<Category>, ErasableDao<Category> {

  /**
   * 判定拥有指定名称的对象是否存在。
   *
   * @param name
   *     指定的名称。
   * @return
   *     若拥有指定名称的对象存在，则返回{@code true}；否则返回{@code false}。
   * @throws DataAccessException
   *     如果出现任何数据存取错误。
   */
  boolean existName(final String entity, final String name) throws DataAccessException;

  /**
   * 根据名称获取指定的类别。
   *
   * @param entity
   *     指定的类别所属得实体。
   * @param name
   *     指定的类别的名称。
   * @return
   *     具有指定所属实体和指定名称的类别对象。
   * @throws DataNotExistException
   *     若指定的对象不存在。
   * @throws DataAccessException
   *     若出现其他数据存取错误。
   */
  Category getByName(final String entity, final String name) throws DataAccessException;

  /**
   * 根据编码获取指定的{@link Category}对象的基本信息。
   *
   * <p><b>注意：</b>此函数不考虑待获取对象是否已被标记删除。
   *
   * @param id
   *     指定的对象的ID。
   * @return 具有指定ID的{@link Category}对象的基本信息。
   * @throws DataNotExistException
   *     若指定的对象不存在。
   * @throws DataAccessException
   *     若出现其他数据存取错误。
   */
  InfoWithEntity getInfo(Long id) throws DataAccessException;

  /**
   * 根据编码获取指定的{@link Category}对象的基本信息。
   *
   * <p><b>注意：</b>此函数不考虑待获取对象是否已被标记删除。
   *
   * @param code
   *     指定的对象的编码。
   * @return 具有指定编码的{@link Category}对象的基本信息。
   * @throws DataNotExistException
   *     若指定的对象不存在。
   * @throws DataAccessException
   *     若出现其他数据存取错误。
   */
  InfoWithEntity getInfoByCode(String code) throws DataAccessException;

  /**
   * 根据名称获取指定的{@link Category}对象的基本信息。
   *
   * <p><b>注意：</b>此函数不考虑待获取对象是否已被标记删除。
   *
   * @param entity
   *     指定的类别所属得实体。
   * @param name
   *     指定的类别的名称。
   * @return 属于指定实体，且具有指定名称的{@link Category}对象的基本信息。
   * @throws DataNotExistException
   *     若指定的对象不存在。
   * @throws DataAccessException
   *     若出现其他数据存取错误。
   */
  InfoWithEntity getInfoByName(String entity, String name)
      throws DataAccessException;

}
