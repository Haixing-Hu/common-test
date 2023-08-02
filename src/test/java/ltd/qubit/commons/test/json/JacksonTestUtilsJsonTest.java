////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.json;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.test.model.App;
import ltd.qubit.commons.test.model.BadBean;
import ltd.qubit.commons.test.model.BeanWithPhone;
import ltd.qubit.commons.test.model.Category;
import ltd.qubit.commons.test.model.Info;
import ltd.qubit.commons.test.model.Location;
import ltd.qubit.commons.test.model.ObjectWithArrayField;
import ltd.qubit.commons.test.model.ObjectWithBigDecimalField;
import ltd.qubit.commons.test.model.ObjectWithEnumField;
import ltd.qubit.commons.test.model.ObjectWithListField;
import ltd.qubit.commons.test.model.State;
import ltd.qubit.commons.test.model.StringSet;
import ltd.qubit.commons.text.jackson.CustomizedJsonMapper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.json.JsonMapper;

import static ltd.qubit.commons.test.json.JacksonJsonTestUtils.testJsonDeserialization;
import static ltd.qubit.commons.test.json.JacksonJsonTestUtils.testJsonSerialization;
import static ltd.qubit.commons.text.CaseFormat.LOWER_CAMEL;
import static ltd.qubit.commons.text.CaseFormat.LOWER_UNDERSCORE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JacksonTestUtilsJsonTest {

  private final RandomBeanGenerator generator = new RandomBeanGenerator();

  private final JsonMapper mapper = new CustomizedJsonMapper();

  @Test
  public void testGetJsonFieldName() throws Exception {
    assertEquals("id", LOWER_CAMEL.to(LOWER_UNDERSCORE, "id"));
    assertEquals("organization", LOWER_CAMEL.to(LOWER_UNDERSCORE, "organization"));
    assertEquals("security_key", LOWER_CAMEL.to(LOWER_UNDERSCORE, "securityKey"));
    assertEquals("create_time", LOWER_CAMEL.to(LOWER_UNDERSCORE, "createTime"));
    assertEquals("token_create_time", LOWER_CAMEL.to(LOWER_UNDERSCORE, "tokenCreateTime"));
  }

  @Test
  public void testTestJsonSerialization() throws Exception {
    @SuppressWarnings("unchecked")
    final Info info = generator.nextObject(Info.class);
    testJsonSerialization(mapper, info);
    testJsonDeserialization(mapper, info);

    final Category category = generator.nextObject(Category.class);
    testJsonSerialization(mapper, category);
    testJsonDeserialization(mapper, category);

    final App app = generator.nextObject(App.class);
    testJsonSerialization(mapper, app);
    testJsonDeserialization(mapper, app);

    final ObjectWithEnumField obj1 = generator
        .nextObject(ObjectWithEnumField.class);
    testJsonSerialization(mapper, obj1);
    testJsonDeserialization(mapper, obj1);

    final ObjectWithListField obj2 = generator
        .nextObject(ObjectWithListField.class);
    testJsonSerialization(mapper, obj2);
    testJsonDeserialization(mapper, obj2);

    final ObjectWithArrayField obj3 = generator
        .nextObject(ObjectWithArrayField.class);
    testJsonSerialization(mapper, obj3);
    testJsonDeserialization(mapper, obj3);

    final Location obj4 = generator.nextObject(Location.class);
    testJsonSerialization(mapper, obj4);
    testJsonDeserialization(mapper, obj4);

    final BeanWithPhone obj5 = generator.nextObject(BeanWithPhone.class);
    testJsonSerialization(mapper, obj5);
    testJsonDeserialization(mapper, obj5);
  }

  @Disabled
  @Test
  public void testTestJsonSerializationBadBean() throws Exception {
    // NOTE:
    // After refactoring, the BadBean class is no longer a bad bean. Since the code
    // will use the @JsonProperty to get the correct serialized field name.
    final BadBean bean = generator.nextObject(BadBean.class);
    try {
      testJsonSerialization(mapper, bean);
      fail("should throw");
    } catch (final AssertionError e) {
      e.printStackTrace();
      assertNotEquals("should throw", e.getMessage());
      // pass
    }
    testJsonDeserialization(mapper, bean);
  }

  @Test
  public void testTestJsonSerializationEnum() throws Exception {
    final State state = generator.nextObject(State.class);
    testJsonSerialization(mapper, state);
  }

  @Test
  public void testTestJsonSerializationString() throws Exception {
    final String str = generator.nextObject(String.class);
    testJsonSerialization(mapper, str);
  }

  @Test
  public void testTestJsonSerializationIntArray() throws Exception {
    final int[] intArray = generator.nextObject(int[].class);
    testJsonSerialization(mapper, intArray);
  }

  @Test
  public void testTestJsonSerializationIntegerList() throws Exception {
    final List<Integer> intList = generator.nextList(Integer.class);
    testJsonSerialization(mapper, intList);
  }

  // FIXME:
//  @Test
//  public void testTestJsonSerializationStringListSubclass() throws Exception {
//    final StringList stringList = generator.nextObject(StringList.class);
//    testJsonSerialization(mapper, stringList);
//  }

  @Test
  public void testTestJsonSerializationIntegerSet() throws Exception {
    final Set<Integer> intSet = generator.nextSet(Integer.class);
    testJsonSerialization(mapper, intSet);
  }

  @Test
  public void testTestJsonSerializationStringSetSubclass() throws Exception {
    final StringSet stringSet = generator.nextObject(StringSet.class);
    testJsonSerialization(mapper, stringSet);
  }

  @Test
  public void testJsonDeserializeWithBigDecimalField() throws Exception {
    final ObjectWithBigDecimalField obj = new ObjectWithBigDecimalField();
    obj.setPrice(new BigDecimal("0.5700"));
    testJsonSerialization(mapper, obj);
    testJsonDeserialization(mapper, obj);
  }

  @Test
  public void testGenerateApp() {
    for (int i = 0; i < 100; ++i) {
      generator.nextObject(App.class);
    }
  }
}
