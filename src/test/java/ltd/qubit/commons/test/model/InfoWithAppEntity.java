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
 * @param <T>
 *     此基本信息所代表的对象的类，该参数只起到标记作用，增加代码可读性，没有实际作用。
 * @author 胡海星
 */
@XmlRootElement(name = "info")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(Include.NON_NULL)
public class InfoWithAppEntity extends Info implements WithApp, WithEntity {

  private static final long serialVersionUID = 7281371900014761423L;

  /**
   * 所属App。
   */
  @Nullable
  private Info app;

  /**
   * 所属实体。
   */
  @Nullable
  private String entity;

  public InfoWithAppEntity() {
    // empty
  }

  @Nullable
  public final Info getApp() {
    return app;
  }

  public final void setApp(@Nullable final Info app) {
    this.app = app;
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
    final InfoWithAppEntity other = (InfoWithAppEntity) o;
    return super.equals(other)
        && Equality.equals(app, other.app)
        && Equality.equals(entity, other.entity);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, super.hashCode());
    result = Hash.combine(result, multiplier, app);
    result = Hash.combine(result, multiplier, entity);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .appendSuper(super.toString())
        .append("app", app)
        .append("entity", entity)
        .toString();
  }
}
