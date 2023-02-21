package main.com.playground.domain;

import main.com.playground.repository.PostgresConn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ToDo {
    String user;
    String message;

    public ToDo(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public List<String> search(String id, String message) throws ClassNotFoundException, SQLException {
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();
        List<String> todos = new ArrayList<>();
        Statement stmt = null;
        stmt = conn.createStatement();
        System.out.println(this.user);
        String sql = "Select * from todos where user_id=" + id + " and message like '%" + message + "%';";
        System.out.println(sql);
        System.out.println("---------------------------------------");
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("message"));
            todos.add(rs.getString("message"));
        }
        rs.close();
        stmt.close();
        conn.close();
        return todos;
    }

    public List<String> getMessage(String id) throws ClassNotFoundException, SQLException {
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();
        List<String> todos = new ArrayList<>();
        Statement stmt = null;
        stmt = conn.createStatement();
        System.out.println(this.user);
        String sql = "Select * from todos where id=" + id + ";";
        System.out.println(sql);
        System.out.println("---------------------------------------");
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("message"));
            todos.add(rs.getString("message"));
        }
        rs.close();
        stmt.close();
        conn.close();
        return todos;
    }

    public void save(int userId) throws ClassNotFoundException, SQLException {
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();
        String sql = "Insert into todos (user_name, message, user_id)" + " Values ('" + this.user + "'," + "'" + this.message + "'," + userId + ");";
        System.out.println(sql);
        Statement stmt = null;
        stmt = conn.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        conn.close();
    }
}
