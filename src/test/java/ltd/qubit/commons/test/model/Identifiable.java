////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.io.Serializable;

/**
 * 此接口表示实体类具有ID属性。
 *
 * @author 胡海星
 */
public interface Identifiable extends Serializable {

  /**
   * 获取当前对象的唯一标志符。
   *
   * @return
   *     当前对象的唯一标志符。
   */
  Long getId();

  /**
   * 设置当前对象的唯一标志符。
   *
   * @param id
   *     待设置的新的唯一标志符。
   */
  void setId(Long id);
}
