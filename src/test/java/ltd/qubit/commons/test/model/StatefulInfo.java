////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * 此模型表示有状态且可删除的对象的基本信息。
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
public class StatefulInfo extends Info implements Stateful {

  private static final long serialVersionUID = 2919962686331957698L;

  /**
   * 状态。
   */
  protected State state;

  public StatefulInfo() {}

  public StatefulInfo(final StatefulInfo other) {
    assign(other);
  }

  public StatefulInfo(final Long id, final String code, final String name,
      final State state) {
    super(id, code, name);
    this.state = state;
  }

  @Override
  public final State getState() {
    return state;
  }

  @Override
  public final void setState(final State state) {
    this.state = state;
  }

  @Override
  public void assign(final StatefulInfo other) {
    super.assign(other);
    state = other.state;
  }

  @Override
  public StatefulInfo clone() {
    return new StatefulInfo(this);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final StatefulInfo other = (StatefulInfo) o;
    return Equality.equals(state, other.state)
            && super.equals(other);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, super.hashCode());
    result = Hash.combine(result, multiplier, state);
    return result;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("state", state)
            .toString();
  }
}
