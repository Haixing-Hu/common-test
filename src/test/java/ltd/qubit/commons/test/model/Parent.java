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

import ltd.qubit.commons.annotation.Identifier;
import ltd.qubit.commons.annotation.Reference;
import ltd.qubit.commons.lang.Argument;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Assignment;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

public class Parent implements WithInfo, Assignable<Parent> {

  private static final long serialVersionUID = 3743825061623698280L;

  @Identifier
  private Long id;

  private String code;

  private String name;

  @Reference(entity = Grandpa.class, property = "id", path = "..")
  private Long parentId;

  @Reference(entity = Child.class, existing = false)
  private List<Child> children;

  @Reference(entity = Country.class, property = "info", path = "province/country")
  private Info country;

  @Reference(entity = Province.class, property = "info")
  private Info province;

  @Reference(entity = Country.class, property = "info", path = "../country")
  private Info parentCountry;

  @Reference(entity = Province.class, property = "info", path = "../province")
  private Info parentProvince;

  public Parent() {
    // empty
  }

  public Parent(final Parent other) {
    assign(other);
  }

  @Override
  public void assign(final Parent other) {
    Argument.requireNonNull("other", other);
    id = other.id;
    code = other.code;
    name = other.name;
    parentId = other.parentId;
    children = Assignment.deepClone(other.children);
    country = Assignment.clone(other.country);
    province = Assignment.clone(other.province);
    parentCountry = Assignment.clone(other.parentCountry);
    parentProvince = Assignment.clone(other.parentProvince);
  }

  @Override
  public Parent clone() {
    return new Parent(this);
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

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(final Long parentId) {
    this.parentId = parentId;
  }

  public List<Child> getChildren() {
    return children;
  }

  public void setChildren(final List<Child> children) {
    this.children = children;
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

  public Info getParentCountry() {
    return parentCountry;
  }

  public void setParentCountry(final Info parentCountry) {
    this.parentCountry = parentCountry;
  }

  public Info getParentProvince() {
    return parentProvince;
  }

  public void setParentProvince(final Info parentProvince) {
    this.parentProvince = parentProvince;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Parent other = (Parent) o;
    return Equality.equals(id, other.id)
        && Equality.equals(code, other.code)
        && Equality.equals(name, other.name)
        && Equality.equals(parentId, other.parentId)
        && Equality.equals(children, other.children)
        && Equality.equals(country, other.country)
        && Equality.equals(province, other.province)
        && Equality.equals(parentCountry, other.parentCountry)
        && Equality.equals(parentProvince, other.parentProvince);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, code);
    result = Hash.combine(result, multiplier, name);
    result = Hash.combine(result, multiplier, parentId);
    result = Hash.combine(result, multiplier, children);
    result = Hash.combine(result, multiplier, country);
    result = Hash.combine(result, multiplier, province);
    result = Hash.combine(result, multiplier, parentCountry);
    result = Hash.combine(result, multiplier, parentProvince);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("code", code)
        .append("name", name)
        .append("parentId", parentId)
        .append("children", children)
        .append("country", country)
        .append("province", province)
        .append("parentCountry", parentCountry)
        .append("parentProvince", parentProvince)
        .toString();
  }
}
