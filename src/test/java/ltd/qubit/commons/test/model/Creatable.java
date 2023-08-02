////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import java.time.Instant;

/**
 * 此接口表示实体类记录了创建时间。
 *
 * @author 胡海星
 */
public interface Creatable {

  /**
   * 获取当前对象的创建时间。
   *
   * @return
   *     当前对象的创建时间，不该为{@code null}。
   */
  Instant getCreateTime();

  /**
   * 设置当前对象的创建时间。
   *
   * @param createTime
   *     待设置的新的创建时间，不可为{@code null}。
   */
  void setCreateTime(Instant createTime);

}
