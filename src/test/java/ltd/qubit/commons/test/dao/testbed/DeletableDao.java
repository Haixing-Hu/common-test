////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import java.time.Instant;

import ltd.qubit.commons.annotation.Modified;
import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.test.model.Deletable;
import ltd.qubit.commons.test.model.Identifiable;

import org.springframework.dao.DataAccessException;

/**
 * 此接口表示实现删除实体操作的DAO。
 *
 * @param <T>
 *     被操作的实体的类型。
 * @author 胡海星
 */
public interface DeletableDao<T extends Identifiable & Deletable> {

  /**
   * 判定是否存在指定的未被标记删除的实体对象。
   *
   * @param id
   *     指定的实体对象的ID。
   * @return
   *     是否存在指定的未被标记删除的实体对象。
   * @throws DataAccessException
   *     若发生任何数据存取错误。
   */
  boolean existNonDeleted(Long id) throws DataAccessException;

  /**
   * 标记删除指定的实体对象。
   *
   * <p><b>注意：</b>此操作并不真正从数据库中删除指定的对象，只是将其标记删除，并记录被
   * 标记删除时的时间戳。若要真正从数据库中删除对象，请使用{@link #purge(Long)}或
   * {@link #purgeAll()}函数。
   *
   * @param id
   *     待删除的实体对象的ID。eeeeeee
   * @return
   *     数据被标记删除时的时间戳。
   * @throws DataNotExistException
   *     若指定的待删除对象不存在或已被标记删除。
   * @throws DataAccessException
   *     若发生任何其他数据存取错误。
   * @see #purge(Long)
   * @see #purgeAll()
   * @see #restore(Long)
   * @see #clear()
   */
  @Modified("deleteTime")
  Instant delete(final Long id) throws DataAccessException;

  /**
   * 恢复被标记删除的实体对象。
   *
   * <p><b>注意：</b>此操作只能恢复被标记删除的对象，即通过调用{@link #delete(Long)}
   * 被标记删除的对象。若该对象已经被彻底清除，则无法再被恢复。
   *
   * @param id
   *     待恢复的已被标记删除的实体对象的ID。
   * @throws DataNotExistException
   *     若指定的待恢复对象不存在，或该对象未被标记删除。
   * @throws DataAccessException
   *     若发生任何其他数据存取错误。
   * @see #delete(Long)
   * @see #purge(Long)
   * @see #purgeAll()
   * @see #clear()
   */
  @Modified("deleteTime")
  void restore(final Long id) throws DataAccessException;

  /**
   * 彻底清除被标记删除的实体对象。
   *
   * <p><b>注意：</b>此操作将从数据库中彻底清除被标记删除的对象，即通过调用
   * {@link #delete(Long)}被标记删除的对象。被彻底清除的对象，无法通过调用
   * {@link #restore(Long)}再被恢复。
   *
   * @param id
   *     待彻底清除的已被标记删除的实体对象的ID。
   * @throws DataNotExistException
   *     若指定的待清除对象不存在，或该对象未被标记删除。
   * @throws DataAccessException
   *     若发生任何其他数据存取错误。
   * @see #delete(Long)
   * @see #restore(Long)
   * @see #purgeAll()
   * @see #clear()
   */
  void purge(final Long id) throws DataAccessException;

  /**
   * 彻底清除<b>所有</b>被标记删除的实体对象。
   *
   * <p><b>注意：</b>此操作将从数据库中彻底清除<b>所有</b>被标记删除的对象，即通过调用
   * {@link #delete(Long)}被标记删除的对象。被彻底清除的对象，无法通过调用
   * {@link #restore(Long)}再被恢复。
   *
   * @return
   *     此操作所彻底清除的对象的数目。
   * @throws DataAccessException
   *     若发生任何其他数据存取错误。
   * @see #delete(Long)
   * @see #restore(Long)
   * @see #purge(Long)
   * @see #clear()
   */
  long purgeAll() throws DataAccessException;

  /**
   * 彻底清除<b>所有</b>的实体对象。
   *
   * <p><b>注意：</b>此操作将从数据库中彻底清除<b>所有</b>的对象，即清空整个数据库表。
   * 此操作不可逆。
   *
   * @return
   *     此操作所彻底清除的对象的数目。
   * @throws DataAccessException
   *     若发生任何其他数据存取错误。
   * @see #delete(Long)
   * @see #restore(Long)
   * @see #purge(Long)
   * @see #purgeAll()
   */
  long clear() throws DataAccessException;
}
