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

import javax.annotation.Nullable;

import ltd.qubit.commons.error.DataNotExistException;

import org.junit.jupiter.api.DynamicTest;

import static ltd.qubit.commons.test.dao.DaoTestUtils.getRespectToParams;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurgeOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public PurgeOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
 protected void buildTests(final DaoDynamicTestBuilder builder) {
    final List<DynamicTest> result = new ArrayList<>();
    purgeExistingModel(builder);
    purgeNonExistingModel(builder);
    purgeNonDeletedModel(builder);
  }

  private void purgeExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Purge a deleted {}: {} of {}", methodName,
            modelName, i + 1, loops);
        final Object model = beanCreator.prepare(modelInfo, identifier);
        daoInfo.add(model);  // dao.add(model)
        final Object id = modelInfo.getId(model);
        assertTrue(daoInfo.exist(id), "The ID of just added model must exist.");
        daoInfo.delete(id);  // dao.delete(id)
        assertTrue(daoInfo.exist(id), "The ID of deleted model must still exist.");
        final Object actual = daoInfo.get(id); // dao.get(id)
        assertNotNull(actual, "Getting deleted model must not return null.");
        final Object deleteTime = modelInfo.get(actual, "deleteTime");
        assertNotNull(deleteTime, "Delete time of the deleted model must not be null.");
        modelInfo.set(model, "deleteTime", deleteTime);
        checkModelEquals(modelInfo, model, actual,
            "Getting the deleted model must return the same as the original one.");
        // dao.purge(model.id) or dao.purgeByXxx(model.xxx)
        final Object returnedValue = doPurge(true, model);
        assertNull(returnedValue, "The returned value of " + methodName
            + " must be void.");
        assertFalse(daoInfo.exist(id), "The ID of purged model must not exist.");
        final DaoMethodInfo getMethod = daoInfo.getGetMethod();
        assertNotNull(getMethod);
        final DataNotExistException e = assertThrows(DataNotExistException.class,
            () -> getMethod.invoke(false, id),  // dao.get(id)
            "Getting a non-existing ID must throw an exception.");
        assertNotNull(modelInfo.getIdProperty(), "The model must have the ID property");
        checkException(e, modelInfo.getIdProperty(), id);
      }
    });
  }

  private Object doPurge(final boolean logging, @Nullable final Object model) throws Throwable {
    if (! identifier.isUnique()) {
      final Object id = identifier.getValue(model);
      return methodInfo.invoke(logging, id);     // dao.purge(id) or dao.purgeByXxx(id)
    } else {
      final Object[] params = getRespectToParams(model, modelInfo, identifier, methodInfo);
      return methodInfo.invokeWithArguments(logging, params); // dao.purgeByXxx(key1, key2, ..., id)
    }
  }

  private void purgeNonExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non-existing " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Purge a non-existing {}.", methodName, modelName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      // 要确保准备好的bean的ID不是null
      if (identifier.getValue(model) == null) {
        beanCreator.prepareProperty(model, identifier);
      }
      // do not add the model
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doPurge(false, model), // dao.purge(model.id) or dao.purgeByXxx(model.xxx)
          "Purging a non-existing " + modelName + " must throw an exception.");
      checkException(e, identifier, identifier.getValue(model));
    });
  }

  private void purgeNonDeletedModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non-deleted " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Purge a non-deleted {}.", methodName, modelName);
      final Object model = beanCreator.prepare(modelInfo, identifier);
      daoInfo.add(model);  // dao.add(model)
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doPurge(false, model), // dao.purge(model.id) or dao.purgeByXxx(model.xxx)
          "Purging a non-deleted ID must throw an exception.");
      checkException(e, identifier, identifier.getValue(model));
    });
  }
}
