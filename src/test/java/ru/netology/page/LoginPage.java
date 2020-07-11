package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public VerificationPage validLogin (DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();
        return new VerificationPage();
    }

    public void invalidLogin (DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();
        $("[data-test-id=error-notification]").shouldBe(Condition.visible);
    }

    public void emptyLogin(DataHelper.AuthInfo info) {
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();
        SelenideElement login = $("[data-test-id=login]");
        login.$("[class='input__sub']").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    public void invalidPassword(DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=password] input").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id=password] input").sendKeys("qwerty12345");
        $("[data-test-id=action-login]").click();
    }

    public void emptyPassword(DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=action-login]").click();
        SelenideElement password = $("[data-test-id=password]");
        password.$("[class='input__sub']").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

}
