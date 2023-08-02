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
import ltd.qubit.commons.test.Tester;
import ltd.qubit.commons.text.jackson.CustomizedXmlMapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ltd.qubit.commons.test.xml.JacksonXmlTestUtils.testXmlDeserialization;
import static ltd.qubit.commons.test.xml.JacksonXmlTestUtils.testXmlSerialization;

/**
 * A tester object used to test the XML serialization/deserialization of
 * classes.
 *
 * @author Haixing Hu
 */
public class JacksonXmlTester extends Tester {

  private final XmlMapper mapper;

  public JacksonXmlTester(final RandomBeanGenerator random) {
    this(random, DEFAULT_LOOPS, new CustomizedXmlMapper());
  }

  public JacksonXmlTester(final RandomBeanGenerator random, final int loops) {
    this(random, loops, new CustomizedXmlMapper());
  }

  public JacksonXmlTester(final RandomBeanGenerator random, final int loops,
      final XmlMapper mapper) {
    super(random, loops);
    this.mapper = mapper;
  }

  protected <T> void doTest(final Class<T> type) throws Exception {
    for (int i = 0; i < loops; ++i) {
      final T obj = random.nextObject(type);
      testXmlSerialization(mapper, obj);
      testXmlDeserialization(mapper, obj);
    }
  }
}
