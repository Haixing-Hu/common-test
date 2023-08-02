////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.xml;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ltd.qubit.commons.annotation.Scale;
import ltd.qubit.commons.io.IoUtils;
import ltd.qubit.commons.reflect.ConstructorUtils;
import ltd.qubit.commons.text.xml.jaxb.JaxbUtils;
import ltd.qubit.commons.util.codec.BigDecimalCodec;
import ltd.qubit.commons.util.codec.IsoDateCodec;
import ltd.qubit.commons.util.codec.IsoInstantCodec;
import ltd.qubit.commons.util.codec.IsoLocalDateCodec;
import ltd.qubit.commons.util.codec.IsoLocalDateTimeCodec;
import ltd.qubit.commons.util.codec.IsoLocalTimeCodec;
import ltd.qubit.commons.util.codec.PeriodCodec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlunit.assertj.XmlAssert;

import static ltd.qubit.commons.reflect.AccessibleUtils.withAccessibleObject;
import static ltd.qubit.commons.reflect.FieldUtils.getAllFields;
import static ltd.qubit.commons.reflect.Option.ALL_ACCESS;
import static ltd.qubit.commons.reflect.Option.BEAN_FIELD;
import static ltd.qubit.commons.reflect.Option.NON_STATIC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The utility class for testing the XML serialization of a object.
 *
 * @author Haixing Hu
 */
public abstract class JaxbTestUtils {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(JaxbTestUtils.class);

  public static <T> void assertXmlMarshalEquals(final Class<T> cls,
      final T object, final String expectedXml) throws Exception {
    final JAXBContext context = JAXBContext.newInstance(cls);
    final Marshaller mr = context.createMarshaller();
    mr.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    mr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    final StringWriter writer = new StringWriter();
    mr.marshal(object, writer);
    final String actualXml = writer.toString();
    LOGGER.debug("Object is:\n{}", object);
    LOGGER.debug("Expected XML is:\n{}", expectedXml);
    LOGGER.debug("Actual XML is:\n{}\n", actualXml);
    XmlAssert.assertThat(actualXml).and(expectedXml)
             .ignoreComments()
             .ignoreChildNodesOrder()
             .ignoreWhitespace()
             .ignoreElementContentWhitespace()
             .areIdentical();
  }

  public static <T> void assertXmlMarshalListEquals(final Class<T> cls,
      final List<T> list, final String rootName, final String expectedXml)
      throws Exception {
    final JAXBContext context = JAXBContext.newInstance(cls);
    final Marshaller mr = context.createMarshaller();
    mr.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
    mr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    final StringWriter writer = new StringWriter();
    marshalList(list, cls, rootName, writer);
    final String actualXml = writer.toString();
    LOGGER.debug("List is:\n{}", list);
    LOGGER.debug("Expected XML is:\n{}", expectedXml);
    LOGGER.debug("Actual XML is:\n{}\n", actualXml);
    XmlAssert.assertThat(actualXml).and(expectedXml)
             .ignoreComments()
             .ignoreChildNodesOrder()
             .ignoreWhitespace()
             .ignoreElementContentWhitespace()
             .areIdentical();
  }

  public static <T> void assertXmlUnmarshalEquals(final Class<T> cls,
      final String xml, final T expectedObject) throws Exception {
    final JAXBContext context = JAXBContext.newInstance(cls);
    final Unmarshaller umr = context.createUnmarshaller();
    final StringReader reader = new StringReader(xml);
    @SuppressWarnings("unchecked")
    final T actualObject = (T) umr.unmarshal(reader);
    LOGGER.debug("XML is:\n{}", xml);
    LOGGER.debug("Expected object is:\n{}", expectedObject);
    LOGGER.debug("Actual object is:\n{}\n", actualObject);
    assertEquals(expectedObject, actualObject);
  }

  public static <T> void assertXmlUnmarshalListEquals(final Class<T> cls,
      final String xml, final List<T> expectedList) throws Exception {
    final StringReader reader = new StringReader(xml);
    final List<T> actualList = unmarshalList(reader, cls);
    LOGGER.debug("XML is:\n{}", xml);
    LOGGER.debug("Expected object is:\n{}", expectedList);
    LOGGER.debug("Actual object is:\n{}\n", actualList);
    assertEquals(expectedList, actualList);
  }

