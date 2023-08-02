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
import ltd.qubit.commons.lang.Argument;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Assignment;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示联系方式。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "contact")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(Include.NON_NULL)
public class Contact implements Serializable, Assignable<Contact> {

  private static final long serialVersionUID = -3017966202832988873L;

  /**
   * 固定电话号码。
   */
  @Nullable
  private Phone phone;

  /**
   * 固定电话号码验证状态。
   */
  private VerifyState phoneVerifyState;

  /**
   * 手机号码。
   */
  @Nullable
  private Phone mobile;

  /**
   * 手机号码验证状态。
   */
  private VerifyState mobileVerifyState;

  /**
   * 电子邮件地址。
   */
  @Size(max = 512)
  @Nullable
  private String email;

  /**
   * 电子邮件地址验证状态。
   */
  private VerifyState emailVerifyState;

  /**
   * 网址 URL。
   */
  @Size(max = 512)
  @Nullable
  private String url;

  /**
   * 联系地址。
   */
  @Reference
  @Nullable
  private Address address;

  /**
   * 联系地址验证状态。
   */
  private VerifyState addressVerifyState;

  public Contact() {
    // empty
  }

  public Contact(final Contact other) {
    assign(other);
  }

  public static Contact create(@Nullable final Phone phone,
          @Nullable final Phone mobile,
          @Nullable final String email,
          @Nullable final String url,
          @Nullable final Address address) {
    if (phone == null
            && mobile == null
            && email == null
            && url == null
            && address == null) {
      return null;
    } else {
      final Contact result = new Contact();
      result.setPhone(phone);
      result.setMobile(mobile);
      result.setEmail(email);
      result.setUrl(url);
      result.setAddress(address);
      return result;
    }
  }

  @Override
  public void assign(final Contact other) {
    Argument.requireNonNull("other", other);
    phone = Assignment.clone(other.phone);
    phoneVerifyState = other.phoneVerifyState;
    mobile = Assignment.clone(other.mobile);
    mobileVerifyState = other.mobileVerifyState;
    email = other.email;
    emailVerifyState = other.emailVerifyState;
    url = other.url;
    address = Assignment.clone(other.address);
    addressVerifyState = other.addressVerifyState;
  }

  @Override
  public Contact clone() {
    return new Contact(this);
  }

  public final Contact setVerifyState() {
    if (phone == null) {
      phoneVerifyState = null;
    } else {
      phoneVerifyState = VerifyState.NONE;
    }
    if (mobile == null) {
      mobileVerifyState = null;
    } else {
      mobileVerifyState = VerifyState.NONE;
    }
    if (email == null) {
      emailVerifyState = null;
    } else {
      emailVerifyState = VerifyState.NONE;
    }
    if (address == null) {
      addressVerifyState = null;
    } else {
      addressVerifyState = VerifyState.NONE;
    }
    return this;
  }

  public final Contact setVerifyState(final Contact old) {
    if (phone == null) {
      phoneVerifyState = null;
    } else if (phone.equals(old.phone)) {
      phoneVerifyState = old.phoneVerifyState;
    } else {
      phoneVerifyState = VerifyState.NONE;
    }
    if (mobile == null) {
      mobileVerifyState = null;
    } else if (mobile.equals(old.mobile)) {
      mobileVerifyState = old.mobileVerifyState;
    } else {
      mobileVerifyState = VerifyState.NONE;
    }
    if (email == null) {
      emailVerifyState = null;
    } else if (email.equals(old.email)) {
      emailVerifyState = old.emailVerifyState;
    } else {
      emailVerifyState = VerifyState.NONE;
    }
    if (address == null) {
      addressVerifyState = null;
    } else if (old.address == null) {
      addressVerifyState = VerifyState.NONE;
    } else if (address.isSame(old.address)) {
      addressVerifyState = old.addressVerifyState;
    } else {
      addressVerifyState = VerifyState.NONE;
    }
    return this;
  }

  public final boolean isEmpty() {
    return (phone == null || phone.isEmpty())
            && (mobile == null || mobile.isEmpty())
            && (email == null)
            && (url == null)
            && ((address == null) || address.isEmpty());
  }

  @Nullable
  public final Phone getPhone() {
    return phone;
  }

  public final void setPhone(@Nullable final Phone phone) {
    this.phone = phone;
  }

  public final VerifyState getPhoneVerifyState() {
    return phoneVerifyState;
  }

  public final void setPhoneVerifyState(final VerifyState phoneVerifyState) {
    this.phoneVerifyState = phoneVerifyState;
  }

  @Nullable
  public final Phone getMobile() {
    return mobile;
  }

  public final void setMobile(@Nullable final Phone mobile) {
    this.mobile = mobile;
  }

  public final VerifyState getMobileVerifyState() {
    return mobileVerifyState;
  }

  public final void setMobileVerifyState(final VerifyState mobileVerifyState) {
    this.mobileVerifyState = mobileVerifyState;
  }

  @Nullable
  public final String getEmail() {
    return email;
  }

  public final void setEmail(@Nullable final String email) {
    this.email = email;
  }

  public final VerifyState getEmailVerifyState() {
    return emailVerifyState;
  }

  public final void setEmailVerifyState(final VerifyState emailVerifyState) {
    this.emailVerifyState = emailVerifyState;
  }

  @Nullable
  public final String getUrl() {
    return url;
  }

  public final void setUrl(@Nullable final String url) {
    this.url = url;
  }

  public final Address getAddress() {
    return address;
  }

  public final void setAddress(final Address address) {
    this.address = address;
  }

  public final VerifyState getAddressVerifyState() {
    return addressVerifyState;
  }

  public final void setAddressVerifyState(final VerifyState addressVerifyState) {
    this.addressVerifyState = addressVerifyState;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Contact other = (Contact) o;
    return Equality.equals(phone, other.phone)
        && Equality.equals(phoneVerifyState, other.phoneVerifyState)
        && Equality.equals(mobile, other.mobile)
        && Equality.equals(mobileVerifyState, other.mobileVerifyState)
        && Equality.equals(email, other.email)
        && Equality.equals(emailVerifyState, other.emailVerifyState)
        && Equality.equals(url, other.url)
        && Equality.equals(address, other.address)
        && Equality.equals(addressVerifyState, other.addressVerifyState);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, phone);
    result = Hash.combine(result, multiplier, phoneVerifyState);
    result = Hash.combine(result, multiplier, mobile);
    result = Hash.combine(result, multiplier, mobileVerifyState);
    result = Hash.combine(result, multiplier, email);
    result = Hash.combine(result, multiplier, emailVerifyState);
    result = Hash.combine(result, multiplier, url);
    result = Hash.combine(result, multiplier, address);
    result = Hash.combine(result, multiplier, addressVerifyState);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("phone", phone)
        .append("phoneVerifyState", phoneVerifyState)
        .append("mobile", mobile)
        .append("mobileVerifyState", mobileVerifyState)
        .append("email", email)
        .append("emailVerifyState", emailVerifyState)
        .append("url", url)
        .append("address", address)
        .append("addressVerifyState", addressVerifyState)
        .toString();
  }
}
