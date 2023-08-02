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
import java.time.Instant;

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
 * 此模型表示各种证件、执照等.
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
public class Credential implements Serializable, Assignable<Credential> {

  private static final long serialVersionUID = 3943505292507133852L;

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
   * <p>对于个人，证件号码通常是身份证号码，社保卡号码等；对于机构，证件号码通常是营业执
   * 照编号、组织机构代码等。
   */
  private String number;

  /**
   * 证件的验证状态。
   */
  @Nullable
  private VerifyState verified;

  /**
   * 创建时间，存储UTC时间戳。
   */
  private Instant createTime;

  /**
   * 最后一次修改时间，存储UTC时间戳。
   */
  @Nullable
  private Instant modifyTime;

  /**
   * 标记删除时间，存储UTC时间戳。
   */
  @Nullable
  private Instant deleteTime;

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
  public static Credential create(@Nullable final CredentialType type,
      @Nullable final String number) {
    if (type == null && number == null) {
      return null;
    } else {
      return new Credential(type, number);
    }
  }

  public Credential() {}

  public Credential(final Credential other) {
    assign(other);
  }

  public Credential(final CredentialType type, final String number) {
    this.type = type;
    this.number = number;
  }

  public void assign(final Credential other) {
    this.id = other.id;
    this.type = other.type;
    this.number = other.number;
    this.verified = other.verified;
  }

  @Override
  public Credential clone() {
    return new Credential(this);
  }

  public final Long getId() {
    return id;
  }

  public final Credential setId(final Long id) {
    this.id = id;
    return this;
  }

  public final CredentialType getType() {
    return type;
  }

  public final Credential setType(final CredentialType type) {
    this.type = type;
    return this;
  }

  public final String getNumber() {
    return number;
  }

  public final Credential setNumber(final String number) {
    this.number = number;
    return this;
  }

  @Nullable
  public final VerifyState getVerified() {
    return verified;
  }

  public final Credential setVerified(@Nullable final VerifyState verified) {
    this.verified = verified;
    return this;
  }

  public final Instant getCreateTime() {
    return createTime;
  }

  public final Credential setCreateTime(final Instant createTime) {
    this.createTime = createTime;
    return this;
  }

  @Nullable
  public final Instant getModifyTime() {
    return modifyTime;
  }

  public final Credential setModifyTime(@Nullable final Instant modifyTime) {
    this.modifyTime = modifyTime;
    return this;
  }

  @Nullable
  public final Instant getDeleteTime() {
    return deleteTime;
  }

  public final Credential setDeleteTime(@Nullable final Instant deleteTime) {
    this.deleteTime = deleteTime;
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
    final Credential other = (Credential) o;
    return Equality.equals(id, other.id)
            && Equality.equals(type, other.type)
            && Equality.equals(number, other.number)
            && Equality.equals(verified, other.verified)
            && Equality.equals(createTime, other.createTime)
            && Equality.equals(modifyTime, other.modifyTime)
            && Equality.equals(deleteTime, other.deleteTime);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, type);
    result = Hash.combine(result, multiplier, number);
    result = Hash.combine(result, multiplier, verified);
    result = Hash.combine(result, multiplier, createTime);
    result = Hash.combine(result, multiplier, modifyTime);
    result = Hash.combine(result, multiplier, deleteTime);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("id", id)
            .append("type", type)
            .append("number", number)
            .append("verified", verified)
            .append("createTime", createTime)
            .append("modifyTime", modifyTime)
            .append("deleteTime", deleteTime)
            .toString();
  }
}