  /**
   * Stores a list of objects to an XML document.
   *
   * <p><b>NOTE: </b> this function will <b>not</b> close the writer. The caller
   * must close it by himself.
   *
   * @param <T>
   *     the type of the objects to be stored.
   * @param list
   *     the list of objects to be stored.
   * @param cls
   *     the class of the objects to be stored.
   * @param rootName
   *     the name of the root element of the stored XML document.
   * @param writer
   *     an writer where to write the marshaled XML.
   * @throws JAXBException
   *     if any error occurs.
   */
  private static <T> void marshalList(final List<T> list, final Class<T> cls,
      final String rootName,
      final Writer writer) throws JAXBException {
    final QName qname = new QName(rootName);
    final JaxbListWrapper<T> wrapper = new JaxbListWrapper<>(list);
    @SuppressWarnings("rawtypes")
    final JAXBElement<JaxbListWrapper> element = new JAXBElement<>(qname,
        JaxbListWrapper.class, wrapper);
    final JAXBContext context = JAXBContext.newInstance(JaxbListWrapper.class, cls);
    final Marshaller mr = context.createMarshaller();
    mr.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    mr.marshal(element, writer);
  }

  /**
   * Unmarshals a list of objects from an XML file.
   *
   * @param <T>
   *     the type of the objects to be loaded.
   * @param reader
   *     the reader where to read the XML snippets.
   * @param cls
   *     the class of the objects to be loaded.
   * @return the list of objects deserialized from the XML file.
   * @throws JAXBException
   *     if any error occurs.
   */
  private static <T> List<T> unmarshalList(final Reader reader,
      final Class<T> cls) throws JAXBException {
    final JAXBContext context = JAXBContext.newInstance(JaxbListWrapper.class, cls);
    final Unmarshaller umr = context.createUnmarshaller();
    final StreamSource source = new StreamSource(reader);
    @SuppressWarnings("unchecked")
    final JaxbListWrapper<T> wrapper = umr.unmarshal(source, JaxbListWrapper.class).getValue();
    return wrapper.getList();
  }

  static class JaxbListWrapper<T> {

    private final List<T> list;

    public JaxbListWrapper() {
      list = new ArrayList<>();
    }

    public JaxbListWrapper(final List<T> list) {
      this.list = list;
    }

    @XmlAnyElement(lax = true)
    public List<T> getList() {
      return list;
    }
  }

  public static <T> void testXmlDeserialization(final T obj)
      throws Exception {
    LOGGER.debug("Testing XML deserialization for the object:\n{}", obj);
    @SuppressWarnings("unchecked")
    final Class<T> type = (Class<T>) obj.getClass();
    final String xml = JaxbUtils.marshal(obj, type);
    LOGGER.debug("The object is serialized to:\n{}", xml);
    final Object result = JaxbUtils.unmarshal(new StringReader(xml), type);
    LOGGER.debug("The XML is deserialized to:\n{}", result);
    assertEquals(obj, result);
    LOGGER.debug("Test finished successfully.");
  }

  private static void assertXmlNodeEqualsField(final String xml,
      @Nullable final String rootPath, final Object obj, final Field field)
      throws Exception {
    final String xmlFieldName = toXmlName(field);
    final String path = (rootPath == null ? xmlFieldName
                                          : rootPath + "/" + xmlFieldName);
    if (field.isAnnotationPresent(XmlTransient.class)
        || Modifier.isTransient(field.getModifiers())) {
      LOGGER.debug("Ignore the XML node '{}' ...", path);
    } else {
      LOGGER.debug("Testing the XML node '{}' ...", path);
      final Object fieldValue = withAccessibleObject(field, f -> f.get(obj), true);
      final Class<?> fieldType = field.getType();
      if (fieldType.isPrimitive()) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else {
        assertXmlNodeEqualsObject(xml, path, fieldValue, field);
      }
    }
  }

