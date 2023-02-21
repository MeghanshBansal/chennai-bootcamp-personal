package main.com.playground.service;

import main.com.playground.domain.User;
import main.com.playground.repository.PostgresConn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUpService {
    private User user;

    public SignUpService(User user) {
        this.user = user;
    }

    public User save() throws SQLException, ClassNotFoundException {
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();

        Statement stmt = null;
        stmt = conn.createStatement();
        String sql = "Insert into users (name, email)" + " Values ('%s', '%s');";
        System.out.println(String.format(sql, user.getName(), user.getEmail()));
        String execute = String.format(sql, user.getName(), user.getEmail());
        stmt.executeUpdate(execute);
        stmt.close();
        conn.close();
        return user;
    }
}
