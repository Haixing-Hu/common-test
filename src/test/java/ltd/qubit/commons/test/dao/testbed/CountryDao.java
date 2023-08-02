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

import ltd.qubit.commons.annotation.Modified;
import ltd.qubit.commons.test.model.Country;

import org.springframework.dao.DataAccessException;

/**
 * 存取{@link Country}对象的DAO的接口。
 *
 * @author 胡海星
 */
public interface CountryDao extends ListableDao<Country>,
    GettableWithInfoDao<Country>, GettableWithNameDao<Country>,
    AddableDao<Country>, UpdatableDao<Country>, UpdatableWithCodeDao<Country>,
    UpdatableWithNameDao<Country>, AddableUpdatableWithCodeDao<Country>,
    AddableUpdatableWithNameDao<Country>, DeletableDao<Country>, ErasableDao<Country> {

  boolean existNonDeletedCode(String code) throws DataAccessException;

  @Modified({"name", "modifyTime"})
  Instant updateName(Long id, String newName) throws DataAccessException;

  @Modified({"name", "modifyTime"})
  Instant updateNameByCode(String code, String newName) throws DataAccessException;

  Instant deleteByCode(String code) throws DataAccessException;

  Instant deleteByName(String name) throws DataAccessException;

  void restoreByCode(String code) throws DataAccessException;

  void restoreByName(String name) throws DataAccessException;

  void purgeByCode(String code) throws DataAccessException;

  void purgeByName(String name) throws DataAccessException;

  void eraseByCode(String code) throws DataAccessException;
}
