////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import ltd.qubit.commons.test.TestGenerator;
import ltd.qubit.commons.test.dao.testbed.CategoryDaoImpl;
import ltd.qubit.commons.test.dao.testbed.CityDaoImpl;
import ltd.qubit.commons.test.dao.testbed.CountryDaoImpl;
import ltd.qubit.commons.test.dao.testbed.DistrictDaoImpl;
import ltd.qubit.commons.test.dao.testbed.ProvinceDaoImpl;
import ltd.qubit.commons.test.dao.testbed.StreetDaoImpl;
import ltd.qubit.commons.test.model.Category;
import ltd.qubit.commons.test.model.City;
import ltd.qubit.commons.test.model.Country;
import ltd.qubit.commons.test.model.District;
import ltd.qubit.commons.test.model.Province;
import ltd.qubit.commons.test.model.Street;

import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DaoTestGeneratorRegistryTest {

  private final DaoTestGeneratorRegistry registry;

  public DaoTestGeneratorRegistryTest() {
    registry = new DaoTestGeneratorRegistry()
                  .register(Country.class, new CountryDaoImpl())
                  .register(Province.class, new ProvinceDaoImpl())
                  .register(City.class, new CityDaoImpl())
                  .register(District.class, new DistrictDaoImpl())
                  .register(Street.class, new StreetDaoImpl())
                  .register(Category.class, new CategoryDaoImpl());
  }

  @TestFactory
  public List<DynamicNode> testGenerate_Country() throws Exception {
    return registry.generate(Country.class);
  }

  @TestFactory
  public List<DynamicNode> testGenerate_Province() throws Exception {
    return registry.generate(Province.class);
  }

  @TestFactory
  public List<DynamicNode> testGenerate_City() throws Exception {
    return registry.generate(City.class);
  }

  @TestFactory
  public List<DynamicNode> testGenerate_District() throws Exception {
    return registry.generate(District.class);
  }

  @TestFactory
  public List<DynamicNode> testGenerate_Street() throws Exception {
    return registry.generate(Street.class);
  }

  @TestFactory
  public List<DynamicNode> testGetGenerator_Country() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class);
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_add() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "add");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_update() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "update");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_updateByCode() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "updateByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_updateByName() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "updateByName");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_updateName() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "updateName");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_updateNameByCode() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "updateNameByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_addOrUpdateByCode()
      throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class,
        "addOrUpdateByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_purgeAll() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "purgeAll");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_clear() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "clear");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_ProvinceDao_existName()
      throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class, "existName");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_ProvinceDao_getByName()
      throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class, "getByName");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_ProvinceDao_add()
      throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class, "add");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_ProvinceDao_update() throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class, "update");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_ProvinceDao_addOrUpdateByCode()
      throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class,
        "addOrUpdateByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_ProvinceDao_updateByCode()
      throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class,
        "updateByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_ProvinceDao_updateByName()
      throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class,
        "updateByName");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGenerator_Province() throws Exception {
    final TestGenerator generator = registry.getGenerator(Province.class);
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGenerate_Category() throws Exception {
    return registry.generate(Category.class);
  }


  @TestFactory
  public List<DynamicNode> testGenerate_Category_count() throws Exception {
    final TestGenerator generator = registry.getGenerator(Category.class, "count");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_deleteByCode() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "deleteByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_deleteByName() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "deleteByName");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_restoreByName() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "restoreByName");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_purgeByCode() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "purgeByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_erase() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "erase");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_eraseByCode() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "eraseByCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_existNonDeleted() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "existNonDeleted");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }

  @TestFactory
  public List<DynamicNode> testGetGeneratorWithMethod_CountryDao_existNonDeletedCode() throws Exception {
    final TestGenerator generator = registry.getGenerator(Country.class, "existNonDeletedCode");
    assertNotNull(generator);
    assertSame(registry.getRandom(), generator.getRandom());
    return generator.generate();
  }
}
