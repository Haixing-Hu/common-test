////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.xml;

import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.test.model.App;
import ltd.qubit.commons.test.model.BadBean;
import ltd.qubit.commons.test.model.BeanWithPhone;
import ltd.qubit.commons.test.model.Category;
import ltd.qubit.commons.test.model.Info;
import ltd.qubit.commons.test.model.Location;
import ltd.qubit.commons.test.model.ObjectWithArrayField;
import ltd.qubit.commons.test.model.ObjectWithEnumField;
import ltd.qubit.commons.test.model.ObjectWithListField;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static ltd.qubit.commons.test.xml.JaxbTestUtils.testXmlDeserialization;
import static ltd.qubit.commons.test.xml.JaxbTestUtils.testXmlSerialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JaxbTestUtilsTest {

  private final RandomBeanGenerator generator = new RandomBeanGenerator();

  @Test
  public void testGetXmlFieldName() throws Exception {
    assertEquals("id", JaxbTestUtils.toXmlName("id"));
    assertEquals("organization", JaxbTestUtils.toXmlName("organization"));
    assertEquals("security-key", JaxbTestUtils.toXmlName("securityKey"));
    assertEquals("create-time", JaxbTestUtils.toXmlName("createTime"));
    assertEquals("token-create-time",
        JaxbTestUtils.toXmlName("tokenCreateTime"));
  }

  @Test
  public void testTestXmlSerialization() throws Exception {
    @SuppressWarnings("unchecked")
    final Info info = generator.nextObject(Info.class);
    testXmlSerialization(info);
    testXmlDeserialization(info);

    final Category category = generator.nextObject(Category.class);
    testXmlSerialization(category);
    testXmlDeserialization(category);

    final App app = generator.nextObject(App.class);
    testXmlSerialization(app);
    testXmlDeserialization(app);

    final ObjectWithEnumField obj1 = generator.nextObject(ObjectWithEnumField.class);
    testXmlSerialization(obj1);
    testXmlDeserialization(obj1);

    final ObjectWithListField obj2 = generator.nextObject(ObjectWithListField.class);
    testXmlSerialization(obj2);
    testXmlDeserialization(obj2);

    final ObjectWithArrayField obj3 = generator.nextObject(ObjectWithArrayField.class);
    testXmlSerialization(obj3);
    testXmlDeserialization(obj3);

    final Location obj4 = generator.nextObject(Location.class);
    testXmlSerialization(obj4);
    testXmlDeserialization(obj4);

    final BeanWithPhone obj5 = generator.nextObject(BeanWithPhone.class);
    testXmlSerialization(obj5);
    testXmlDeserialization(obj5);
  }

  @Disabled
  @Test
  public void testTestXmlSerializationBadBean() throws Exception {
    // NOTE:
    // After refactoring, the BadBean class is no longer a bad bean. Since the code
    // will use the @XmlElement to get the correct serialized field name.
    final BadBean bean = generator.nextObject(BadBean.class);
    try {
      testXmlSerialization(bean);
      fail("should throw");
    } catch (final AssertionError e) {
      e.printStackTrace();
      assertNotEquals("should throw", e.getMessage());
      // pass
    }
    testXmlDeserialization(bean);
  }

}
