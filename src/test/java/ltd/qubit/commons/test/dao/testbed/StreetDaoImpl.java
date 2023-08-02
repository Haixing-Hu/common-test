////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.Info;
import ltd.qubit.commons.test.model.Street;

import javax.annotation.Nullable;

public class StreetDaoImpl extends DaoImplBase<Street>
    implements StreetDao {

  public StreetDaoImpl() {
    super(Street.class);
  }

  @Override
  protected String makeNameKey(final Street street) {
    return makeNameKey(street.getDistrict(), street.getName());
  }

  protected String makeNameKey(@Nullable final Info district, final String name) {
    return makeNameKey(district == null ? null : district.getCode(), name);
  }

  protected String makeNameKey(@Nullable final String districtCode, final String name) {
    if (districtCode == null) {
      return ":" + name;
    } else {
      return districtCode + ":" +  name;
    }
  }

  @Override
  protected String makeDatabaseNameKey(final Street entity) {
    final Info district = entity.getDistrict();
    if (district == null) {
      return "-" + entity.getName();
    } else {
      return district.getId() + "-" + entity.getName();
    }
  }

}
