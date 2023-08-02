////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import ltd.qubit.commons.lang.Equality;
import ltd.qubit.commons.lang.Hash;
import ltd.qubit.commons.text.tostring.ToStringBuilder;

/**
 * 提供DAO自动化测试的参数。
 *
 * @author 胡海星
 */
public class DaoTestParameters {

  public static final int DEFAULT_LOOPS = 10;

  public static final int DEFAULT_COLLECTION_SIZE = 5;

  public static final int DEFAULT_TABLE_SIZE = 10;

  /**
   * 随机测试重复次数。
   */
  private int loops;

  /**
   * 随机生成的集合的大小上限。
   */
  private int collectionSize;

  /**
   * 随机生成的数据库表中元素的最大数目。
   */
  private int tableSize;

  public DaoTestParameters() {
    loops = DEFAULT_LOOPS;
    collectionSize = DEFAULT_COLLECTION_SIZE;
    tableSize = DEFAULT_TABLE_SIZE;
  }

  public final int getLoops() {
    return loops;
  }

  public final void setLoops(final int loops) {
    this.loops = loops;
  }

  public final int getCollectionSize() {
    return collectionSize;
  }

  public final void setCollectionSize(final int collectionSize) {
    this.collectionSize = collectionSize;
  }

  public final int getTableSize() {
    return tableSize;
  }

  public final void setTableSize(final int tableSize) {
    this.tableSize = tableSize;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    final DaoTestParameters other = (DaoTestParameters) o;
    return Equality.equals(loops, other.loops)
        && Equality.equals(collectionSize, other.collectionSize)
        && Equality.equals(tableSize, other.tableSize);
  }

  public int hashCode() {
    final int multiplier = 7;
    int result = 3;
    result = Hash.combine(result, multiplier, loops);
    result = Hash.combine(result, multiplier, collectionSize);
    result = Hash.combine(result, multiplier, tableSize);
    return result;
  }

  public String toString() {
    return new ToStringBuilder(this)
        .append("loops", loops)
        .append("collectionSize", collectionSize)
        .append("tableSize", tableSize)
        .toString();
  }
}
