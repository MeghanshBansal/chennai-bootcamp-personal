package main.com.playground.service;

import main.com.playground.domain.User;
import main.com.playground.repository.PostgresConn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class UserService {
    public List<User> getAllUsers() {
        return null;
    }

    public User getUser(String id) {
        return null;
    }

    public User createUser(String name, String email) {
        return null;
    }

    public User updateUser(String id, String name, String email) {
        return null;
    }

    public int getUserId(User user) throws SQLException, ClassNotFoundException {
        int id = 0;
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();
        Statement stmt = null;
        stmt = conn.createStatement();
        String sql = "Select * from users where name = '" + user.getName() + "'" + "and email = '" + user.getEmail() + "'";
        System.out.println(sql);
        System.out.println("---------------------------------------");
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt("id"));
            id = rs.getInt("id");
        }
        stmt.close();
        conn.close();
        System.out.println(user);
        return id;
    }

    public User getLoggedInUser(String token) {
        PostgresConn connection = new PostgresConn();
        Connection conn = null;
        try {
            conn = connection.connect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String sql = "select * from users where token = '" + token + "'";
        System.out.println(sql);

        Statement stmt = null;
        User user = new User("", "");
        try {
            stmt = conn.createStatement();
            ResultSet userResultSet = stmt.executeQuery(sql);
            if (userResultSet.next()) {
                user.setEmail(userResultSet.getString("email"));
                user.setName(userResultSet.getString("name"));
            }
            stmt.close();
            conn.close();
            System.out.println(user);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return user;
        }
    }

}
