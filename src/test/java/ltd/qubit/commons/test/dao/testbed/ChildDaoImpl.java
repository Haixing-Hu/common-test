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

public class ChildDaoImpl extends SimpleDaoImpl<Child> implements ChildDao {

  public ChildDaoImpl() {
    super(Child.class);
  }
}
