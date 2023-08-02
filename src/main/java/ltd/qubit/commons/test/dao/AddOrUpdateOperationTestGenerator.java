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
import java.util.List;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.error.DuplicateKeyException;
import ltd.qubit.commons.error.FieldTooLongException;
import ltd.qubit.commons.error.NullFieldException;
import ltd.qubit.commons.reflect.Property;

import static ltd.qubit.commons.lang.StringUtils.capitalize;
import static ltd.qubit.commons.test.dao.DaoTestUtils.setUniquePropertyValues;
import static ltd.qubit.commons.test.dao.DaoTestUtils.setUnmodifiedRespectToProperties;
import static ltd.qubit.commons.test.dao.DaoTestUtils.setUpdateKeys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddOrUpdateOperationTestGenerator<T>
    extends DaoOperationTestGenerator<T> {

  private final DaoMethodInfo addMethodInfo;
  private final DaoMethodInfo updateMethodInfo;

  public AddOrUpdateOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
    this.addMethodInfo = daoInfo.getAddMethod();
    this.updateMethodInfo = daoInfo.getUpdateMethod(identifier);
    if (this.addMethodInfo == null) {
      throw new IllegalArgumentException("No add() method found for the DAO "
        + daoInfo.getDaoType().getName());
    }
    if (this.updateMethodInfo == null) {
      throw new IllegalArgumentException("No update"
          + (identifier == null ? "()" : "By" + capitalize(identifier.getName()))
          + " method found for the DAO " + daoInfo.getDaoType().getName());
    }
  }

  @Override
  protected void buildTests(final DaoDynamicTestBuilder builder) {
    addNormalModel(builder);
    addModelWithNullField(builder);
    addModelWithLongField(builder);
    addModelWithDuplicatedField(builder);
    addModelWithUnreferencedField(builder);
    updateNormalModel(builder);
    if (daoInfo.hasDelete()) {
      updateDeletedModel(builder);
    }
    updateModelWithNullField(builder);
    updateModelWithLongField(builder);
    updateModelWithDuplicatedField(builder);
    updateModelWithUnreferencedField(builder);
  }

  private void addNormalModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non-existing normal " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Add or update a normal {} by a non-existing {}: "
            + "{} of {}", methodName, modelName, identifierName, i + 1, loops);
        addNormalModelImpl();
      }
    });
  }

  private Object addNormalModelImpl() throws Throwable {
    final Object model = beanCreator.prepare(modelInfo, identifier);
    final Object createTime = methodInfo.invoke(true, model); // dao.addOrUpdateByXXX(model)
    assertNotNull(createTime, "The returned value of " + methodName
        + " must not be null.");
    assertEquals(Instant.class, createTime.getClass(), "The returned type of "
        + methodName + " must be java.time.Instant.");
    final Object id = modelInfo.getId(model);
    assertNotNull(id, "The ID of the added object must not be null.");
    assertEquals(createTime, modelInfo.get(model, "createTime"), "The "
        + methodName + "must return the createTime of the added "
        + modelName + ".");
    if (modelInfo.hasProperty("modifyTime")) {
      assertNull(modelInfo.get(model, "modifyTime"), "The modifyTime of the "
          + "added " + modelName + " must be null.");
    }
    if (modelInfo.hasProperty("deleteTime")) {
      assertNull(modelInfo.get(model, "deleteTime"), "The deleteTime of the "
          + "added " + modelName + " must be null.");
    }
    final Object actual = daoInfo.get(id); // dao.get(id)
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
          && addMethodInfo.isUnmodified(prop)
          && (!prop.equals(methodInfo.getIdentifier()))) {
        final String displayName = getDisplayName("Non-existing " + modelName
            + " with a null " + prop.getName());
        builder.add(displayName, () -> {
          setUp();
          logger.info("Test {}: Add or update a {} with a null {}"
              + " by a non-existing {}", methodName, modelName, prop.getName(),
              identifierName);
          final Object model = beanCreator.prepare(modelInfo, identifier);
          prop.setValue(model, null);
          final NullFieldException e = assertThrows(NullFieldException.class,
              () -> methodInfo.invoke(false, model), // dao.addOrUpdateByXXX(model)
              "Adding or updating a " + modelName
              + " with a null " + prop.getName()
              + " by a non-existing " + identifierName
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
          && addMethodInfo.isUnmodified(prop)) {
        final int maxSize = prop.getSizeRange().getMax();
        final String displayName = getDisplayName("Non-existing " + modelName
              + " with a very long " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Add or update a {} with a very "
              + "long {} by a non-existing {}", methodName, modelName,
              prop.getName(), identifierName);
          final Object model = beanCreator.prepare(modelInfo, identifier);
          final String longValue = random.nextLetterString(maxSize + 1);
          prop.setValue(model, longValue);
          final FieldTooLongException e = assertThrows(FieldTooLongException.class,
              () -> methodInfo.invoke(false, model), // dao.addOrUpdateByXXX(model)
              "Adding or updating " + modelName
              + " with a very long " + prop.getName()
              + " by a non-existing " + identifierName
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
          && addMethodInfo.isUnmodified(prop)
          && (!prop.equals(methodInfo.getIdentifier()))) {
        final String displayName = getDisplayName("Non-existing " + modelName
            + " with a duplicated " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Add a normal {}", methodName, modelName);
          final Object existingModel = beanCreator.prepare(modelInfo, prop);
          methodInfo.invoke(true, existingModel); // dao.add(existingModel);
          logger.info("Test {}: Add or update a {} with a duplicated {} by "
              + "a non-existing {}", methodName, modelName, prop.getName(),
              identifierName);
          final Object newModel = beanCreator.prepare(modelInfo, identifier);
          final String duplicatedValue = setUniquePropertyValues(modelInfo,
              prop, existingModel, newModel);
          final DuplicateKeyException e = assertThrows(DuplicateKeyException.class,
              () -> methodInfo.invoke(false, newModel), // dao.addOrUpdateByXXXX(model)
              "Adding or updating a " + modelName
              + " with a duplicated " + prop.getName()
              + " by a non-existing " + identifierName
              + " must throw a DuplicateKeyException.");
          checkException(e, prop, duplicatedValue);
        });
      }
    }
  }

  private void addModelWithUnreferencedField(final DaoDynamicTestBuilder builder) {
    // TODO
  }

  private void updateNormalModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Existing normal " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      logger.debug("methodInfo: name = {}, modified = {}",
          methodInfo.getName(), methodInfo.getModifiedPropertyNames());
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Add or update a normal {} by an existing {}: "
            + "{} of {}", methodName, modelName, identifierName, i + 1, loops);
        final Object oldModel = addNormalModelImpl();
        final Object newModel = beanCreator.prepare(modelInfo, identifier);
        setUpdateKeys(modelInfo, identifier, oldModel, newModel);
        // dao.addOrUpdateByXXX(newValue)
        final Object modifyTime = methodInfo.invoke(true, newModel);
        assertNotNull(modifyTime, "The returned value of " + methodName
            + " must not be null.");
        assertEquals(Instant.class, modifyTime.getClass(), "The returned type of "
            + methodName + " must be java.time.Instant.");
        if (modelInfo.hasProperty("modifyTime")) {
          assertEquals(modifyTime, modelInfo.get(newModel, "modifyTime"),
              "The " + methodName + " must return the modifyTime of the "
                  + "updated " + modelName + ".");
        }
        final Object id = modelInfo.getId(oldModel);
        final Object updatedModel = daoInfo.get(id); // updated = dao.get(existing.id)
        checkModifiedProperties(updateMethodInfo, oldModel, newModel, updatedModel);
        checkUnmodifiedProperties(updateMethodInfo, oldModel, updatedModel);
      }
    });
  }

  private void updateDeletedModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Deleted " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Add or update a deleted {}", methodName, modelName);
      final Object oldModel = beanCreator.prepare(modelInfo, identifier);
      daoInfo.add(oldModel);
      final Object id = modelInfo.getId(oldModel);
      daoInfo.delete(id);
      final Object newModel = beanCreator.prepare(modelInfo, identifier);
      setUpdateKeys(modelInfo, identifier, oldModel, newModel);
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> methodInfo.invoke(false, newModel),
          "Adding or updating a deleted " + modelName
              + " must throw a DataNotExistException.");
      checkException(e, identifier, identifier.getValue(oldModel));
    });
  }

  private void updateModelWithNullField(final DaoDynamicTestBuilder builder) {
    final List<Property> respectTo = modelInfo.getRespectToProperties(identifier);
    for (final Property prop : modelInfo.getProperties()) {
      if (prop.equals(identifier)) {
        continue;  // should not set the identifier to null
      }
      if (respectTo.contains(prop)) {
        continue; // should not set the identifier respect to properties to null
      }
      if ((!prop.isNullable())
          && (!prop.isReadonly())
          && (!prop.isComputed())
          && (!prop.isPrimitive())
          && updateMethodInfo.isModified(prop)) {
        final String displayName = getDisplayName("Existing " + modelName
            + " with a null " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Add or update a {} with a null {} by an "
              + "existing {}", methodName, modelName, prop.getName(),
              identifierName);
          final Object oldModel = addNormalModelImpl();
          final Object newModel = beanCreator.prepare(modelInfo, identifier);
          setUpdateKeys(modelInfo, identifier, oldModel, newModel);
          prop.setValue(newModel, null);
          final NullFieldException e = assertThrows(NullFieldException.class,
              () -> methodInfo.invoke(false, newModel),  // dao.addOrUpdateByXXXX(newModel)
              "Adding or updating a " + modelName
              + " with a null " + prop.getName()
              + " by an existing " + identifierName
              + " must throw a NullFieldException.");
          checkException(e, prop);
        });
      }
    }
  }

  private void updateModelWithLongField(final DaoDynamicTestBuilder builder) {
    for (final Property prop : modelInfo.getProperties()) {
      if (prop.equals(identifier)) {
        continue;
      }
      if ((prop.getSizeRange() != null)
          && (prop.getSizeRange().getMax() != null)
          && (prop.getType() == String.class)
          && (!prop.isReadonly())
          && (!prop.isComputed())
          && updateMethodInfo.isModified(prop)) {
        final int maxSize = prop.getSizeRange().getMax();
        final String displayName = getDisplayName("Existing " + modelName
            + " with a very long " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Add or update a {} with a very long {} by an "
              + "existing {}", methodName, modelName, prop.getName(),
              identifierName);
          final Object oldModel = addNormalModelImpl();
          final Object newModel = beanCreator.prepare(modelInfo, identifier);
          setUpdateKeys(modelInfo, identifier, oldModel, newModel);
          final String longValue = random.nextLetterString(maxSize + 1);
          prop.setValue(newModel, longValue);
          final FieldTooLongException e = assertThrows(FieldTooLongException.class,
              () -> methodInfo.invoke(false, newModel),
              "Adding or updating a " + modelName
              + " with a very long " + prop.getName()
              + " by an existing " + identifierName
              + " must throw a FieldTooLongException.");
          checkException(e, prop);
        });
      }
    }
  }

  private void updateModelWithDuplicatedField(final DaoDynamicTestBuilder builder) {
    for (final Property prop : modelInfo.getProperties()) {
      if ((prop.isUnique())
          && (!prop.isReadonly())
          && (!prop.isComputed())
          && updateMethodInfo.isModified(prop)) { // 注意需要通过update方法来确定修改的属性
        final String displayName = getDisplayName("Existing " + modelName
            + " with a duplicated " + prop.getName());
        builder.add(displayName, () -> {
          final Object existingModel = beanCreator.prepare(modelInfo, prop);
          daoInfo.add(existingModel); // dao.add(model)
          logger.debug("Test {}: Add a normal {} as existing model: {}",
              methodName, modelName, existingModel);
          // 注意，如果当前Unique属性prop的某个respectTo属性未被当前被测试方法修改，
          // 那么我们需要将 oldModel 的这些未被修改的respectTo属性和existingModel的这些
          // 属性设为同样的值才能进行期望的测试。
          // 例如，Category.name 在 Category.entity下是unique的，
          // existingMode.id = 1, existingModel.entity = 'e', existingModel.name = 'x';
          // oldModel.id = 2, oldModel.entity = 'e', oldModel.name = 'y';
          // newModel.id = 2, newModel.entity = 'e', newModel.name = 'x';
          // 上面这样的三组数据才能进行期望的 duplicated key 异常测试
          final Object oldModel = beanCreator.prepare(modelInfo, identifier);
          // 注意需要通过update方法来复制未被修改的属性
          setUnmodifiedRespectToProperties(updateMethodInfo, prop, existingModel, oldModel);
          daoInfo.add(oldModel);
          logger.debug("Test {}: Add a normal {} as old model: {}",
              methodName, modelName, oldModel);
          final Object newModel = beanCreator.prepare(modelInfo, identifier);
          setUpdateKeys(modelInfo, identifier, oldModel, newModel);
          final String duplicatedValue = setUniquePropertyValues(modelInfo,
              prop, existingModel, newModel);
          logger.info("Test {}: Update a {} with a duplicated {}: {}", methodName,
              modelName, prop.getName(), newModel);
          final DuplicateKeyException e = assertThrows(DuplicateKeyException.class,
              () -> methodInfo.invoke(false, newModel),  // dao.addOrUpdateByXXXX(newModel)
              "Adding or updating a " + modelName
              + " with a duplicated " + prop.getName()
              + " by an existing " + identifierName
              + " must throw a DuplicateKeyException.");
          checkException(e, prop, duplicatedValue);
        });
      }
    }
  }

  private void updateModelWithUnreferencedField(final DaoDynamicTestBuilder builder) {
    //  TODO
  }
}
