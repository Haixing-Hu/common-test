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

import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.test.model.Address;
import ltd.qubit.commons.test.model.Child;
import ltd.qubit.commons.test.model.Country;
import ltd.qubit.commons.test.model.Family;
import ltd.qubit.commons.test.model.Grandpa;
import ltd.qubit.commons.test.model.Parent;
import ltd.qubit.commons.test.model.Province;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test of {@link EntityInfoStack}.
 */
public class EntityInfoStackTest {

  @Test
  public void testAddReferencedProperty() {
    final EntityInfoStack stack = new EntityInfoStack();
    stack.push(new EntityInfo(Address.class, new Address()));
    final BeanInfo addressBeanInfo = BeanInfo.of(Address.class);
    final Property countryProp = addressBeanInfo.getProperty("country");
    assertNotNull(countryProp);
    stack.addReferencedProperty(countryProp);
    final Property provinceProp = addressBeanInfo.getProperty("province");
    assertNotNull(provinceProp);
    stack.addReferencedProperty(provinceProp);
    final Property cityProp = addressBeanInfo.getProperty("city");
    assertNotNull(cityProp);
    stack.addReferencedProperty(cityProp);
    final Property districtProp = addressBeanInfo.getProperty("district");
    assertNotNull(districtProp);
    stack.addReferencedProperty(districtProp);
    assertEquals(1, stack.size());
    final EntityInfo entityInfo = stack.peek();
    assertEquals(Arrays.asList(
            new ReferencedProperty(countryProp, "province/country", "country"),
            new ReferencedProperty(provinceProp, "city/province", "province"),
            new ReferencedProperty(cityProp, "district/city", "city"),
            new ReferencedProperty(districtProp, "street/district", "district")
        ),
        entityInfo.getReferencedProperties());
  }

