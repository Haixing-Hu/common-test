////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao.testbed;

import ltd.qubit.commons.test.model.Info;

/**
 * 此接口表示实现了对{@link Info}类进行查询、添加、更新、删除等基本操作的DAO。
 *
 * @param <T>
 *     被操作的实体的类型。
 * @author 胡海星
 */
public interface InfoDao<T extends Info> extends GettableWithCodeDao<T>,
    AddableDao<T>, UpdatableDao<T>, DeletableDao<T> {

  //  empty
}
