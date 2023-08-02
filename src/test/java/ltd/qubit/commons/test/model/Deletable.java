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

import javax.annotation.Nullable;

import ltd.qubit.commons.annotation.Computed;

/**
 * 此接口表示实体类记录了标记删除时间。
 *
 * @author 胡海星
 */
public interface Deletable {

  /**
   * 获取当前对象的标记删除时间。
   *
   * @return
   *     当前对象的标记删除时间；如为{@code null}则表示当前对象未被标记删除。
   */
  @Nullable
  Instant getDeleteTime();

  /**
   * 设置当前对象的标记删除时间。
   *
   * @param deleteTime
   *     待设置的新的标记删除时间；如为{@code null}则表示当前对象未被标记删除。
   */
  void setDeleteTime(@Nullable Instant deleteTime);

  /**
   * 判定此对象是否已被标记删除。
   *
   * @return
   *     若此对象已被标记删除，返回{@code true}；否则返回{@code false}。
   */
  @Computed("deleteTime")
  default boolean isDeleted() {
    return getDeleteTime() != null;
  }
}
