package com.fow.core.database.mybatis.service;


import com.fow.core.database.mybatis.constants.SqlId;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by greg.chen on 14-9-12.
 */
public abstract class BaseService<T> {

    @Autowired(required = true)
    protected SqlSession sqlSessionTemplate;

    //获取泛型T
    private Class<?> classt;

    private String sqlNamespace;

    public BaseService() {
        Type t = this.getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.classt = ((Class<?>) p[0]);
        }
        this.sqlNamespace = this.classt.getName();
    }

    /**
     * 将SqlMapping命名空间与给定的SqlMapping名组合在一起。
     *
     * @param sqlName SqlMapping名
     * @return 组合了SqlMapping命名空间后的完整SqlMapping名
     */
    protected String getSqlName(String sqlName) {
        return sqlNamespace + SqlId.SQLNAME_SEPARATOR + sqlName;
    }


    public List<T> selectList(Map<String, Object> query) {
        return sqlSessionTemplate.selectList(getSqlName(SqlId.SQL_SELECT), query);
    }


    public T selectOne(Map<String, Object> query) {
        return (T) sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT), query);
    }


    public T selectById(String id) {
        return (T) sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_BY_ID), id);
    }


    public T selectById(Integer id) {
        return (T) sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_BY_ID), id);
    }


    public int insert(T entity) {
        return sqlSessionTemplate.insert(getSqlName(SqlId.SQL_INSERT), entity);
    }


    public int delete(Map<String, Object> query) {
        return sqlSessionTemplate.delete(getSqlName(SqlId.SQL_DELETE), query);
    }


    public int deleteById(String id) {
        return sqlSessionTemplate.delete(getSqlName(SqlId.SQL_DELETE_BY_ID), id);
    }


    public int deleteById(int id) {
        return sqlSessionTemplate.delete(getSqlName(SqlId.SQL_DELETE_BY_ID), id);
    }


    public int update(T entity) {
        return sqlSessionTemplate.update(getSqlName(SqlId.SQL_UPDATE_BY_ID), entity);
    }


    public int selectCount(Map<String, Object> query) {

        return sqlSessionTemplate.selectOne(getSqlName(SqlId.SQL_SELECT_COUNT), query);
    }


    public <K, V> Map<K, V> selectMap(Map<String, Object> query, String mapKey) {

        return sqlSessionTemplate.selectMap(getSqlName(SqlId.SQL_MAP), query, mapKey);
    }


    public int batchInsert(List<T> list) {
        return sqlSessionTemplate.insert(getSqlName(SqlId.Batch_Insert), list);
    }


    public int batchUpdate(List<T> list) {
        return sqlSessionTemplate.insert(getSqlName(SqlId.Batch_Update), list);
    }

}
