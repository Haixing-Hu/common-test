////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

/**
 * 此模型表示待设置的属性。
 */
public class ReferencedProperty {

  /**
   * 引用的实体的类型。
   */
  private final Class<?> referenceEntity;

  /**
   * 引用的实体的属性名称。
   */
  private final String referenceProperty;

  /**
   * 引用的实体的路径。
   */
  private final String referencePath;

  /**
   * 待设置的属性路径。
   */
  private final String propertyPath;

  public ReferencedProperty(final Property property,
      final String referencePath, final String propertyPath) {
    this.referenceEntity = property.getReferenceEntity();
    this.referenceProperty = property.getReferenceProperty();
    this.referencePath = referencePath;
    this.propertyPath = propertyPath;
  }

  public ReferencedProperty(final Property property) {
    this.referenceEntity = property.getReferenceEntity();
    this.referenceProperty = property.getReferenceProperty();
    this.referencePath = property.getReferencePath();
    this.propertyPath = property.getName();
  }

  public ReferencedProperty(final Class<?> referenceEntity,
      final String referenceProperty, final String referencePath,
      final String propertyPath) {
    this.referenceEntity = referenceEntity;
    this.referenceProperty = referenceProperty;
    this.referencePath = referencePath;
    this.propertyPath = propertyPath;
  }

  public String getReferenceProperty() {
    return referenceProperty;
  }

  public Class<?> getReferenceEntity() {
    return referenceEntity;
  }

  public String getReferencePath() {
    return referencePath;
  }

  public String getPropertyPath() {
    return propertyPath;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final ReferencedProperty other = (ReferencedProperty) o;
    return Equality.equals(referenceProperty, other.referenceProperty)
        && Equality.equals(referenceEntity, other.referenceEntity)
        && Equality.equals(referencePath, other.referencePath)
        && Equality.equals(propertyPath, other.propertyPath);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, referenceProperty);
    result = Hash.combine(result, multiplier, referenceEntity);
    result = Hash.combine(result, multiplier, referencePath);
    result = Hash.combine(result, multiplier, propertyPath);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("referenceProperty", referenceProperty)
        .append("referenceEntity", referenceEntity)
        .append("referencePath", referencePath)
        .append("propertyPath", propertyPath)
        .toString();
  }
}
