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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ltd.qubit.commons.annotation.Identifier;
import ltd.qubit.commons.lang.Assignable;
import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.lang.StringUtils;
import ltd.qubit.commons.text.tostring.ToStringBuilder;
import ltd.qubit.commons.text.xml.jaxb.IsoInstantXmlAdapter;
import ltd.qubit.commons.util.pair.KeyValuePair;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示可删除对象的基本信息。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "info")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Info implements Identifiable, WithCode, WithName, Deletable,
    Assignable<Info> {

  private static final long serialVersionUID = 7281371900014761423L;

  /**
   * 唯一标识。
   */
  @XmlElement(name = "id")
  @Identifier
  protected Long id;

  /**
   * 编码。
   */
  @XmlElement(name = "code")
  @Size(min = 1, max = 64)
  @NotBlank
  protected String code;

  /**
   * 名称。
   */
  @XmlElement(name = "name")
  @Size(min = 1, max = 128)
  @NotBlank
  protected String name;

  /**
   * 标记删除时间。
   */
  @XmlElement(name = "delete-time")
  @XmlJavaTypeAdapter(IsoInstantXmlAdapter.class)
  @Nullable
  protected Instant deleteTime;

  /**
   * 创建一个{@link Info}对象。
   *
   * @param id
   *     对象ID，可以为{@code null}.
   * @return
   *     若{@code id}不为{@code null}，则返回一个指定ID的{@link Info}对象，否则返回
   *     {@code null}.
   */
  public static Info create(@Nullable final Long id) {
    if (id == null) {
      return null;
    } else {
      return new Info(id);
    }
  }

  /**
   * 创建一个{@link Info}对象。
   *
   * @param id
   *     对象ID，可以为{@code null}.
   * @param code
   *     对象编码，可以为{@code null}.
   * @return
   *     若{@code id}和{@code code}不全为{@code null}，则返回一个指定ID和编码的{@link
   *     Info}对象，否则返回{@code null}.
   */
  public static Info create(@Nullable final Long id, @Nullable final String code) {
    if (id == null && code == null) {
      return null;
    } else {
      return new Info(id, code);
    }
  }

  /**
   * 创建一个{@link Info}对象。
   *
   * @param id
   *     对象ID，可以为{@code null}.
   * @param code
   *     对象编码，可以为{@code null}.
   * @param name
   *     对象名称，可以为{@code null}.
   * @return
   *     若{@code id}，{@code code}和{@code name}不全为{@code null}，则返回一个指定
   *     ID、编码和名称的{@link Info}对象，否则返回{@code null}.
   */
  public static Info create(@Nullable final Long id,
      @Nullable final String code, @Nullable final String name) {
    if (id == null && code == null && name == null) {
      return null;
    } else {
      return new Info(id, code, name);
    }
  }

  public Info() {
    // empty
  }

  public Info(@Nullable final Long id) {
    this.id = id;
  }

  public Info(@Nullable final Long id, @Nullable final String code) {
    this.id = id;
    this.code = code;
  }

  public Info(@Nullable final Long id, @Nullable final String code,
      @Nullable final String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public Info(final Info other) {
    assign(other);
  }

  public Info(final StatefulInfo other) {
    assign(other);
  }

  @Override
  public final Long getId() {
    return id;
  }

  @Override
  public final void setId(final Long id) {
    this.id = id;
  }

  @Override
  public final String getCode() {
    return code;
  }

  @Override
  public final void setCode(final String code) {
    this.code = code;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final void setName(final String name) {
    this.name = name;
  }

  @Override
  @Nullable
  public final Instant getDeleteTime() {
    return deleteTime;
  }

  @Override
  public final void setDeleteTime(@Nullable final Instant deleteTime) {
    this.deleteTime = deleteTime;
  }

  @Override
  public boolean isDeleted() {
    return this.deleteTime != null;
  }

  public final boolean isEmpty() {
    return (id == null)
            && StringUtils.isEmpty(code)
            && StringUtils.isEmpty(name);
  }

  @Override
  public void assign(final Info other) {
    id = other.id;
    code = other.code;
    name = other.name;
    deleteTime = other.deleteTime;
  }

  public void assign(final StatefulInfo other) {
    id = other.getId();
    code = other.getCode();
    name = other.getName();
    deleteTime = other.getDeleteTime();
  }

  @Override
  public Info clone() {
    return new Info(this);
  }

  public KeyValuePair[] toParams() {
    final List<KeyValuePair> params = new ArrayList<>();
    params.add(new KeyValuePair("id", id));
    params.add(new KeyValuePair("name", name));
    params.add(new KeyValuePair("code", code));
    return params.toArray(new KeyValuePair[0]);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    // 注意：允许Info的trivial子类和Info进行比较
    if ((o == null) || (! (o instanceof final Info other))) {
      return false;
    }
    return Equality.equals(id, other.id)
            && Equality.equals(code, other.code)
            && Equality.equals(name, other.name)
            && Equality.equals(deleteTime, other.deleteTime);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, id);
    result = Hash.combine(result, multiplier, code);
    result = Hash.combine(result, multiplier, name);
    result = Hash.combine(result, multiplier, deleteTime);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .append("id", id)
            .append("code", code)
            .append("name", name)
            .append("deleteTime", deleteTime)
            .toString();
  }
}
