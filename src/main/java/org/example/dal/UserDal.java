package org.example.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.example.exception.JdbcException;
import org.example.model.User;
import org.example.util.JdbcUtil;

public class UserDal {
  private static final String INSERT_USER_SQL =
      "INSERT INTO users (first_name, last_name, email) VALUES (?, ?, ?)";
  private static final String GET_USER_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
  private static final String DEL_USER_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
  private static final String GET_ALL_SQL = "SELECT * FROM users";
  private static final String UPD_USER_SQL = "UPDATE users SET first_name = ?,last_name = ?, email = ? WHERE id = ?";
  private static final DataSource dataSource = JdbcUtil.createPostgresDataSource();

  public static User createUser(User user) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, user.getFirstName());
      statement.setString(2, user.getLastName());
      statement.setString(3, user.getEmail());
      statement.executeUpdate();
      ResultSet resultSet = statement.getGeneratedKeys();
      if (resultSet.next()) {
        user.setId(resultSet.getLong("id"));
      }
    } catch (SQLException e) {
      throw new JdbcException("error during saving user");
    }
    return user;
  }

  public static User getUserById(Long userId) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_SQL);
      statement.setLong(1, userId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return createUserEntity(resultSet);
      } else {
        throw new SQLException();
      }

    } catch (SQLException e) {
      throw new JdbcException(String.format("user with id:%s was not found", userId));
    }
  }

  public static List<User> getAll() {
    try (Connection connection = dataSource.getConnection()) {
      List<User> userList = new ArrayList<>();
      ResultSet resultSet = connection.createStatement().executeQuery(GET_ALL_SQL);
      while (resultSet.next()) {
        userList.add(createUserEntity(resultSet));
      }
      return userList;
    } catch (SQLException e) {
      throw new JdbcException("error during getting all users");
    }
  }

  private static void updateUser(Long userId, User user) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(UPD_USER_SQL);
      statement.setString(1, user.getFirstName());
      statement.setString(2, user.getLastName());
      statement.setString(3, user.getEmail());
      statement.setLong(4, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public static void deleteUser(Long userId) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(DEL_USER_BY_ID_SQL);
      statement.setLong(1, userId);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new JdbcException(String.format("error during deleting user with id %s", userId));
    }
  }

  private static User createUserEntity(ResultSet resultSet) throws SQLException {
    return User.builder().id(resultSet.getLong("id"))
        .firstName(resultSet.getString("first_name"))
        .lastName(resultSet.getString("last_name"))
        .email(resultSet.getString("email")).build();
  }
}
