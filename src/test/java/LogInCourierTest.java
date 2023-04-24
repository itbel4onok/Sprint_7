import actions.*;
import constants.CourierFields;
import constants.ErrorMessage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import resources.CourierCard;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Log in courier - POST /api/v1/courier/login")
public class LogInCourierTest {
    private CourierAction courierAction;
    private CourierCard courierCard;
    private boolean clearTestDataFlag = true;
    private String initialValue = "";
    private GenerateCourierData generateCourierData = new GenerateCourierData();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        generateCourierData.generateLoginPass();
        courierCard = new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword());
        courierAction = new CourierAction(courierCard);
    }

    @Test
    @DisplayName("Send correct POST request to /api/v1/courier/login")
    @Description("Happy path for /api/v1/courier/login")
    public void logInCourierHappyPathTest() {
        courierAction.postRequestCreateCourierByCard();
        Response response = courierAction.postRequestLogInByCard();
        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login with changed password")
    @Description("Impossible to log in with changed password")
    public void logInCourierChangedPasswordTest() {
        courierAction.postRequestCreateCourierByCard();
        initialValue = courierCard.getPassword();
        courierCard.setPassword(CourierFields.RANDOM_PASSWORD);
        Response response = courierAction.postRequestLogInByCard();
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_FOUND_DATA_FOR_LOG_IN))
                .and().statusCode(404);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login with incorrect/non-created courier data")
    @Description("Impossible to log in with incorrect/non-created couriers data")
    public void logInCourierIncorrectDataTest() {
        Response response = courierAction.postRequestLogInByCard();
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_FOUND_DATA_FOR_LOG_IN))
                .and().statusCode(404);
        clearTestDataFlag = false;
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login with only login")
    @Description("Impossible to log in without password")
    public void logInCourierMissedPasswordFieldTest() {
        courierAction.postRequestCreateCourierByCard();
        String json = generateCourierData.generateCustomJson(CourierFields.LOGIN);
        Response response = courierAction.postRequestLogInByJson(json);
        response.then().assertThat().body(equalTo(ErrorMessage.SERVICE_UNAVAILABLE))
                .and().statusCode(504);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login with only password")
    @Description("Impossible to log in without login")
    public void logInCourierMissedLoginFieldTest() {
        courierAction.postRequestCreateCourierByCard();
        String json = generateCourierData.generateCustomJson(CourierFields.PASSWORD);
        Response response = courierAction.postRequestLogInByJson(json);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_LOG_IN))
                .and().statusCode(400);
        courierAction.postRequestLogInByCard();
    }

    @After
    public void removeTestData() {
        if(!(initialValue.isEmpty())) {
            courierCard.setPassword(initialValue);
        }
        if(clearTestDataFlag) {
            courierAction.deleteRequestRemoveCourier();
        }
    }
}
