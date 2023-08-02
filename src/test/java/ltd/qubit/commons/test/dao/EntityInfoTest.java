////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.util.Arrays;
import java.util.List;

import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.test.model.Address;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntityInfoTest {

  @Test
  public void testTopologySortReferencedProperties() {
    final BeanInfo beanInfo = BeanInfo.of(Address.class);
    final EntityInfo entityInfo = new EntityInfo(Address.class, new Address());
    final List<ReferencedProperty> list = entityInfo.getReferencedProperties();

    final Property p_country = beanInfo.getProperty("country");
    final Property p_province = beanInfo.getProperty("province");
    final Property p_city = beanInfo.getProperty("city");
    final Property p_district = beanInfo.getProperty("district");
    assertNotNull(p_country);
    assertNotNull(p_province);
    assertNotNull(p_city);
    assertNotNull(p_district);

    final ReferencedProperty r_country = new ReferencedProperty(p_country, "province/country", "country");
    final ReferencedProperty r_province = new ReferencedProperty(p_province, "city/province", "province");
    final ReferencedProperty r_city = new ReferencedProperty(p_city, "district/city", "city");
    final ReferencedProperty r_district = new ReferencedProperty(p_district, "street/district", "district");
    list.add(r_country);
    list.add(r_province);
    list.add(r_city);
    list.add(r_district);

    entityInfo.topologySortReferencedProperties();
    assertEquals(Arrays.asList(
        r_district,
        r_city,
        r_province,
        r_country
    ), list);
  }
}
