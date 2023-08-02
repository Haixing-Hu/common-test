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
 * 此枚举表示实体类型的状态。
 *
 * @author 胡海星
 */
@XmlRootElement(name = "state")
public enum State {

  /**
   * 未激活。
   */
  INACTIVE,

  /**
   * 正常。
   */
  NORMAL,

  /**
   * 锁定/冻结。
   */
  LOCKED,

  /**
   * 屏蔽/封杀。
   */
  BLOCKED,

  /**
   * 已废弃。
   */
  OBSOLETED,

  /**
   * 禁用。
   */
  DISABLED,
}
