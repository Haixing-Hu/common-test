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

public class Child implements WithInfo, Assignable<Child> {

  private static final long serialVersionUID = -9029169564667187509L;

  @Identifier
  private Long id;

  private String code;

  private String name;

  @Reference(entity = Parent.class, property = "id", path = "..")
  private Long parentId;

  @Reference(entity = Grandpa.class, property = "id", path = "../..")
  private Long grandpaId;

  @Reference(entity = Country.class, property = "info", path = "province/country")
  private Info country;

  @Reference(entity = Province.class, property = "info", path = "city/province")
  private Info province;

  @Reference(entity = City.class, property = "info")
  private Info city;

  @Reference(entity = Country.class, property = "info", path = "../country")
  private Info parentCountry;

  @Reference(entity = Province.class, property = "info", path = "../province")
  private Info parentProvince;

  @Reference(entity = Country.class, property = "info", path = "../../country")
  private Info grandpaCountry;

  @Reference(entity = Province.class, property = "info", path = "../../province")
  private Info grandpaProvince;

  @Reference(entity = Family.class, property = "info", path = "subFamily/family")
  private Info family;

  @Reference(entity = SubFamily.class, property = "info")
  private Info subFamily;

  public Child() {
    // empty
  }

  public Child(final Child other) {
    assign(other);
  }

  @Override
  public void assign(final Child other) {
    Argument.requireNonNull("other", other);
    id = other.id;
    code = other.code;
    name = other.name;
    parentId = other.parentId;
    grandpaId = other.grandpaId;
    country = Assignment.clone(other.country);
    province = Assignment.clone(other.province);
    city = Assignment.clone(other.city);
    parentCountry = Assignment.clone(other.parentCountry);
    parentProvince = Assignment.clone(other.parentProvince);
    grandpaCountry = Assignment.clone(other.grandpaCountry);
    grandpaProvince = Assignment.clone(other.grandpaProvince);
    family = Assignment.clone(other.family);
    subFamily = Assignment.clone(other.subFamily);
  }

  @Override
  public Child clone() {
    return new Child(this);
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

  public Long getGrandpaId() {
    return grandpaId;
  }

  public void setGrandpaId(final Long grandpaId) {
    this.grandpaId = grandpaId;
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

  public Info getCity() {
    return city;
  }

  public void setCity(final Info city) {
    this.city = city;
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

  public Info getGrandpaCountry() {
    return grandpaCountry;
  }

  public void setGrandpaCountry(final Info grandpaCountry) {
    this.grandpaCountry = grandpaCountry;
  }

  public Info getGrandpaProvince() {
    return grandpaProvince;
  }

  public void setGrandpaProvince(final Info grandpaProvince) {
    this.grandpaProvince = grandpaProvince;
  }

  public Info getFamily() {
    return family;
  }

  public void setFamily(final Info family) {
    this.family = family;
  }

  public Info getSubFamily() {
    return subFamily;
  }

  public void setSubFamily(final Info subFamily) {
    this.subFamily = subFamily;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Child other = (Child) o;
    return Equality.equals(id, other.id)
        && Equality.equals(code, other.code)
        && Equality.equals(name, other.name)
        && Equality.equals(parentId, other.parentId)
        && Equality.equals(grandpaId, other.grandpaId)
        && Equality.equals(country, other.country)
        && Equality.equals(province, other.province)
        && Equality.equals(city, other.city)
        && Equality.equals(parentCountry, other.parentCountry)
        && Equality.equals(parentProvince, other.parentProvince)
        && Equality.equals(grandpaCountry, other.grandpaCountry)
        && Equality.equals(grandpaProvince, other.grandpaProvince)
        && Equality.equals(family, other.family)
        && Equality.equals(subFamily, other.subFamily);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, code);
    result = Hash.combine(result, multiplier, name);
    result = Hash.combine(result, multiplier, parentId);
    result = Hash.combine(result, multiplier, grandpaId);
    result = Hash.combine(result, multiplier, country);
    result = Hash.combine(result, multiplier, province);
    result = Hash.combine(result, multiplier, city);
    result = Hash.combine(result, multiplier, parentCountry);
    result = Hash.combine(result, multiplier, parentProvince);
    result = Hash.combine(result, multiplier, grandpaCountry);
    result = Hash.combine(result, multiplier, grandpaProvince);
    result = Hash.combine(result, multiplier, family);
    result = Hash.combine(result, multiplier, subFamily);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("code", code)
        .append("name", name)
        .append("parentId", parentId)
        .append("grandpaId", grandpaId)
        .append("country", country)
        .append("province", province)
        .append("city", city)
        .append("parentCountry", parentCountry)
        .append("parentProvince", parentProvince)
        .append("grandpaCountry", grandpaCountry)
        .append("grandpaProvince", grandpaProvince)
        .append("family", family)
        .append("subFamily", subFamily)
        .toString();
  }
}
