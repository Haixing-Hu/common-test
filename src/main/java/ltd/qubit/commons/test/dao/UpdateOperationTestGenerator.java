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

import javax.annotation.Nullable;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.error.DuplicateKeyException;
import ltd.qubit.commons.error.FieldTooLongException;
import ltd.qubit.commons.error.NullFieldException;
import ltd.qubit.commons.lang.ArrayUtils;
import ltd.qubit.commons.reflect.Property;

import static ltd.qubit.commons.test.dao.DaoTestUtils.copyAllProperties;
import static ltd.qubit.commons.test.dao.DaoTestUtils.getRespectToParams;
import static ltd.qubit.commons.test.dao.DaoTestUtils.setUniquePropertyValues;
import static ltd.qubit.commons.test.dao.DaoTestUtils.setUnmodifiedRespectToProperties;
import static ltd.qubit.commons.test.dao.DaoTestUtils.setUpdateKeys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UpdateOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public UpdateOperationTestGenerator(final DaoTestGeneratorRegistry factory,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(factory, modelType, methodInfo);
  }

  @Override
 protected void buildTests(final DaoDynamicTestBuilder builder) {
    updateNormalModel(builder);
    updateNonExistingModel(builder);
    if (daoInfo.hasDelete()) {
      updateDeletedModel(builder);
    }
    updateModelWithNullField(builder);
    updateModelWithLongField(builder);
    updateModelWithDuplicatedField(builder);
    updateModelWithUnreferencedField(builder);
  }

  private Object doUpdate(final boolean logging, @Nullable final Object oldModel,
      final Object newModel) throws Throwable {
    if (target == null) {
      // dao.update(newModel), dao.updateByYxx(newModel)
      return methodInfo.invoke(logging, newModel);
    } else {
      final Object[] params = getRespectToParams(newModel, modelInfo, identifier, methodInfo);
      final Object newValue = target.getValue(newModel);
      // dao.updateXxxByYyy(id, newValue)
      final Object modifyTime = methodInfo.invokeWithArguments(logging,
          ArrayUtils.add(params, newValue));
      if (oldModel != null) {
        copyAllProperties(modelInfo, oldModel, newModel);
        target.setValue(newModel, newValue);
      }
      if (modelInfo.hasProperty("modifyTime")) {
        modelInfo.set(newModel, "modifyTime", modifyTime);
      }
      return modifyTime;
    }
  }

  private void updateNormalModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Normal " + modelName);
    final int loops = parameters.getLoops();
    builder.add(displayName, () -> {
      for (int i = 0; i < loops; ++i) {
        logger.info("Test {}: Update a normal {} by {}: {} of {}", methodName,
            modelName, identifierName, i + 1, loops);
        final Object oldModel = addNormalModelImpl();
        final Object newModel = beanCreator.prepare(modelInfo);
        setUpdateKeys(modelInfo, identifier, oldModel, newModel);
        final Object modifyTime = doUpdate(true, oldModel, newModel);  // dao.updateXxxByXxx(...)
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
        final Object updatedModel = daoInfo.get(id);
        checkModifiedProperties(methodInfo, oldModel, newModel, updatedModel);
        checkUnmodifiedProperties(methodInfo, oldModel, updatedModel);
      }
    });
  }

  private Object addNormalModelImpl() throws Throwable {
    final Object model = beanCreator.prepare(modelInfo, identifier);
    daoInfo.add(model); // dao.add(model)
    final Object id = modelInfo.getId(model);
    final Object actual = daoInfo.get(id); // dao.get(id)
    assertEquals(model, actual, "Getting a just added " + modelName
        + " must get an object equals to the added one.");
    return model;
  }

  private void updateNonExistingModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Non existing " + identifierName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Update a non-existing {}", methodName, identifierName);
      final Object newModel = beanCreator.prepare(modelInfo, identifier);
      // 要确保准备好的bean的ID不是null
      if (identifier.getValue(newModel) == null) {
        beanCreator.prepareProperty(newModel, identifier);
      }
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doUpdate(false, null, newModel),
          "Updating a " + modelName
          + " with a non-existing " + identifierName
          + " must throw a DataNotExistException.");
      checkException(e, identifier, identifier.getValue(newModel));
    });
  }

  private void updateDeletedModel(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Deleted " + modelName);
    builder.add(displayName, () -> {
      logger.info("Test {}: Update a deleted {}", methodName, modelName);
      final Object oldModel = beanCreator.prepare(modelInfo, identifier);
      daoInfo.add(oldModel);
      final Object id = modelInfo.getId(oldModel);
      daoInfo.delete(id);
      final Object newModel = beanCreator.prepare(modelInfo);
      setUpdateKeys(modelInfo, identifier, oldModel, newModel);
      final DataNotExistException e = assertThrows(DataNotExistException.class,
          () -> doUpdate(false, oldModel, newModel),
          "Updating a deleted " + modelName + " must throw a DataNotExistException.");
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
          && methodInfo.isModified(prop)) {
        final String displayName = getDisplayName(modelName + " with a null "
            + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Update a {} with a null {}", methodName, modelName,
              prop.getName());
          final Object oldModel = addNormalModelImpl();
          final Object newModel = beanCreator.prepare(modelInfo);
          setUpdateKeys(modelInfo, identifier, oldModel, newModel);
          prop.setValue(newModel, null);
          final NullFieldException e = assertThrows(NullFieldException.class,
              () -> doUpdate(false, oldModel, newModel), "Updating a " + modelName
                  + " with a null " + prop.getName()
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
          && methodInfo.isModified(prop)) {
        final int maxSize = prop.getSizeRange().getMax();
        final String displayName = getDisplayName(modelName
            + " with a very long " + prop.getName());
        builder.add(displayName, () -> {
          logger.info("Test {}: Update a {} with a very long {}", methodName,
              modelName, prop.getName());
          final Object oldModel = addNormalModelImpl();
          final Object newModel = beanCreator.prepare(modelInfo);
          setUpdateKeys(modelInfo, identifier, oldModel, newModel);
          final String longValue = random.nextLetterString(maxSize + 1);
          prop.setValue(newModel, longValue);
          final FieldTooLongException e = assertThrows(FieldTooLongException.class,
              () -> doUpdate(false, oldModel, newModel), "Updating a " + modelName
                  + " with a very long " + prop.getName()
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
          && methodInfo.isModified(prop)) {
        final String displayName = getDisplayName(modelName
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
          final Object oldModel = beanCreator.prepare(modelInfo);
          setUnmodifiedRespectToProperties(methodInfo, prop, existingModel, oldModel);
          daoInfo.add(oldModel);
          logger.debug("Test {}: Add a normal {} as old model: {}",
              methodName, modelName, oldModel);
          final Object newModel = beanCreator.prepare(modelInfo);
          setUpdateKeys(modelInfo, identifier, oldModel, newModel);
          final String duplicatedValue = setUniquePropertyValues(modelInfo,
              prop, existingModel, newModel);
          logger.info("Test {}: Update a {} with a duplicated {}: {}", methodName,
              modelName, prop.getName(), newModel);
          final DuplicateKeyException e = assertThrows(DuplicateKeyException.class,
              () -> doUpdate(false, oldModel, newModel),
              "Updating a " + modelName
              + " with a duplicated " + prop.getName()
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
