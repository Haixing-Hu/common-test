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

import org.springframework.dao.DataAccessException;

/**
 * 此接口表示对具有ID属性的实体类实现了查询操作的DAO。
 *
 * @param <T>
 *     被操作的实体的类型。
 * @author 胡海星
 */
public interface GettableDao<T extends Identifiable> {

  /**
   * 判定拥有指定ID的对象是否存在。
   *
   * @param id
   *     指定的ID。
   * @return
   *     若拥有指定ID的对象存在，则返回{@code true}；否则返回{@code false}。
   * @throws DataAccessException
   *     如果出现任何数据存取错误。
   */
  boolean exist(final Long id) throws DataAccessException;

  /**
   * 根据ID获取指定的对象。
   *
   * @param id
   *    指定的对象的ID。
   * @return
   *    具有指定ID的对象。
   * @throws DataNotExistException
   *    若指定的对象不存在。
   * @throws DataAccessException
   *    若出现其他数据存取错误。
   */
  T get(final Long id) throws DataAccessException;
}
