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
import org.example.model.Organization;
import org.example.util.JdbcUtil;

public class OrganizationDal {
  private static final String INSERT_ORGANIZATION_SQL =
      "INSERT INTO organizations (address, name) VALUES (?, ?)";
  private static final String GET_ORGANIZATION_BY_ID_SQL = "SELECT * FROM organizations WHERE id = ?";
  private static final String DEL_ORGANIZATION_BY_ID_SQL = "DELETE FROM organizations WHERE id = ?";
  private static final String GET_ALL_SQL = "SELECT * FROM organizations";
  private static final String UPD_ORGANIZATION_SQL =
      "UPDATE organizations SET address = ?, name = ?  WHERE id = ?";
  private static final DataSource dataSource = JdbcUtil.createPostgresDataSource();

  public static Organization SaveOrganization(Organization organization) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement =
          connection.prepareStatement(INSERT_ORGANIZATION_SQL, Statement.RETURN_GENERATED_KEYS);
      statement.setString(1, organization.getAddress());
      statement.setString(2, organization.getName());
      statement.executeUpdate();
      ResultSet resultSet = statement.getGeneratedKeys();
      if (resultSet.next()) {
        organization.setId(resultSet.getLong("id"));
      }
    } catch (SQLException e) {
      throw new JdbcException("error during saving organization");
    }
    return organization;
  }

  public static Organization getOrganizationById(Long organizationId) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(GET_ORGANIZATION_BY_ID_SQL);
      statement.setLong(1, organizationId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return createOrganizationEntity(resultSet);
      } else {
        throw new SQLException();
      }

    } catch (SQLException e) {
      throw new JdbcException(String.format("organization with id:%s was not found", organizationId));
    }
  }

  public static List<Organization> getAll() {
    try (Connection connection = dataSource.getConnection()) {
      List<Organization> organizationList = new ArrayList<>();
      ResultSet resultSet = connection.createStatement().executeQuery(GET_ALL_SQL);
      while (resultSet.next()) {
        organizationList.add(createOrganizationEntity(resultSet));
      }
      return organizationList;
    } catch (SQLException e) {
      throw new JdbcException("error during getting all organizations");
    }
  }

  private static void updateOrganization(Long organizationId, Organization organization) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(UPD_ORGANIZATION_SQL);
      statement.setString(1, organization.getAddress());
      statement.setString(2, organization.getName());
      statement.setLong(3, organization.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new JdbcException(String.format("error during updating organization with id %s", organizationId));
    }
  }

  public static void deleteOrganization(Long organizationId) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement statement = connection.prepareStatement(DEL_ORGANIZATION_BY_ID_SQL);
      statement.setLong(1, organizationId);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new JdbcException(String.format("error during deleting organization with id %s", organizationId));
    }
  }

  private static Organization createOrganizationEntity(ResultSet resultSet) throws SQLException {
    return Organization.builder().id(resultSet.getLong("id"))
        .name(resultSet.getString("name"))
        .address(resultSet.getString("address"))
        .build();
  }
}
