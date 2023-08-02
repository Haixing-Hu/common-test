////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@XmlRootElement(name = "object-with-list")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
    getterVisibility = NONE,
    isGetterVisibility = NONE,
    setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectWithList {

  @Size(min = 2, max = 5)
  private List<Integer> intList;

  @Size(min = 2, max = 5)
  private StringList stringList;

  public final List<Integer> getIntList() {
    return intList;
  }

  public final ObjectWithList setIntList(final List<Integer> intList) {
    this.intList = intList;
    return this;
  }

  public final StringList getStringList() {
    return stringList;
  }

  public final ObjectWithList setStringList(final StringList stringList) {
    this.stringList = stringList;
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
    final ObjectWithList that = (ObjectWithList) o;
    return Objects.equals(intList, that.intList)
        && Objects.equals(stringList, that.stringList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(intList, stringList);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", ObjectWithList.class.getSimpleName() + "[",
        "]")
        .add("intList=" + intList)
        .add("stringList=" + stringList)
        .toString();
  }
}
