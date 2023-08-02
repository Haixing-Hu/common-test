////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.City;

/**
 * 存取{@link City}对象的DAO的接口。
 *
 * @author 胡海星
 */
public interface CityDao extends ListableDao<City>,
    GettableWithInfoDao<City>,
    AddableDao<City>, UpdatableDao<City>, UpdatableWithCodeDao<City>,
    UpdatableWithNameDao<City>, AddableUpdatableWithCodeDao<City>,
    AddableUpdatableWithNameDao<City>, DeletableDao<City>, ErasableDao<City> {
  //  empty
}
