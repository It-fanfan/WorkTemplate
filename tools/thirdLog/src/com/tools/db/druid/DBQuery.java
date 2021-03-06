package com.tools.db.druid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;

import com.tools.db.BaseResultSetMapper;

public class DBQuery implements Closeable {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBQuery.class);
    private DBPoolConnection db;
    private Connection conn;

    public DBQuery(BaseResultSetMapper mapper) {
        db = mapper.getPool();
        conn = mapper.openConnection();
    }

    /**
     * 查询接口
     *
     * @param SQL sql
     * @return 结果集
     */
    public ResultSet executeQuery(String SQL) {
        LOGGER.debug(SQL);
        return db.executeQuery(conn, SQL);
    }

    @Override
    public void close() {
        if (db != null && conn != null) {
            db.closeConnection(conn);
        }
    }
}
