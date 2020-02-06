package tool.db.druid;

import tool.db.XWHResultSetMapper;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;

public class DBQuery implements Closeable
{
    private DBPoolConnection db;
    private Connection conn;

    public DBQuery(XWHResultSetMapper mapper)
    {
        db = mapper.getPool();
        conn = mapper.openConnection();
    }

    /**
     * ��ѯ�ӿ�
     *
     * @param SQL sql
     * @return �����
     */
    public ResultSet executeQuery(String SQL)
    {
        return db.executeQuery(conn, SQL);
    }

    @Override
    public void close()
    {
        if (db != null && conn != null)
        {
            db.closeConnection(conn);
        }
    }
}
