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
import ltd.qubit.commons.test.model.Info;
import ltd.qubit.commons.test.model.WithInfo;

import org.springframework.dao.DataAccessException;

/**
 * 此接口表示对拥有{@code info}属性的实体类实现了查询操作的DAO。
 *
 * @param <T>
 *     被操作的实体的类型。
 */
public interface GettableWithInfoDao<T extends WithInfo> extends
    GettableWithCodeDao<T> {

  /**
   * 根据ID获取指定的对象的基本信息。
   *
   * @param id
   *    指定的对象的ID。
   * @return
   *    具有指定ID的对象的基本信息。
   * @throws DataNotExistException
   *    若指定的对象不存在。
   * @throws DataAccessException
   *    若出现其他数据存取错误。
   */
  Info getInfo(final Long id) throws DataAccessException;

  /**
   * 根据编码获取指定的对象的基本信息。
   *
   * @param code
   *    指定的对象的编码。
   * @return
   *    具有指定编码的对象的基本信息。
   * @throws DataNotExistException
   *    若指定的对象不存在。
   * @throws DataAccessException
   *    若出现其他数据存取错误。
   */
  Info getInfoByCode(final String code) throws DataAccessException;

}
