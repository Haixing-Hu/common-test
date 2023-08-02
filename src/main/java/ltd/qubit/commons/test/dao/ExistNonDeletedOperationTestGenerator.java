////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import static ltd.qubit.commons.test.dao.DaoTestUtils.getRespectToParams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExistNonDeletedOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public ExistNonDeletedOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
 protected void buildTests(final DaoDynamicTestBuilder builder) {
    existOfExistingNonDeletedModel(builder);
    existOfExistingDeletedModel(builder);
    existOfNonExistingModel(builder);
  }

  private void existOfExistingNonDeletedModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing non-deleted " + identifierName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Test the existence of an existing non-deleted {}",
          methodName, identifierName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      daoInfo.add(model);  //  dao.add(model)
      final Object actual = doTestExistence(model); // dao.existNonDeletedXxx(prop)
      assertNotNull(actual, "Return value of " + methodName + " cannot be null.");
      assertEquals(Boolean.class, actual.getClass(), "The returned type of "
          + methodName + " must be boolean.");
      assertTrue((Boolean) actual, "Calling " + methodName + " with an existing non-deleted "
          + identifierName + " must return true.");
    });
  }

  private void existOfExistingDeletedModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing deleted " + identifierName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Test the existence of an existing deleted {}",
          methodName, identifierName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      daoInfo.add(model);                       //  dao.add(model)
      daoInfo.delete(modelInfo.getId(model));   //  dao.delete(model.id)
      final Object actual = doTestExistence(model); // dao.existNonDeletedProp(prop)
      assertNotNull(actual, "Return value of " + methodName + " cannot be null.");
      assertEquals(Boolean.class, actual.getClass(), "The returned type of "
          + methodName + " must be boolean.");
      assertFalse((Boolean) actual, "Calling " + methodName + " with an existing deleted "
          + identifierName + " must return false.");
    });
  }

  private void existOfNonExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non existing " + identifierName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Test the existence of a non-existing {}",
          methodName, identifierName);
      final Object model = beanCreator.prepare(modelInfo);
      // 要确保准备好的bean的ID不是null
      if (identifier.getValue(model) == null) {
        beanCreator.prepareProperty(model, identifier);
      }
      final Object actual = doTestExistence(model); // dao.existProp(prop)
      assertNotNull(actual, "Return value of " + methodName + " cannot be null.");
      assertEquals(Boolean.class, actual.getClass(), "The returned type of "
          + methodName + " must be boolean.");
      assertFalse((Boolean) actual, "Calling " + methodName + " with a non-existing "
          + identifierName + " must return false.");
    });
  }

  private Object doTestExistence(final Object model) throws Throwable {
    if (! identifier.isUnique()) {
      final Object id = identifier.getValue(model);
      // dao.existNonDeletedXxx(id)
      return methodInfo.invoke(true, id);
    } else {
      final Object[] params = getRespectToParams(model, modelInfo, identifier, methodInfo);
      // dao.existNonDeletedXxx(key1, key2, ..., id)
      return methodInfo.invokeWithArguments(true, params);
    }
  }

}
