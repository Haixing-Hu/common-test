////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import ltd.qubit.commons.error.DataNotExistException;

import static ltd.qubit.commons.reflect.ClassUtils.isMutableType;
import static ltd.qubit.commons.reflect.ClassUtils.isPrimitiveType;
import static ltd.qubit.commons.test.dao.DaoTestUtils.getRespectToParams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public GetOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
 protected void buildTests(final DaoDynamicTestBuilder builder) {
    getExistingModel(builder);
    final Class<?> targetType = (target == null ? modelType : target.getType());
    if (!isPrimitiveType(targetType) && isMutableType(targetType)) {
      getExistingModelTwice(builder);
    }
    getNonExistingModel(builder);
  }

  private void getExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing " + identifier.getName());
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Get the {} with an existing {}: {} of {}",
            methodName, targetName, identifier.getName(), i + 1, loops);
        final Object model = beanCreator.prepare(modelInfo, identifier);
        daoInfo.add(model);
        final Object actual = doGet(true, model);  //  dao.getYYYByXXX(id)
        if (target == null) {
          assertNotNull(actual,
              "Return value of " + methodName + " cannot be null.");
          assertEquals(model, actual, "Calling " + methodName
              + " with an existing " + identifier.getName()
              + " must equal to the original model.");
        } else {
          final Object expected = target.getValue(model);
          assertEquals(expected, actual, "Calling " + methodName
              + " with an existing " + identifier.getName()
              + " must equal to the original one.");
        }
      }
    });
  }

  private void getExistingModelTwice(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Get by existing "
        + identifier.getName() + " twice.");
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Get the {} with an existing {} twice: {} of {}",
            methodName, targetName, identifier.getName(), i + 1, loops);
        final Object model = beanCreator.prepare(modelInfo, identifier);
        daoInfo.add(model);
        final Object actual1 = doGet(true, model);  //  dao.getYYYByXXX(id)
        if (target == null) {
          assertNotNull(actual1,
              "Return value of " + methodName + " cannot be null.");
          assertEquals(model, actual1, "Calling " + methodName
              + " with an existing " + identifier.getName()
              + " must equal to the original model.");
        } else {
          final Object expected = target.getValue(model);
          assertEquals(expected, actual1, "Calling " + methodName
              + " with an existing " + identifier.getName()
              + " must equal to the original one.");
        }
        final Object actual2 = doGet(true, model);  //  dao.getYYYByXXX(id)
        assertEquals(actual1, actual2, "Get " + targetName
            + " by existing " + identifier.getName()
            + " twice with the same arguments should returns the equal objects.");
        if (actual1 != null) {
          assertNotSame(actual1, actual2, "Get " + targetName + " by existing "
              + identifier.getName()
              + " twice with the same arguments should NOT returns the same objects.");
        }
      }
    });
  }

  private void getNonExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non existing " + identifier.getName());
    builder.add(displayName, () -> {
      logger.info("Test {}: Get the {} with a non-existing {}", methodName,
          targetName, identifier.getName());
      final Object model = beanCreator.prepare(modelInfo, identifier);
      // 要确保准备好的bean的ID不是null
      if (identifier.getValue(model) == null) {
        beanCreator.prepareProperty(model, identifier);
      }
      if (methodInfo.isAllowNullReturn()) {
        final Object result = doGet(true, model);
        final String message = "Calling " + methodName + " with a non-existing "
            + identifier.getName() + " must returns null.";
        assertNull(result, message);
      } else {
        final String message = "Calling " + methodName + " with a non-existing "
            + identifier.getName() + " must throw a DataNotExistException.";
        final DataNotExistException e = assertThrows(DataNotExistException.class,
            () -> doGet(false, model), message);
        checkException(e, identifier, identifier.getValue(model));
      }
    });
  }

  private Object doGet(final boolean logging, final Object model) throws Throwable {
    if (! identifier.isUnique()) {
      final Object id = identifier.getValue(model);
      // dao.getYyyByXxx(id)
      return methodInfo.invoke(logging, id);
    } else {
      final Object[] params = getRespectToParams(model, modelInfo, identifier, methodInfo);
      // dao.getYyyByXxx(key1, key2, ..., id)
      return methodInfo.invokeWithArguments(logging, params);
    }
  }
}
