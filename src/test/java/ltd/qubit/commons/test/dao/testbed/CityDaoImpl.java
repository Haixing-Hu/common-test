////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.City;
import ltd.qubit.commons.test.model.Info;

import javax.annotation.Nullable;

public class CityDaoImpl extends DaoImplBase<City>
    implements CityDao {

  public CityDaoImpl() {
    super(City.class);
  }

  @Override
  protected String makeNameKey(final City city) {
    return makeNameKey(city.getProvince(), city.getName());
  }

  protected String makeNameKey(@Nullable final Info province, final String name) {
    return makeNameKey(province == null ? null : province.getCode(), name);
  }

  protected String makeNameKey(@Nullable final String provinceCode, final String name) {
    if (provinceCode == null) {
      return ":" + name;
    } else {
      return provinceCode + ":" +  name;
    }
  }

  @Override
  protected String makeDatabaseNameKey(final City entity) {
    final Info province = entity.getProvince();
    if (province == null) {
      return "-" + entity.getName();
    } else {
      return province.getId() + "-" + entity.getName();
    }
  }

}
