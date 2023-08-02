////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.annotation.Unique;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@XmlRootElement(name = "bad-bean")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BadBean {


  /**
   * 唯一标识，系统自动生成。
   */
  @Unique
  @NotNull
  private Long id;

  /**
   * 代码，全局不可重复，一旦设置不能更改。
   */
  @Unique
  @Size(min = 1, max = 64)
  private String code;

  /**
   * 名称，同一机构下不可重复。
   */
  @Size(min = 1, max = 256)
  private String name;

  /**
   * 创建时间。
   *
   * <p>注意这里的序列化名称故意错误！！！
   */
  @JsonProperty("createTime")
  @XmlElement(name = "createTime")
  private Instant createTime;

  public final Long getId() {
    return id;
  }

  public final BadBean setId(final Long id) {
    this.id = id;
    return this;
  }

  public final String getCode() {
    return code;
  }

  public final BadBean setCode(final String code) {
    this.code = code;
    return this;
  }

  public final String getName() {
    return name;
  }

  public final BadBean setName(final String name) {
    this.name = name;
    return this;
  }

  public final Instant getCreateTime() {
    return createTime;
  }

  public final BadBean setCreateTime(final Instant createTime) {
    this.createTime = createTime;
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
    final BadBean other = (BadBean) o;
    return Equality.equals(id, other.id)
            && Equality.equals(code, other.code)
            && Equality.equals(name, other.name)
            && Equality.equals(createTime, other.createTime);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, code);
    result = Hash.combine(result, multiplier, name);
    result = Hash.combine(result, multiplier, createTime);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("id", id)
            .append("code", code)
            .append("name", name)
            .append("createTime", createTime)
            .toString();
  }
}
