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
 * 此接口表示实体类具有编码属性。
 *
 * @author 胡海星
 */
public interface WithCode {

  /**
   * 获取当前对象的编码。
   *
   * @return
   *     当前对象的编码。
   */
  String getCode();

  /**
   * 设置当前对象的编码。
   *
   * @param code
   *     待设置的新的编码。
   */
  void setCode(String code);
}
