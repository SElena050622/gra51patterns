package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import ru.netology.delivery.data.DataGenerator;
import static ru.netology.delivery.data.DataGenerator.Registration.generateUser;
import static ru.netology.delivery.data.DataGenerator.Registration.*;
import com.codeborne.selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.*;
import org.jsoup.select.Selector;
import org.junit.jupiter.api.AfterEach;

import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class CardDeliveryTest {
    @BeforeEach
    void setup() { open("http://localhost:9999"); }
    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessPlanMeeting() {
        DataGenerator.UserInfo validUser = DataGenerator, Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").setKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName);
        $("[data-test-id=phone] input").setValue(validUser.getPhone);
        $("data-test-id=agreement").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $("[data-test-id=date] input").setKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input")setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
