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
import ltd.qubit.commons.test.model.WithCode;

import org.springframework.dao.DataAccessException;

/**
 * 此接口表示对拥有{@code code}属性的实体类实现了查询操作的DAO。
 *
 * @param <T>
 *     被操作的实体的类型。
 */
public interface GettableWithCodeDao<T extends Identifiable & WithCode>
    extends GettableDao<T> {

  /**
   * 判定拥有指定编码的对象是否存在。
   *
   * @param code
   *     指定的编码。
   * @return
   *     若拥有指定编码的对象存在，则返回{@code true}；否则返回{@code false}。
   * @throws DataAccessException
   *     如果出现任何数据存取错误。
   */
  boolean existCode(final String code) throws DataAccessException;

  /**
   * 根据编码获取指定的对象。
   *
   * @param code
   *    指定的对象的编码。
   * @return
   *    具有指定编码的对象。
   * @throws DataNotExistException
   *    若指定的对象不存在。
   * @throws DataAccessException
   *    若出现其他数据存取错误。
   */
  T getByCode(final String code) throws DataAccessException;

}
