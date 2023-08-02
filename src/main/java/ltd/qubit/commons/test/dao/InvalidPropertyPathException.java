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

import static ltd.qubit.commons.test.dao.EntityInfo.PATH_SEPARATOR;

public class InvalidPropertyPathException extends IllegalArgumentException {

  private static final long serialVersionUID = -2955992866193832791L;

  public InvalidPropertyPathException(final BeanInfo modelInfo,
      final ReferencedProperty property) {
    super(String.format(
        "Invalid property path for the %s.%s",
        modelInfo.getName(),
        property.getPropertyPath().replace(PATH_SEPARATOR, ".")
    ));
  }
}
