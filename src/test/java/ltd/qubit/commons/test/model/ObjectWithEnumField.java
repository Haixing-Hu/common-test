////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@XmlRootElement(name = "object-with-enum-field")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
    getterVisibility = NONE,
    isGetterVisibility = NONE,
    setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectWithEnumField {

  @XmlElement(name = "enum-field")
  private EnumWithField enumField;

  public final EnumWithField getEnumField() {
    return enumField;
  }

  public final ObjectWithEnumField setEnumField(final EnumWithField enumField) {
    this.enumField = enumField;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final ObjectWithEnumField other = (ObjectWithEnumField) o;
    return Equality.equals(enumField, other.enumField);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, enumField);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("enumField", enumField)
        .toString();
  }
}
