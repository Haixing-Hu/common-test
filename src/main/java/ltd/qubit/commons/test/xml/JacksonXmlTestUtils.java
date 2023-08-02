////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ltd.qubit.commons.io.IoUtils;
import ltd.qubit.commons.reflect.ClassUtils;
import ltd.qubit.commons.text.jackson.TypeRegistrationModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import static ltd.qubit.commons.reflect.AccessibleUtils.withAccessibleObject;
import static ltd.qubit.commons.reflect.FieldUtils.getAllFields;
import static ltd.qubit.commons.reflect.Option.BEAN_FIELD;
import static ltd.qubit.commons.text.jackson.JacksonUtils.getPropertyName;
import static ltd.qubit.commons.text.jackson.JacksonUtils.getPropertyWrappedName;
import static ltd.qubit.commons.text.jackson.JacksonUtils.getPropertyWrapperName;
import static ltd.qubit.commons.text.jackson.JacksonUtils.getRootName;
import static ltd.qubit.commons.text.jackson.JacksonUtils.serializeWithAdapter;
import static ltd.qubit.commons.text.jackson.JacksonUtils.serializeWithSerializer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The utility class for testing the XML serialization of a object.
 *
 * @author Haixing Hu
 */
public abstract class JacksonXmlTestUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(JacksonXmlTestUtils.class);

  public static <T> void assertXmlSerializeEquals(final XmlMapper mapper,
      final Class<T> cls, final T object, final String expectedXml)
      throws Exception {
    final String actualXml = mapper.writeValueAsString(object);
    XmlUnitUtils.assertXmlEqual(object, expectedXml, actualXml);
  }

  public static <T> void assertXmlDeserializeEquals(final XmlMapper mapper,
      final Class<T> cls, final String xml, final T expectedObject)
      throws Exception {
    final T actualObject = mapper.readValue(xml, cls);
    LOGGER.debug("XML is:\n{}", xml);
    LOGGER.debug("Expected object is:\n{}", expectedObject);
    LOGGER.debug("Actual object is:\n{}\n", actualObject);
    assertEquals(expectedObject, actualObject);
  }

  @SuppressWarnings("unchecked")
  public static <T> void testXmlDeserialization(final XmlMapper mapper, final T obj)
      throws Exception {
    LOGGER.debug("Testing XML deserialization for the object:\n{}", obj);
    final String xml = mapper.writerWithDefaultPrettyPrinter()
        .writeValueAsString(obj);
    LOGGER.info("The object is serialized to:\n{}", xml);
    final T result = mapper.readValue(xml, (Class<T>) obj.getClass());
    LOGGER.debug("The XML is deserialized to:\n{}", result);
    assertEquals(obj, result);
    LOGGER.debug("Test finished successfully.");
  }

  public static <T> void testXmlSerialization(final XmlMapper mapper, final T obj)
      throws Exception {
    LOGGER.debug("Testing XML serialization for the object:\n{}", obj);
    @SuppressWarnings("unchecked")
    final Class<T> type = (Class<T>) obj.getClass();
    final String xml = mapper.writeValueAsString(obj);
    LOGGER.info("The object is serialized to:\n{}", xml);
    final PropertyName root = getRootName(mapper, type);
    assertXmlNodeEqualsObject(mapper, xml, root.getSimpleName(), null, obj);
    LOGGER.debug("Test finished successfully.");
  }

  public static <T> void testXmlSerialization(final XmlMapper mapper,
      final URL url, final Class<T> cls) throws Exception {
    final String xml = IoUtils.toString(url, StandardCharsets.UTF_8);
    testXmlSerialization(mapper, xml, cls);
  }

  public static <T> void testXmlSerialization(final XmlMapper mapper,
      final String xml, final Class<T> cls) throws Exception {
    LOGGER.debug("Expected XML is:\n{}", xml);
    final T obj = mapper.readValue(xml, cls);
    final String marshaledXml = mapper.writeValueAsString(obj);
    LOGGER.debug("Actual XML is:\n{}", marshaledXml);
    XmlUnitUtils.assertXmlEqual(obj, xml, marshaledXml);
    final T unmarshaledObj = mapper.readValue(marshaledXml, cls);
    LOGGER.debug("Expected object is: {}", obj);
    LOGGER.debug("Actual object is:   {}", unmarshaledObj);
    assertEquals(obj, unmarshaledObj);
  }

  @SuppressWarnings("rawtypes")
  private static void assertXmlNodeEqualsObject(final XmlMapper mapper,
      final String xml, final String path, @Nullable final Field field,
      @Nullable final Object fieldValue) throws Exception {
    if (fieldValue == null) {
      XmlUnitUtils.assertXPathAbsent(xml, path);
    } else if (field != null && field.isAnnotationPresent(XmlJavaTypeAdapter.class)) {
      final XmlJavaTypeAdapter annotation = field.getAnnotation(XmlJavaTypeAdapter.class);
      final String expected = serializeWithAdapter(mapper, annotation, field, fieldValue);
      XmlUnitUtils.assertXPathEquals(xml, path, expected);
    } else if (field != null && field.isAnnotationPresent(JsonSerialize.class)) {
      final JsonSerialize annotation = field.getAnnotation(JsonSerialize.class);
      final String expected = serializeWithSerializer(mapper, annotation, field, fieldValue);
      XmlUnitUtils.assertXPathEquals(xml, path, expected);
    } else {
      final Class<?> type = fieldValue.getClass();
      final JsonSerializer serializer = TypeRegistrationModule.getSerializer(type);
      if (serializer != null) {
        final String expected = serializeWithSerializer(mapper, serializer, field, fieldValue);
        XmlUnitUtils.assertXPathEquals(xml, path, expected);
      } else {
        assertXmlNodeEqualsNonNullObject(mapper, xml, path, field, fieldValue);
      }
    }
  }

  private static void assertXmlNodeEqualsNonNullObject(final XmlMapper mapper,
      final String xml, final String path, @Nullable final Field field,
      final Object fieldValue) throws Exception {
    assert fieldValue != null;
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
    } else if (ClassUtils.isEnumType(type)) {
      XmlUnitUtils.assertXPathEquals(xml, path, (Enum<?>) fieldValue);
    } else if (ClassUtils.isArrayType(type)) {
      assertXmlNodeEqualsArray(mapper, xml, path, fieldValue, field);
    } else if (ClassUtils.isCollectionType(type)) {
      assertXmlNodeEqualsCollection(mapper, xml, path, (Collection<?>) fieldValue, field);
    } else {
      final List<Field> subfields = getAllFields(fieldValue.getClass(), BEAN_FIELD);
      for (final Field subfield : subfields) {
        assertXmlNodeEqualsField(mapper, xml, path, fieldValue, subfield);
      }
    }
  }

  private static void assertXmlNodeEqualsField(final XmlMapper mapper,
      final String xml, @Nullable final String rootPath, final Object obj,
      final Field field) throws Exception {
    final Class<?> fieldType = field.getType();
    final PropertyName propertyName;
    if (ClassUtils.isArrayType(fieldType)
        || ClassUtils.isCollectionType(fieldType)
        || ClassUtils.isMapType(fieldType)) {
      propertyName = getPropertyWrapperName(mapper, field);
    } else {
      propertyName = getPropertyName(mapper, field);
    }
    final String path = (rootPath == null ? propertyName.getSimpleName()
                                          : rootPath + "/" + propertyName.getSimpleName());
    if (field.isAnnotationPresent(XmlTransient.class)
        || field.isAnnotationPresent(JsonIgnore.class)
        || Modifier.isTransient(field.getModifiers())) {
      LOGGER.debug("Ignore the XML node '{}' ...", path);
    } else {
      LOGGER.debug("Testing the XML node '{}' ...", path);
      final Object fieldValue = withAccessibleObject(field, f -> f.get(obj), true);
      if (fieldType.isPrimitive()) {
        XmlUnitUtils.assertXPathEquals(xml, path, fieldValue);
      } else {
        assertXmlNodeEqualsObject(mapper, xml, path, field, fieldValue);
      }
    }
  }

  private static void assertXmlNodeEqualsCollection(final XmlMapper mapper,
      final String xml, final String path,
      @Nullable final Collection<?> collection, @Nullable final Field field)
      throws Exception {
    if (collection == null) {
      XmlUnitUtils.assertXPathAbsent(xml, path);
    } else {
      String elementPath = path;
      if (field != null) {
        final PropertyName elementName = getPropertyWrappedName(mapper, field);
        elementPath = path + "/" + elementName;
      }
      int i = 1;
      for (final Object obj : collection) {
        assertXmlNodeEqualsObject(mapper, xml, elementPath + "[" + i + "]", null, obj);
        ++i;
      }
    }
  }

  private static void assertXmlNodeEqualsArray(final XmlMapper mapper,
      final String xml, final String path, @Nullable final Object array,
      @Nullable final Field field) throws Exception {
    if (array == null) {
      XmlUnitUtils.assertXPathAbsent(xml, path);
    } else {
      assert array.getClass().isArray();
      final Class<?> elementType = array.getClass().getComponentType();
      String elementPath = path;
      if (field != null && elementType != byte.class) {
        // byte[] should be serialized to BASE64 encoded string
        final PropertyName elementName = getPropertyWrappedName(mapper, field);
        elementPath = path + "/" + elementName.getSimpleName();
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
          assertXmlNodeEqualsObject(mapper, xml,
              elementPath + "[" + (i + 1) + "]", null, Array.get(array, i));
        }
      }
    }
  }
}
