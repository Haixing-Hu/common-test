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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.Nullable;

import ltd.qubit.commons.annotation.TypeCodec;
import ltd.qubit.commons.lang.ArrayUtils;
import ltd.qubit.commons.lang.Comparison;
import ltd.qubit.commons.lang.StringUtils;
import ltd.qubit.commons.random.EasyRandom;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.ClassUtils;
import ltd.qubit.commons.reflect.ConstructorUtils;
import ltd.qubit.commons.reflect.MethodByNameComparator;
import ltd.qubit.commons.reflect.MethodUtils;
import ltd.qubit.commons.reflect.Option;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.sql.Criterion;
import ltd.qubit.commons.sql.SimpleCriterion;
import ltd.qubit.commons.util.codec.Codec;
import ltd.qubit.commons.util.codec.EncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Boolean.FALSE;

import static ltd.qubit.commons.lang.StringUtils.substring;
import static ltd.qubit.commons.reflect.MethodUtils.getMatchingMethod;
import static ltd.qubit.commons.reflect.MethodUtils.hasMethod;
import static ltd.qubit.commons.reflect.MethodUtils.invokeMethod;
import static ltd.qubit.commons.sql.impl.CriterionImplUtils.isSupportedNonArrayDataType;
import static ltd.qubit.commons.text.CaseFormat.LOWER_CAMEL;
import static ltd.qubit.commons.text.CaseFormat.LOWER_UNDERSCORE;
import static ltd.qubit.commons.text.CaseFormat.UPPER_CAMEL;

/**
 * Utility functions for implementing the {@link DaoTestGeneratorRegistry}.
 *
 * @author Haixing Hu
 */
public class DaoTestUtils {
  public static final String DAO_SUFFIX = "Dao";
  private static final Logger LOGGER = LoggerFactory.getLogger(DaoTestUtils.class);

  private static final int MYSQL_ERROR_VALUE_MAX_LENGTH = 64;

  /**
   * Gets the name of the interface of the DAO of a model.
   *
   * @param modelType
   *     the type of the model.
   * @return
   *     the name of the interface of the DAO of the model.
   */
  public static String getDaoInterfaceName(final Class<?> modelType) {
    final String modelName = modelType.getSimpleName();
    return modelName + DAO_SUFFIX;
  }

  /**
   * Gets the class of the interface of the DAO of a model.
   *
   * @param modelType
   *     the type of the model.
   * @param dao
   *     the DAO object of the model.
   * @return
   *     the class of the interface of the DAO of the model, or {@code null} if
   *     no such interface.
   */
  public static Class<?> getDaoInterface(final Class<?> modelType, final Object dao) {
    final String daoTypeName = getDaoInterfaceName(modelType);
    final Class<?> daoImplType = dao.getClass();
    final Class<?> daoType = ClassUtils.getInterface(daoImplType, daoTypeName);
    if (daoType == null) {
      LOGGER.error("Cannot get the DAO interface: daoTypeName = {}, daoImplType = {}",
          daoTypeName, daoImplType);
    }
    return daoType;
  }

  /**
   * Gets the list of methods of the DAO of a model.
   *
   * @param modelType
   *     the type of the model.
   * @param dao
   *     the DAO object of the model.
   * @return
   *     the list of methods of the DAO of the model.
   */
  public static Method[] getDaoMethods(final Class<?> modelType, final Object dao) {
    final Class<?> daoInterface = getDaoInterface(modelType, dao);
    if (daoInterface == null) {
      throw new IllegalArgumentException("Cannot find the standard DAO interface of the model "
        + modelType.getSimpleName());
    }
    final List<Method> result = MethodUtils.getAllMethods(daoInterface, Option.BEAN_METHOD);
    result.sort(new MethodByNameComparator());
    return result.toArray(new Method[0]);
  }

  public static String getTableName(final BeanInfo modelInfo) {
    return UPPER_CAMEL.to(LOWER_UNDERSCORE, modelInfo.getName());
  }

