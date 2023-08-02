////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.util.Stack;

import static ltd.qubit.commons.test.dao.DaoTestUtils.stackToString;

public class ReferenceDependencyLoopException extends IllegalArgumentException {

  private static final long serialVersionUID = 9039343574544358661L;

  public ReferenceDependencyLoopException(final Stack<Class<?>> typeStack) {
    super("Find a dependency loop: " + stackToString(typeStack));
  }
}
