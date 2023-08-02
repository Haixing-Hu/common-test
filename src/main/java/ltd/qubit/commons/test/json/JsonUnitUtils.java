////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javacrumbs.jsonunit.assertj.JsonAssert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Nullable;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

/**
 * Some utilities functions for JSON unit.
 *
 * @author Haixing Hu
 */
public class JsonUnitUtils {

  public static <T> void assertJsonArrayEquals(final String json,
      final String field, @Nullable final T[] expected, final ObjectMapper mapper)
      throws Exception {
    if (expected == null) {
      assertThatJson(json).node(field).isAbsent();
    } else {
      final JsonAssert assertion = assertThatJson(json);
      assertion.node(field).isArray().hasSize(expected.length);
      for (int i = 0; i < expected.length; ++i) {
        final String str = mapper.writerWithDefaultPrettyPrinter()
                                 .writeValueAsString(expected[i]);
        assertion.node(field + "[" + i + "]").isEqualTo(str);
      }
    }
  }

  public static <T> void assertJsonArrayEquals(final String json,
      final String field, @Nullable final List<T> expected,
      final ObjectMapper mapper) throws Exception {
    if (expected == null) {
      assertThatJson(json).node(field).isAbsent();
    } else {
      final JsonAssert assertion = assertThatJson(json);
      assertion.node(field).isArray().hasSize(expected.size());
      for (int i = 0; i < expected.size(); ++i) {
        final String str = mapper.writerWithDefaultPrettyPrinter()
                                 .writeValueAsString(expected.get(i));
        assertion.node(field + "[" + i + "]").isEqualTo(str);
      }
    }
  }

  public static void assertJsonNodeNull(final String json,
      @Nullable final String path) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    ja.isNull();
  }

  public static void assertJsonNodeAbsent(final String json,
      @Nullable final String path) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    ja.isAbsent();
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Object expected,
      final ObjectMapper mapper) throws Exception {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      final String expectedJson = mapper.writerWithDefaultPrettyPrinter()
                                  .writeValueAsString(expected);
      ja.isEqualTo(expectedJson);
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Boolean expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isBoolean().isEqualTo(expected);
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final String expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isString().isEqualTo(expected);
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Character expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isString().isEqualTo(expected.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Long expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(expected.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Integer expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(expected.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Short expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(expected.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Byte expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(expected.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Float expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(expected.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Double expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(expected.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final Enum<?> expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isString().isEqualTo(expected.name());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final BigDecimal value) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (value == null) {
      ja.isAbsent();
    } else {
      // 必须把 BigDecimal 转换为字符串在比较，否则可能丧失精度，
      // 例如原来的值是 4.1230 若直接比较则会和 4.123 比较，导致不同
      ja.isNumber().isEqualByComparingTo(value.toString());
    }
  }

  public static void assertJsonNodeEquals(final String json,
      @Nullable final String path, @Nullable final BigInteger bigInteger) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (bigInteger == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(bigInteger.toString());
    }
  }

  public static void assertJsonNodeEqualsFloat(final String json,
      @Nullable final String path, @Nullable final String floatText) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (floatText == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(floatText);
    }
  }

  public static void assertJsonNodeEqualsDouble(final String json,
      @Nullable final String path, @Nullable final String doubleText) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (doubleText == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(doubleText);
    }
  }

  public static void assertJsonNodeEqualsBigDecimal(final String json,
      @Nullable final String path, @Nullable final String bigDecimalText) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (bigDecimalText == null) {
      ja.isAbsent();
    } else {
      ja.isNumber().isEqualByComparingTo(bigDecimalText);
    }
  }

  public static void assertJsonNodeEqualsRaw(final String json,
      @Nullable final String path, @Nullable final String expected) {
    JsonAssert ja = assertThatJson(json);
    if (path != null) {
      ja = ja.node(path);
    }
    if (expected == null) {
      ja.isAbsent();
    } else {
      ja.isEqualTo(expected);
    }
  }
}
