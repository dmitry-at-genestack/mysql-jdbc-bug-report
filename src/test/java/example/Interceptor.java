/*
 * Copyright (c) 2011-2016 Genestack Limited
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF GENESTACK LIMITED
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */


package example;

import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.*;

public class Interceptor implements StatementInterceptorV2 {
    @Override
    public void init(Connection conn, Properties props) throws SQLException {
        /* no-op */
    }

    @Override
    public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection) throws SQLException {
        if (interceptedStatement instanceof PreparedStatement) {
            String preparedSql = ((PreparedStatement) interceptedStatement).getPreparedSql();
            System.out.println(preparedSql);
        }
        return null;
    }

    @Override
    public boolean executeTopLevelOnly() {
        return false;
    }

    @Override
    public void destroy() {
        /* no-op */
    }

    @Override
    public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed, boolean noGoodIndexUsed, SQLException statementException) throws SQLException {
        return null;
    }
}