  private static void assertXmlNodeEqualsObject(final String xml,
      final String path, @Nullable final Object fieldValue, @Nullable final Field field)
      throws Exception {
    if (fieldValue == null) {
      XmlUnitUtils.assertXPathAbsent(xml, path);
    } else if ((field != null) && field.isAnnotationPresent(XmlJavaTypeAdapter.class)) {
      assertXmlNodeEqualsObjectWithAdapter(xml, path, fieldValue,
          field.getAnnotation(XmlJavaTypeAdapter.class));
    } else {
      final Class<?> type = fieldValue.getClass();
      if (type == Boolean.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Character.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Byte.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Short.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Integer.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Long.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Float.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Double.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == String.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else if (type == Date.class) {
        final IsoDateCodec codec = new IsoDateCodec();
        XmlUnitUtils.assertXPathEquals(xml, path, codec.encode((Date) fieldValue));
      } else if (type == LocalDate.class) {
        final IsoLocalDateCodec codec = new IsoLocalDateCodec();
        XmlUnitUtils.assertXPathEquals(xml, path, codec.encode((LocalDate) fieldValue));
      } else if (type == LocalTime.class) {
        final IsoLocalTimeCodec codec = new IsoLocalTimeCodec();
        XmlUnitUtils.assertXPathEquals(xml, path, codec.encode((LocalTime) fieldValue));
      } else if (type == LocalDateTime.class) {
        final IsoLocalDateTimeCodec codec = new IsoLocalDateTimeCodec();
        XmlUnitUtils.assertXPathEquals(xml, path, codec.encode((LocalDateTime) fieldValue));
      } else if (type == Instant.class) {
        final IsoInstantCodec codec = new IsoInstantCodec();
        XmlUnitUtils.assertXPathEquals(xml, path, codec.encode((Instant) fieldValue));
      } else if (type == Period.class) {
        final PeriodCodec codec = new PeriodCodec();
        XmlUnitUtils.assertXPathEquals(xml, path, codec.encode((Period) fieldValue));
      } else if (type == BigInteger.class) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue.toString());
      } else if (type == BigDecimal.class) {
        int scale = BigDecimalCodec.DEFAULT_SCALE;
        if (field != null && field.isAnnotationPresent(Scale.class)) {
          scale = field.getAnnotation(Scale.class).value();
        }
        final BigDecimalCodec codec = new BigDecimalCodec(scale);
        XmlUnitUtils.assertXPathEquals(xml, path, codec.encode((BigDecimal) fieldValue));
      } else if (Enum.class.isAssignableFrom(type)) {
        XmlUnitUtils.assertXPathEquals(xml, path, (Enum<?>) fieldValue);
      } else if (type.isArray()) {
        assertXmlNodeEqualsArray(xml, path, fieldValue, field);
      } else if (Collection.class.isAssignableFrom(type)) {
        assertXmlNodeEqualsCollection(xml, path, (Collection<?>) fieldValue, field);
      } else {
        final int options = NON_STATIC | ALL_ACCESS;
        final List<Field> subfields = getAllFields(fieldValue.getClass(), options);
        for (final Field subfield : subfields) {
          assertXmlNodeEqualsField(xml, path, fieldValue, subfield);
        }
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void assertXmlNodeEqualsObjectWithAdapter(final String xml,
      final String path, @Nullable final Object obj, final XmlJavaTypeAdapter annotation)
      throws Exception {
    if (obj == null) {
      XmlUnitUtils.assertXPathAbsent(xml, path);
    } else {
      final Class<? extends XmlAdapter> adapterClass = annotation.value();
      final XmlAdapter adapter = ConstructorUtils.newInstance(adapterClass);
      final String str = (String) adapter.marshal(obj);
      XmlUnitUtils.assertXPathEquals(xml, path, str);
    }
  }

  private static void assertXmlNodeEqualsCollection(final String xml,
      final String path,
      @Nullable final Collection<?> collection, @Nullable final Field field)
      throws Exception {
    if (collection == null) {
      XmlUnitUtils.assertXPathAbsent(xml, path);
    } else {
      String elementPath = path;
      if (field != null) {
        if (!field.isAnnotationPresent(XmlElement.class)) {
          fail("The XmlElement annotation must be presented at the "
              + "Collection field.");
        }
        final String elementName = field.getAnnotation(XmlElement.class).name();
        if (field.isAnnotationPresent(XmlElementWrapper.class)) {
          elementPath = path + "/" + elementName;
        } else {
          elementPath = getXmlPathParent(path) + "/" + elementName;
        }
      }
      int i = 1;
      for (final Object obj : collection) {
        assertXmlNodeEqualsObject(xml, elementPath + "[" + i + "]", obj, null);
        ++i;
      }
    }
  }

  private static String getXmlPathParent(final String path) {
    final int i = path.lastIndexOf('/');
    if (i >= 0) {
      return path.substring(0, i);
    } else {
      return "";
    }
  }

  private static void assertXmlNodeEqualsArray(final String xml, final String path,
      @Nullable final Object array, @Nullable final Field field) throws Exception {
    if (array == null) {
      XmlUnitUtils.assertXPathAbsent(xml, path);
    } else {
      assert array.getClass().isArray();
      final Class<?> elementType = array.getClass().getComponentType();
      String elementPath = path;
      if (field != null && elementType != byte.class) {
        // byte[] should be serialized to BASE64 encoded string
        if (!field.isAnnotationPresent(XmlElementWrapper.class)) {
          fail("The XmlElementWrapper annotation must be presented at "
              + "the array field.");
        }
        if (!field.isAnnotationPresent(XmlElement.class)) {
          fail("The XmlElement annotation must be presented at "
              + "the array field.");
        }
        final String wrapperName = field.getAnnotation(XmlElementWrapper.class).name();
        final String elementName = field.getAnnotation(XmlElement.class).name();
        elementPath = path + "/" + elementName;
      }
      final int n = Array.getLength(array);
      if (elementType == boolean.class) {
        for (int i = 0; i < n; ++i) {
          XmlUnitUtils.assertXPathEquals(xml, elementPath + "[" + (i + 1) + "]",
              Array.getBoolean(array, i));
        }
      } else if (elementType == char.class) {
        // NOTE: a char array was serialized as a int array in JAXB
        for (int i = 0; i < n; ++i) {
          XmlUnitUtils.assertXPathEquals(xml, elementPath + "[" + (i + 1) + "]",
              (int) Array.getChar(array, i));
        }
      } else if (elementType == byte.class) {
        // NOTE: a byte array was serialized as a BASE64 encoded string in JAXB
        final Base64.Encoder encoder = Base64.getEncoder();
        final String expected = encoder.encodeToString((byte[]) array);
        XmlUnitUtils.assertXPathEquals(xml, path, expected);
      } else if (elementType == short.class) {
        for (int i = 0; i < n; ++i) {
          XmlUnitUtils.assertXPathEquals(xml, elementPath + "[" + (i + 1) + "]",
              Array.getShort(array, i));
        }
      } else if (elementType == int.class) {
        for (int i = 0; i < n; ++i) {
          XmlUnitUtils.assertXPathEquals(xml, elementPath + "[" + (i + 1) + "]",
              Array.getInt(array, i));
        }
      } else if (elementType == long.class) {
        for (int i = 0; i < n; ++i) {
          XmlUnitUtils.assertXPathEquals(xml, elementPath + "[" + (i + 1) + "]",
              Array.getLong(array, i));
        }
      } else if (elementType == float.class) {
        for (int i = 0; i < n; ++i) {
          XmlUnitUtils.assertXPathEquals(xml, elementPath + "[" + (i + 1) + "]",
              Array.getFloat(array, i));
        }
      } else if (elementType == double.class) {
        for (int i = 0; i < n; ++i) {
          XmlUnitUtils.assertXPathEquals(xml, elementPath + "[" + (i + 1) + "]",
              Array.getDouble(array, i));
        }
      } else {
        for (int i = 0; i < n; ++i) {
          assertXmlNodeEqualsObject(xml, elementPath + "[" + (i + 1) + "]",
              Array.get(array, i), null);
        }
      }
    }
  }

  public static String toXmlName(final Field field) {
    final String fieldName = field.getName();
    final String result = toXmlName(fieldName);
    if (field.isAnnotationPresent(XmlAttribute.class)) {
      return "@" + result;
    } else {
      return result;
    }
  }

  public static String toXmlName(final String fieldName) {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0; i < fieldName.length(); ++i) {
      final char ch = fieldName.charAt(i);
      if (ch >= 'A' && ch <= 'Z') {
        if (i > 0) {
          builder.append('-');
        }
        builder.append(Character.toLowerCase(ch));
      } else {
        builder.append(ch);
      }
    }
    return builder.toString();
  }

  public static String getXmlRootElement(final Class<?> type) {
    if (!type.isAnnotationPresent(XmlRootElement.class)) {
      fail("No XmlRootElement annotation presented in the class.");
    }
    final XmlRootElement rootElement = type.getAnnotation(XmlRootElement.class);
    return rootElement.name();
  }

  public static <T> void testXmlSerialization(final T obj)
      throws Exception {
    LOGGER.debug("Testing XML serialization for the object:\n{}", obj);
    @SuppressWarnings("unchecked")
    final Class<T> type = (Class<T>) obj.getClass();
    final String xml = JaxbUtils.marshal(obj, type);
    LOGGER.debug("The object is serialized to:\n{}", xml);
    final String rootElement = getXmlRootElement(type);
    if (obj instanceof Enum<?>) {
      // 对枚举类型特殊处理
      final String value = ((Enum<?>) obj).name();
      XmlUnitUtils.assertXPathEquals(xml, rootElement, value);
    } else {
      final List<Field> fields = getAllFields(obj.getClass(), BEAN_FIELD);
      for (final Field field : fields) {
        assertXmlNodeEqualsField(xml, rootElement, obj, field);
      }
    }
    LOGGER.debug("Test finished successfully.");
  }

  public static <T> void testXmlSerialization(final String xml, final Class<T> cls)
      throws Exception {
    LOGGER.debug("Expected XML is:\n{}", xml);
    final T obj = JaxbUtils.unmarshal(new StringReader(xml), cls);
    final String marshaledXml = JaxbUtils.marshal(obj, cls);
    LOGGER.debug("Actual XML is:\n{}", marshaledXml);
    XmlUnitUtils.assertXmlEqual(obj, xml, marshaledXml);
    final T unmarshaledObj = JaxbUtils.unmarshal(new StringReader(marshaledXml), cls);
    LOGGER.debug("Expected object is: {}", obj);
    LOGGER.debug("Actual object is:   {}", unmarshaledObj);
    assertEquals(obj, unmarshaledObj);
  }

  public static <T> void testXmlSerialization(final URL url, final Class<T> cls) throws Exception {
    final String xml = IoUtils.toString(url, StandardCharsets.UTF_8);
    testXmlSerialization(xml, cls);
  }
}
