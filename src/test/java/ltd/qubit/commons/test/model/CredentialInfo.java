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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示各种证件、执照的基本信息。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "credential")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CredentialInfo implements Serializable, Assignable<CredentialInfo> {

  private static final long serialVersionUID = -1465636291913090852L;

  /**
   * 唯一标识，系统自动生成。
   */
  private Long id;

  /**
   * 证件类型。
   *
   * <p>对于个人，证件类型通常是身份证，社保卡等；对于机构，证件类型通常是营业执照、组织
   * 机构代码证等。
   */
  private CredentialType type;

  /**
   * 证件号码。
   *
   * <p>对于个人，证件号码通常是身份证号码，社保卡号码等；对于机构，证件号码通常是营业
   * 执照编号、组织机构代码等。
   */
  private String number;

  /**
   * 证件的验证状态。
   */
  @Nullable
  private VerifyState verified;

  /**
   * 创建一个证件。
   *
   * @param type
   *     证件类型，可以为{@code null}.
   * @param number
   *     证件号码，可以为{@code null}.
   * @return 若{@code type}和{@code number}不全为{@code null}，则返回一个指定类型和号
   *     码的证件对象，否则返回{@code null}.
   */
  public static CredentialInfo create(@Nullable final CredentialType type,
      @Nullable final String number) {
    if (type == null && number == null) {
      return null;
    } else {
      return new CredentialInfo(type, number);
    }
  }

  public CredentialInfo() {}

  public CredentialInfo(final CredentialType type, final String number) {
    this.type = type;
    this.number = number;
  }

  public CredentialInfo(final Credential credential) {
    this.id = credential.getId();
    this.type = credential.getType();
    this.number = credential.getNumber();
  }

  public CredentialInfo(final CredentialInfo other) {
    assign(other);
  }

  @Override
  public void assign(final CredentialInfo other) {
    this.id = other.id;
    this.type = other.type;
    this.number = other.number;
  }

  @Override
  public CredentialInfo clone() {
    return new CredentialInfo(this);
  }

  public boolean isSame(@Nullable final CredentialInfo other) {
    if (other == null) {
      return false;
    } else if (id != null && other.id != null) {
      return (id.longValue() == other.id.longValue());
    } else {
      return (type != null)
              && (type == other.type)
              && (number != null)
              && number.equals(other.number);
    }
  }

  public final Long getId() {
    return id;
  }

  public final CredentialInfo setId(final Long id) {
    this.id = id;
    return this;
  }

  public final CredentialType getType() {
    return type;
  }

  public final CredentialInfo setType(final CredentialType type) {
    this.type = type;
    return this;
  }

  public final String getNumber() {
    return number;
  }

  public final CredentialInfo setNumber(final String number) {
    this.number = number;
    return this;
  }

  @Nullable
  public final VerifyState getVerified() {
    return verified;
  }

  public final CredentialInfo setVerified(@Nullable final VerifyState verified) {
    this.verified = verified;
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
    final CredentialInfo other = (CredentialInfo) o;
    return Equality.equals(id, other.id)
            && Equality.equals(type, other.type)
            && Equality.equals(number, other.number)
            && Equality.equals(verified, other.verified);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, type);
    result = Hash.combine(result, multiplier, number);
    result = Hash.combine(result, multiplier, verified);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("id", id)
            .append("type", type)
            .append("number", number)
            .append("verified", verified)
            .toString();
  }
}
