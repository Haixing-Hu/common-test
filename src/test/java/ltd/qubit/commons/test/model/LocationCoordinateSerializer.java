////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.model;

import ltd.qubit.commons.text.jackson.serializer.BigDecimalSerializer;

/**
 * 地理位置坐标的JSON序列化器。
 *
 * @author 胡海星
 */
public class LocationCoordinateSerializer extends BigDecimalSerializer {

  private static final long serialVersionUID = 2115578678289300490L;

  public LocationCoordinateSerializer() {
    super(new LocationCoordinateCodec());
  }

}
