package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.delivery.data.DataGenerator.Registration.generateUser;

class CardDeliveryTest {
    @BeforeEach
    void setup() { open("http://localhost:9999"); }
    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessPlanMeeting() {
        DataGenerator.UserInfo validUser = generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id=replan-notification] .notification__title")
                .shouldHave(exactText("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] .notification__content")
                .shouldHave(exactText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        //Ответ Идеи в переводе: Элемент должен иметь точный текст «У вас уже запланирована встреча по другому адресу».
        // Перепланировать? {[data-test-id=replan-notification] .notification__content}
        //Элемент: '<div class="notification__content">У вас уже запланирована встреча на другом домене. Перепланировать?
        //Перепланировать</div>'
        //$("[data-test-id=notification__content")  Element not found {[data-test-id=notification__content}
        //Expected: exact text 'У вас уже ...
        //        .shouldHave(exactText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button.button").click();
        //$("[data-test-id=replan-notification] .button").click();
        //$("[data-test-id=replan-notification] .button__text").shouldHave(exactText("Перепланировать")).click();
        //$(byText("Перепланировать")).click();
        $("[data-test-id=success-notification] .notification__title")
                .shouldHave(exactText("Успешно!"));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
