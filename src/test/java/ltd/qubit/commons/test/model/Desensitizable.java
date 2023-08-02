////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

/**
 * 此接口表示实体类可进行脱敏操作。
 *
 * @author 胡海星
 */
public interface Desensitizable {

  /**
   * 对此对象进行脱敏操作。
   */
  void desensitize();
}
