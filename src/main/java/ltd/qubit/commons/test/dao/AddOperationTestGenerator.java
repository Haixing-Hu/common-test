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

import ltd.qubit.commons.error.DuplicateKeyException;
import ltd.qubit.commons.error.FieldTooLongException;
import ltd.qubit.commons.error.NullFieldException;
import ltd.qubit.commons.reflect.Property;

import static ltd.qubit.commons.test.dao.DaoTestUtils.setUniquePropertyValues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public AddOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
  protected void buildTests(final DaoDynamicTestBuilder builder) {
    addNormalModel(builder);
    addModelWithNullField(builder);
    addModelWithLongField(builder);
    addModelWithDuplicatedField(builder);
    addModelWithUnreferencedField(builder);
  }

  private void addNormalModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Normal " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Add a normal {}: {} of {}", methodName, modelName,
            i + 1, loops);
        addNormalModelImpl();
      }
    });
  }

  private Object addNormalModelImpl() throws Throwable {
    final Object model = beanCreator.prepare(modelInfo);
    final Object createTime = methodInfo.invoke(true, model);  // dao.add(model)
    assertNotNull(createTime, "The returned value of " + methodName
        + " must not be null.");
    assertEquals(Instant.class, createTime.getClass(), "The returned type of "
        + methodName + " must be java.time.Instant.");
    final Object id = modelInfo.getId(model);
    assertNotNull(id, "The ID of the added object must not be null.");
    if (modelInfo.hasProperty("createTime")) {
      assertEquals(createTime, modelInfo.get(model, "createTime"), "The "
          + methodName + "must return the createTime of the added "
          + modelName + ".");
    }
    if (modelInfo.hasProperty("modifyTime")) {
      assertNull(modelInfo.get(model, "modifyTime"), "The modifyTime of the "
          + "added " + modelName + " must be null.");
    }
    if (modelInfo.hasProperty("deleteTime")) {
      assertNull(modelInfo.get(model, "deleteTime"), "The deleteTime of the "
          + "added " + modelName + " must be null.");
    }
    final Object actual = daoInfo.get(id);  // dao.get(id)
    assertEquals(model, actual, "Getting a added " + modelName
        + " must get an object equals to the added one.");
    return model;
  }

  private void addModelWithNullField(final DaoDynamicTestBuilder builder) {
    for (final Property prop : modelInfo.getProperties()) {
      if ((!prop.isNullable())
          && (!prop.isReadonly())
          && (!prop.isComputed())
          && (!prop.isPrimitive())
          && methodInfo.isUnmodified(prop)) {
        final String displayName = getDisplayName(modelName + " with a null " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Add a {} with a null {}", methodName, modelName,
              prop.getName());
          final Object model = beanCreator.prepare(modelInfo);
          prop.setValue(model, null);
          final NullFieldException e = assertThrows(NullFieldException.class,
              () -> methodInfo.invoke(false, model), // dao.add(model)
              "Adding a " + modelName
              + " with a null " + prop.getName()
              + " must throw a NullFieldException.");
          checkException(e, prop);
        });
      }
    }
  }

  private void addModelWithLongField(final DaoDynamicTestBuilder builder) {
    for (final Property prop : modelInfo.getProperties()) {
      if ((prop.getSizeRange() != null)
          && (prop.getSizeRange().getMax() != null)
          && (prop.getType() == String.class)
          && (!prop.isReadonly())
          && (!prop.isComputed())
          && methodInfo.isUnmodified(prop)) {
        final int maxSize = prop.getSizeRange().getMax();
        final String displayName = getDisplayName(modelName
            + " with a very long " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Add a {} with a very long {}", methodName, modelName,
              prop.getName());
          final Object model = beanCreator.prepare(modelInfo);
          final String longValue = random.nextLetterString(maxSize + 1);
          prop.setValue(model, longValue);
          final FieldTooLongException e = assertThrows(FieldTooLongException.class,
              () -> methodInfo.invoke(false, model), // dao.add(model)
              "Adding a " + modelName
              + " with a very long " + prop.getName()
              + " must throw a FieldTooLongException.");
          checkException(e, prop);
        });
      }
    }
  }

  private void addModelWithDuplicatedField(final DaoDynamicTestBuilder builder) {
    for (final Property prop : modelInfo.getProperties()) {
      if ((prop.isUnique())
          && (!prop.isReadonly())
          && (!prop.isComputed())
          && methodInfo.isUnmodified(prop)) {
        final String displayName = getDisplayName(modelName
            + " with a duplicated " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Add a normal {}", methodName, modelName);
          final Object existingModel = beanCreator.prepare(modelInfo, prop);
          methodInfo.invoke(true, existingModel); // dao.add(existingModel);
          logger.info("Test {}: Add a {} with a duplicated {}", methodName,
              modelName, prop.getName());
          final Object newModel = beanCreator.prepare(modelInfo);
          final String duplicatedValue = setUniquePropertyValues(modelInfo,
              prop, existingModel, newModel);
          final DuplicateKeyException e = assertThrows(DuplicateKeyException.class,
              () -> methodInfo.invoke(false, newModel),  // dao.add(model)
              "Adding a " + modelName
              + " with a duplicated " + prop.getName()
              + " must throw a DuplicateKeyException.");
          checkException(e, prop, duplicatedValue);
        });
      }
    }
  }

  private void addModelWithUnreferencedField(final DaoDynamicTestBuilder builder) {
    // TODO
  }
}
