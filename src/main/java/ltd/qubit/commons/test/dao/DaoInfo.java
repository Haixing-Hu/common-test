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
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ltd.qubit.commons.lang.Argument.requireNonNull;
import static ltd.qubit.commons.lang.Equality.nullOrEquals;

/**
 * Stores the meta-information about a DAO.
 *
 * @author Haixing Hu
 */
public class DaoInfo {
  private static final Logger LOGGER = LoggerFactory.getLogger(DaoInfo.class);

  private final Class<?> modelType;
  private final BeanInfo modelInfo;
  private final Object dao;
  private final Class<?> daoType;
  private final Map<Method, DaoMethodInfo> methodInfoMap;
  private final DaoMethodInfo exist;
  private final DaoMethodInfo add;
  private final DaoMethodInfo update;
  private final DaoMethodInfo get;
  private final DaoMethodInfo delete;
  private final DaoMethodInfo erase;
  private final DaoMethodInfo clear;
  private final DaoMethodInfo count;

  public DaoInfo(final Class<?> modelType, final Object dao) {
    LOGGER.info("Creating a DaoInfo for {}", modelType.getName());
    this.modelType = requireNonNull("modelType", modelType);
    this.modelInfo = BeanInfo.of(modelType);
    this.dao = requireNonNull("dao", dao);
    this.daoType = DaoTestUtils.getDaoInterface(modelType, dao);
    this.methodInfoMap = new HashMap<>();
    final Method[] methods = DaoTestUtils.getDaoMethods(modelType, dao);
    final Property idProperty = modelInfo.getIdProperty();
    DaoMethodInfo existMethod = null;
    DaoMethodInfo addMethod = null;
    DaoMethodInfo updateMethod = null;
    DaoMethodInfo getMethod = null;
    DaoMethodInfo deleteMethod = null;
    DaoMethodInfo eraseMethod = null;
    DaoMethodInfo clearMethod = null;
    DaoMethodInfo countMethod = null;
    for (final Method method : methods) {
      final DaoMethodInfo info = DaoMethodInfo.create(modelInfo, daoType, dao, method);
      if (info != null) {
        LOGGER.debug("Add a DAO method information of {}: {}", info.getQualifiedName(), info);
        methodInfoMap.put(method, info);
        switch (info.getOperation()) {
          case EXIST:
            if ((info.getTarget() == null)
                && nullOrEquals(info.getIdentifier(), idProperty)) {
              existMethod = info;
            }
            break;
          case GET:
            if ((info.getTarget() == null)
                && nullOrEquals(info.getIdentifier(), idProperty)) {
              getMethod = info;
            }
            break;
          case ADD:
            if ((info.getTarget() == null) && (info.getIdentifier() == null)) {
              addMethod = info;
            }
            break;
          case UPDATE:
            if ((info.getTarget() == null)
                && nullOrEquals(info.getIdentifier(), idProperty)) {
              updateMethod = info;
            }
            break;
          case DELETE:
            if ((info.getTarget() == null)
                && nullOrEquals(info.getIdentifier(), idProperty)) {
              deleteMethod = info;
            }
            break;
          case ERASE:
            if ((info.getTarget() == null)
                && nullOrEquals(info.getIdentifier(), idProperty)) {
              eraseMethod = info;
            }
            break;
          case CLEAR:
            if ((info.getTarget() == null) && (info.getIdentifier() == null)) {
              clearMethod = info;
            }
            break;
          case COUNT:
            countMethod = info;
            break;
          default:
            break;
        }
      }
    }
    this.exist = existMethod;
    this.get = getMethod;
    this.add = addMethod;
    this.update = updateMethod;
    this.delete = deleteMethod;
    this.erase = eraseMethod;
    this.clear = clearMethod;
    this.count = countMethod;
    if (addMethod == null) {
      throw new IllegalArgumentException("No add() method for the DAO "
          + daoType.getSimpleName());
    }
  }

  public final String getName() {
    return daoType.getSimpleName();
  }

  public final Class<?> getModelType() {
    return modelType;
  }

  public final BeanInfo getModelInfo() {
    return modelInfo;
  }

  public final Object getDao() {
    return dao;
  }

