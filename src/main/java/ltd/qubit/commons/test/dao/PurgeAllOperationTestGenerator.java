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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PurgeAllOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {
  public PurgeAllOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
  protected void buildTests(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Purge all deleted " + modelName);
    builder.add(displayName, () -> {
      final List<Object> ids = new ArrayList<>();
      final List<Boolean> deleted = new ArrayList<>();
      addAndDeleteModels(ids, deleted);
      logger.info("Test {}: Purge all deleted {}.", methodName, modelName);
      final Object count = methodInfo.invoke(true);  // dao.purgeAll()
      assertNotNull(count, "The returned value of " + methodName
          + " must not be null.");
      assertEquals(Long.class, count.getClass(), "The returned type of "
          + methodName + " must be long.");
      final Long expected = checkPurgedModels(ids, deleted);
      assertEquals(expected, count, "The " + methodName + " must return the total "
          + "number of purged " + modelName + ".");
    });
  }

  private void addAndDeleteModels(final List<Object> ids,
      final List<Boolean> deleted) throws Throwable {
    logger.info("Test {}: Clears all models {} before testing.",
        methodName, modelName);
    daoInfo.clear();
    final int loops = parameters.getLoops();
    for (int i = 0; i < loops; ++i) {
      logger.info("Test {}: Add a model {}: {} of {}", methodName,
          modelName, i + 1, loops);
      final Object model = beanCreator.prepare(modelInfo);
      daoInfo.add(model);  // dao.add(model)
      final Object id = modelInfo.getId(model);
      ids.add(id);
      if (daoInfo.hasExist()) {
        assertTrue(daoInfo.exist(id), "The ID of added model must exist.");
      }
      if (daoInfo.hasDelete() && random.nextBoolean()) {
        logger.info("Test {}: Delete the just added model: {}", methodName, id);
        daoInfo.delete(id);  // dao.delete(id)
        deleted.add(true);
      } else {
        deleted.add(false);
      }
    }
  }

  private long checkPurgedModels(final List<Object> ids,
      final List<Boolean> deleted) throws Throwable {
    final int loops = parameters.getLoops();
    long result = 0;
    for (int i = 0; i < loops; ++i) {
      final Object id = ids.get(i);
      if (deleted.get(i)) {
        logger.info("Test {}: Test the existence of the deleted model: {}",
            methodName, id);
        assertFalse(daoInfo.exist(id), "The ID of a deleted model after the "
            + methodName + " operation must not exist.");
        ++result;
      } else {
        logger.info("Test {}: Test the existence of the non-deleted model: {}",
            methodName, id);
        assertTrue(daoInfo.exist(id), "The ID of a non-deleted model must exist.");
        logger.info("Test {}: Test the deleted time of the non-deleted model: {}",
            methodName, id);
        final Object actual = daoInfo.get(id); // dao.get(id)
        assertNotNull(actual, "Getting a non-deleted model must not return null.");
        final Object deleteTime = modelInfo.get(actual, "deleteTime");
        assertNull(deleteTime, "Delete time of the non-deleted model must be null.");
      }
    }
    return result;
  }
}
