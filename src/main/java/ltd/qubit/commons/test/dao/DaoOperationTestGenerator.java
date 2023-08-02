////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import jakarta.validation.constraints.NotNull;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.error.DuplicateKeyException;
import ltd.qubit.commons.error.FieldTooLongException;
import ltd.qubit.commons.error.NullFieldException;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.ClassUtils;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.test.TestGenerator;

import org.junit.jupiter.api.DynamicNode;

import static ltd.qubit.commons.lang.Argument.requireNonNull;
import static ltd.qubit.commons.lang.ObjectUtils.defaultIfNull;
import static ltd.qubit.commons.test.dao.DaoTestUtils.fixMySqlValueLength;
import static ltd.qubit.commons.test.dao.DaoTestUtils.getColumnName;
import static ltd.qubit.commons.test.dao.DaoTestUtils.getTableName;
import static ltd.qubit.commons.test.dao.DaoTestUtils.normalize;
import static ltd.qubit.commons.text.CaseFormat.LOWER_CAMEL;
import static ltd.qubit.commons.text.CaseFormat.LOWER_UNDERSCORE;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * A test factory generates test for DAO operations.
 *
 * @param <T>
 *     the type of the model under testing.
 * @author Haixing Hu
 */
public abstract class DaoOperationTestGenerator<T> extends TestGenerator {

  protected final DaoTestGeneratorRegistry registry;
  protected final Class<T> modelType;
  protected final BeanInfo modelInfo;
  protected final DaoInfo daoInfo;
  protected final DaoMethodInfo methodInfo;
  protected final String methodName;
  protected final String modelName;
  protected final Property identifier;
  protected final String identifierName;
  protected final Property target;
  protected final String targetName;
  protected final BeanCreator beanCreator;

