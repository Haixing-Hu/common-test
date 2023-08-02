////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import ltd.qubit.commons.reflect.BeanInfo;

public class InvalidReferencePathException extends IllegalArgumentException {

  private static final long serialVersionUID = -2955992866193832791L;

  public InvalidReferencePathException(final BeanInfo modelInfo,
      final ReferencedProperty property) {
    super(String.format(
        "Invalid reference path for the %s.%s: %s",
        modelInfo.getName(),
        property.getPropertyPath().replace(EntityInfo.PATH_SEPARATOR, "."),
        property.getReferencePath()
    ));
  }
}
