////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import javax.annotation.Nullable;

/**
 * 此接口表示实体类具有联系方式属性。
 *
 * @author 胡海星
 */
public interface WithContact {

  /**
   * 获取当前对象的联系方式。
   *
   * @return
   *     当前对象的联系方式，或{@code null}如果当前对象没有设置联系方式。
   */
  @Nullable
  Contact getContact();

  /**
   * 设置当前对象的联系方式。
   *
   * @param contact
   *     新的联系方式，可以为{@code null}。
   */
  void setContact(@Nullable Contact contact);
}
