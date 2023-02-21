package main.com.playground.domain;

import main.com.playground.repository.PostgresConn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Something
public class OTP {
    String email;

    public OTP(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public int generateOTP() throws SQLException, ClassNotFoundException {
        int otp = ((int) (Math.random() * 1000000)) % 10000;
        saveOTP(this.email, otp);
        return otp;
    }

    private void saveOTP(String email, int otp) throws ClassNotFoundException, SQLException {
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();
        Statement stmt = null;
        int userId = 0;
        String sqlUser = "Select * from users where email = '" + this.email + "'";
        System.out.println(sqlUser);
        System.out.println("---------------------------------------");
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sqlUser);
        while (rs.next()) {
            System.out.println(rs.getInt("id"));
            userId = rs.getInt("id");
        }
        String sql = "Insert into otps (email, otp, user_id)" + " Values ('%s', '%d', %d);";
        System.out.println(String.format(sql, email, otp, userId));
        String execute = String.format(sql, email, otp, userId);
        stmt = conn.createStatement();
        stmt.executeUpdate(execute);
        stmt.close();
        conn.close();
    }

    public int validate(int otp) throws ClassNotFoundException, SQLException {
        int userId = 0;
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();
        Statement stmt = null;
        stmt = conn.createStatement();
        System.out.println(this.email);
        String sql = "Select * from otps where email ='" + this.email + "'";
        System.out.println(sql);
        System.out.println("---------------------------------------");
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getInt("otp"));
            if (rs.getInt("otp") == otp) {
                userId = rs.getInt("user_id");
                System.out.println(userId);
                break;
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        return userId;
    }
}
