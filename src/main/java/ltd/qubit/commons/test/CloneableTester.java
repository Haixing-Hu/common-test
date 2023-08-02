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

public class CloneableTester extends Tester {

  public CloneableTester() {
  }

  public CloneableTester(final int loops) {
    super(loops);
  }

  public CloneableTester(final RandomBeanGenerator random) {
    super(random);
  }

  public CloneableTester(final RandomBeanGenerator random, final int loops) {
    super(random, loops);
  }

  protected <T> void doTest(final Class<T> type) throws Exception {
  //    for (int i = 0; i < loops; ++i) {
  //      final T obj = random.nextObject(type);
  //      final T other = (T) obj.clone();
  //    }
  //  TODO
  }
}
