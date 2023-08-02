////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@XmlRootElement(name = "object-with-set")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
    getterVisibility = NONE,
    isGetterVisibility = NONE,
    setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectWithSet {

  @Size(min = 2, max = 5)
  private Set<Integer> intSet;

  @Size(min = 2, max = 5)
  private StringSet stringSet;

  public final Set<Integer> getIntSet() {
    return intSet;
  }

  public final ObjectWithSet setIntSet(final Set<Integer> intSet) {
    this.intSet = intSet;
    return this;
  }

  public final StringSet getStringSet() {
    return stringSet;
  }

  public final ObjectWithSet setStringSet(
      final StringSet stringSet) {
    this.stringSet = stringSet;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ObjectWithSet that = (ObjectWithSet) o;
    return Objects.equals(intSet, that.intSet)
        && Objects.equals(stringSet, that.stringSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(intSet, stringSet);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ObjectWithSet.class.getSimpleName() + "[", "]")
        .add("intSet=" + intSet)
        .add("stringSet=" + stringSet)
        .toString();
  }
}
