////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.Grandpa;
import ltd.qubit.commons.test.model.Parent;

import java.time.Instant;

import static java.util.Objects.requireNonNull;

public class GrandpaDaoImpl extends SimpleDaoImpl<Grandpa> implements GrandpaDao {

  private final ParentDao parentDao;

  public GrandpaDaoImpl(final ParentDao parentDao) {
    super(Grandpa.class);
    this.parentDao = requireNonNull(parentDao);
  }

  public Instant add(Grandpa entity) {
    final Instant result = super.add(entity);
    // TODO: check validity
    final Parent child = entity.getChild();
    child.setParentId(entity.getId());
    child.setParentCountry(entity.getCountry());
    child.setParentProvince(entity.getProvince());
    parentDao.add(entity.getChild());
    return result;
  }
}
