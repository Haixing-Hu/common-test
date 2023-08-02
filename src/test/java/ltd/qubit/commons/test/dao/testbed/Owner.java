////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import java.io.Serializable;

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.annotation.KeyIndex;
import ltd.qubit.commons.lang.Argument;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示一个指定类型和ID的实体对象。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "entity")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
    getterVisibility = NONE,
    isGetterVisibility = NONE,
    setterVisibility = NONE)
@JsonInclude(Include.NON_NULL)
public class Owner implements Serializable, Assignable<Owner> {

  private static final long serialVersionUID = -6788704346831608108L;

  /**
   * 实体对象的类型的名字。
   */
  @Size(min = 1, max = 64)
  @KeyIndex(0)
  private String type;

  /**
   * 实体对象的唯一标识。
   */
  @KeyIndex(1)
  private Long id;

  public Owner() {
    // empty
  }

  public Owner(final Owner other) {
    assign(other);
  }

  @Override
  public void assign(final Owner other) {
    Argument.requireNonNull("other", other);
    type = other.type;
    id = other.id;
  }

  @Override
  public Owner clone() {
    return new Owner(this);
  }

  public final String getType() {
    return type;
  }

  public final void setType(final String type) {
    this.type = type;
  }

  public final Long getId() {
    return id;
  }

  public final void setId(final Long id) {
    this.id = id;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Owner other = (Owner) o;
    return Equality.equals(type, other.type)
        && Equality.equals(id, other.id);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, type);
    result = Hash.combine(result, multiplier, id);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("type", type)
        .append("id", id)
        .toString();
  }
}
