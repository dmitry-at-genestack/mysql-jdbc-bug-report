Test for reproducing bug in [MySQL Connector/J](https://github.com/mysql/mysql-connector-j).

Contains 2 tests, each doing batch insert of multiple (int, string) pairs in a MySQL table.
Failing test uses `rewriteBatchedStatements=true` JDBC connection parameter, passing test sets `rewriteBatchedStatements=false`.
 
Test uses [embedded MariaDB](https://github.com/vorburger/MariaDB4j), 
but this problem also happens with Oracle MySQL 5.7.13.

[MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) version 5.1.40 (latest GA) used.

Run `mvn clean test` to get the following exception:
```
java.sql.BatchUpdateException: Unexpected exception encountered during query.
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423)
	at com.mysql.jdbc.Util.handleNewInstance(Util.java:425)
	at com.mysql.jdbc.Util.getInstance(Util.java:408)
	at com.mysql.jdbc.SQLError.createBatchUpdateException(SQLError.java:1162)
	at com.mysql.jdbc.PreparedStatement.executeBatchedInserts(PreparedStatement.java:1582)
	at com.mysql.jdbc.PreparedStatement.executeBatchInternal(PreparedStatement.java:1248)
	at com.mysql.jdbc.StatementImpl.executeBatch(StatementImpl.java:958)
	at example.StatementInterceptorTest.executeBatchQuery(StatementInterceptorTest.java:63)
	at example.StatementInterceptorTest.testWithRewriteBatchedStatements(StatementInterceptorTest.java:49)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:27)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.apache.maven.surefire.junit4.JUnit4Provider.execute(JUnit4Provider.java:252)
	at org.apache.maven.surefire.junit4.JUnit4Provider.executeTestSet(JUnit4Provider.java:141)
	at org.apache.maven.surefire.junit4.JUnit4Provider.invoke(JUnit4Provider.java:112)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.apache.maven.surefire.util.ReflectionUtils.invokeMethodWithArray(ReflectionUtils.java:189)
	at org.apache.maven.surefire.booter.ProviderFactory$ProviderProxy.invoke(ProviderFactory.java:165)
	at org.apache.maven.surefire.booter.ProviderFactory.invokeProvider(ProviderFactory.java:85)
	at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:115)
	at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:75)
Caused by: java.sql.SQLException: Unexpected exception encountered during query.
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:964)
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:897)
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:886)
	at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:860)
	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2582)
	at com.mysql.jdbc.PreparedStatement.executeInternal(PreparedStatement.java:1861)
	at com.mysql.jdbc.PreparedStatement.executeUpdateInternal(PreparedStatement.java:2073)
	at com.mysql.jdbc.PreparedStatement.executeUpdateInternal(PreparedStatement.java:2009)
	at com.mysql.jdbc.PreparedStatement.executeLargeUpdate(PreparedStatement.java:5098)
	at com.mysql.jdbc.PreparedStatement.executeBatchedInserts(PreparedStatement.java:1543)
	... 35 more
Caused by: java.lang.NullPointerException
	at java.util.concurrent.ConcurrentHashMap.get(ConcurrentHashMap.java:936)
	at com.mysql.jdbc.StringUtils.findCharset(StringUtils.java:124)
	at com.mysql.jdbc.StringUtils.toString(StringUtils.java:2199)
	at com.mysql.jdbc.PreparedStatement$ParseInfo.getSqlForBatch(PreparedStatement.java:484)
	at com.mysql.jdbc.PreparedStatement.getPreparedSql(PreparedStatement.java:5049)
	at example.Interceptor.preProcess(Interceptor.java:26)
	at com.mysql.jdbc.MysqlIO.invokeStatementInterceptorsPre(MysqlIO.java:2859)
	at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2580)
	at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2549)
	... 40 more
```
