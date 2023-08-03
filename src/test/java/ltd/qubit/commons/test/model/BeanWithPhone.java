////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.util.StringJoiner;

import javax.annotation.Nullable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@XmlRootElement(name = "bean-with-phone")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = ANY,
                getterVisibility = NONE,
                isGetterVisibility = NONE,
                setterVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeanWithPhone {

  /**
   * 手机号码。
   */
  @XmlElement(name = "mobile")
  @Nullable
  private Phone mobile;

  @Nullable
  public final Phone getMobile() {
    return mobile;
  }

  public final BeanWithPhone setMobile(
          @Nullable final Phone mobile) {
    this.mobile = mobile;
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
    final BeanWithPhone other = (BeanWithPhone) o;
    return Equality.equals(mobile, other.mobile);
  }

  @Override
  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, mobile);
    return result;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BeanWithPhone.class.getSimpleName() + "[", "]")
            .add("mobile=" + mobile)
            .toString();
  }
}
