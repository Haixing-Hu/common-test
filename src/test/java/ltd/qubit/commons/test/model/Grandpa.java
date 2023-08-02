////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import ltd.qubit.commons.annotation.Identifier;
import ltd.qubit.commons.annotation.Reference;
import ltd.qubit.commons.lang.Argument;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Assignment;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

public class Grandpa implements WithInfo, Assignable<Grandpa> {

  private static final long serialVersionUID = 5299661903848743805L;

  @Identifier
  private Long id;

  private String code;

  private String name;

  @Reference(entity = Parent.class, existing = false)
  private Parent child;

  @Reference(entity = Country.class, property = "info", path = "province/country")
  private Info country;

  @Reference(entity = Province.class, property = "info")
  private Info province;

  public Grandpa() {
    // empty
  }

  public Grandpa(final Grandpa other) {
    assign(other);
  }

  @Override
  public void assign(final Grandpa other) {
    Argument.requireNonNull("other", other);
    id = other.id;
    code = other.code;
    name = other.name;
    child = Assignment.clone(other.child);
    country = Assignment.clone(other.country);
    province = Assignment.clone(other.province);
  }

  @Override
  public Grandpa clone() {
    return new Grandpa(this);
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Parent getChild() {
    return child;
  }

  public void setChild(final Parent child) {
    this.child = child;
  }

  public Info getCountry() {
    return country;
  }

  public void setCountry(final Info country) {
    this.country = country;
  }

  public Info getProvince() {
    return province;
  }

  public void setProvince(final Info province) {
    this.province = province;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Grandpa other = (Grandpa) o;
    return Equality.equals(id, other.id)
        && Equality.equals(code, other.code)
        && Equality.equals(name, other.name)
        && Equality.equals(child, other.child)
        && Equality.equals(country, other.country)
        && Equality.equals(province, other.province);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, code);
    result = Hash.combine(result, multiplier, name);
    result = Hash.combine(result, multiplier, child);
    result = Hash.combine(result, multiplier, country);
    result = Hash.combine(result, multiplier, province);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("code", code)
        .append("name", name)
        .append("child", child)
        .append("country", country)
        .append("province", province)
        .toString();
  }
}
