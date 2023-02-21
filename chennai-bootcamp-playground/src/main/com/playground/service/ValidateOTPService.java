package main.com.playground.service;

import main.com.playground.domain.OTP;
import main.com.playground.repository.PostgresConn;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class ValidateOTPService {
    int otp;
    String email;

    public ValidateOTPService(int otp, String email) {
        this.otp = otp;
        this.email = email;
    }

    public int validateOTP() throws SQLException, ClassNotFoundException {
        OTP message = new OTP(this.email);
        return message.validate(this.otp);
    }

    public String getAuthToken() {
        Random random = new Random();
        String randomVariance = Integer.toString(100 + random.nextInt(900));

        String md5Hex = DigestUtils.md5Hex(this.email).toUpperCase();
        return md5Hex + randomVariance;
    }

    public void saveAuthToken(String token, int id) throws ClassNotFoundException, SQLException {
        PostgresConn connection = new PostgresConn();
        Connection conn = connection.connect();
        String sql = "update users set token ='" + token + "'" + "where email ='" + this.email + "'" + "and id=" + id;
        System.out.println(String.format(sql, token, this.email, id));
        String execute = String.format(sql, token, this.email, id);
        Statement stmt = null;
        stmt = conn.createStatement();
        stmt.executeUpdate(execute);
        stmt.close();
        conn.close();
    }
}
