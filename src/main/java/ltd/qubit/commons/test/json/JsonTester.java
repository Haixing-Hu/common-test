////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.json;

import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.test.Tester;

import com.fasterxml.jackson.databind.json.JsonMapper;

import static ltd.qubit.commons.test.json.JacksonJsonTestUtils.testJsonDeserialization;
import static ltd.qubit.commons.test.json.JacksonJsonTestUtils.testJsonSerialization;

/**
 * A tester object used to test the JSON serialization/deserialization of
 * classes.
 *
 * @author Haixing Hu
 */
public class JsonTester extends Tester {

  private JsonMapper mapper;

  public JsonTester(final RandomBeanGenerator random) {
    this(random, DEFAULT_LOOPS, new JsonMapper());
  }

  public JsonTester(final RandomBeanGenerator random, final int loops) {
    this(random, loops, new JsonMapper());
  }

  public JsonTester(final RandomBeanGenerator random, final int loops,
      final JsonMapper mapper) {
    super(random, loops);
    this.mapper = mapper;
  }

  public final JsonMapper getMapper() {
    return mapper;
  }

  public void setMapper(final JsonMapper mapper) {
    this.mapper = mapper;
  }

  protected <T> void doTest(final Class<T> type) throws Exception {
    for (int i = 0; i < loops; ++i) {
      final T obj = random.nextObject(type);
      testJsonSerialization(mapper, obj);
      testJsonDeserialization(mapper, obj);
    }
  }
}
