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
import ltd.qubit.commons.test.dao.testbed.CategoryDao;
import ltd.qubit.commons.test.dao.testbed.CategoryDaoImpl;
import ltd.qubit.commons.test.dao.testbed.CityDao;
import ltd.qubit.commons.test.dao.testbed.CityDaoImpl;
import ltd.qubit.commons.test.dao.testbed.CountryDao;
import ltd.qubit.commons.test.dao.testbed.CountryDaoImpl;
import ltd.qubit.commons.test.dao.testbed.DistrictDao;
import ltd.qubit.commons.test.dao.testbed.DistrictDaoImpl;
import ltd.qubit.commons.test.dao.testbed.FamilyDao;
import ltd.qubit.commons.test.dao.testbed.FamilyDaoImpl;
import ltd.qubit.commons.test.dao.testbed.ProvinceDao;
import ltd.qubit.commons.test.dao.testbed.ProvinceDaoImpl;
import ltd.qubit.commons.test.dao.testbed.StreetDao;
import ltd.qubit.commons.test.dao.testbed.StreetDaoImpl;
import ltd.qubit.commons.test.dao.testbed.SubFamilyDao;
import ltd.qubit.commons.test.dao.testbed.SubFamilyDaoImpl;
import ltd.qubit.commons.test.model.Address;
import ltd.qubit.commons.test.model.Category;
import ltd.qubit.commons.test.model.Child;
import ltd.qubit.commons.test.model.City;
import ltd.qubit.commons.test.model.Contact;
import ltd.qubit.commons.test.model.Country;
import ltd.qubit.commons.test.model.District;
import ltd.qubit.commons.test.model.Family;
import ltd.qubit.commons.test.model.Grandpa;
import ltd.qubit.commons.test.model.Info;
import ltd.qubit.commons.test.model.Organization;
import ltd.qubit.commons.test.model.Parent;
import ltd.qubit.commons.test.model.Province;
import ltd.qubit.commons.test.model.Street;
import ltd.qubit.commons.test.model.SubFamily;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeanCreatorTest {

  private static final int LOOPS = 1000;

  private final CountryDao countryDao = new CountryDaoImpl();
  private final ProvinceDao provinceDao = new ProvinceDaoImpl();
  private final CityDao cityDao = new CityDaoImpl();
  private final DistrictDao districtDao = new DistrictDaoImpl();
  private final StreetDao streetDao = new StreetDaoImpl();
  private final CategoryDao categoryDao = new CategoryDaoImpl();
  private final SubFamilyDao subFamilyDao = new SubFamilyDaoImpl();
  private final FamilyDao familyDao = new FamilyDaoImpl(subFamilyDao);
  private final DaoTestGeneratorRegistry registry;
  private final BeanCreator creator;

  public BeanCreatorTest() {
    registry = new DaoTestGeneratorRegistry()
        .register(Country.class, countryDao)
        .register(Province.class, provinceDao)
        .register(City.class, cityDao)
        .register(District.class, districtDao)
        .register(Street.class, streetDao)
        .register(Category.class, categoryDao)
        .register(Family.class, familyDao)
        .register(SubFamily.class, subFamilyDao);
    creator = new BeanCreator(registry);
  }

  @Test
  public void testPrepareOrganization() throws Throwable {
    for (int i = 0; i < LOOPS; ++i) {
      final BeanInfo info = BeanInfo.of(Organization.class);
      final Organization org = (Organization) creator.prepare(info);
      assertNotNull(org);
      final Contact contact = org.getContact();
      if (contact == null) continue;
      final Address address = contact.getAddress();
      if (address == null) continue;
      final Info countryInfo = address.getCountry();
      final Info provinceInfo = address.getProvince();
      final Info cityInfo = address.getCity();
      final Info districtInfo = address.getDistrict();
      final Info streetInfo = address.getStreet();
      if (streetInfo == null) {
        assertNull(countryInfo);
        assertNull(provinceInfo);
        assertNull(cityInfo);
        assertNull(districtInfo);
      } else {
        assertNotNull(countryInfo);
        assertNotNull(provinceInfo);
        assertNotNull(cityInfo);
        assertNotNull(districtInfo);

        assertTrue(streetDao.exist(streetInfo.getId()));
        final Street street = streetDao.get(streetInfo.getId());
        assertNotNull(street);
        assertEquals(streetInfo, street.getInfo());

        assertTrue(districtDao.exist(districtInfo.getId()));
        final District district = districtDao.get(districtInfo.getId());
        assertNotNull(district);
        assertEquals(districtInfo, district.getInfo());

        assertTrue(cityDao.exist(cityInfo.getId()));
        final City city = cityDao.get(cityInfo.getId());
        assertNotNull(city);
        assertEquals(cityInfo, city.getInfo());

        assertTrue(provinceDao.exist(provinceInfo.getId()));
        final Province province = provinceDao.get(provinceInfo.getId());
        assertNotNull(province);
        assertEquals(provinceInfo, province.getInfo());

        assertTrue(countryDao.exist(countryInfo.getId()));
        final Country country = countryDao.get(streetInfo.getId());
        assertNotNull(country);
        assertEquals(countryInfo, country.getInfo());

        assertEquals(countryInfo, province.getCountry());
        assertEquals(provinceInfo, city.getProvince());
        assertEquals(cityInfo, district.getCity());
        assertEquals(districtInfo, street.getDistrict());
      }
    }
  }

  @Test
  public void testPrepareGrandpa() throws Throwable {
    for (int i = 0; i < LOOPS; ++i) {
      final BeanInfo info = BeanInfo.of(Grandpa.class);
      final Grandpa grandpa = (Grandpa) creator.prepare(info);
      assertNotNull(grandpa);
      assertNotNull(grandpa.getCountry());
      assertNotNull(grandpa.getProvince());
      final Province grandpaProvince = provinceDao.get(grandpa.getProvince().getId());
      assertEquals(grandpaProvince.getCountry(), grandpa.getCountry());
      assertNotNull(grandpa.getChild());
      final Parent parent = grandpa.getChild();
      assertNotNull(parent);
      assertEquals(grandpa.getId(), parent.getParentId());
      assertEquals(grandpa.getCountry(), parent.getParentCountry());
      assertEquals(grandpa.getProvince(), parent.getParentProvince());
      final Province parentProvince = provinceDao.get(parent.getProvince().getId());
      assertEquals(parentProvince.getCountry(), parent.getCountry());
      assertNotNull(parent.getChildren());
      for (final Child child : parent.getChildren()) {
        assertNotNull(child);
        assertEquals(parent.getId(), child.getParentId());
        assertEquals(parent.getCountry(), child.getParentCountry());
        assertEquals(grandpa.getId(), child.getGrandpaId());
        assertEquals(grandpa.getCountry(), child.getGrandpaCountry());
        assertEquals(grandpa.getProvince(), child.getGrandpaProvince());
        final City childCity = cityDao.get(child.getCity().getId());
        assertEquals(childCity.getProvince(), child.getProvince());
        final Province childProvince = provinceDao.get(child.getProvince().getId());
        assertEquals(childProvince.getCountry(), child.getCountry());
      }
    }
  }
}