  public static String getColumnName(final Property property) {
    return LOWER_CAMEL.to(LOWER_UNDERSCORE, property.getName());
  }

  /**
   * Fixes the length of a value returned by the error message of the MySQL
   * database.
   *
   * <p>The column value returned by the MySQL database has a limitation of 64
   * characters. Therefore, we should fix the length of the expecting value
   * while comparing it with the actual value captured from the error messages
   * returned by MySQL.
   *
   * @param value
   *     the value to be fixed.
   * @return
   *     the fixing result.
   */
  public static String fixMySqlValueLength(@Nullable final Object value) {
    return substring(String.valueOf(value), 0, MYSQL_ERROR_VALUE_MAX_LENGTH);
  }

  /**
   * Converts a stack of classes to a string.
   *
   * @param stack
   *     the specified stack of classes.
   * @return
   *     the string representation of the stack.
   */
  public static String stackToString(final Stack<Class<?>> stack) {
    final StringBuilder builder = new StringBuilder();
    for (final Class<?> cls : stack) {
      if (builder.length() > 0) {
        builder.append(" -> ");
      }
      builder.append(cls.getName());
    }
    return builder.toString();
  }

  /**
   * 获取指定的值的字符串形式。
   *
   * @param type
   *      指定的值的类对象。
   * @param value
   *     指定的值。
   * @return
   *     该主键值的字符串形式。
   */
  public static String toStringRepresentation(final Class<?> type, @Nullable final Object value) {
    if (value == null) {
      return "";
    }
    if (type.getName().startsWith("java")) {                  // 对于Java内部类，直接返回其 toString()
      return value.toString();
    } else if (Enum.class.isAssignableFrom(type)) {           // 对于枚举类，返回其名称
      return ((Enum<?>) value).name();
    } else if (type.isAnnotationPresent(TypeCodec.class)) {   // 对于标记了 @TypeCodec 的类，根据编码器返回其字符串形式
      final Class<?> codecClass = type.getAnnotation(TypeCodec.class).value();
      try {
        @SuppressWarnings("unchecked")
        final Codec<Object, String> codec =
            (Codec<Object, String>) ConstructorUtils.newInstance(codecClass);
        return codec.encode(value);
      } catch (final EncodingException e) {
        throw new RuntimeException(e);
      }
    } else {
      final BeanInfo info = BeanInfo.of(type);
      if (info.hasIdProperty()) {                           // 如果该对象有ID属性，直接返回其ID属性值
        final Object id = info.getId(value);
        return (id == null ? "" : id.toString());
      } else {
        // if there is no ID property, use all the properties to build a string
        final StringBuilder builder = new StringBuilder();
        // ignore the computed and JDK built-in fields
        final List<Property> props = info.getProperties(
            p -> ((!p.isComputed()) && (!p.isJdkBuiltIn())));
        // sort the property by their key indexes
        props.sort((x, y) -> Comparison.compare(x.getKeyIndex(), y.getKeyIndex()));
        for (final Property prop : props) {
          final Object propValue = prop.getValue(value);
          if (builder.length() > 0) {
            builder.append('-');
          }
          builder.append(toStringRepresentation(prop.getType(), propValue));
        }
        return builder.toString();
      }
    }
  }

  public static String setUniquePropertyValues(final BeanInfo modelInfo,
      final Property uniqueProperty, final Object existingModel,
      final Object newModel) {
    final List<Property> props = modelInfo.getRespectToProperties(uniqueProperty);
    final StringBuilder builder = new StringBuilder();
    for (final Property prop : props) {
      final Object propValue = prop.getValue(existingModel);
      prop.setValue(newModel, propValue);
      if (builder.length() > 0) {
        builder.append('-');
      }
      builder.append(toStringRepresentation(prop.getType(), propValue));
    }
    return builder.toString();
  }

