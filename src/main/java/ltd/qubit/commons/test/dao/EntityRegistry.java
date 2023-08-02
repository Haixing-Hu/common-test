////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.util.HashMap;

import javax.annotation.Nullable;

import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.reflect.Property;

import static java.lang.System.identityHashCode;

/**
 * 用于存储引用实体的哈希表。
 *
 * @author 胡海星
 */
public class EntityRegistry {

  /**
   * 表示注册表的主键数据类型。
   */
  private static class Key {
    final Object model;
    final Property property;

    public Key(final Object model, final Property property) {
      this.model = model;
      this.property = property;
    }

    public boolean equals(@Nullable final Object o) {
      if (this == o) {
        return true;
      }
      if ((o == null) || (getClass() != o.getClass())) {
        return false;
      }
      // 注意我们使用 == 而非 equals() 比较 model，且仅比较 property 的name
      // 这是因为我们认为两个key相等当且仅当他们的model是同一个对象(物理意义上相同内存地址，
      // 而非逻辑意义上相同内容)，且他们的 property 是同名的
      final Key other = (Key) o;
      return (model == other.model)
          && property.getName().equals(other.property.getName());
    }

    public int hashCode() {
      final int multiplier = 7;
      int result = 3;
      // 注意我们使用 System.identityHashCode 计算 model 的哈希值分量，
      // 用 property.name 的哈希值作为 property 的哈希值分量。
      // 这是因为我们认为两个key相等当且仅当他们的model是同一个对象(物理意义上相同内存地址，
      // 而非逻辑意义上相同内容)，且他们的 property 是同名的
      result = Hash.combine(result, multiplier, identityHashCode(model));
      result = Hash.combine(result, multiplier, property.getName());
      return result;
    }
  }

  private final HashMap<Key, Object> registry;

  public EntityRegistry() {
    registry = new HashMap<>();
  }

  public void put(final Object model, final Property property,
      final Object referencedEntity) {
    final Key key = new Key(model, property);
    registry.put(key, referencedEntity);
  }

  public Object get(final Object model, final Property property) {
    final Key key = new Key(model, property);
    return registry.get(key);
  }

  public boolean containsKey(final Object model, final Property property) {
    final Key key = new Key(model, property);
    return registry.containsKey(key);
  }

  public void clear() {
    registry.clear();
  }
}
