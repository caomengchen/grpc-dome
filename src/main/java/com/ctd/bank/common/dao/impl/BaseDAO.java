package com.ctd.bank.common.dao.impl;

import com.ctd.bank.common.dao.IBaseDAO;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author zhoujy
 *
 */
@Repository
public class BaseDAO<T> implements IBaseDAO<T> {

	private SqlSessionTemplate sqlSessionTemplate;

	public BaseDAO(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	/**
	 * 查询
	 * @param str
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public T selectOne(String str, T entity) throws Exception {
		
		return sqlSessionTemplate.selectOne(str, entity);
	}

	/**
	 * 查询某个表中的某个字段是否存在
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> isExtisColumn(String str, Map<String, Object> map) throws Exception {
		return sqlSessionTemplate.selectOne(str, map);
	}

	/**
	 * 查询并封装成List
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public List<T> selectList(String str, T model) throws Exception {
		return sqlSessionTemplate.selectList(str, model);
	}

	/**
	 * 查询并封装成Map
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMap(String str, T model, String key, String value) throws Exception {
		return sqlSessionTemplate.selectMap(str, model, key);
	}

	/**
	 * 新增
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public long insert(String str, T entity) throws Exception {
		return sqlSessionTemplate.insert(str, entity);
	}

	/**
	 * 批量新增
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public long batchInsert(String str, List<T> entity) throws Exception {
		return sqlSessionTemplate.insert(str, entity);
	}

	/**
	 * 修改
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public long update(String str, T entity) throws Exception {
		return sqlSessionTemplate.update(str, entity);
	}

	/**
	 * 修改
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public long updateUserName(String str, Map<String, Object> map) throws Exception {
		return sqlSessionTemplate.update(str, map);
	}

	/**
	 * 批量修改
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public void batchUpdate(String str, List<T> list) throws Exception {
		SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
		//批量执行器
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		try {
			if (list != null) {
				for (int i = 0, size = list.size(); i < size; i++) {
					sqlSession.update(str, list.get(i));
				}
				sqlSession.flushStatements();
				sqlSession.commit();
				sqlSession.clearCache();
			}
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * 删除
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public long delete(String str, T entity) throws Exception {
		return sqlSessionTemplate.delete(str, entity);
	}

	/**
	 * 批量删除
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public long batchDelete(String str, List<T> list) throws Exception {
		return sqlSessionTemplate.delete(str, list);
	}

}