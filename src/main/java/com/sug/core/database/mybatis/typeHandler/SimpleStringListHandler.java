package com.sug.core.database.mybatis.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/10/21.
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(java.util.List.class)
public class SimpleStringListHandler extends BaseTypeHandler<List<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> list, JdbcType jdbcType) throws SQLException {
        StringBuilder buffer = new StringBuilder();
        list.forEach(s->{
            buffer.append(s);
            buffer.append(",");
        });
        buffer.deleteCharAt(buffer.length() - 1);
        preparedStatement.setString(i,buffer.toString());
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String result = resultSet.getString(s);
        return Objects.isNull(result) || result.length() == 0? null : Arrays.asList(result.split(","));
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        return Objects.isNull(result) || result.length() == 0? null : Arrays.asList(result.split(","));
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        return Objects.isNull(result) || result.length() == 0? null : Arrays.asList(result.split(","));
    }
}
