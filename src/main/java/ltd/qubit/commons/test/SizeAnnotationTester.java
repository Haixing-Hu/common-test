////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test;

import java.util.List;

import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.util.range.CloseRange;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SizeAnnotationTester extends Tester {

  public SizeAnnotationTester() {
  }

  public SizeAnnotationTester(final int loops) {
    super(loops);
  }

  public SizeAnnotationTester(final RandomBeanGenerator random) {
    super(random);
  }

  public SizeAnnotationTester(final RandomBeanGenerator random, final int loops) {
    super(random, loops);
  }

  @Override
  protected <T> void doTest(final Class<T> type) throws Exception {
    if (Enum.class.isAssignableFrom(type)) {
      return;     // 忽略枚举类型
    }
    final BeanInfo beanInfo = BeanInfo.of(type);
    final List<Property> properties = beanInfo.getProperties();
    for (final Property prop : properties) {
      if (prop.isComputed()) {
        continue;   // 忽略计算出的属性
      }
      if (prop.isJdkBuiltIn()) {
        continue;   // 忽略JDK内置属性
      }
      if (prop.getType() != String.class) {
        continue;  // 只考虑 String 类型的字段
      }
      final CloseRange<Integer> sizeRange = prop.getSizeRange();
      if (prop.isUnique()) {
        assertNotNull(sizeRange, "Unique string field " + prop.getFullname()
            + " must be annotated by a @Size annotation.");
        assertNotEquals(0, sizeRange.getMin(), "Unique string field " + prop.getFullname()
            + " must be annotated by a @Size annotation with a min value of at least 1.");
      } else if (!prop.isNullable() && sizeRange != null) {
        // assertNotNull(sizeRange, "Non-nullable string field " + prop.getFullname()
        //    + " must be annotated by a @Size annotation.");
        assertNotEquals(0,
            sizeRange.getMin(),
            "@Size annotated non-nullable string field " + prop.getFullname()
                + " must have a min value of at least 1.");
      } else if (sizeRange != null) {
        assertNotEquals(0, sizeRange.getMin(),
            "@Size annotated string field " + prop.getFullname()
                + " must specify a min value of at least 1.");
      }
    }
  }
}
