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
import ltd.qubit.commons.test.model.Country;
import ltd.qubit.commons.test.model.Province;

import org.springframework.dao.DataAccessException;

/**
 * 存取{@link Province}对象的DAO的接口。
 *
 * @author 胡海星
 */
public interface ProvinceDao extends ListableDao<Province>,
    GettableWithInfoDao<Province>,
    AddableDao<Province>, UpdatableDao<Province>, UpdatableWithCodeDao<Province>,
    UpdatableWithNameDao<Province>, AddableUpdatableWithCodeDao<Province>,
    AddableUpdatableWithNameDao<Province>, DeletableDao<Province>, ErasableDao<Province> {

  /**
   * 测试指定的{@link Province}对象是否存在。
   *
   * @param countryCode
   *     指定的{@link Province}所属的{@link Country}的编码。
   * @param name
   *     指定的{@link Province}对象的名称。
   * @return 若存在拥有指定名称的{@link Province}对象，无论该对象是否被标记删除，均返回
   *     {@code true}；否则返回{@code false}。
   * @throws DataAccessException
   *     若发生任何数据存取错误。
   */
  boolean existName(String countryCode, String name) throws DataAccessException;

  /**
   * 根据名称获取指定的{@link Province}对象。
   *
   * <p><b>注意：</b>此函数不考虑待获取对象是否已被标记删除。
   *
   * @param countryCode
   *     指定的{@link Province}所属的{@link Country}的编码。
   * @param name
   *     指定的{@link Province}的名称。
   * @return 具有指定名称的{@link Province}对象。
   * @throws DataNotExistException
   *     若指定的对象不存在。
   * @throws DataAccessException
   *     若出现其他数据存取错误。
   */
  Province getByName(String countryCode, String name)
      throws DataAccessException;
}
