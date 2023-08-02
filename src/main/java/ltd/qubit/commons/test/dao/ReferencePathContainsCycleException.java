////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.util.List;

import ltd.qubit.commons.reflect.BeanInfo;

public class ReferencePathContainsCycleException extends IllegalArgumentException {

  private static final long serialVersionUID = -2955992866193832791L;

  public ReferencePathContainsCycleException(final BeanInfo modelInfo,
      final List<ReferencedProperty> properties) {
    super(buildErrorMessage(modelInfo, properties));
  }

  private static String buildErrorMessage(final BeanInfo modelInfo,
      final List<ReferencedProperty> properties) {
    final StringBuilder builder = new StringBuilder();
    builder.append("The references path of properties in ")
        .append(modelInfo.getName())
        .append(" contains a cycle: ");
    int i = 0;
    for (final ReferencedProperty prop : properties) {
      if (i > 0) {
        builder.append(", ");
      }
      builder.append(prop.getPropertyPath())
          .append(": ")
          .append(prop.getReferencePath());
      ++i;
    }
    return builder.toString();
  }
}
