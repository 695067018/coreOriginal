package com.sug.core.database.mybatis.typeHandler;

import com.sug.core.platform.json.JSONBinder;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2017/10/21.
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(HashMap.class)
public class SimpleStringMapHandler extends BaseTypeHandler<HashMap<String,String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, HashMap<String, String> maps, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,JSONBinder.binder(HashMap.class).toJSON(maps));
    }

    @Override
    public HashMap<String, String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String result = resultSet.getString(s);
        return Objects.isNull(result) || result.length() == 0? null : JSONBinder.binder(HashMap.class).fromJSON(result);
    }

    @Override
    public HashMap<String, String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        return Objects.isNull(result) || result.length() == 0? null : JSONBinder.binder(HashMap.class).fromJSON(result);
    }

    @Override
    public HashMap<String, String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        return Objects.isNull(result) || result.length() == 0? null : JSONBinder.binder(HashMap.class).fromJSON(result);
    }
}