  protected DaoOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry.getRandom(), registry.getParameters());
    this.registry = requireNonNull("registry", registry);
    this.modelType = requireNonNull("modelType", modelType);
    this.modelInfo = registry.getModelInfo(modelType);
    this.daoInfo = registry.getDaoInfo(modelType);
    this.methodInfo = requireNonNull("methodInfo", methodInfo);
    this.methodName = methodInfo.getQualifiedName();
    this.modelName = modelInfo.getName();
    this.identifier = defaultIfNull(methodInfo.getIdentifier(), modelInfo.getIdProperty());
    if (identifier == null) {
      throw new IllegalArgumentException("No ID property found for the model: "
          + modelInfo.getName());
    }
    this.identifierName = identifier.getQualifiedName();
    this.target = methodInfo.getTarget();
    this.targetName = (target == null ? modelInfo.getName()
                                      : target.getQualifiedName());
    this.beanCreator = new BeanCreator(registry);
  }

  public URI getUri() {
    return methodInfo.getUri();
  }

  protected void setUp() throws Throwable {
    logger.info("Setting up test case ...");
    // do nothing
  }

  protected void tearDown() throws Throwable {
    logger.info("Tearing down test case ...");
    logger.info("Clear all the entries.");
    daoInfo.clear();    // FIXME: should clear all depended entries too.
  }

  protected abstract void buildTests(DaoDynamicTestBuilder builder);

  public final List<DynamicNode> generate() throws Exception {
    final DaoDynamicTestBuilder builder = new DaoDynamicTestBuilder(this);
    buildTests(builder);
    return builder.build();
  }

  /**
   * 获取当前测试用例的显示名称。
   *
   * @param message
   *     额外消息。
   * @return
   *     当前测试用例的显示名称。
   */
  protected String getDisplayName(@Nullable final String message) {
    final StringBuilder builder = new StringBuilder();
    builder.append("Test ")
           .append(methodInfo.getQualifiedName());
    if (message != null && !message.isEmpty()) {
      builder.append(": ").append(message);
    }
    return builder.toString();
  }

  /**
   * 检查指定的异常所包含的信息是否正确。
   *
   * @param e
   *     指定的异常对象。
   * @param key
   *     指定的异常对应的属性。
   * @param value
   *     指定的异常对应的属性的值。
   */
  protected void checkException(final DataNotExistException e,
      final Property key, final Object value) {
    assertEquals(getTableName(modelInfo), e.getEntity(),
        "Thrown DataNotExistException must catch the entity type.");
    assertEquals(LOWER_CAMEL.to(LOWER_UNDERSCORE, key.getName()), e.getKey(),
        "Thrown DataNotExistException must catch the identifier field.");
    assertEquals(value, e.getValue(),
        "Thrown DataNotExistException must catch the identifier value.");
  }

  /**
   * 检查指定的异常所包含的信息是否正确。
   *
   * @param e
   *     指定的异常对象。
   * @param key
   *     指定的异常对应的属性。
   */
  protected void checkException(final NullFieldException e, final Property key) {
    final String expectedColumnName = getColumnName(key);
    final String actualColumnName = e.getField();
    // 如果实际字段名和期望字段名一样，则没问题
    if (expectedColumnName.equals(actualColumnName)) {
      return; // correct
    }
    // 如果实际字段名表示的是期望字段名代表的属性的某个子属性，也没问题
    // 例如，Upload.file 是一个非空属性，我们设置某个Upload对象的file为空，
    // Dao.add()抛出一个NullFieldException，且该异常的 field 字段名为 file_url，
    // 因为在数据库中，Upload.file 对象被拆解为若干行，其中第一行 file_url 表示
    // file.url 属性，数据库中设置其不能为空，因此抛出的 NullFieldException 的
    // field 值为 "file_url" 而非 "file"。但这也是正确的。
    if (actualColumnName.startsWith(expectedColumnName + '_')) {
      return; // correct, 实际字段名是期望字段名表示的对象的子属性
    }
    fail("Thrown NullFieldException must catch the null field: expected = '"
        + expectedColumnName + "', actual = '" + actualColumnName + "'");
  }

  /**
   * 检查指定的异常所包含的信息是否正确。
   *
   * @param e
   *     指定的异常对象。
   * @param key
   *     指定的异常对应的属性。
   */
  protected void checkException(final FieldTooLongException e, final Property key) {
    final String expectedColumnName = getColumnName(key);
    final String actualColumnName = e.getField();
    // 如果实际字段名和期望字段名一样，则没问题
    if (expectedColumnName.equals(actualColumnName)) {
      return; // correct
    }
    // 如果实际字段名表示的是期望字段名代表的属性的某个子属性，也没问题
    // 例如，Upload.file 是一个非空属性，我们设置某个Upload对象的file为空，
    // Dao.add()抛出一个NullFieldException，且该异常的 field 字段名为 file_url，
    // 因为在数据库中，Upload.file 对象被拆解为若干行，其中第一行 file_url 表示
    // file.url 属性，数据库中设置其不能为空，因此抛出的 NullFieldException 的
    // field 值为 "file_url" 而非 "file"。但这也是正确的。
    if (actualColumnName.startsWith(expectedColumnName + '_')) {
      return; // correct, 实际字段名是期望字段名表示的对象的子属性
    }
    fail("Thrown FieldTooLongException must catch the long field: expected = '"
        + expectedColumnName + "', actual = '" + actualColumnName + "'");
  }

  /**
   * 检查指定的异常所包含的信息是否正确。
   *
   * @param e
   *     指定的异常对象。
   * @param key
   *     指定的异常对应的属性。
   * @param value
   *     指定的异常对应的属性的值。
   */
  protected static void checkException(final DuplicateKeyException e,
      final Property key, final Object value) {
    assertEquals(LOWER_CAMEL.to(LOWER_UNDERSCORE, key.getName()), e.getKey(),
        "Thrown DuplicateKeyException must catch the duplicated field.");
    assertEquals(fixMySqlValueLength(value), fixMySqlValueLength(e.getValue()),
        "Thrown DuplicateKeyException must catch the duplicated value.");
  }

  /**
   * 检查指定的两个模型对象的相等性。
   *
   * @param modelInfo
   *     该模型对象的类型信息。
   * @param expected
   *     期望的实体对象的值。
   * @param actual
   *     实体对象的实际值。
   * @param message
   *     若两者不等时显示的错误消息。
   */
  protected void checkModelEquals(final BeanInfo modelInfo, @Nullable final Object expected,
      @Nullable final Object actual, final String message) {
    if (expected == null) {
      assertNull(actual, message);
    } else if (actual == null) {
      fail(message);
    } else if (! expected.equals(actual)) {
      for (final Property prop : modelInfo.getNonComputedProperties()) {
        final Object expectedPropValue = prop.getValue(expected);
        final Object actualPropValue = prop.getValue(actual);
        assertValueEquals(prop.getType(), prop.isReference(), expectedPropValue,
            actualPropValue, message);
      }
    }
  }

  /**
   * 检查指定的操作执行后应该被修改的属性值是否正确。
   *
   * @param methodInfo
   *     指定的操作的具体信息。
   * @param oldModel
   *     更新前的实体对象。
   * @param newModel
   *     用于更新的实体对象。
   * @param updatedModel
   *     更新后的实体对象。
   */
  protected void checkModifiedProperties(final DaoMethodInfo methodInfo,
      @Nullable final Object oldModel, @NotNull final Object newModel,
      @NotNull final Object updatedModel) {
    assert newModel != null && updatedModel != null;
    for (final Property prop : methodInfo.getModifiedProperties()) {
      final Object oldValue = (oldModel == null ? null : prop.getValue(oldModel));
      final Object newValue = normalize(prop.getValue(newModel));
      final Object updatedValue = normalize(prop.getValue(updatedModel));
      logger.info("[{}] Verifying updated {}: {} -> {}",
          methodInfo.getQualifiedName(), prop.getQualifiedName(), oldValue, newValue);
      assertValueEquals(prop.getType(), prop.isReference(), newValue, updatedValue,
          "The property " + prop.getQualifiedName() + " should be updated.");
    }
  }

  /**
   * 检查指定的操作执行后不该被修改的属性值是否正确。
   *
   * @param methodInfo
   *     指定的操作的具体信息。
   * @param oldModel
   *     更新前的实体对象。
   * @param updatedModel
   *     更新后的实体对象。
   */
  protected void checkUnmodifiedProperties(final DaoMethodInfo methodInfo,
      @NotNull final Object oldModel, @NotNull final Object updatedModel) {
    assert oldModel != null && updatedModel != null;
    final Set<Property> unmodifiedProperties = methodInfo.getUnmodifiedProperties();
    for (final Property prop : unmodifiedProperties) {
      if (prop.isReadonly() || prop.isComputed()) {
        continue;
      }
      final Object oldValue = normalize(prop.getValue(oldModel));
      final Object updatedValue = normalize(prop.getValue(updatedModel));
      logger.info("[{}] Verifying unmodified {}: {}",
          methodInfo.getQualifiedName(), prop.getQualifiedName(), updatedValue);
      assertValueAbsoluteEquals(prop.getType(), oldValue, updatedValue,
          "The property " + prop.getQualifiedName() + " should NOT be updated.");
    }
  }

  /**
   * 确保实际的值和期望值一致。
   *
   * <p>此操作会考虑被比较的值的内部结构。如果是复杂的实体对象，还需考虑更新操作对其
   * 带来的影响（即其某些属性会被改变某些属性不会被改变）。</p>
   *
   * @param type
   *     待比较的值的类型。
   * @param isReference
   *     待比较的值是否是对某个实体的引用
   * @param expected
   *     期望的值。
   * @param actual
   *     实际的值。
   * @param message
   *     错误消息。
   */
  protected void assertValueEquals(final Class<?> type, final boolean isReference,
      @Nullable final Object expected, @Nullable final Object actual,
      final String message) {
    if (expected == null) {
      assertNull(actual, message);
    } else if (actual == null) {
      fail(message);
    } else if (type.isPrimitive()) {                // 处理primitive类型
      assertEquals(expected, actual, message);
    } else if (type.isArray()) {                    // 递归处理数组类型
      assertArrayValueEquals(type, isReference, (Object[]) expected,
          (Object[]) actual, message);
    } else if (ClassUtils.isCollectionType(type)) { // 递归处理集合类型
      assertCollectionValueEquals(type, isReference, (Collection<?>) expected,
          (Collection<?>) actual, message);
    } else if (ClassUtils.isCollectionType(type)) { // 递归处理集合类型
      assertCollectionValueEquals(type, isReference, (Collection<?>) expected,
          (Collection<?>) actual, message);
    } else if (ClassUtils.isMapType(type)) {        // 递归处理映射类型
      assertMapValueEquals(type, isReference, (Map<?, ?>) expected,
          (Map<?, ?>) actual, message);
    } else if (isReference) {                       // 若待比较的值是对某个实体对象的引用
      final BeanInfo typeInfo = BeanInfo.of(type);
      if (typeInfo.hasIdProperty()) {
        // 若待比较的值是对某个实体对象的引用，且该值有ID属性，则比较其ID即可
        final Object expectedId = typeInfo.getId(expected);
        final Object actualId = typeInfo.getId(actual);
        assertEquals(expectedId, actualId, message);
      } else {
        // 直接比较两个值是否绝对相等
        assertValueAbsoluteEquals(type, expected, actual, message);
      }
    } else {    // 其他情况
      // 直接比较两个值是否绝对相等
      assertValueAbsoluteEquals(type, expected, actual, message);
    }
  }

  /**
   * 确保实际的数组和期望数组一致。
   *
   * <p>此操作会考虑被比较的数组元素的内部结构。如果是复杂的实体对象，还需考虑更新操作对其
   * 带来的影响（即其某些属性会被改变某些属性不会被改变）。</p>
   *
   * <p>此函数将递归调用{@link #assertValueEquals(Class, boolean, Object, Object, String)}</p>
   *
   * @param type
   *     待比较的数组的类型。
   * @param expected
   *     期望的数组。
   * @param actual
   *     实际的数组。
   * @param message
   *     错误消息。
   */
  private void assertArrayValueEquals(final Class<?> type, final boolean isReference,
      final Object[] expected, final Object[] actual, final String message) {
    assertEquals(expected.length, actual.length, message);
    final Class<?> elementType = type.getComponentType();
    for (int i = 0; i < expected.length; ++i) {
      final Object expectedElement = expected[i];
      final Object actualElement = actual[i];
      assertValueEquals(elementType, isReference, expectedElement,
          actualElement, message); // 递归调用
    }
  }

  /**
   * 确保实际的集合和期望集合一致。
   *
   * <p>此操作会考虑被比较的集合元素的内部结构。如果是复杂的实体对象，还需考虑更新操作对其
   * 带来的影响（即其某些属性会被改变某些属性不会被改变）。</p>
   *
   * <p>此函数将递归调用{@link #assertValueEquals(Class, boolean, Object, Object, String)}</p>
   *
   * @param type
   *     待比较的集合的类型。
   * @param expected
   *     期望的集合。
   * @param actual
   *     实际的集合。
   * @param message
   *     错误消息。
   */
  private void assertCollectionValueEquals(final Class<?> type, final boolean isReference,
      final Collection<?> expected, final Collection<?> actual,
      final String message) {
    assertEquals(expected.size(), actual.size(), message);
    // FIXME: 对于无序集合，比如Set，如何处理？
    final Iterator<?> expectedIter = expected.iterator();
    final Iterator<?> actualIter = actual.iterator();
    while (expectedIter.hasNext()) {
      final Object expectedElement = expectedIter.next();
      final Object actualElement = actualIter.next();
      if (expectedElement == null) {
        assertNull(actualElement, message);
      } else if (actualElement == null) {
        fail(message);
      } else {
        final Class<?> elementType = expectedElement.getClass();
        assertValueEquals(elementType, isReference, expectedElement,
            actualElement, message); // 递归调用
      }
    }
  }

  /**
   * 确保实际的映射和期望映射一致。
   *
   * <p>此操作会考虑被比较的映射的元素的内部结构。如果是复杂的实体对象，还需考虑更新操作对其
   * 带来的影响（即其某些属性会被改变某些属性不会被改变）。</p>
   *
   * <p>此函数将递归调用{@link #assertValueEquals(Class, boolean, Object, Object, String)}</p>
   *
   * @param type
   *     待比较的映射的类型。
   * @param expected
   *     期望的映射。
   * @param actual
   *     实际的映射。
   * @param message
   *     错误消息。
   */
  private void assertMapValueEquals(final Class<?> type, final boolean isReference,
      final Map<?, ?> expected, final Map<?, ?> actual, final String message) {
    assertEquals(expected.size(), actual.size(), message);
    for (final Map.Entry<?, ?> entry : expected.entrySet()) {
      final Object key = entry.getKey();
      final Object expectedValue = entry.getValue();
      final Object actualValue = actual.get(key);
      if (expectedValue == null) {
        assertNull(actualValue, message);
      } else if (actualValue == null) {
        fail(message);
      } else {
        final Class<?> elementType = expectedValue.getClass();
        assertValueEquals(elementType, isReference, expectedValue,
            actualValue, message); // 递归调用
      }
    }
  }

  /**
   * 确保实际的值和期望值完全一致。
   *
   * <p>此操作不考虑被比较的值的内部复杂结构，仅通过{@link Object#equals(Object)}函数
   * 判定被比较的值是否完全一致。</p>
   *
   * @param type
   *     待比较的值的类型。
   * @param expected
   *     期望的值。
   * @param actual
   *     实际的值。
   * @param message
   *     错误消息。
   */
  private void assertValueAbsoluteEquals(final Class<?> type, final Object expected,
      final Object actual, final String message) {
    if (type.isArray()) {
      assertArrayEquals((Object[]) expected, (Object[]) actual, message);
    } else {
      assertEquals(expected, actual, message);
    }
  }

  private void assertSameNullability(final Object expected,
      final Object actual, final String message) {
    if (expected == null) {
      assertNull(actual, message);
    } else {
      assertNotNull(actual, message);
    }
  }
}
