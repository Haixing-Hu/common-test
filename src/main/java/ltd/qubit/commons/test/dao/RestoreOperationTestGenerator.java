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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestoreOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public RestoreOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
 protected void buildTests(final DaoDynamicTestBuilder builder) {
    restoreExistingModel(builder);
    restoreNonExistingModel(builder);
    restoreNonDeletedModel(builder);
  }

  private void restoreExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Restore a deleted {}: {} of {}", methodName,
            modelName, i + 1, loops);
        final Object model = beanCreator.prepare(modelInfo, identifier);
        daoInfo.add(model);  // dao.add(model)
        final Object id = modelInfo.getId(model);
        assertTrue(daoInfo.exist(id), "The ID of just added model must exist.");
        daoInfo.delete(id);  // dao.delete(id)
        assertTrue(daoInfo.exist(id), "The ID of deleted model must still exist.");
        Object actual = daoInfo.get(id); // dao.get(id)
        assertNotNull(actual, "Getting deleted model must not return null.");
        Object deleteTime = modelInfo.get(actual, "deleteTime");
        assertNotNull(deleteTime, "Delete time of the deleted model must not be null.");
        modelInfo.set(model, "deleteTime", deleteTime);
        checkModelEquals(modelInfo, model, actual,
            "Getting the deleted model must return the same as the original one.");
        doRestore(true, model);  // dao.restore(model.id) or dao.restoreByXxx(model.xxx)
        assertTrue(daoInfo.exist(id), "The ID of restored model must still exist.");
        actual = daoInfo.get(id); // dao.get(id)
        assertNotNull(actual, "Getting restored model must not return null.");
        deleteTime = modelInfo.get(actual, "deleteTime");
        assertNull(deleteTime, "Delete time of the restored model must be null.");
        modelInfo.set(model, "deleteTime", null);
        checkModelEquals(modelInfo, model, actual,
            "Getting the restored model must return the same as the original one.");
      }
    });
  }

  private Object doRestore(final boolean logging, @Nullable final Object model)
      throws Throwable {
    if (! identifier.isUnique()) {
      final Object id = identifier.getValue(model);
      // dao.restore(id) or dao.restoreByXxx(id)
      return methodInfo.invoke(logging, id);
    } else {
      final Object[] params = getRespectToParams(model, modelInfo, identifier, methodInfo);
      // dao.restoreByXxx(key1, key2, ..., id)
      return methodInfo.invokeWithArguments(logging, params);
    }
  }

  private void restoreNonExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non-existing " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Restore a non-existing {}.", methodName, modelName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      // 要确保准备好的bean的ID不是null
      if (identifier.getValue(model) == null) {
        beanCreator.prepareProperty(model, identifier);
      }
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doRestore(false, model), // dao.restore(model.id) or dao.restoreByXxx(model.xxx)
          "Restoring a non-existing " + modelName + " must throw an exception.");
      checkException(e, identifier, identifier.getValue(model));
    });
  }

  private void restoreNonDeletedModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non-deleted " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Restore a non-deleted {}.", methodName, modelName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      daoInfo.add(model);  // dao.add(model)
      final Object id = modelInfo.getId(model);
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doRestore(false, model), // dao.restore(model.id) or dao.restoreByXxx(model.xxx)
          "Restoring a non-deleted " + modelName + " must throw an exception.");
      checkException(e, identifier, identifier.getValue(model));
    });
  }
}
