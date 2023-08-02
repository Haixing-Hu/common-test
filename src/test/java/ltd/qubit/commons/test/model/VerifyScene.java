////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * 此枚举表示验证场景。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "verify-scene")
public enum VerifyScene {

  /**
   * 注册。
   */
  REGISTER,

  /**
   * 重置密码。
   */
  RESET_PASSWORD,

  /**
   * 支付。
   */
  PAY,

  /**
   * 退款。
   */
  REFUND,

  /**
   * 验证手机号。
   */
  VERIFY_MOBILE,

  /**
   * 验证电子邮箱。
   */
  VERIFY_EMAIL,

  /**
   * 实名认证。
   */
  VERIFY_REALNAME,

  /**
   * 取药验证码。
   */
  RECEIVE_DRUG,

  /**
   * 更新信息。
   */
  MODIFY,

  /**
   * 登陆。
   */
  LOGIN,

  /**
   * 其他。
   */
  OTHER,
}
