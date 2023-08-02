////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (c) 2022 - 2023.
//    Haixing Hu, Qubit Co. Ltd.
//
//    All rights reserved.
//
////////////////////////////////////////////////////////////////////////////////
package ltd.qubit.commons.test.dao;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;

import ltd.qubit.commons.reflect.BeanInfo;
import ltd.qubit.commons.reflect.Option;
import ltd.qubit.commons.test.dao.testbed.CountryDao;
import ltd.qubit.commons.test.dao.testbed.CountryDaoImpl;
import ltd.qubit.commons.test.model.Country;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ltd.qubit.commons.reflect.FieldUtils.getField;
import static ltd.qubit.commons.reflect.MethodUtils.getMethodByName;
import static ltd.qubit.commons.reflect.Option.BEAN_FIELD;
import static ltd.qubit.commons.reflect.Option.BEAN_METHOD;
import static ltd.qubit.commons.test.assertion.Assertions.assertCollectionEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DaoMethodInfoTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(DaoMethodInfoTest.class);

  @Test
  public void testConstructor() throws Exception {
    final CountryDao countryDao = new CountryDaoImpl();
    final Class<?> daoType = CountryDao.class;
    final Class<?> modelType = Country.class;
    final BeanInfo modelInfo = BeanInfo.of(modelType);
    Method method;
    DaoMethodInfo methodInfo;

    method = getMethodByName(daoType, BEAN_METHOD, "count");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals("CountryDao.count", methodInfo.getQualifiedName());
    assertEquals(URI.create("method:ltd.qubit.commons.test.dao.testbed."
        + "ListableDao#count(ltd.qubit.commons.sql.Criterion)"),
        methodInfo.getUri());
    assertEquals(DaoOperation.COUNT, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertNull(methodInfo.getIdentifier());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(), methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "list");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.LIST, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertNull(methodInfo.getIdentifier());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "exist");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.EXIST, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "existCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.EXIST, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "existName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.EXIST, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "existNonDeleted");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.EXIST_NON_DELETED, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "existNonDeletedCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.EXIST_NON_DELETED, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "get");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.GET, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "getByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.GET, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "getByName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.GET, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "getInfo");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.GET, methodInfo.getOperation());
    assertNull(methodInfo.getTarget().getField());
    assertEquals(getMethodByName(Country.class, BEAN_METHOD, "getInfo"),
        methodInfo.getTarget().getReadMethod());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "getInfoByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.GET, methodInfo.getOperation());
    assertNull(methodInfo.getTarget().getField());
    assertEquals(getMethodByName(Country.class, BEAN_METHOD, "getInfo"),
        methodInfo.getTarget().getReadMethod());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(), methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getNonComputedProperties(),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "add");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals("CountryDao.add", methodInfo.getQualifiedName());
    assertEquals(URI.create("method:ltd.qubit.commons.test.dao.testbed."
            + "AddableDao#add(ltd.qubit.commons.test.model.Identifiable)"),
        methodInfo.getUri());
    assertEquals(DaoOperation.ADD, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertNull(methodInfo.getIdentifier());
    assertCollectionEquals(
        modelInfo.getProperties("id", "createTime", "modifyTime", "deleteTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "addOrUpdateByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.ADD_OR_UPDATE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());

    method = getMethodByName(daoType, BEAN_METHOD, "addOrUpdateByName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.ADD_OR_UPDATE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getIdentifier().getField());

    method = getMethodByName(daoType, BEAN_METHOD, "update");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.UPDATE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(
        modelInfo.getProperties("name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "modifyTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getProperties("id", "code", "createTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "updateByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.UPDATE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(
        modelInfo.getProperties("name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "modifyTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "createTime", "deleteTime",
           "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "updateByName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.UPDATE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(
        modelInfo.getProperties("phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "modifyTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "createTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "updateName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.UPDATE, methodInfo.getOperation());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getTarget().getField());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("name", "modifyTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getProperties("id", "code", "phoneArea",
            "postalcode", "icon", "url", "description", "createTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "updateNameByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.UPDATE, methodInfo.getOperation());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getTarget().getField());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("name", "modifyTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(modelInfo.getProperties("id", "code", "phoneArea",
            "postalcode", "icon", "url", "description", "createTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "delete");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.DELETE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("deleteTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "deleteByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.DELETE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("deleteTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "deleteByName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.DELETE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("deleteTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "restore");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.RESTORE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("deleteTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "restoreByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.RESTORE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("deleteTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "restoreByName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.RESTORE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(modelInfo.getProperties("deleteTime"),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "purge");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.PURGE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "purgeByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.PURGE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "purgeByName");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.PURGE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "name"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "purgeAll");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.PURGE_ALL, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertNull(methodInfo.getIdentifier());

    method = getMethodByName(daoType, BEAN_METHOD, "erase");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.ERASE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "id"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "eraseByCode");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.ERASE, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertEquals(getField(modelType, BEAN_FIELD, "code"),
        methodInfo.getIdentifier().getField());
    assertCollectionEquals(Collections.emptySet(),
        methodInfo.getModifiedProperties());
    assertCollectionEquals(
        modelInfo.getProperties("id", "code", "name", "phoneArea", "postalcode",
            "icon", "url", "description", "predefined", "createTime", "modifyTime",
            "deleteTime", "class"),
        methodInfo.getUnmodifiedProperties());

    method = getMethodByName(daoType, BEAN_METHOD, "clear");
    assertNotNull(method);
    methodInfo = DaoMethodInfo.create(modelInfo, daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.CLEAR, methodInfo.getOperation());
    assertNull(methodInfo.getTarget());
    assertNull(methodInfo.getIdentifier());
  }

  @Test
  public void testConstructor_delete() throws Exception {
    final CountryDao countryDao = new CountryDaoImpl();
    final Class<?> daoType = CountryDao.class;
    final Class<?> modelType = Country.class;
    final BeanInfo modelInfo = BeanInfo.of(modelType);

    final Method method = getMethodByName(daoType, BEAN_METHOD, "delete");
    assertNotNull(method);
    final DaoMethodInfo methodInfo = DaoMethodInfo.create(modelInfo,
        daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.DELETE, methodInfo.getOperation());
    assertEquals(modelInfo.getProperty("id"), methodInfo.getIdentifier());
    assertNull(methodInfo.getTarget());
  }

  @Test
  public void testConstructor_deleteBy() throws Exception {
    final CountryDao countryDao = new CountryDaoImpl();
    final Class<?> daoType = CountryDao.class;
    final Class<?> modelType = Country.class;
    final BeanInfo modelInfo = BeanInfo.of(modelType);

    final Method method = getMethodByName(daoType, BEAN_METHOD, "deleteByCode");
    assertNotNull(method);
    final DaoMethodInfo methodInfo = DaoMethodInfo.create(modelInfo,
        daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.DELETE, methodInfo.getOperation());
    assertEquals(modelInfo.getProperty("code"), methodInfo.getIdentifier());
    assertNull(methodInfo.getTarget());
  }

  @Test
  public void testConstructor_existCode() throws Exception {
    final CountryDao countryDao = new CountryDaoImpl();
    final Class<?> daoType = CountryDao.class;
    final Class<?> modelType = Country.class;
    final BeanInfo modelInfo = BeanInfo.of(modelType);

    final Method method = getMethodByName(daoType, BEAN_METHOD, "existCode");
    assertNotNull(method);
    final DaoMethodInfo methodInfo = DaoMethodInfo.create(modelInfo,
        daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.EXIST, methodInfo.getOperation());
    assertEquals(modelInfo.getProperty("code"), methodInfo.getIdentifier());
    assertNull(methodInfo.getTarget());
  }

  @Test
  public void testConstructor_restoreBy() throws Exception {
    final CountryDao countryDao = new CountryDaoImpl();
    final Class<?> daoType = CountryDao.class;
    final Class<?> modelType = Country.class;
    final BeanInfo modelInfo = BeanInfo.of(modelType);

    final Method method = getMethodByName(daoType, BEAN_METHOD, "restoreByCode");
    assertNotNull(method);
    final DaoMethodInfo methodInfo = DaoMethodInfo.create(modelInfo,
        daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.RESTORE, methodInfo.getOperation());
    assertEquals(modelInfo.getProperty("code"), methodInfo.getIdentifier());
    assertNull(methodInfo.getTarget());
  }

  @Test
  public void testConstructor_purgeBy() throws Exception {
    final CountryDao countryDao = new CountryDaoImpl();
    final Class<?> daoType = CountryDao.class;
    final Class<?> modelType = Country.class;
    final BeanInfo modelInfo = BeanInfo.of(modelType);

    final Method method = getMethodByName(daoType, BEAN_METHOD, "purgeByName");
    assertNotNull(method);
    final DaoMethodInfo methodInfo = DaoMethodInfo.create(modelInfo,
        daoType, countryDao, method);
    assertNotNull(methodInfo);
    assertEquals(modelType, methodInfo.getModelInfo().getType());
    assertSame(countryDao, methodInfo.getDao());
    assertSame(method, methodInfo.getMethod());
    assertEquals(DaoOperation.PURGE, methodInfo.getOperation());
    assertEquals(modelInfo.getProperty("name"), methodInfo.getIdentifier());
    assertNull(methodInfo.getTarget());
  }
}
