////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import ltd.qubit.commons.annotation.Modified;
import ltd.qubit.commons.annotation.NoAutoTest;
import ltd.qubit.commons.annotation.Unique;
import ltd.qubit.commons.annotation.Unmodified;
import ltd.qubit.commons.lang.CompareToBuilder;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.MethodUtils;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ltd.qubit.commons.lang.Argument.requireNonNull;
import static ltd.qubit.commons.lang.ArrayUtils.EMPTY_OBJECT_ARRAY;
import static ltd.qubit.commons.lang.StringUtils.isEmpty;
import static ltd.qubit.commons.lang.StringUtils.lowerCaseFirstChar;
import static ltd.qubit.commons.reflect.MethodUtils.getMethodUri;

/**
 * Stores the information about a method of a DAO.
 *
 * @author Haixing Hu
 */
public class DaoMethodInfo implements Comparable<DaoMethodInfo> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DaoMethodInfo.class);

  private final BeanInfo modelInfo;
  private final Class<?> daoType;
  private final Object dao;
  private final Method method;
  private final String name;
  private final String qualifiedName;
  private final URI uri;
  private final DaoOperation operation;
  private final Property target;
  private final Property identifier;
  private final boolean allowNullReturn;
  private final Set<Property> modifiedProperties;
  private final Set<String> modifiedPropertyNames;
  private final Set<Property> unmodifiedProperties;
  private final Set<String> unmodifiedPropertyNames;

  /**
   * Creates a {@link DaoMethodInfo} object for a method of a DAO.
   *
   * @param modelInfo
   *     the meta-information of the model.
   * @param daoType
   *     the class object of the interface of the DAO.
   * @param dao
   *     the instance of the DAO object.
   * @param method
   *     the method of the DAO.
   * @return
   *     a {@link DaoMethodInfo} object storing the meta-information of the
   *     method of the DAO; or {@code null} if the method is not a standard
   *     DAO method.
   */
  public static DaoMethodInfo create(final BeanInfo modelInfo,
      final Class<?> daoType, final Object dao, final Method method) {
    requireNonNull("modelInfo", modelInfo);
    requireNonNull("daoType", daoType);
    requireNonNull("dao", dao);
    requireNonNull("method", method);
    // 注意：接口的 annotation 不能被继承，因此如果某个父接口的方法被标注 @NotDaoOperation，
    // 子接口又重载了父接口的同名方法，但子接口的该方法上没有再次标注 @NotDaoOperation，
    // 那么通过 method.isAnnotationPresent() 是无法判定该方法是否不属于DAO操作的。
    // 因此这里我们通过重新实现的 MethodUtils.isAnnotationPresent() 来做判断。
    if (MethodUtils.isAnnotationPresent(method, NoAutoTest.class)) {
      LOGGER.debug("Skip the non-DAO operation {}.{}.",
          daoType.getSimpleName(), method.getName());
      return null;
    }
    final ParsedResult result = parseMethodInfo(modelInfo, daoType, method);
    if (result == null) {
      LOGGER.warn("The non-standard DAO method {}.{} cannot be automatically tested.",
          daoType.getSimpleName(), method.getName());
      return null;
    } else {
      return new DaoMethodInfo(modelInfo, daoType, dao, method, result);
    }
  }

  static ParsedResult parseMethodInfo(final BeanInfo modelInfo,
      final Class<?> daoType, final Method method) {
    final String methodName = method.getName();
    for (final DaoOperation operation : DaoOperation.values()) {
      final Pattern pattern = Pattern.compile(operation.pattern());
      final Matcher matcher = pattern.matcher(methodName);
      if (matcher.matches()) {
        return extractIdentifierTarget(modelInfo, operation, matcher);
      }
    }
    return null;
  }

  static ParsedResult extractIdentifierTarget(final BeanInfo modelInfo,
      final DaoOperation operation, final Matcher matcher) {
    Property first = null;
    Property second = null;
    if (matcher.groupCount() == 1) {
      final String firstName = lowerCaseFirstChar(matcher.group(1));
      if (!isEmpty(firstName)) {
        first = modelInfo.getProperty(firstName);
        if (first == null) {
          LOGGER.debug("No field '{}' found for the class {}. ", firstName,
              modelInfo.getType().getName());
          return null;
        }
      }
    } else if (matcher.groupCount() >= 2) {
      final String firstName = lowerCaseFirstChar(matcher.group(1));
      if (!isEmpty(firstName)) {
        first = modelInfo.getProperty(firstName);
        if (first == null) {
          LOGGER.debug("No field '{}' found for the class {}. ", firstName,
              modelInfo.getType().getName());
          return null;
        }
      }
      final String secondName = lowerCaseFirstChar(matcher.group(2));
      if (!isEmpty(secondName)) {
        second = modelInfo.getProperty(secondName);
        if (second == null) {
          LOGGER.debug("No field '{}' found for the class {}. ", secondName,
              modelInfo.getType().getName());
          return null;
        }
      }
    }
    switch (operation) {
      case EXIST_NON_DELETED:
      case EXIST:
      case DELETE:
      case RESTORE:
      case PURGE:
      case ERASE:
        return new ParsedResult(operation, second,
            (first == null ? modelInfo.getIdProperty() : first));
      case GET_OR_NULL:
      case GET:
      case UPDATE:
      case ADD_OR_UPDATE:
        return new ParsedResult(operation, first,
            (second == null ? modelInfo.getIdProperty() : second));
      default:
        return new ParsedResult(operation, first, second);
    }
  }

  private DaoMethodInfo(final BeanInfo modelInfo, final Class<?> daoType,
      final Object dao, final Method method,
      final ParsedResult info) {
    this.modelInfo = modelInfo;
    this.daoType = daoType;
    this.dao = dao;
    this.method = method;
    this.name = method.getName();
    this.qualifiedName = daoType.getSimpleName() + "." + method.getName();
    this.uri = getMethodUri(method);
    this.operation = info.operation;
    this.target = info.target;
    this.identifier = info.identifier;
    this.allowNullReturn = (info.operation.name().endsWith("_OR_NULL"));
    this.modifiedProperties = new HashSet<>();
    this.unmodifiedProperties = new HashSet<>();
    this.modifiedPropertyNames = new HashSet<>();
    this.unmodifiedPropertyNames = new HashSet<>();
    this.setModifiedUnmodifiedProperties(method);
    if (LOGGER.isInfoEnabled()) {
      switch (this.operation) {
        case UPDATE:
        case ADD_OR_UPDATE:
        case DELETE:
        case RESTORE:
          LOGGER.info("Successfully parsed the information of {}: operation = {}, "
                  + "target = {}, identifier = {}, modified = {}",
              this.qualifiedName, this.operation,
              (this.target == null ? "null" : this.target.getName()),
              (this.identifier == null ? "null" : this.identifier.getName()),
              this.modifiedPropertyNames);
          break;
        default:
          LOGGER.info("Successfully parsed the information of {}: operation = {}, "
                  + "target = {}, identifier = {}",
              this.qualifiedName, this.operation,
              (this.target == null ? "null" : this.target.getName()),
              (this.identifier == null ? "null" : this.identifier.getName()));
          break;
      }
    }
  }

  private void setModifiedUnmodifiedProperties(final Method method) {
    modifiedProperties.clear();
    unmodifiedProperties.clear();
    modifiedPropertyNames.clear();
    unmodifiedPropertyNames.clear();
    switch (operation) {
      case ADD:
      case ADD_OR_UPDATE:
      case UPDATE:
        if (method.isAnnotationPresent(Modified.class)) {
          final Modified annotation = method.getAnnotation(Modified.class);
          setModifiedProperties(annotation.value());
        } else if (method.isAnnotationPresent(Unmodified.class)) {
          final Unmodified annotation = method.getAnnotation(Unmodified.class);
          setUnmodifiedProperties(annotation.value());
        } else {
          setModifiedProperties(new String[]{});
        }
        break;
      case DELETE:
      case RESTORE:
        setModifiedProperties(new String[]{ "deleteTime" });
        break;
      default:
        setModifiedProperties(new String[]{});
        break;
    }
    modifiedProperties.forEach(e -> modifiedPropertyNames.add(e.getName()));
    unmodifiedProperties.forEach(e -> unmodifiedPropertyNames.add(e.getName()));
  }

  private void setModifiedProperties(final String[] modifiedFields) {
    unmodifiedProperties.addAll(modelInfo.getProperties());
    for (final String field : modifiedFields) {
      final Property property = modelInfo.getProperty(field);
      if (property != null) {
        modifiedProperties.add(property);
        unmodifiedProperties.remove(property);
      }
    }
    fixReadonlyProperties();
    fixComputedProperties();
  }

  private void setUnmodifiedProperties(final String[] unmodifiedFields) {
    modifiedProperties.addAll(modelInfo.getProperties());
    for (final String field : unmodifiedFields) {
      final Property property = modelInfo.getProperty(field);
      if (property != null) {
        unmodifiedProperties.add(property);
        modifiedProperties.remove(property);
      }
    }
    fixReadonlyProperties();
    fixComputedProperties();
  }

  private void fixReadonlyProperties() {
    for (final Property prop : modelInfo.getProperties()) {
      if (prop.isReadonly()) {
        this.unmodifiedProperties.add(prop);
        this.modifiedProperties.remove(prop);
      }
    }
  }

  private void fixComputedProperties() {
    for (final Property prop : modelInfo.getProperties()) {
      if (prop.isComputed()) {
        // 计算出的属性不应该在update后被检查，否则其依赖有些是被update修改的有些没被update
        // 修改过，确认其值变化比较复杂。因此在update之后只需检查所有非计算属性即可。
        this.unmodifiedProperties.remove(prop);
        this.modifiedProperties.remove(prop);
        //        final String[] dependOn = prop.getComputedDependOn();
        //        if (dependOn == null) {   // no depend on properties, it is unmodified
        //          this.unmodifiedProperties.add(prop);
        //          this.modifiedProperties.remove(prop);
        //        } else if (SetUtils.containsAny(this.modifiedProperties,
        //            (p) -> ArrayUtils.contains(dependOn, p.getName()))) {
        //          this.modifiedProperties.add(prop);
        //          this.unmodifiedProperties.remove(prop);
        //        } else {
        //          this.unmodifiedProperties.add(prop);
        //          this.modifiedProperties.remove(prop);
        //        }
      }
    }
  }

  public final BeanInfo getModelInfo() {
    return modelInfo;
  }

  public final Class<?> getDaoType() {
    return daoType;
  }

  public final Object getDao() {
    return dao;
  }

  public final Method getMethod() {
    return method;
  }

  public final Class<?>[] getParameterTypes() {
    return method.getParameterTypes();
  }

  public final String getName() {
    return name;
  }

  public final String getQualifiedName() {
    return qualifiedName;
  }

  public final URI getUri() {
    return uri;
  }

  public final DaoOperation getOperation() {
    return operation;
  }

  public final Property getTarget() {
    return target;
  }

  public boolean hasTarget() {
    return (target != null);
  }

  public final Property getIdentifier() {
    return identifier;
  }

  public final boolean hasIdentifier() {
    return (identifier != null);
  }

  public boolean isAllowNullReturn() {
    return allowNullReturn;
  }

  public final Set<Property> getModifiedProperties() {
    return modifiedProperties;
  }

  public final Set<String> getModifiedPropertyNames() {
    return modifiedPropertyNames;
  }

  public final Set<Property> getUnmodifiedProperties() {
    return unmodifiedProperties;
  }

  public final Set<String> getUnmodifiedPropertyNames() {
    return unmodifiedPropertyNames;
  }

  /**
   * 判定此方法是否修改给定的属性的值。
   *
   * @param property
   *     给定的属性。
   * @return
   *     若此方法修改给定属性的值，返回{@code true}；否则返回{@code false}。
   */
  public boolean isModified(final Property property) {
    return modifiedProperties.contains(property);
  }

  /**
   * 判定此方法是否修改给定的属性的值。
   *
   * @param propertyName
   *     给定的属性的名称。
   * @return
   *     若此方法修改给定属性的值，返回{@code true}；否则返回{@code false}。
   */
  public boolean isModified(final String propertyName) {
    final Property prop = modelInfo.getProperty(propertyName);
    return prop != null && modifiedProperties.contains(prop);
  }

  /**
   * 判定此方法是否不修改给定的属性的值。
   *
   * @param property
   *     给定的属性。
   * @return
   *     若此方法不修改给定属性的值，返回{@code true}；否则返回{@code false}。
   */
  public boolean isUnmodified(final Property property) {
    return unmodifiedProperties.contains(property);
  }

  /**
   * 判定此方法是否不修改给定的属性的值。
   *
   * @param propertyName
   *     给定的属性的名称。
   * @return
   *     若此方法不修改给定属性的值，返回{@code true}；否则返回{@code false}。
   */
  public boolean isUnmodified(final String propertyName) {
    final Property prop = modelInfo.getProperty(propertyName);
    return prop != null && unmodifiedProperties.contains(prop);
  }

  /**
   * 判定此方法是否修改给定的唯一(unique)属性及其所有相对(respect to)属性的值。
   *
   * @param property
   *     给定的属性，必须是一个被{@link Unique}标注的
   *     唯一属性。
   * @return
   *     若此方法修改了给定的唯一属性的值，并且修改了给定的唯一属性的所有相对属性（通过
   *     {@link Unique}标注的{@code respectTo}参数值
   *     指定）的值，则返回{@code true}；否则返回{@code false}。
   * @see Unique
   */
  public boolean isUniquePropertyModified(final Property property) {
    if (!modifiedProperties.contains(property)) {
      return false;
    }
    final String[] respectTo = property.getUniqueRespectTo();
    if (respectTo != null) {
      for (final String p : respectTo) {
        final Property prop = modelInfo.getProperty(p);
        if (prop == null) {
          continue;
        }
        if (!modifiedProperties.contains(prop)) {
          return false;
        }
      }
    }
    return true;
  }

  public Object invoke(final boolean logging) throws Throwable {
    return invokeWithArguments(logging, EMPTY_OBJECT_ARRAY);
  }

  public Object invoke(final boolean logging, @Nullable final Object argument) throws Throwable {
    return invokeWithArguments(logging, new Object[]{ argument });
  }

  // We must make different between invoke(null) and invoke(a, b, c)
  public Object invoke(final boolean logging, @Nullable final Object firstArg,
      @Nullable final Object... otherArgs) throws Throwable {
    final Object[] arguments;
    if (otherArgs == null) {
      // calling invoke(arg1, null) means two arguments
      arguments = new Object[2];
      arguments[0] = firstArg;
      arguments[1] = null;
    } else {
      arguments = new Object[otherArgs.length + 1];
      arguments[0] = firstArg;
      System.arraycopy(otherArgs, 0, arguments, 1, otherArgs.length);
    }
    return invokeWithArguments(logging, arguments);
  }

  public Object invokeWithArguments(final boolean logging,
      @Nullable final Object[] arguments) throws Throwable {
    try {
      if (arguments == null) {
        return method.invoke(dao, EMPTY_OBJECT_ARRAY);
      } else {
        return method.invoke(dao, arguments);
      }
    } catch (final Exception e) {
      final Throwable error;
      if (e instanceof InvocationTargetException) {
        error = ((InvocationTargetException) e).getTargetException();
      } else {
        error = e;
      }
      if (logging) {
        LOGGER.error("Failed to invoke the DAO method {}: arguments = {}, "
                + "exception = {}", qualifiedName, arguments, error.getMessage(), e);
      }
      throw error;
    }
  }

  @Override
  public int compareTo(final DaoMethodInfo other) {
    return new CompareToBuilder()
        .append(operation, other.operation)
        .append(name, other.name)
        .compare();
  }

  public boolean equals(@Nullable final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final DaoMethodInfo other = (DaoMethodInfo) o;
    return Equality.equals(modelInfo, other.modelInfo)
        && Equality.equals(daoType, other.daoType)
        && Equality.equals(dao, other.dao)
        && Equality.equals(method, other.method)
        && Equality.equals(name, other.name)
        && Equality.equals(qualifiedName, other.qualifiedName)
        && Equality.equals(uri, other.uri)
        && Equality.equals(operation, other.operation)
        && Equality.equals(target, other.target)
        && Equality.equals(identifier, other.identifier)
        && Equality.equals(allowNullReturn, other.allowNullReturn)
        && Equality.equals(modifiedProperties, other.modifiedProperties)
        && Equality.equals(modifiedPropertyNames, other.modifiedPropertyNames)
        && Equality.equals(unmodifiedProperties, other.unmodifiedProperties)
        && Equality.equals(unmodifiedPropertyNames, other.unmodifiedPropertyNames);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, modelInfo);
    result = Hash.combine(result, multiplier, daoType);
    result = Hash.combine(result, multiplier, dao);
    result = Hash.combine(result, multiplier, method);
    result = Hash.combine(result, multiplier, name);
    result = Hash.combine(result, multiplier, qualifiedName);
    result = Hash.combine(result, multiplier, uri);
    result = Hash.combine(result, multiplier, operation);
    result = Hash.combine(result, multiplier, target);
    result = Hash.combine(result, multiplier, identifier);
    result = Hash.combine(result, multiplier, allowNullReturn);
    result = Hash.combine(result, multiplier, modifiedProperties);
    result = Hash.combine(result, multiplier, modifiedPropertyNames);
    result = Hash.combine(result, multiplier, unmodifiedProperties);
    result = Hash.combine(result, multiplier, unmodifiedPropertyNames);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("modelInfo", modelInfo)
        .append("daoType", daoType)
        .append("dao", dao)
        .append("method", method)
        .append("name", name)
        .append("qualifiedName", qualifiedName)
        .append("uri", uri)
        .append("operation", operation)
        .append("target", target)
        .append("identifier", identifier)
        .append("allowNullReturn", allowNullReturn)
        .append("modifiedProperties", modifiedProperties)
        .append("modifiedPropertyNames", modifiedPropertyNames)
        .append("unmodifiedProperties", unmodifiedProperties)
        .append("unmodifiedPropertyNames", unmodifiedPropertyNames)
        .toString();
  }

  static class ParsedResult {

    final DaoOperation operation;
    final Property target;
    final Property identifier;

    ParsedResult(final DaoOperation operation,
        final Property target, final Property identifier) {
      this.operation = operation;
      this.target = target;
      this.identifier = identifier;
    }
  }
}
