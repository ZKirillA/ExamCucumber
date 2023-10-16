package apiSteps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Затем;
import io.cucumber.java.ru.И;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import io.qameta.allure.Step;
import static apiSteps.ApiBaseSteps.*;

public class ApiMortySteps {

    public static String lastEpisode;
    public static String mortyname;
    public static String mortylocation;
    public static String mortyrace;
    public static String charId;
    public static String lastCharacterName;
    public static String lastCharacterrace;
    public static String lastCharacterNum;
    public static String lastCharacterLocation;

    @Step ("Находим информацию по персонажу Морти Смит")
    @Дано("^Получаем информацию о Морти$")
    public static void mortyInformation(String id) {
        Response response1 = getResponseCharacter(id);
        mortyname = parseResponse(response1, "name");
        mortylocation = parseResponse(response1, "location", "name");
        mortyrace = parseResponse(response1, "species");
        charId = id;
    }

    @Step ("Выбираем последний эпизод, где появился Морти")
    @Затем("Получаем информацию о последнем эпизоде с Морти")
    public static void lastEpisode() {
        Response response2 = getResponseCharacter(charId);
        int jsonSize1 = new JSONObject(response2.asString()).getJSONArray("episode").length();
        lastEpisode = parseResponseWithJsonArray(response2, "episode")
                .get(jsonSize1 - 1).toString().replaceAll("[^0-9]", "");
    }

    @Step ("Получаем из списка последнего эпизода последнего персонажа")
    @Затем("Получаем индекс последнего персонажа")
    public static void getLastCharacterNum() {
        Response response3 = getResponseEpisode(lastEpisode);
        int lastCharacterIndex = (parseResponseWithJsonArray(response3, "characters").length() - 1);
        lastCharacterNum = parseResponseWithJsonArray(response3, "characters")
                .get(lastCharacterIndex).toString().replaceAll("[^0-9]", "");
    }


    @Step ("Получаем информацию по локации и расе последнего персонажа")
    @Затем("Получаем информацию о последнем персонаже")
    public static void getLastCharacterInfo() {
        Response response4 = getResponseCharacter(lastCharacterNum);
        lastCharacterName = parseResponse(response4, "name");
        lastCharacterLocation = parseResponse(response4, "location", "name");
        lastCharacterrace = parseResponse(response4, "species");

    }

    @Step ("Сравниваем расу последнего персонажа с расой Морти")
    @И("Сравниваем расу персонажей")
    public static void checkRace() {
        Assertions.assertEquals(mortyrace, lastCharacterrace, "Расы персонажей не совпадают");
    }

    @Step ("Сравниваем локацию последнего персонажа с локацией Морти")
    @И("Сравниваем местоположение персонажей")
    public static void checkLocation() {
        Assertions.assertNotEquals(mortylocation, lastCharacterLocation, "Локации персонажей не совпадают");
    }
}
