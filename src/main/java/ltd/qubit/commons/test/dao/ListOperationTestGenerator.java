////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import org.junit.jupiter.api.DynamicTest;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ListOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public ListOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
  protected void buildTests(final DaoDynamicTestBuilder builder) {
    //  TODO
  }

  private DynamicTest listEmptyDatasetWithoutFilter() {
    final String displayName = getDisplayName("Empty " + modelName + " data set");
    return dynamicTest(displayName, methodInfo.getUri(), () -> {
      logger.info("Test {}: List from an empty {} data set without filter and sort.",
          methodName, modelName);
      final Object result = methodInfo.invoke(true, null, null, 100, 0);
    });
  }

  private DynamicTest listNonEmptyDatasetWithoutFilter() {
    final String displayName = getDisplayName("Non-empty " + modelName
        + " data set without filter");
    final int loops = parameters.getLoops();
    return dynamicTest(displayName, methodInfo.getUri(), () -> {
      for (int i = 0; i < loops; ++i) {
          //  TODO
      }
    });
  }
}
