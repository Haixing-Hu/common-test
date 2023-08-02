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
import ltd.qubit.commons.test.model.InfoWithEntity;

import org.springframework.dao.DataAccessException;

public interface InfoWithEntityDao<T extends Info> extends InfoDao<T> {

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
  InfoWithEntity getInfo(Long id) throws DataAccessException;

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
  InfoWithEntity getInfoByCode(String code) throws DataAccessException;

  /**
   * 根据名称获取指定的对象的基本信息。
   *
   * @param entity
   *    指定的对象所属实体。
   * @param name
   *    指定的对象的名称。
   * @return
   *    属于系统App或指定App，属于指定实体，且具有指定名称的对象的基本信息。
   * @throws DataNotExistException
   *    若指定的对象不存在。
   * @throws DataAccessException
   *    若出现其他数据存取错误。
   */
  InfoWithEntity getInfoByName(String entity, String name) throws DataAccessException;
}
