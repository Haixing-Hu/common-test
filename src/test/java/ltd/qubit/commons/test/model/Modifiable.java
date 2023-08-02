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

/**
 * 此接口表示实体类记录了最后一次修改时间。
 *
 * @author 胡海星
 */
public interface Modifiable {

  /**
   * 获取当前对象的最后一次修改时间。
   *
   * @return
   *     当前对象的最后一次修改时间；如为{@code null}则表示当前对象未被修改过。
   */
  @Nullable
  Instant getModifyTime();

  /**
   * 设置当前对象的最后一次修改时间。
   *
   * @param modifyTime
   *     待设置的新的最后一次修改时间；如为{@code null}则表示当前对象未被修改过。
   */
  void setModifyTime(@Nullable Instant modifyTime);

}
