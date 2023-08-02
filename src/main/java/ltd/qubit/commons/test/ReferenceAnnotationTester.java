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

import ltd.qubit.commons.lang.ArrayUtils;
import ltd.qubit.commons.random.RandomBeanGenerator;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;

import static ltd.qubit.commons.lang.StringUtils.isEmpty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 检查属性的 {@code @Reference} 标注是否合法。
 *
 * @author 胡海星
 */
public class ReferenceAnnotationTester extends Tester {

  public ReferenceAnnotationTester() {
  }

  public ReferenceAnnotationTester(final int loops) {
    super(loops);
  }

  public ReferenceAnnotationTester(final RandomBeanGenerator random) {
    super(random);
  }

  public ReferenceAnnotationTester(final RandomBeanGenerator random, final int loops) {
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
      if (! prop.isReference()) {
        continue;   // 忽略非引用属性
      }
      final Class<?> refClass = prop.getReferenceEntity();
      if (refClass.equals(Object.class)) {
        continue;   // 忽略 "间接引用"
      }
      final String refPropName = prop.getReferenceProperty();
      final Class<?> expectedReferencedType;
      if (isEmpty(refPropName)) {
        // 所引用的是实体类本身
        expectedReferencedType = refClass;
      } else {
        final BeanInfo refClassInfo = BeanInfo.of(refClass);
        final Property refProp = refClassInfo.getProperty(refPropName);
        assertNotNull(refProp, "The property " + refClass.getSimpleName()
            + "." + refPropName + " does not exist.");
        expectedReferencedType = refProp.getType();
      }
      if (prop.getActualComponentType() != null) {
        assertEquals(expectedReferencedType, prop.getActualComponentType(),
            "The component type of the property " + prop.getFullname()
            + " must be " + expectedReferencedType.getName());
      } else if (prop.getActualTypeArguments() != null) {
        final Class<?>[] actual = prop.getActualTypeArguments();
        assertTrue(ArrayUtils.contains(actual, expectedReferencedType),
            "The actual type arguments of the property " + prop.getFullname()
            + " must contain " + expectedReferencedType.getName());
      } else {
        assertEquals(expectedReferencedType, prop.getType(),
            "The type of the property " + prop.getFullname()
            + " must be " + expectedReferencedType.getName());
      }
      // TODO: 检查 path
    }
  }
}
