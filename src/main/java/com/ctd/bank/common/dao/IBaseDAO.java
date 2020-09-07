package com.ctd.bank.common.dao;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhoujy
 *
 */
public interface IBaseDAO<T> {

	/**
	 * 查询
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	T selectOne(String str, T entity) throws Exception;

	/**
	 * 查询并封装成List
	 * @param str
	 * @param model
	 * @return
	 * @throws Exception
	 */
	List<T> selectList(String str, T model) throws Exception;

	/**
	 * 查询并封装成Map
	 * @param str
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectMap(String str, T model, String key, String value) throws Exception;

	/**
	 * 新增
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	long insert(String str, T entity) throws Exception;

	/**
	 * 批量新增
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	long batchInsert(String str, List<T> entity) throws Exception;

	/**
	 * 修改
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	long update(String str, T entity) throws Exception;

	/**
	 * 修改
	 * @param str
	 * @param map
	 * @return
	 * @throws Exception
	 */
	long updateUserName(String str, Map<String, Object> map) throws Exception;

	/**
	 * 批量修改
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	void batchUpdate(String str, List<T> entity) throws Exception;

	/**
	 * 删除
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	long delete(String str, T entity) throws Exception;

	/**
	 * 批量删除
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	long batchDelete(String str, List<T> entity) throws Exception;

	/**
	 * 判断一个表中是否存在某个字段
	 * @param string
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> isExtisColumn(String string, Map<String, Object> map) throws Exception;
}
