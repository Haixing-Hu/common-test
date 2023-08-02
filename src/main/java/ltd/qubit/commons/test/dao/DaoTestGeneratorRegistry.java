////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ltd.qubit.commons.lang.ClassKey;
import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.test.TestGenerator;

import org.junit.jupiter.api.DynamicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ltd.qubit.commons.lang.Argument.requireNonNull;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;

/**
 * A test factory registry used to generate integration test factory on the DAO
 * objects.
 *
 * @author Haixing Hu
 */
public class DaoTestGeneratorRegistry {

  private final Logger logger = LoggerFactory.getLogger(DaoTestGeneratorRegistry.class);
  private final DaoRegistry daoRegistry = new DaoRegistry();
  private final Map<ClassKey, BeanInfo> modelInfoRegistry = new HashMap<>();
  private final Map<ClassKey, DaoInfo> daoInfoRegistry = new HashMap<>();
  private final DaoTestParameters parameters = new DaoTestParameters();
  private final RandomBeanGenerator random = new RandomBeanGenerator();

  public DaoTestGeneratorRegistry() {}

  public final DaoRegistry getDaoRegistry() {
    return daoRegistry;
  }

  public DaoTestParameters getParameters() {
    return parameters;
  }

  public final RandomBeanGenerator getRandom() {
    return random;
  }

  public final DaoTestGeneratorRegistry register(final Class<?> modelType,
      final Object dao) {
    final BeanInfo beanInfo = BeanInfo.of(modelType);
    final DaoInfo daoInfo = new DaoInfo(modelType, dao);
    if (beanInfo.getIdProperty() == null) {
      throw new IllegalArgumentException("No ID property (marked with "
          + "@Identifier) found in the model " + modelType.getSimpleName());
    }
    final ClassKey key = new ClassKey(modelType);
    daoRegistry.put(key, dao);
    modelInfoRegistry.put(key, beanInfo);
    daoInfoRegistry.put(key, daoInfo);
    return this;
  }

  public BeanInfo getModelInfo(final Class<?> modelType) {
    final ClassKey key = new ClassKey(requireNonNull("modelType", modelType));
    return modelInfoRegistry.get(key);
  }

  public DaoInfo getDaoInfo(final Class<?> modelType) {
    final ClassKey key = new ClassKey(requireNonNull("modelType", modelType));
    return daoInfoRegistry.get(key);
  }

  public DaoMethodInfo getMethodInfo(final Class<?> modelType, final Method method) {
    final ClassKey key = new ClassKey(requireNonNull("modelType", modelType));
    final DaoInfo daoInfo = daoInfoRegistry.get(key);
    final Map<Method, DaoMethodInfo> methodInfos = daoInfo.getMethodInfoMap();
    return methodInfos.get(requireNonNull("method", method));
  }

  public <T> List<DynamicNode> generate(final Class<T> modelType) throws Exception {
    return getGenerator(modelType).generate();
  }

  public <T> List<DynamicNode> generate(final Class<T> modelType, final String methodName)
      throws Exception {
    return getGenerator(modelType, methodName).generate();
  }

  public <T> TestGenerator getGenerator(final Class<T> modelType) {
    final ClassKey key = new ClassKey(requireNonNull("modelType", modelType));
    final Object dao = daoRegistry.get(key);
    final BeanInfo modelInfo = modelInfoRegistry.get(key);
    final DaoInfo daoInfo = daoInfoRegistry.get(key);
    if (dao == null || modelInfo == null || daoInfo == null) {
      throw new IllegalArgumentException("The DAO of the model "
          + modelType.getName() + " was not registered.");
    }
    final DaoTestGeneratorRegistry registry = this;
    return new TestGenerator(random, parameters) {
      @Override
      public List<DynamicNode> generate() throws Exception {
        final Map<Method, DaoMethodInfo> methodInfoMap = daoInfo.getMethodInfoMap();
        final List<DynamicNode> result = new ArrayList<>();
        final List<DaoMethodInfo> methodInfos = new ArrayList<>(methodInfoMap.values());
        Collections.sort(methodInfos);
        for (final DaoMethodInfo method : methodInfos) {
          final DaoOperation operation = method.getOperation();
          final DaoOperationTestGenerator<T> generator =
              operation.getGenerator(registry, modelType, method);
          generator.setRandom(registry.random);
          final String displayName = generator.getDisplayName("");
          final DynamicNode node = dynamicContainer(displayName, generator.generate());
          result.add(node);
        }
        return result;
      }
    };
  }

  public TestGenerator getGenerator(final Class<?> modelType, final String methodName) {
    final ClassKey key = new ClassKey(requireNonNull("modelType", modelType));
    final Object dao = daoRegistry.get(key);
    final BeanInfo modelInfo = modelInfoRegistry.get(key);
    final DaoInfo daoInfo = daoInfoRegistry.get(key);
    if (dao == null || modelInfo == null || daoInfo == null) {
      throw new IllegalArgumentException("The DAO of the model "
          + modelType.getName() + " was not registered.");
    }
    final DaoMethodInfo methodInfo = daoInfo.getMethodInfo(methodName);
    if (methodInfo == null) {
      throw new IllegalArgumentException("No method " + methodName + " for "
          + "the DAO " + daoInfo.getName());
    }
    final DaoOperation operation = methodInfo.getOperation();
    return operation.getGenerator(this, modelType, methodInfo);
  }
}
