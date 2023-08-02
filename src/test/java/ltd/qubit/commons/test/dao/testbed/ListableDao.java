////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import java.util.List;

import javax.annotation.Nullable;

import ltd.qubit.commons.sql.Criterion;
import ltd.qubit.commons.sql.SortRequest;

import org.springframework.dao.DataAccessException;

/**
 * 此接口表示可以执行列表查询操作的DAO。
 *
 * @param <T>
 *    被操作的实体的类型。
 * @author 胡海星
 */
public interface ListableDao<T> {

  /**
   * 获取符合条件的实体的数目。
   *
   * @param filter
   *     用于过滤实体的条件，如果为{@code null}则不做限制。
   * @return
   *     符合过滤条件的所有实体的数目。
   * @throws DataAccessException
   *     若出现任何数据存取错误。
   */
  long count(@Nullable final Criterion<T> filter) throws DataAccessException;

  /**
   * 列出符合条件的实体的指定子序列。
   *
   * @param filter
   *     用于过滤实体的条件，若为{@code null}则不做限制。
   * @param sortRequest
   *     指定排序的字段和排序方式，若为{@code null}则使用默认排序。
   * @param limit
   *     指定需返回的子序列的最大长度，若为{@code null}则不做限制。
   * @param offset
   *     指定需返回的子序列的第一个元素在所有符合条件的对象序列中的位置（从0开始），若为
   *     {@code null}则使用默认值0。
   * @return
   *     符合条件的实体的指定子序列，按照指定的排序方式排序。
   * @throws DataAccessException
   *     如果出现任何数据存取错误。
   */
  List<T> list(@Nullable final Criterion<T> filter,
      @Nullable final SortRequest<T> sortRequest,
      @Nullable final Integer limit, @Nullable final Long offset)
      throws DataAccessException;
}
