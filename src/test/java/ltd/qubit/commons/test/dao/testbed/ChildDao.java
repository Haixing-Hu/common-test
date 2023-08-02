////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.Child;

public interface ChildDao extends GettableDao<Child>,
    AddableDao<Child>, UpdatableDao<Child>, ErasableDao<Child> {
  //  empty
}
