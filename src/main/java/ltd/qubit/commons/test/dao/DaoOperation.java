////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.lang.reflect.Method;

import static ltd.qubit.commons.reflect.ConstructorUtils.newInstance;
import static ltd.qubit.commons.reflect.Option.DEFAULT;

/**
 * The enumeration of operations performed by a method of a DAO.
 *
 * @author Haixing Hu
 */
public enum DaoOperation {

  EXIST_NON_DELETED("^existNonDeleted(\\p{Upper}\\p{Alpha}+)?$",
      ExistNonDeletedOperationTestGenerator.class),

  EXIST("^exist(\\p{Upper}\\p{Alpha}+)?$",
      ExistOperationTestGenerator.class),

  COUNT("^count$",
      CountOperationTestGenerator.class),

  LIST("^list$",
      ListOperationTestGenerator.class),

  GET_OR_NULL("^get(?:(\\p{Upper}\\p{Alpha}+?)??(?:By(\\p{Upper}\\p{Alpha}+))?)?OrNull$",
      GetOperationTestGenerator.class),

  GET("^get(?:(\\p{Upper}\\p{Alpha}+?)??(?:By(\\p{Upper}\\p{Alpha}+))?)?$",
      GetOperationTestGenerator.class),

  ADD("^add$", AddOperationTestGenerator.class),

  UPDATE("^update(?:(\\p{Upper}\\p{Alpha}+?)??(?:By(\\p{Upper}\\p{Alpha}+))?)?$",
      UpdateOperationTestGenerator.class),

  ADD_OR_UPDATE("^addOrUpdate(?:(\\p{Upper}\\p{Alpha}+?)??(?:By(\\p{Upper}\\p{Alpha}+))?)?$",
      AddOrUpdateOperationTestGenerator.class),

  DELETE("^delete(?:By(\\p{Upper}\\p{Alpha}+))?$",
      DeleteOperationTestGenerator.class),

  RESTORE("^restore(?:By(\\p{Upper}\\p{Alpha}+))?$",
      RestoreOperationTestGenerator.class),

  PURGE("^purge(?:By(\\p{Upper}\\p{Alpha}+))?$",
      PurgeOperationTestGenerator.class),

  PURGE_ALL("^purgeAll$",
      PurgeAllOperationTestGenerator.class),

  ERASE("^erase(?:By(\\p{Upper}\\p{Alpha}+))?$",
      EraseOperationTestGenerator.class),

  CLEAR("^clear$",
      ClearOperationTestGenerator.class);

  private final String pattern;

  @SuppressWarnings("rawtypes")
  private final Class<? extends DaoOperationTestGenerator> factoryClass;

  public static DaoOperation of(final Method method) {
    final String name = method.getName();
    for (final DaoOperation value : values()) {
      if (name.matches(value.pattern)) {
        return value;
      }
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  DaoOperation(final String pattern,
      final Class<? extends DaoOperationTestGenerator> factoryClass) {
    this.pattern = pattern;
    this.factoryClass = factoryClass;
  }

  String pattern() {
    return pattern;
  }

  @SuppressWarnings("unchecked")
  <T> DaoOperationTestGenerator<T> getGenerator(
      final DaoTestGeneratorRegistry factory,
      final Class<T> modelType,
      final DaoMethodInfo methodInfo) {
    return newInstance(factoryClass, DEFAULT, factory, modelType, methodInfo);
  }
}
