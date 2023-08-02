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

import javax.annotation.Nullable;

import jakarta.validation.constraints.NotNull;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Property;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

/**
 * 此模型表示实体对象在对象树中的信息。
 *
 * @author 胡海星
 */
public class EntityInfo {

  /**
   * 对象路径分隔符。
   */
  public static final String PATH_SEPARATOR = "/";

  /**
   * 表示父对象的路径。
   */
  public static final String PARENT_PATH = "..";

  /**
   * 父对象路径前缀。
   */
  public static final String PARENT_PATH_PREFIX = PARENT_PATH + PATH_SEPARATOR;

  /**
   * 实体对象的类型。
   */
  private final Class<?> type;

  /**
   * 实体对象。
   */
  private final Object entity;

  /**
   * 该对象在其父对象中的属性。
   */
  @Nullable
  private final Property propertyInParent;

  /**
   * 待设置的引用属性。
   */
  private final List<ReferencedProperty> referencedProperties = new ArrayList<>();

  public EntityInfo(@NotNull final Class<?> type, @NotNull final Object entity) {
    this.type = type;
    this.entity = entity;
    this.propertyInParent = null;
  }

  public EntityInfo(@NotNull final Class<?> type, @NotNull final Object entity,
      @Nullable final Property propertyInParent) {
    this.type = type;
    this.entity = entity;
    this.propertyInParent = propertyInParent;
  }

  public Object getEntity() {
    return entity;
  }

  @NotNull
  public Class<?> getType() {
    return type;
  }

  @Nullable
  public Property getPropertyInParent() {
    return propertyInParent;
  }

  @NotNull
  public List<ReferencedProperty> getReferencedProperties() {
    return referencedProperties;
  }

  /**
   * 根据属性的 reference path 的依赖关系，对引用属性列表中的引用属性进行拓扑排序。
   */
  public void topologySortReferencedProperties() {
    if (referencedProperties.isEmpty()) {
      return;
    }
    // 首先根据属性间依赖关系构造一个有向图
    final DirectedAcyclicGraph<ReferencedProperty, DefaultEdge>
        g = new DirectedAcyclicGraph<>(DefaultEdge.class);
    Graphs.addAllVertices(g, referencedProperties);
    for (final ReferencedProperty p : referencedProperties) {
      for (final ReferencedProperty q : referencedProperties) {
        if (! p.equals(q)) {
          // 若属性p依赖于属性q，则从q到p连接一条有向边
          final String p_ref_path = p.getReferencePath();
          final String p_prop_path = p.getPropertyPath();
          final String q_ref_path = q.getReferencePath();
          final String q_prop_path = q.getPropertyPath();
          if (p_ref_path.equals(q_prop_path)
              || p_ref_path.startsWith(q_prop_path + PATH_SEPARATOR)) {
            // 若 p 的引用属性路径等于q的属性值路径，或者p的引用属性路径以q的属性值路径为前缀，
            // 则增加从q到p的边，这是因为要确定 p 的引用属性值，必须先确定q的属性值，
            // 因此需要先确定q的值，即让q出现在拓扑序的前面
            try {
              g.addEdge(q, p);
            } catch (final IllegalArgumentException e) {
              throw new ReferencePathContainsCycleException(BeanInfo.of(type),
                  referencedProperties);
            }
          }
        }
      }
    }
    // 接下来对该图进行拓扑排序
    final List<ReferencedProperty> result = new ArrayList<>();
    for (final ReferencedProperty v : g) {
      result.add(v);
    }
    // 结果重新写入到 referencedProperties 中
    referencedProperties.clear();
    referencedProperties.addAll(result);
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final EntityInfo other = (EntityInfo) o;
    return Equality.equals(entity, other.entity)
        && Equality.equals(type, other.type)
        && Equality.equals(propertyInParent, other.propertyInParent)
        && Equality.equals(referencedProperties, other.referencedProperties);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, entity);
    result = Hash.combine(result, multiplier, type);
    result = Hash.combine(result, multiplier, propertyInParent);
    result = Hash.combine(result, multiplier, referencedProperties);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("entity", entity)
        .append("type", type)
        .append("propertyInParent", propertyInParent)
        .append("referencedProperties", referencedProperties)
        .toString();
  }
}
