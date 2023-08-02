////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.Family;
import ltd.qubit.commons.test.model.SubFamily;

import org.springframework.dao.DataAccessException;

import java.time.Instant;

public class FamilyDaoImpl extends SimpleDaoImpl<Family> implements FamilyDao {

  private final SubFamilyDao subFamilyDao;

  public FamilyDaoImpl(final SubFamilyDao subFamilyDao) {
    super(Family.class);
    this.subFamilyDao = subFamilyDao;
  }

  public Instant add(final Family entity) throws DataAccessException {
    final Instant result = super.add(entity);
    for (final SubFamily subFamily : entity.getSubFamilies()) {
      subFamily.setFamily(entity.getInfo());
      subFamilyDao.add(subFamily);
    }
    return result;
  }
}
