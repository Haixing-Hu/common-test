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

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

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
 * 此模型表示编码。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "code")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(Include.NON_NULL)
public class Code implements Assignable<Code> {

  /**
   * 所属APP。
   */
  private Info app;

  /**
   * 编码依据的规范标准。
   */
  @Size(min = 1, max = 128)
  @Nullable
  private String standard;

  /**
   * 具体编码值。
   */
  @Size(min = 1, max = 128)
  private String code;

  public Code() {
    // empty
  }

  public Code(final Code other) {
    assign(other);
  }

  @Override
  public void assign(final Code other) {
    Argument.requireNonNull("other", other);
    app = Assignment.clone(other.app);
    standard = other.standard;
    code = other.code;
  }

  @Override
  public Code clone() {
    return new Code(this);
  }

  public final Info getApp() {
    return app;
  }

  public final void setApp(final Info app) {
    this.app = app;
  }

  @Nullable
  public final String getStandard() {
    return standard;
  }

  public final void setStandard(@Nullable final String standard) {
    this.standard = standard;
  }

  public final String getCode() {
    return code;
  }

  public final void setCode(final String code) {
    this.code = code;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final Code other = (Code) o;
    return Equality.equals(app, other.app)
        && Equality.equals(standard, other.standard)
        && Equality.equals(code, other.code);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, app);
    result = Hash.combine(result, multiplier, standard);
    result = Hash.combine(result, multiplier, code);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("app", app)
        .append("standard", standard)
        .append("code", code)
        .toString();
  }
}
