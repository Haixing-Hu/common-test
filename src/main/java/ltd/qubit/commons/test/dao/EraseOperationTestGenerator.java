////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import javax.annotation.Nullable;

import ltd.qubit.commons.error.DataNotExistException;

import static ltd.qubit.commons.test.dao.DaoTestUtils.getRespectToParams;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EraseOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public EraseOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
 protected void buildTests(final DaoDynamicTestBuilder builder) {
    eraseExistingModel(builder);
    eraseNonExistingModel(builder);
  }

  private void eraseExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Erase an existing {}: {} of {}", methodName,
            modelName, i + 1, loops);
        final Object model = beanCreator.prepare(modelInfo, identifier);
        daoInfo.add(model);  // dao.add(model)
        final Object id = modelInfo.getId(model);
        assertTrue(daoInfo.exist(id), "The ID of just added model must exist.");
        // dao.erase(model.id) or dao.eraseByXxx(model.xxx)
        final Object returnedValue = doErase(true, model);
        assertNull(returnedValue, "The returned value of " + methodName
            + " must be void.");
        assertFalse(daoInfo.exist(id), "The ID of erased model must not exist.");
        final DaoMethodInfo getMethod = daoInfo.getGetMethod();
        assertNotNull(getMethod);
        final DataNotExistException e = assertThrows(DataNotExistException.class,
            () -> getMethod.invoke(false, id),  // dao.get(id)
            "Getting a non-existing ID must throw an exception.");
        checkException(e, modelInfo.getIdProperty(), id);
      }
    });
  }

  private Object doErase(final boolean logging, @Nullable final Object model) throws Throwable {
    if (! identifier.isUnique()) {
      final Object id = identifier.getValue(model);
      return methodInfo.invoke(logging, id);     // dao.erase(id) or dao.eraseByXxx(id)
    } else {
      final Object[] params = getRespectToParams(model, modelInfo, identifier, methodInfo);
      return methodInfo.invokeWithArguments(logging, params); // dao.eraseByXxx(key1, key2, ..., id)
    }
  }

  private void eraseNonExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non-existing " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Erase a non-existing {}.", methodName, modelName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      // 要确保准备好的bean的ID不是null
      if (identifier.getValue(model) == null) {
        beanCreator.prepareProperty(model, identifier);
      }
      // do not add the model
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doErase(false, model),  // dao.erase(model.id) or dao.eraseByXxx(model.xxx)
          "Erasing a non-existing " + modelName + " must throws an exception.");
      checkException(e, identifier, identifier.getValue(model));
    });
  }
}