  /**
   * 获取指定对象的主键。
   *
   * <p>此函数用于获取一些DAO方法的参数值。例如，{@code updateStateByCode()}方法，根据
   * 编码更新模型的状态。如果该模型的编码是全局唯一的，那么该方法应该有两个参数，第一个参数是
   * 指定对象的编码，第二个参数是待更新的新状态值。但如果该模型的编码是相对于另一个属性唯一，
   * 比如相对于该模型的{@code app}属性，即其所属应用，是唯一的，那么该方法应该至少有3个参数，
   * 第一个参数应该是其{@code app}属性值的某个主键，比如{@code app.id}或{@code app.code}，
   * 第二个参数是其编码，第三个参数是待更新的新状态值。此方法的作用就是，给定了其{@code app}
   * 属性值（一般是一个{@code StatefulInfo}对象），确定DAO方法的第一个参数，应该选择该
   * 属性值的哪个主键，是{@code app.id}，还是{@code app.code}，亦或者{@code app.name}。</p>
   *
   * @param obj
   *     指定的对象。
   * @param expectedKeyType
   *     期望的主键的类对象。
   * @return
   *     指定对象的主键值。
   */
  public static Object getKey(@Nullable final Object obj, final Class<?> expectedKeyType) {
    if (obj == null) {
      return null;
    }
    final Class<?> type = obj.getClass();
    if (type.getName().startsWith("java")) {
      return obj;   // 若此对象是 JDK 内置类型，直接返回该对象
    } else if (Enum.class.isAssignableFrom(type)) {
      return obj;   // 若此对象是枚举类型，直接返回该对象
    } else if (expectedKeyType.isAssignableFrom(type)) {
      return obj;   // 若期望的主键类型就是此对象的类型或其超类
    } else {
      final BeanInfo info = BeanInfo.of(type);
      final Property idProperty = info.getProperty("id");
      if (idProperty != null && idProperty.getType().equals(expectedKeyType)) {
        return idProperty.getValue(obj);
      }
      final Property codeProperty = info.getProperty("code");
      if (codeProperty != null && codeProperty.getType().equals(expectedKeyType)) {
        return codeProperty.getValue(obj);
      }
      final Property nameProperty = info.getProperty("name");
      if (nameProperty != null && nameProperty.getType().equals(expectedKeyType)) {
        return nameProperty.getValue(obj);
      }
      // FIXME: 只能直接返回此对象
      return obj;
    }
  }

  public static void setUpdateKeys(final BeanInfo modelInfo,
      final Property identifier, final Object source, final Object target) {
    if (! identifier.isUnique()) {
      final Object id = identifier.getValue(source);
      identifier.setValue(target, id);
    } else {
      final List<Property> properties = modelInfo.getRespectToProperties(identifier);
      for (final Property property : properties) {
        final Object value = property.getValue(source);
        property.setValue(target, value);
      }
    }
  }

  /**
   * 将指定的Unique属性的respect to属性中，未被当前方法修改的属性值，从指定的源对象复制到
   * 目标对象。
   *
   * @param methodInfo
   *     当前方法的信息。
   * @param property
   *     指定的Unique属性。
   * @param source
   *     指定的源对象。
   * @param target
   *     指定的目标对象。
   */
  public static void setUnmodifiedRespectToProperties(final DaoMethodInfo methodInfo,
      final Property property, final Object source, final Object target) {
    final String[] respectTo = property.getUniqueRespectTo();
    if (respectTo != null && respectTo.length > 0) {
      final BeanInfo modelInfo = methodInfo.getModelInfo();
      for (final String name : respectTo) {
        final Property prop = modelInfo.getProperty(name);
        if (prop != null && (!prop.isComputed()) && methodInfo.isUnmodified(prop)) {
          final Object value = prop.getValue(source);
          prop.setValue(target, value);
        }
      }
    }
  }

