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

import static ltd.qubit.commons.lang.Argument.requireNonNull;
import static ltd.qubit.commons.lang.Argument.requirePositive;

/**
 * The base class of tester objects.
 *
 * @author Haixing Hu
 */
public abstract class Tester {

  public static final int DEFAULT_LOOPS = 10;

  protected final int loops;
  protected final RandomBeanGenerator random;
  protected boolean enabled = true;

  public Tester() {
    this(new RandomBeanGenerator(), DEFAULT_LOOPS);
  }

  public Tester(final int loops) {
    this(new RandomBeanGenerator(), loops);
  }

  public Tester(final RandomBeanGenerator random) {
    this(random, DEFAULT_LOOPS);
  }

  public Tester(final RandomBeanGenerator random, final int loops) {
    this.random = requireNonNull("random", random);
    this.loops = requirePositive("loops", loops);
  }

  public final int getLoops() {
    return loops;
  }

  public final RandomBeanGenerator getRandom() {
    return random;
  }

  public final boolean isEnabled() {
    return enabled;
  }

  public final void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  public final <T> void test(final Class<T> type) throws Exception {
    if (enabled) {
      doTest(type);
    }
  }

  protected abstract <T> void doTest(Class<T> type) throws Exception;
}
