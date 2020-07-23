package ru.netology.data;

import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
    private DataHelper() throws SQLException {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }


    public static String getVerificationCode() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/app";
        String user = "app";
        String password = "pass";
        val verificationCode = "SELECT code FROM auth_codes WHERE created = (SELECT MAX(created) FROM auth_codes);";
        try ( val conn = DriverManager.getConnection(url, user, password);
                val countStmt = conn.createStatement();)
         {
            try (val rs = countStmt.executeQuery(verificationCode)) {
                if (rs.next()) {
                    val code = rs.getString("code");
                    return code;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void cleanData() throws SQLException {
        val runner = new QueryRunner();
        val codes = "DELETE FROM auth_codes";
        val cards = "DELETE FROM cards";
        val users = "DELETE FROM users";
        String url = "jdbc:mysql://localhost:3306/app";
        String user = "app";
        String password = "pass";
        val conn = DriverManager.getConnection(url, user, password);

        try (conn) {
            runner.update(conn, codes);
            runner.update(conn, cards);
            runner.update(conn, users);
        }
    }
}
