package com.sug.core.database.mybatis.constants;

/**
 * Created by greg.chen on 14-9-16.
 */
public interface SqlId {
    public String SQL_SELECT_COUNT = "selectCount";
    public String SQL_SELECT = "select";
    public String SQL_SELECT_BY_ID = "selectById";
    public String SQL_UPDATE_BY_ID = "updateById";
    public String SQL_UPDATE_BY_ID_SELECTIVE = "updateByIdSelective";
    public String SQL_DELETE = "delete";
    public String SQL_DELETE_BY_ID = "deleteById";
    public String SQL_INSERT = "insert";
    public String SQL_MAP = "selectMap";
    public String SQLNAME_SEPARATOR = ".";
    public String Batch_Insert = "batchInsert";
    public String Batch_Update = "batchUpdate";
}
