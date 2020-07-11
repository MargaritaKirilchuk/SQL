package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import java.sql.DriverManager;
import java.sql.SQLException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:7777");
    }

    @Test
    void shouldSubmitValidUser() throws SQLException {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        String verificationCode = DataHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
        $(byText("Личный кабинет")).waitUntil(Condition.visible, 15000);
    }

    @Test
    void shouldNotSubmitInvalidLogin() {
        val loginPage = new LoginPage();
        val authInfo = new DataHelper.AuthInfo("petya", "qwerty123");
        loginPage.invalidLogin(authInfo);
    }

    @Test
    void shouldNotSubmitEmptyLogin() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        loginPage.emptyLogin(authInfo);
    }

    @Test
    void shouldNotSubmitInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        loginPage.invalidPassword(authInfo);

    }

    @Test
    void shouldNotSubmitEmptyPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        loginPage.emptyPassword(authInfo);
    }

    @Test
    void shouldNotSubmitInvalidCode() throws SQLException {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = "98765";
        verificationPage.invalidVerify(verificationCode);
    }

    @AfterAll
    public static void cleanData() throws SQLException {
        val runner = new QueryRunner();
        val codes = "DELETE FROM auth_codes";
        val cards = "DELETE FROM cards";
        val users = "DELETE FROM users";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                )
        ) {
            runner.update(conn, codes);
            runner.update(conn, cards);
            runner.update(conn, users);
        }
    }
}
