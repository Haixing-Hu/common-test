////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.District;

/**
 * 存取{@link District}对象的DAO的接口。
 *
 * @author 胡海星
 */
public interface DistrictDao extends ListableDao<District>,
    GettableWithInfoDao<District>,
    AddableDao<District>, UpdatableDao<District>, UpdatableWithCodeDao<District>,
    UpdatableWithNameDao<District>, AddableUpdatableWithCodeDao<District>,
    AddableUpdatableWithNameDao<District>, DeletableDao<District>, ErasableDao<District> {
  //  empty
}
