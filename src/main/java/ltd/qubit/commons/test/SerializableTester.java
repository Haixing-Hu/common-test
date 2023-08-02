////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test;

import java.io.Serializable;

import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.reflect.FieldUtils;
import ltd.qubit.commons.reflect.Option;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A tester object used to test the serializable interfaces of model classes.
 *
 * @author Haixing Hu
 */
public class SerializableTester extends Tester {

  public SerializableTester() {
  }

  public SerializableTester(final int loops) {
    super(loops);
  }

  public SerializableTester(final RandomBeanGenerator random) {
    super(random);
  }

  public SerializableTester(final RandomBeanGenerator random, final int loops) {
    super(random, loops);
  }

  @SuppressWarnings("unchecked")
  protected <T> void doTest(final Class<T> type) throws Exception {
    if (!type.isEnum()) {
      assertTrue(Serializable.class.isAssignableFrom(type));
      assertNotNull(FieldUtils.getField(type, Option.STATIC | Option.PRIVATE,
          "serialVersionUID"), "The serialVersionUID must be defined.");
    }
  }

}