  public final Class<?> getDaoType() {
    return daoType;
  }

  public final Map<Method, DaoMethodInfo> getMethodInfoMap() {
    return methodInfoMap;
  }

  public final DaoMethodInfo getMethodInfo(final Method method) {
    return methodInfoMap.get(method);
  }

  public final DaoMethodInfo getMethodInfo(final String methodName) {
    for (final DaoMethodInfo info : methodInfoMap.values()) {
      if (info.getName().equals(methodName)) {
        return info;
      }
    }
    return null;
  }

  public DaoMethodInfo getAddMethod() {
    return add;
  }

  public DaoMethodInfo getUpdateMethod() {
    return update;
  }

  public DaoMethodInfo getUpdateMethod(@Nullable final Property identifier) {
    for (final DaoMethodInfo info : methodInfoMap.values()) {
      if ((info.getOperation() == DaoOperation.UPDATE)
          && (info.getTarget() == null)
          && Equality.equals(info.getIdentifier(), identifier)) {
        return info;
      }
    }
    return null;
  }

  public DaoMethodInfo getExistMethod() {
    return exist;
  }

  public DaoMethodInfo getGetMethod() {
    return get;
  }

  public DaoMethodInfo getDeleteMethod() {
    return delete;
  }

  public DaoMethodInfo getEraseMethod() {
    return erase;
  }

  public DaoMethodInfo getClearMethod() {
    return clear;
  }

  public DaoMethodInfo getCountMethod() {
    return count;
  }

  public final boolean exist(final Object id) throws Throwable {
    if (exist == null) {
      throw new IllegalArgumentException("No exist method for the DAO " + getName());
    }
    return (Boolean) exist.invoke(true, id);
  }

  public final void add(final Object model) throws Throwable {
    if (add == null) {
      throw new IllegalArgumentException("No add method for the DAO " + getName());
    }
    add.invoke(true, model);
  }

  public Object get(final Object id) throws Throwable {
    if (get == null) {
      throw new IllegalArgumentException("No get method for the DAO " + getName());
    }
    return get.invoke(true, id);
  }

  public Object delete(final Object id) throws Throwable {
    if (delete == null) {
      throw new IllegalArgumentException("No delete method for the DAO " + getName());
    }
    return delete.invoke(true, id);
  }

  public Long count(@Nullable final Object arg) throws Throwable {
    if (count == null) {
      throw new IllegalArgumentException("No count method for the DAO " + getName());
    }
    return (Long) count.invoke(true, arg);
  }

  public Object erase(final Object id) throws Throwable {
    if (erase == null) {
      throw new IllegalArgumentException("No erase method for the DAO " + getName());
    }
    return erase.invoke(true, id);
  }

  public Object clear() throws Throwable {
    if (clear == null) {
      throw new IllegalArgumentException("No clear method for the DAO " + getName());
    }
    return clear.invoke(true);
  }

  public boolean hasExist() {
    return (exist != null);
  }

  public boolean hasAdd() {
    return (add != null);
  }

  public boolean hasGet() {
    return (get != null);
  }

  public boolean hasDelete() {
    return (delete != null);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final DaoInfo other = (DaoInfo) o;
    return Equality.equals(modelType, other.modelType)
        && Equality.equals(modelInfo, other.modelInfo)
        && Equality.equals(dao, other.dao)
        && Equality.equals(daoType, other.daoType)
        && Equality.equals(methodInfoMap, other.methodInfoMap)
        && Equality.equals(add, other.add)
        && Equality.equals(get, other.get);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, modelType);
    result = Hash.combine(result, multiplier, modelInfo);
    result = Hash.combine(result, multiplier, dao);
    result = Hash.combine(result, multiplier, daoType);
    result = Hash.combine(result, multiplier, methodInfoMap);
    result = Hash.combine(result, multiplier, add);
    result = Hash.combine(result, multiplier, get);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("modelType", modelType)
        .append("modelInfo", modelInfo)
        .append("dao", dao)
        .append("daoType", daoType)
        .append("methodInfos", methodInfoMap)
        .append("addMethod", add)
        .append("getMethod", get)
        .toString();
  }
}