  public static Object[] getRespectToParams(final Object model,
      final BeanInfo modelInfo, final Property prop, final DaoMethodInfo methodInfo) {
    final Class<?>[] parameterTypes = methodInfo.getParameterTypes();
    final List<Property> properties = modelInfo.getRespectToProperties(prop);
    if (parameterTypes.length < properties.size()) {
      throw new IllegalArgumentException("The number of parameters of "
          + methodInfo.getQualifiedName() + " is invalid. There should be at least "
          + properties.size() + " parameters.");
    }
    final List<Object> params = new ArrayList<>();
    for (int i = 0; i < properties.size(); ++i) {
      final Property property = properties.get(i);
      final Class<?> parameterType = parameterTypes[i];
      final Object value = property.getValue(model);
      final Object key = getKey(value, parameterType);
      params.add(key);
    }
    return params.toArray(new Object[0]);
  }

  public static void copyAllProperties(final BeanInfo modelInfo,
      final Object source, final Object target) {
    for (final Property prop : modelInfo.getProperties()) {
      if (prop.isReadonly() || prop.isComputed()) {
        continue;
      }
      final Object value = prop.getValue(source);
      prop.setValue(target, value);
    }
  }

  public static <T> Criterion<T> prepareSimpleCriterion(final Class<T> modelType,
      final BeanInfo modelInfo, final EasyRandom random) {
    final List<Property> props = modelInfo.getProperties(
        p -> (!p.isComputed() && isSupportedNonArrayDataType(p.getType())));
    final Property selected = random.choose(props);
    return prepareSimpleCriterionImpl(modelType, selected, random);
  }

  //  public static <T> Criterion<T> prepareCriterion(final Class<T> modelType,
  //      final BeanInfo modelInfo, final EasyRandom random) {
  //    final List<Property> props = modelInfo.getProperties(
  //        p -> (!p.isComputed() && isSupportedNonArrayDataType(p.getType())));
  //    final int k = random.nextInt(1, props.size() + 1);
  //    final List<Property> selected = random.choose(props, k);
  //    if (k == 1) {
  //      return prepareSimpleCriterionImpl(modelType, selected.get(0), random);
  //    } else {
  //
  //    }
  //  }

  private static <T> SimpleCriterion<T> prepareSimpleCriterionImpl(
      final Class<T> modelType, final Property prop, final EasyRandom random) {
    //  TODO
    return null;
  }

  @SuppressWarnings("unchecked")
  public static <T> T normalize(@Nullable final T obj) {
    if (obj == null) {
      return null;
    }
    final Class<?> type = obj.getClass();
    if (isNormalizable(obj)) {
      callNormalize(obj);
    }
    if (obj instanceof String) {
      // 对于string，去除其头尾空白
      final String str = StringUtils.strip((String) obj);
      return (str.isEmpty() ? null : (T) str);
    }
    if ((obj instanceof Collection) && ((Collection<?>) obj).isEmpty()) {
      return null;
    }
    if ((obj instanceof Map) && ((Map<?, ?>) obj).isEmpty()) {
      return null;
    }
    if (ArrayUtils.isArray(obj) && ArrayUtils.isEmpty(obj)) {
      return null;
    }
    if (isEmptyful(obj) && callIsEmpty(obj)) {
      return null;
    }
    return obj;
  }

  private static <T> boolean isNormalizable(final T obj) {
    final Class<?> type = obj.getClass();
    return hasMethod(type, Option.DEFAULT_PUBLIC, "normalize", null);
  }

  private static <T> void callNormalize(final T obj) {
    final Class<?> type = obj.getClass();
    final Method method = getMatchingMethod(type, Option.DEFAULT_PUBLIC, "normalize", null);
    if (method != null) {
      invokeMethod(method, obj);
    }
  }

  private static <T> boolean isEmptyful(final T obj) {
    final Class<?> type = obj.getClass();
    return hasMethod(type, Option.DEFAULT_PUBLIC, "isEmpty", null);
  }

  private static <T> Boolean callIsEmpty(final T obj) {
    final Class<?> type = obj.getClass();
    final Method method = getMatchingMethod(type, Option.DEFAULT_PUBLIC, "isEmpty", null);
    if (method != null) {
      return (Boolean) invokeMethod(method, obj);
    } else {
      return FALSE;
    }
  }

}
