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

/**
 * A tester object used to test the XML serialization/deserialization of
 * classes.
 *
 * @author Haixing Hu
 */
public class JaxbXmlTester extends Tester {

  public JaxbXmlTester() {
  }

  public JaxbXmlTester(final int loops) {
    super(loops);
  }

  public JaxbXmlTester(final RandomBeanGenerator random) {
    super(random);
  }

  public JaxbXmlTester(final RandomBeanGenerator random, final int loops) {
    super(random, loops);
  }

  protected <T> void doTest(final Class<T> type) throws Exception {
    for (int i = 0; i < loops; ++i) {
      final T obj = random.nextObject(type);
      JaxbTestUtils.testXmlSerialization(obj);
      JaxbTestUtils.testXmlDeserialization(obj);
    }
  }
}
