////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

public class EntityDaoNotRegisteredException extends IllegalArgumentException {

  private static final long serialVersionUID = -7035635529777420226L;

  public EntityDaoNotRegisteredException(final Class<?> entityType) {
    super("The DAO of entity " + entityType.getName() + " was not registered.");
  }
}
