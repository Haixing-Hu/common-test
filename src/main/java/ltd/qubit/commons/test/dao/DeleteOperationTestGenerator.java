////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.time.Instant;

import javax.annotation.Nullable;

import ltd.qubit.commons.error.DataNotExistException;

import static ltd.qubit.commons.test.dao.DaoTestUtils.getRespectToParams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public DeleteOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
 protected void buildTests(final DaoDynamicTestBuilder builder) {
    deleteExistingModel(builder);
    deleteNonExistingModel(builder);
    deleteDeletedModel(builder);
  }

  private void deleteExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Delete an existing {}: {} of {}", methodName,
            modelName, i + 1, loops);
        final Object model = beanCreator.prepare(modelInfo, identifier);
        daoInfo.add(model);  // dao.add(model)
        final Object id = modelInfo.getId(model);
        assertTrue(daoInfo.exist(id), "The ID of just added model must exist.");
        // dao.delete(model.id) or dao.deleteByXxx(model.xxx)
        final Object deleteTime = doDelete(true, model);
        assertNotNull(deleteTime, "The returned value of " + methodName
            + " must not be null.");
        assertEquals(Instant.class, deleteTime.getClass(), "The returned type of "
            + methodName + " must be java.time.Instant.");
        assertTrue(daoInfo.exist(id), "The ID of deleted model must still exist.");
        final Object actual = daoInfo.get(id); // dao.get(id)
        assertNotNull(actual, "Getting deleted model must not return null.");
        assertEquals(deleteTime, modelInfo.get(actual, "deleteTime"),
            "The " + methodName + " must return the deleteTime of the "
                + "deleted " + modelName + ".");
        modelInfo.set(model, "deleteTime", deleteTime);
        checkModelEquals(modelInfo, model, actual,
            "Getting the deleted model must return the same as the original one.");
      }
    });
  }

  private Object doDelete(final boolean logging, @Nullable final Object model) throws Throwable {
    if (! identifier.isUnique()) {
      final Object id = identifier.getValue(model);
      // dao.delete(id) or dao.deleteByXxx(id)
      return methodInfo.invoke(logging, id);
    } else {
      final Object[] params = getRespectToParams(model, modelInfo, identifier, methodInfo);
      // dao.deleteByXxx(key1, key2, ..., id)
      return methodInfo.invokeWithArguments(logging, params);
    }
  }

  private void deleteNonExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non-existing " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Delete a non-existing {}.", methodName, modelName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      // 要确保准备好的bean的ID不是null
      if (identifier.getValue(model) == null) {
        beanCreator.prepareProperty(model, identifier);
      }
      // do not add the model
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doDelete(false, model),  // dao.delete(model.id) or dao.deleteByXxx(model.xxx)
          "Deleting a non-existing " + modelName + " must throws an exception.");
      checkException(e, identifier, identifier.getValue(model));
    });
  }

  private void deleteDeletedModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Deleted " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Delete a deleted {}.", methodName, modelName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      daoInfo.add(model);  // dao.add(model)
      final Object id = modelInfo.getId(model);
      assertTrue(daoInfo.exist(id), "The ID of just added model must exist.");
      doDelete(true, model);    // dao.delete(model.id) or dao.deleteByXxx(model.xxx)
      assertTrue(daoInfo.exist(id), "The ID of deleted model must still exist.");
      final Object actual = daoInfo.get(id); // dao.get(id)
      assertNotNull(actual, "Getting deleted model must not return null.");
      final Object deleteTime = modelInfo.get(actual, "deleteTime");
      assertNotNull(deleteTime, "Delete time of the deleted model must not be null.");
      modelInfo.set(model, "deleteTime", deleteTime);
      checkModelEquals(modelInfo, model, actual,
          "Getting the deleted model must return the same as the original one.");
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doDelete(false, model),  // dao.delete(model.id) or dao.deleteByXxx(model.xxx)
          "Deleting a deleted " + modelName + " must throw an exception.");
      checkException(e, identifier, identifier.getValue(model));
    });
  }
}
