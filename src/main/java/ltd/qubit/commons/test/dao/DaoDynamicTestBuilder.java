////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

import static ltd.qubit.commons.lang.Argument.requireNonNull;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * A class helps to build list of dynamic tests for DAOs.
 *
 * @author Haixing Hu
 */
public final class DaoDynamicTestBuilder {

  private final List<DynamicNode> list;
  private final DaoOperationTestGenerator<?> generator;

  public DaoDynamicTestBuilder(final DaoOperationTestGenerator<?> generator) {
    this.generator = requireNonNull("generator", generator);
    this.list = new ArrayList<>();
  }

  public DaoDynamicTestBuilder add(final String name, final Executable executable) {
    final DynamicTest test = dynamicTest(name, generator.getUri(), () -> {
      generator.setUp();
      executable.execute();
      generator.tearDown();
    });
    list.add(test);
    return this;
  }

  public List<DynamicNode> build() {
    return list;
  }
}
