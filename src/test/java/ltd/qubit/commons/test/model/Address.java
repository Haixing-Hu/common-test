////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.io.Serializable;

import javax.annotation.Nullable;

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.annotation.Reference;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Assignment;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import static ltd.qubit.commons.lang.Argument.requireNonNull;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示地址。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "address")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(Include.NON_NULL)
public class Address implements Serializable, Normalizable,
    Assignable<Address> {

  private static final long serialVersionUID = -5349750421674359532L;

  /**
   * 所属国家的基本信息。
   */
  @Reference(entity = Country.class, property = "info", path = "province/country")
  @Nullable
  private Info country;

  /**
   * 所属省份的基本信息。
   */
  @Reference(entity = Province.class, property = "info", path = "city/province")
  @Nullable
  private Info province;

  /**
   * 所属城市的基本信息。
   */
  @Reference(entity = City.class, property = "info", path = "district/city")
  @Nullable
  private Info city;

  /**
   * 所属区县的基本信息。
   */
  @Reference(entity = District.class, property = "info", path = "street/district")
  @Nullable
  private Info district;

  /**
   * 所属街道的基本信息。
   */
  @Reference(entity = Street.class, property = "info")
  @Nullable
  private Info street;

  /**
   * 详细地址，门牌号码等。
   */
  @Size(max = 4096)
  @Nullable
  private String detail;

  /**
   * 邮政编码。
   */
  @Size(max = 64)
  @Nullable
  private String postalcode;

  /**
   * 定位。
   */
  @Nullable
  private Location location;

  public Address() {
    // empty
  }

  public Address(final Address other) {
    assign(other);
  }

  @Override
  public void assign(final Address other) {
    requireNonNull("other", other);
    country = Assignment.clone(other.country);
    province = Assignment.clone(other.province);
    city = Assignment.clone(other.city);
    district = Assignment.clone(other.district);
    street = Assignment.clone(other.street);
    detail = other.detail;
    postalcode = other.postalcode;
    location = Assignment.clone(other.location);
  }

  public Address clone() {
    return new Address(this);
  }

  public final Info getCountry() {
    return country;
  }

  public final void setCountry(final Info country) {
    this.country = country;
  }

  public final Info getProvince() {
    return province;
  }

  public final void setProvince(final Info province) {
    this.province = province;
  }

  public final Info getCity() {
    return city;
  }

  public final void setCity(final Info city) {
    this.city = city;
  }

  public final Info getDistrict() {
    return district;
  }

  public final void setDistrict(final Info district) {
    this.district = district;
  }

  public final Info getStreet() {
    return street;
  }

  public final void setStreet(final Info street) {
    this.street = street;
  }

  @Nullable
  public final String getDetail() {
    return detail;
  }

  public final void setDetail(@Nullable final String detail) {
    this.detail = detail;
  }

  @Nullable
  public final String getPostalcode() {
    return postalcode;
  }

  public final void setPostalcode(@Nullable final String postalcode) {
    this.postalcode = postalcode;
  }

  @Nullable
  public final Location getLocation() {
    return location;
  }

  public final void setLocation(@Nullable final Location location) {
    this.location = location;
  }

  public final boolean isEmpty() {
    return (country == null || country.isEmpty())
            && (province == null || province.isEmpty())
            && (city == null || city.isEmpty())
            && (district == null || district.isEmpty())
            && (street == null || street.isEmpty())
            && (detail == null || detail.isEmpty())
            && (postalcode == null || postalcode.isEmpty())
            && (location == null || location.isEmpty());
  }

  public boolean isSame(final Address other) {
    if (other == null) {
      return false;
    } else {
      return Equality.equals(detail, other.detail)
              && Equality.equals(postalcode, other.postalcode)
              && Equality.equals(location, other.location)
              && ((street == null && other.street == null)
                || (street != null
                      && other.street != null
                      && Equality.equals(street.getId(), other.street.getId())));
    }
  }

  @Override
  public void normalize() {
    if (location != null) {
      location.normalize();
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Address other = (Address) o;
    return Equality.equals(country, other.country)
            && Equality.equals(province, other.province)
            && Equality.equals(city, other.city)
            && Equality.equals(district, other.district)
            && Equality.equals(street, other.street)
            && Equality.equals(detail, other.detail)
            && Equality.equals(postalcode, other.postalcode)
            && Equality.equals(location, other.location);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, country);
    result = Hash.combine(result, multiplier, province);
    result = Hash.combine(result, multiplier, city);
    result = Hash.combine(result, multiplier, district);
    result = Hash.combine(result, multiplier, street);
    result = Hash.combine(result, multiplier, detail);
    result = Hash.combine(result, multiplier, postalcode);
    result = Hash.combine(result, multiplier, location);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("country", country)
            .append("province", province)
            .append("city", city)
            .append("district", district)
            .append("street", street)
            .append("detail", detail)
            .append("postalcode", postalcode)
            .append("location", location)
            .toString();
  }
}
