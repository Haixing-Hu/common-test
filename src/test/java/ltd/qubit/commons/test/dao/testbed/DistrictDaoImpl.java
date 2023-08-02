////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.District;
import ltd.qubit.commons.test.model.Info;

import javax.annotation.Nullable;

public class DistrictDaoImpl extends DaoImplBase<District>
    implements DistrictDao {

  public DistrictDaoImpl() {
    super(District.class);
  }

  @Override
  protected String makeNameKey(final District district) {
    return makeNameKey(district.getCity(), district.getName());
  }

  protected String makeNameKey(@Nullable final Info city, final String name) {
    return makeNameKey(city == null ? null : city.getCode(), name);
  }

  protected String makeNameKey(@Nullable final String cityCode, final String name) {
    if (cityCode == null) {
      return ":" + name;
    } else {
      return cityCode + ":" +  name;
    }
  }

  @Override
  protected String makeDatabaseNameKey(final District entity) {
    final Info city = entity.getCity();
    if (city == null) {
      return "-" + entity.getName();
    } else {
      return city.getId() + "-" + entity.getName();
    }
  }

}
