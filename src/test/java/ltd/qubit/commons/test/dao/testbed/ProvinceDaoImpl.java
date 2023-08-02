////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import javax.annotation.Nullable;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.test.model.Info;
import ltd.qubit.commons.test.model.Province;

import org.springframework.dao.DataAccessException;

public class ProvinceDaoImpl extends DaoImplBase<Province>
    implements ProvinceDao {

  public ProvinceDaoImpl() {
    super(Province.class);
  }

  @Override
  protected String makeNameKey(final Province province) {
    return makeNameKey(province.getCountry(), province.getName());
  }

  protected String makeNameKey(@Nullable final Info country, final String name) {
    return makeNameKey(country == null ? null : country.getCode(), name);
  }

  protected String makeNameKey(@Nullable final String countryCode, final String name) {
    if (countryCode == null) {
      return ":" + name;
    } else {
      return countryCode + ":" +  name;
    }
  }

  @Override
  protected String makeDatabaseNameKey(final Province entity) {
    final Info country = entity.getCountry();
    if (country == null) {
      return "-" + entity.getName();
    } else {
      return country.getId() + "-" + entity.getName();
    }
  }

  @Override
  public boolean existName(final String countryCode, final String name)
      throws DataAccessException {
    logger.debug("Test the existence of a province by name: countryCode = {}, "
        + "name = {}", countryCode, name);
    return nameMap.containsKey(makeNameKey(countryCode, name));
  }

  @Override
  public Province getByName(final String countryCode, final String name)
      throws DataAccessException {
    final Province province = nameMap.get(makeNameKey(countryCode, name));
    if (province != null) {
      logger.debug("Get a province by name {}: {} - {}", countryCode, name,
          province);
      return province.clone();
    } else {
      throw new DataNotExistException(Province.class, "name", name);
    }
  }

}
