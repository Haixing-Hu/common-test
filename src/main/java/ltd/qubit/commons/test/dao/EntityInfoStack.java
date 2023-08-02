////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.Stack;

import javax.annotation.Nullable;

import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;

/**
 * 用于存储当前对象树的堆栈。
 *
 * @author 胡海星
 */
public class EntityInfoStack {

  private final LinkedList<EntityInfo> stack = new LinkedList<>();
  private final Stack<Class<?>> typeStack = new Stack<>();
  private final Set<Class<?>> types = new HashSet<>();

  public void push(final EntityInfo info) {
    stack.push(info);
    typeStack.push(info.getType());
    types.add(info.getType());
  }

  public EntityInfo peek() {
    return stack.peek();
  }

  public EntityInfo pop() {
    final EntityInfo result = stack.pop();
    typeStack.pop();
    types.remove(result.getType());
    return result;
  }

  public void clear() {
    stack.clear();
    typeStack.clear();
    types.clear();
  }

  public int size() {
    return stack.size();
  }

  public EntityInfo get(final int index) {
    return stack.get(index);
  }

  public boolean containsType(final Class<?> type) {
    return types.contains(type);
  }

  public Stack<Class<?>> getTypeStack() {
    return typeStack;
  }

  public void addReferencedProperty(final Property property) {
    if (!property.isReference() || property.getReferencePath().isEmpty()) {
      throw new IllegalArgumentException(
          "The property must be a reference property with path attribute: "
          + property.getName());
    }
    if (stack.isEmpty()) {
      throw new IllegalStateException("The entity info stack is empty.");
    }
    final ListIterator<EntityInfo> iter = stack.listIterator();
    String referencePath = property.getReferencePath();
    String propertyPath = property.getName();
    EntityInfo info = null;
    while (iter.hasNext()) {
      info = iter.next();
      if (!referencePath.startsWith(EntityInfo.PARENT_PATH)) {
        break;
      }
      if (info.getPropertyInParent() == null) {
        // 若引用路径依然指向父对象属性，但父对象不存在，直接将该引用路径加入当前对象并退出
        info.getReferencedProperties()
            .add(new ReferencedProperty(property, referencePath, propertyPath));
        return;
      }
      referencePath = referencePath.substring(EntityInfo.PARENT_PATH.length());
      if (referencePath.startsWith(EntityInfo.PATH_SEPARATOR)) {
        referencePath = referencePath.substring(EntityInfo.PATH_SEPARATOR.length());
      }
      propertyPath = info.getPropertyInParent().getName() + EntityInfo.PATH_SEPARATOR + propertyPath;
    }
    if (referencePath.startsWith(EntityInfo.PARENT_PATH)) {
      assert !stack.isEmpty();
      info = stack.peek();
      final BeanInfo modelInfo = BeanInfo.of(info.getType());
      throw new InvalidReferencePathException(modelInfo, new ReferencedProperty(property));
    }
    assert info != null;
    final Class<?> referToEntityType = getReferToEntityType(info.getType(), referencePath);
    if (property.getReferenceEntity().equals(referToEntityType)) {
      info.getReferencedProperties()
          .add(new ReferencedProperty(property, referencePath, propertyPath));
    } else {
      // 若最终找到的父对象的类型和引用属性所引用的实体类型不符合，
      // 说明这个父对象并非该引用属性指定的父对象，因此只能将该引用属性的路径加入到stack的
      // 顶层（即当前实体对象）的 referencedProperties中，在后面处理该引用属性时，会创
      // 建一个新的实体对象并加入数据库，作为该属性的引用实体对象。
      info = stack.peek();
      info.getReferencedProperties()
          .add(new ReferencedProperty(property));
    }
  }

  @Nullable
  private Class<?> getReferToEntityType(final Class<?> modelType, final String path) {
    if (path.isEmpty()) {
      return modelType;
    }
    final String propName;        // path所表示的第一级属性的名称
    final String remainedPath;    // path去掉第一级属性后余下的路径
    final int pos = path.indexOf(EntityInfo.PATH_SEPARATOR);
    if (pos >= 0) {               //  path 是一个多级属性路径
      propName = path.substring(0, pos);
      remainedPath = path.substring(pos + 1);
    } else {                      // path 就是一个简单的属性名
      propName = path;
      remainedPath = null;
    }
    final BeanInfo modelInfo = BeanInfo.of(modelType);
    final Property prop = modelInfo.getProperty(propName);
    if (prop == null) {
      return null; // 属性不存在，返回null
    }
    if (remainedPath == null) {   // 递归终止
      if (prop.isReference()) {
        return prop.getReferenceEntity();
      } else {
        return prop.getType();
      }
    } else if (prop.isCollection() || prop.isArray()) {
      return null;    // 不支持指向集合或数组的引用路径
    } else {
      // 递归处理余下的路径
      if (prop.isReference()) {
        return getReferToEntityType(prop.getReferenceEntity(), remainedPath);
      } else {
        return getReferToEntityType(prop.getType(), remainedPath);
      }
    }
  }

}
