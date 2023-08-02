////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import javax.annotation.Nullable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示属于某个App，某个实体的可删除对象的基本信息。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "info")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(Include.NON_NULL)
public class InfoWithEntity extends Info implements WithEntity {

  private static final long serialVersionUID = 7281371900014761423L;

  /**
   * 所属实体。
   */
  @Nullable
  private String entity;

  /**
   * 创建一个{@link Info}对象。
   *
   * @param id
   *     对象ID，可以为{@code null}.
   * @return 若{@code id}不为{@code null}，则返回一个指定ID的{@link InfoWithEntity}对
   *     象，否则 返回{@code null}.
   */
  public static InfoWithEntity create(@Nullable final Long id) {
    if (id == null) {
      return null;
    } else {
      return new InfoWithEntity(id);
    }
  }

  /**
   * 创建一个{@link Info}对象。
   *
   * @param id
   *     对象ID，可以为{@code null}.
   * @param code
   *     对象编码，可以为{@code null}.
   * @return 若{@code id}和{@code code}不全为{@code null}，则返回一个指定ID和编码的
   *     {@link InfoWithEntity}对象，否则返回{@code null}.
   */
  public static InfoWithEntity create(@Nullable final Long id,
      @Nullable final String code) {
    if (id == null && code == null) {
      return null;
    } else {
      return new InfoWithEntity(id, code);
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
   * @return 若{@code id}，{@code code}和{@code name}不全为{@code null}，则返回一个
   *     指定ID、编码和名称的{@link InfoWithEntity}对象，否则返回{@code null}.
   */
  public static InfoWithEntity create(@Nullable final Long id,
      @Nullable final String code, @Nullable final String name) {
    if (id == null && code == null && name == null) {
      return null;
    } else {
      return new InfoWithEntity(id, code, name);
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
   * @param entity
   *     对象所属实体，可以为{@code null}.
   * @return 若{@code id}，{@code code}，{@code name}和{@code entity}不全为
   *     {@code null}，则返回一个指定ID、编码、名称和实体的{@link InfoWithEntity}对象，
   *     否则返回{@code null}.
   */
  public static InfoWithEntity create(@Nullable final Long id,
      @Nullable final String code, @Nullable final String name,
      @Nullable final String entity) {
    if (id == null && code == null && name == null && entity == null) {
      return null;
    } else {
      return new InfoWithEntity(id, code, name, entity);
    }
  }

  public InfoWithEntity(@Nullable final Long id) {
    this.id = id;
  }

  public InfoWithEntity(@Nullable final Long id, @Nullable final String code) {
    this.id = id;
    this.code = code;
  }

  public InfoWithEntity(@Nullable final Long id, @Nullable final String code,
      @Nullable final String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  public InfoWithEntity(@Nullable final Long id, @Nullable final String code,
      @Nullable final String name, @Nullable final String entity) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.entity = entity;
  }

  public InfoWithEntity() {
    // empty
  }

  @Nullable
  public final String getEntity() {
    return entity;
  }

  public final void setEntity(@Nullable final String entity) {
    this.entity = entity;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final InfoWithEntity other = (InfoWithEntity) o;
    return super.equals(other)
        && Equality.equals(entity, other.entity);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, super.hashCode());
    result = Hash.combine(result, multiplier, entity);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .appendSuper(super.toString())
        .append("entity", entity)
        .toString();
  }
}
