package org.example.util;

import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public class JdbcUtil {
  private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String DB_USER = "postgres";
  private static final String DB_PASSWORD = "root";

  public static DataSource createPostgresDataSource() {
    PGSimpleDataSource dataSource = new PGSimpleDataSource();
    dataSource.setUrl(DB_URL);
    dataSource.setUser(DB_USER);
    dataSource.setPassword(DB_PASSWORD);
    return dataSource;
  }
}