  @Test
  public void testAddReferencedPropertyWithParentPath() {
    final BeanInfo beanInfoOfA = BeanInfo.of(Grandpa.class);
    final BeanInfo beanInfoOfB = BeanInfo.of(Parent.class);
    final BeanInfo beanInfoOfC = BeanInfo.of(Child.class);

    final Property childPropInA = beanInfoOfA.getProperty(Grandpa::getChild);
    assertNotNull(childPropInA);
    final Property countryPropInA = beanInfoOfA.getProperty(Grandpa::getCountry);
    assertNotNull(countryPropInA);
    final Property provincePropInA = beanInfoOfA.getProperty(Grandpa::getProvince);
    assertNotNull(provincePropInA);

    final Property parentIdPropInB = beanInfoOfB.getProperty(Parent::getParentId);
    assertNotNull(parentIdPropInB);
    final Property childrenPropInB = beanInfoOfB.getProperty(Parent::getChildren);
    assertNotNull(childrenPropInB);
    final Property countryPropInB = beanInfoOfB.getProperty(Parent::getCountry);
    assertNotNull(countryPropInB);
    final Property provincePropInB = beanInfoOfB.getProperty(Parent::getProvince);
    assertNotNull(provincePropInB);
    final Property parentCountryPropInB = beanInfoOfB.getProperty(Parent::getParentCountry);
    assertNotNull(parentCountryPropInB);
    final Property parentProvincePropInB = beanInfoOfB.getProperty(Parent::getParentProvince);
    assertNotNull(parentProvincePropInB);

    final Property parentIdPropInC = beanInfoOfC.getProperty(Child::getParentId);
    assertNotNull(parentIdPropInC);
    final Property grandpaIdPropInC = beanInfoOfC.getProperty(Child::getGrandpaId);
    assertNotNull(grandpaIdPropInC);
    final Property countryPropInC = beanInfoOfC.getProperty(Child::getCountry);
    assertNotNull(countryPropInC);
    final Property provincePropInC = beanInfoOfC.getProperty(Child::getProvince);
    assertNotNull(provincePropInC);
    final Property cityPropInC = beanInfoOfC.getProperty(Child::getCity);
    assertNotNull(cityPropInC);
    final Property parentCountryPropInC = beanInfoOfC.getProperty(Child::getParentCountry);
    assertNotNull(parentCountryPropInC);
    final Property parentProvincePropInC = beanInfoOfC.getProperty(Child::getParentProvince);
    assertNotNull(parentProvincePropInC);
    final Property grandpaCountryPropInC = beanInfoOfC.getProperty(Child::getGrandpaCountry);
    assertNotNull(grandpaCountryPropInC);
    final Property grandpaProvincePropInC = beanInfoOfC.getProperty(Child::getGrandpaProvince);
    assertNotNull(grandpaProvincePropInC);
    final Property familyPropInC = beanInfoOfC.getProperty(Child::getFamily);
    assertNotNull(familyPropInC);
    final Property subFamilyPropInC = beanInfoOfC.getProperty(Child::getSubFamily);
    assertNotNull(subFamilyPropInC);

    final EntityInfoStack stack = new EntityInfoStack();
    stack.push(new EntityInfo(Grandpa.class, new Grandpa()));
    stack.addReferencedProperty(countryPropInA);
    stack.push(new EntityInfo(Parent.class, new Parent(), childPropInA));
    stack.addReferencedProperty(parentIdPropInB);
    stack.addReferencedProperty(countryPropInB);
    stack.addReferencedProperty(parentCountryPropInB);
    stack.addReferencedProperty(parentProvincePropInB);
    stack.push(new EntityInfo(Child.class, new Child(), childrenPropInB));
    stack.addReferencedProperty(parentIdPropInC);
    stack.addReferencedProperty(grandpaIdPropInC);
    stack.addReferencedProperty(countryPropInC);
    stack.addReferencedProperty(provincePropInC);
    stack.addReferencedProperty(parentCountryPropInC);
    stack.addReferencedProperty(parentProvincePropInC);
    stack.addReferencedProperty(grandpaCountryPropInC);
    stack.addReferencedProperty(grandpaProvincePropInC);
    stack.addReferencedProperty(familyPropInC);

    assertEquals(3, stack.size());
    final EntityInfo entityInfoOfC = stack.get(0);
    final EntityInfo entityInfoOfB = stack.get(1);
    final EntityInfo entityInfoOfA = stack.get(2);
    assertEquals(Child.class, entityInfoOfC.getType());
    assertEquals(Parent.class, entityInfoOfB.getType());
    assertEquals(Child.class, entityInfoOfC.getType());

    assertEquals(Arrays.asList(
            new ReferencedProperty(Country.class, "info", "province/country", "country"),
            new ReferencedProperty(Province.class, "info", "city/province", "province"),
            new ReferencedProperty(Family.class, "info", "subFamily/family", "family")
        ),
        entityInfoOfC.getReferencedProperties());
    assertEquals(Arrays.asList(
            new ReferencedProperty(Country.class, "info", "province/country", "country"),
            new ReferencedProperty(Parent.class, "id", "", "children/parentId"),
            new ReferencedProperty(Country.class, "info", "country", "children/parentCountry"),
            new ReferencedProperty(Province.class, "info", "province", "children/parentProvince")
        ),
        entityInfoOfB.getReferencedProperties());
    assertEquals(Arrays.asList(
            new ReferencedProperty(Country.class, "info", "province/country", "country"),
            new ReferencedProperty(Grandpa.class, "id", "", "child/parentId"),
            new ReferencedProperty(Country.class, "info", "country", "child/parentCountry"),
            new ReferencedProperty(Province.class, "info", "province", "child/parentProvince"),
            new ReferencedProperty(Grandpa.class, "id", "", "child/children/grandpaId"),
            new ReferencedProperty(Country.class, "info", "country", "child/children/grandpaCountry"),
            new ReferencedProperty(Province.class, "info", "province", "child/children/grandpaProvince")
        ),
        entityInfoOfA.getReferencedProperties());
  }
}
