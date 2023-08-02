////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import ltd.qubit.commons.util.codec.BigDecimalCodec;

/**
 * 地理位置坐标编码器。
 *
 * @author 胡海星
 */
public final class LocationCoordinateCodec extends BigDecimalCodec {

  /**
   * 坐标精度，即小数点后面数字的位数，保留5位。
   */
  public static final int SCALE = 5;

  public LocationCoordinateCodec() {
    super(SCALE);
  }
}
