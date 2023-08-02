////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearOperationTestGenerator<T> extends DaoOperationTestGenerator<T> {

  public ClearOperationTestGenerator(final DaoTestGeneratorRegistry registry,
      final Class<T> modelType, final DaoMethodInfo methodInfo) {
    super(registry, modelType, methodInfo);
  }

  @Override
  protected void buildTests(final DaoDynamicTestBuilder builder) {
    final String displayName = getDisplayName("Clear all " + modelName);
    final int loops = parameters.getLoops();
    final int tableSize = parameters.getTableSize();
    builder.add(displayName, () -> {
      logger.info("Test {}: Clear all {}.", methodName, modelName);
      for (int i = 0; i < loops; ++ i) {
        final int n = random.nextInt(0, tableSize);
        logger.info("Add {} {}s.", n, modelName);
        // 注意：必须先准备好所有待添加的模型对象，并且记录下准备这些模型对象后目前数据库中
        // 该对象对应的表已经有多少对象，这是因为在准备某些对象时，可能需要往数据库中加入其他
        // 一些同类型的对象。例如对于下面两个类：
        //   class A {
        //     @Reference(entity = B.class, existing = false)
        //     private B child;
        //   }
        //   class B {
        //     @Reference(entity = A.class, property = "id")
        //     private Long parentId;
        //   }
        // 上述两个类的关系是常见的父子关系。在准备类B对象时，会先创建一个存在的A对象并将其加
        // 入数据库，而将A对象加入数据库时有可能会将A.child也加入数据库。这样在准备好B对象后，
        // 数据库中就会已经有一个B对象了。
        final List<Object> models = new ArrayList<>();
        for (int j = 0; j < n; ++j) {
          final Object model = beanCreator.prepare(modelInfo);
          models.add(model);
        }
        // 通过 count 记录下目前数据库中已有的模型数量
        final Long existingCount = daoInfo.count(null);
        // 接下来把准备好的模型加入，随机标记删除其中一些，并记录所有加入对象的ID
        final List<Object> ids = new ArrayList<>();
        for (final Object model : models) {
          daoInfo.add(model);
          // 记录所有加入对象的ID
          final Object id = modelInfo.getId(model);
          ids.add(id);
          if (daoInfo.hasExist()) {
            assertTrue(daoInfo.exist(id), "The ID of added model must exist.");
          }
          // 随机标记删除其中一些
          if (daoInfo.hasDelete() && random.nextBoolean()) {
            logger.info("Test {}: Delete the just added model: {}", methodName, id);
            daoInfo.delete(id);  // dao.delete(id)
          }
        }
        // 计算出目前实际应该有的对象总数
        final long expectedCount = n + existingCount;
        // 接下来调用 dao.clear()
        final Object count = methodInfo.invoke(true);  // dao.clear()
        assertNotNull(count, "The returned value of " + methodName
            + " must not be null.");
        assertEquals(Long.class, count.getClass(), "The returned type of "
            + methodName + " must be long.");
        assertEquals(expectedCount, count,
            "The " + methodName + " must return the total "
            + "number of cleared " + modelName + ".");
        checkClearedModels(ids);
      }
    });
  }

  private void checkClearedModels(final List<Object> ids) throws Throwable {
    for (final Object id : ids) {
      logger.info("Test {}: Test the existence of the cleared model: {}", methodName, id);
      assertFalse(daoInfo.exist(id), "The ID of a cleared model must not exist.");
    }
  }
}
