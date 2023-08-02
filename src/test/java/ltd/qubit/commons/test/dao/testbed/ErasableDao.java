////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.error.DataNotExistException;
import ltd.qubit.commons.test.model.Identifiable;

import org.springframework.dao.DataAccessException;

/**
 * 此接口表示实现删除实体操作的DAO。
 *
 * <p>此接口实现了以下DAO操作：</p>
 * <ul>
 * <li>{@link #erase(Long)} </li>
 * </ul>
 *
 * @param <T>
 *     被操作的实体的类型。
 * @author 胡海星
 */
public interface ErasableDao<T extends Identifiable> {
  /**
   * 彻底删除指定的实体对象。
   *
   * <p><b>注意：</b>此操作将真正从数据库中删除指定的对象，被删除的对象不可恢复。
   *
   * @param id
   *     待彻底删除的实体对象的ID。
   * @throws DataNotExistException
   *     若指定的待删除对象不存在。
   * @throws DataAccessException
   *     若发生任何其他数据存取错误。
   * @see DeletableDao#delete(Long)
   * @see DeletableDao#restore(Long)
   * @see DeletableDao#purge(Long)
   * @see DeletableDao#purgeAll()
   */
  void erase(final Long id) throws DataAccessException;
}
