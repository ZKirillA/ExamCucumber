package apiSteps;

import io.cucumber.java.ru.Затем;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static apiSteps.ApiBaseSteps.getResponsePotato;

import io.qameta.allure.Step;

public class ApiPotatoSteps {

    private static JSONObject respJson;
    @Step("Создаем запрос для пользователя, меняем имя, добавляем поле job")
    @Затем("^Отправка запроса на регресc и проверка ответа на валидность данных$")
    public static void sendPostRequest() throws IOException {

        JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/.json"))));

        jsonObject.put("name", "Tomato");
        jsonObject.put("job", "Eat maket");

        respJson = new JSONObject(getResponsePotato(jsonObject).getBody().asString());
    }

    @Step("Сравниваем имя")
    public static void checkName() {
        Assertions.assertEquals(respJson.getString("name"), "Tomato");
    }

    @Step("Сравниваем работу")
    public static void checkJob() {
        Assertions.assertEquals(respJson.getString("job"), "Eat maket");
    }

    @Step("Сравниваем id")
    public static void checkId() {
        Assertions.assertNotNull(respJson.getString("id"));
    }
}

