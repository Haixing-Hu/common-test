////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.Street;

/**
 * 存取{@link Street}对象的DAO的接口。
 *
 * @author 胡海星
 */
public interface StreetDao extends ListableDao<Street>,
    GettableWithInfoDao<Street>,
    AddableDao<Street>, UpdatableDao<Street>, UpdatableWithCodeDao<Street>,
    UpdatableWithNameDao<Street>, AddableUpdatableWithCodeDao<Street>,
    AddableUpdatableWithNameDao<Street>, DeletableDao<Street>, ErasableDao<Street> {
  //  empty
}
