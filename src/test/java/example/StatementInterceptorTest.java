package example;

import java.io.IOException;
import java.sql.*;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.junit.*;

public class StatementInterceptorTest {
    // use other PORT to avoid conflict with system MySQL service
    private static final int PORT = 3307;
    private static final String DB_NAME = "test";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private MysqlDataSource dataSource;
    private String connectionURL;
    private DB db;

    @Before
    public void setUp() throws ManagedProcessException {
        final DBConfigurationBuilder builder = DBConfigurationBuilder.newBuilder().setPort(PORT);
        dataSource = new MysqlDataSource();

        connectionURL = builder.getURL(DB_NAME) + "?statementInterceptors=example.Interceptor";

        db = DB.newEmbeddedDB(builder.build());
        db.start();
        db.run("CREATE TABLE test.test (id INT PRIMARY KEY, name VARCHAR(200))");
    }

    @After
    public void tearDown() throws ManagedProcessException, IOException {
        db.stop();
    }

    @Test
    public void testWithoutRewriteBatchedStatements() throws SQLException {
        dataSource.setUrl(connectionURL + "&rewriteBatchedStatements=false");
        executeBatchQuery();
    }

    @Test
    public void testWithRewriteBatchedStatements() throws SQLException {
        dataSource.setUrl(connectionURL + "&rewriteBatchedStatements=true");
        executeBatchQuery();
    }

    private void executeBatchQuery() throws SQLException {
        final String sql = "INSERT INTO test.test (id, name) VALUES (?, ?)";
        try (
                Connection conn = dataSource.getConnection(USER, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            for (int i = 0; i < 10; i++) {
                ps.setInt(1, i);
                ps.setString(2, "name" + i);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
