package org.example.dao;

import org.example.model.User;
import java.util.List;
import org.example.util.Util;
import java.sql.*;
import java.util.ArrayList;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection conn = Util.getInstance().getConnection();
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() throws SQLException { //
        conn.setAutoCommit(false); //
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("create table if not exists table_users " +
                    "(id bigint primary key auto_increment, name char(255), last_name char(255), age int)");
            conn.commit(); //
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback(); //
        }
    }

    public void dropUsersTable() throws SQLException { //
        conn.setAutoCommit(false); //
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("drop table if exists table_users");
            conn.commit(); //
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback(); //
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        conn.setAutoCommit(false); //
        try (PreparedStatement pstm = conn.prepareStatement("insert into table_users (name, last_name, age) values (?, ?, ?)")) {
            pstm.setString(1, name);
            pstm.setString(2, lastName);
            pstm.setByte(3, age);
            pstm.executeUpdate();
            conn.commit(); //
        } catch (SQLException e) {
            e.printStackTrace();
            conn.rollback(); //
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement pstm = conn.prepareStatement("delete from table_users where id = ?")) {
            pstm.setLong(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (ResultSet resultSet = conn.createStatement().executeQuery("select * from table_users")) {
            while(resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("last_name"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                users.add(user);
            } //
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate("truncate table table_users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
