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
import java.util.Arrays;

import ltd.qubit.commons.lang.ArrayUtils;
import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.test.dao.testbed.CountryDao;
import ltd.qubit.commons.test.dao.testbed.CountryDaoImpl;
import ltd.qubit.commons.test.dao.testbed.Owner;
import ltd.qubit.commons.test.model.City;
import ltd.qubit.commons.test.model.Code;
import ltd.qubit.commons.test.model.CodeMap;
import ltd.qubit.commons.test.model.Country;
import ltd.qubit.commons.test.model.District;
import ltd.qubit.commons.test.model.Province;
import ltd.qubit.commons.test.model.Street;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ltd.qubit.commons.test.dao.DaoTestUtils.getDaoInterface;
import static ltd.qubit.commons.test.dao.DaoTestUtils.getDaoInterfaceName;
import static ltd.qubit.commons.test.dao.DaoTestUtils.getDaoMethods;
import static ltd.qubit.commons.test.dao.DaoTestUtils.toStringRepresentation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DaoTestUtilsTest {

  private final Logger logger = LoggerFactory.getLogger(DaoTestUtilsTest.class);

  @Test
  public void testGetDaoInterfaceName() {
    assertEquals("CountryDao", getDaoInterfaceName(Country.class));
    assertEquals("ProvinceDao", getDaoInterfaceName(Province.class));
    assertEquals("CityDao", getDaoInterfaceName(City.class));
    assertEquals("DistrictDao", getDaoInterfaceName(District.class));
    assertEquals("StreetDao", getDaoInterfaceName(Street.class));
    assertEquals("CodeDao", getDaoInterfaceName(Code.class));
    assertEquals("CodeMapDao", getDaoInterfaceName(CodeMap.class));
  }

  @Test
  public void testGetDaoInterface() {
    final CountryDaoImpl countryDao = new CountryDaoImpl();
    assertEquals(CountryDao.class, getDaoInterface(Country.class, countryDao));
  }

  @Test
  public void testGetDaoMethods() {
    final CountryDaoImpl countryDao = new CountryDaoImpl();
    final Method[] methods = getDaoMethods(Country.class, countryDao);
    final Object[] methodNames = Arrays.stream(methods).map(Method::getName).toArray();
    logger.info("methods are: {}", ArrayUtils.toString(methodNames));
    assertArrayEquals(new Object[]{
        "add",
        "addOrUpdateByCode",
        "addOrUpdateByName",
        "clear",
        "count",
        "delete",
        "deleteByCode",
        "deleteByName",
        "erase",
        "eraseByCode",
        "exist",
        "existCode",
        "existName",
        "existNonDeleted",
        "existNonDeletedCode",
        "get",
        "getByCode",
        "getByName",
        "getInfo",
        "getInfoByCode",
        "list",
        "purge",
        "purgeAll",
        "purgeByCode",
        "purgeByName",
        "restore",
        "restoreByCode",
        "restoreByName",
        "update",
        "updateByCode",
        "updateByName",
        "updateName",
        "updateNameByCode",
    }, methodNames);
  }

  @Test
  public void testToStringRepresentation() {
    final RandomBeanGenerator random = new RandomBeanGenerator();
    final Owner owner = random.nextObject(Owner.class);
    final String expected = owner.getType() + '-' + owner.getId();
    assertEquals(expected, toStringRepresentation(Owner.class, owner));
  }
}
