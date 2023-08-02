////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test;

import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.test.json.JsonTester;
import ltd.qubit.commons.test.xml.JacksonXmlTester;
import ltd.qubit.commons.text.jackson.CustomizedJsonMapper;
import ltd.qubit.commons.text.jackson.CustomizedXmlMapper;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * 所有领域对象模型单元测试的基类。
 *
 * <p>这个类设置为abstract，从而使得JUnit不会对这个类本身进行测试。</p>
 *
 * @param <T>
 *     待测试的领域对象模型的类型。
 */
public abstract class ModelTestBase<T> {

  protected static final int DEFAULT_TEST_LOOPS = 10;

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected final Class<T> type;

  protected final int loops;

  protected final JsonMapper jsonMapper;

  protected final XmlMapper xmlMapper;

  protected final RandomBeanGenerator random;

  protected final JsonTester jsonTester;
  protected final JacksonXmlTester xmlTester;
  protected final CloneTester cloneTester;
  protected final SerializableTester serializableTester;
  protected final SizeAnnotationTester sizeAnnotationTester;
  protected final ReferenceAnnotationTester referenceAnnotationTester;

  protected ModelTestBase(final Class<T> type) {
    this(type, DEFAULT_TEST_LOOPS, new CustomizedJsonMapper(), new CustomizedXmlMapper());
  }

  protected ModelTestBase(final Class<T> type, final JsonMapper jsonMapper,
      final XmlMapper xmlMapper) {
    this(type, DEFAULT_TEST_LOOPS, jsonMapper, xmlMapper);
  }

  protected ModelTestBase(final Class<T> type, final int loops,
      final JsonMapper jsonMapper, final XmlMapper xmlMapper) {
    this.type = type;
    this.loops = loops;
    this.jsonMapper = jsonMapper;
    this.xmlMapper = xmlMapper;
    this.random = new RandomBeanGenerator();
    this.jsonTester = new JsonTester(random, loops, jsonMapper);
    this.xmlTester = new JacksonXmlTester(random, loops, xmlMapper);
    this.cloneTester = new CloneTester(random, loops);
    this.serializableTester = new SerializableTester(random, loops);
    this.sizeAnnotationTester = new SizeAnnotationTester(random, loops);
    this.referenceAnnotationTester = new ReferenceAnnotationTester(random, loops);
  }

  public final Class<T> getType() {
    return type;
  }

  public final int getLoops() {
    return loops;
  }

  public final ObjectMapper getJsonMapper() {
    return jsonMapper;
  }

  public final RandomBeanGenerator getRandom() {
    return random;
  }

  @Test
  public void testJsonSerializeDeserialize() throws Exception {
    jsonTester.test(type);
  }

  @Test
  public void testXmlSerializeDeserialize() throws Exception {
    xmlTester.test(type);
  }

  @Test
  public void testClone() throws Exception {
    cloneTester.test(type);
  }

  @Test
  public void testSerializable() throws Exception {
    serializableTester.test(type);
  }

  @Test
  public void testSizeAnnotation() throws Exception {
    sizeAnnotationTester.test(type);
  }

  @Test
  public void testReferenceAnnotation() throws Exception {
    referenceAnnotationTester.test(type);
  }
}
