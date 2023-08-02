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
 * 此接口表示实体类具有状态属性。
 *
 * @author 胡海星
 */
public interface Stateful {

  /**
   * 获取当前对象的状态。
   *
   * @return
   *     当前对象的状态。
   */
  State getState();

  /**
   * 设置当前对象的状态。
   *
   * @param state
   *     待设置的新状态。
   */
  void setState(State state);
}
