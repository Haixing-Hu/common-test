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
import ltd.qubit.commons.test.model.Identifiable;
import ltd.qubit.commons.test.model.WithName;

import org.springframework.dao.DataAccessException;

/**
 * 此接口表示对拥有{@code name}属性的实体类实现了查询操作的DAO。
 *
 * @param <T>
 *     被操作的实体的类型。
 */
public interface GettableWithNameDao<T extends Identifiable & WithName>
    extends GettableDao<T> {

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
  boolean existName(final String name) throws DataAccessException;

  /**
   * 根据名称获取指定的对象。
   *
   * @param name
   *     指定的对象的名称。
   * @return
   *     具有指定名称的对象。
   * @throws DataNotExistException
   *     若指定的对象不存在。
   * @throws DataAccessException
   *     若出现其他数据存取错误。
   */
  T getByName(final String name) throws DataAccessException;

}
