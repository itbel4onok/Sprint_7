import base.BaseCourierTest;
import constants.CourierFields;
import constants.ErrorMessage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Log in courier - POST /api/v1/courier/login")
public class LogInCourierTest extends BaseCourierTest {
    @Test
    @DisplayName("Send correct POST request to /api/v1/courier/login")
    @Description("Happy path for /api/v1/courier/login")
    public void logInCourierHappyPathTest() {
        createNewTestCourier();
        Response response = courierAction.postRequestLogIn(courierCard);
        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(SC_OK);
        deleteTestCourier();
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login with changed password")
    @Description("Impossible to log in with changed password")
    public void logInCourierChangedPasswordTest() {
        createNewTestCourier();
        String initialValue = courierCard.getPassword();
        courierCard.setPassword(CourierFields.RANDOM_PASSWORD);
        Response response = courierAction.postRequestLogIn(courierCard);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_FOUND_DATA_FOR_LOG_IN))
                .and().statusCode(SC_NOT_FOUND);
        removeTestDataWithChangedPass(initialValue);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login with incorrect/non-created courier data")
    @Description("Impossible to log in with incorrect/non-created couriers data")
    public void logInCourierIncorrectDataTest() {
        generateCourierData();
        Response response = courierAction.postRequestLogIn(courierCard);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_FOUND_DATA_FOR_LOG_IN))
                .and().statusCode(SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login password null value")
    @Description("Impossible to log in without password")
    public void logInCourierPassNullValueTest() {
        createNewTestCourier();
        String initialValue = courierCard.getPassword();
        courierCard.setPassword(CourierFields.NULL_VALUE);
        Response response = courierAction.postRequestLogIn(courierCard);
        response.then().assertThat().body(equalTo(ErrorMessage.SERVICE_UNAVAILABLE))
                .and().statusCode(SC_GATEWAY_TIMEOUT);
        removeTestDataWithChangedPass(initialValue);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login password empty value")
    @Description("Impossible to log in without password")
    public void logInCourierPassEmptyValueTest() {
        createNewTestCourier();
        String initialValue = courierCard.getPassword();
        courierCard.setPassword(CourierFields.EMPTY_VALUE);
        Response response = courierAction.postRequestLogIn(courierCard);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_LOG_IN))
                .and().statusCode(SC_BAD_REQUEST);
        removeTestDataWithChangedPass(initialValue);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login login null value")
    @Description("Impossible to log in without login")
    public void logInCourierLoginNullValueTest() {
        createNewTestCourier();
        String initialValue = courierCard.getLogin();
        courierCard.setLogin(CourierFields.NULL_VALUE);
        Response response = courierAction.postRequestLogIn(courierCard);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_LOG_IN))
                .and().statusCode(SC_BAD_REQUEST);
        removeTestDataWithChangedLogin(initialValue);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier/login login empty value")
    @Description("Impossible to log in without login")
    public void logInCourierLoginEmptyValueTest() {
        createNewTestCourier();
        String initialValue = courierCard.getLogin();
        courierCard.setLogin(CourierFields.EMPTY_VALUE);
        Response response = courierAction.postRequestLogIn(courierCard);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_LOG_IN))
                .and().statusCode(SC_BAD_REQUEST);
        removeTestDataWithChangedLogin(initialValue);
    }

    @Step("Remove created test-data")
    public void removeTestDataWithChangedPass(String initialValue) {
        courierCard.setPassword(initialValue);
        deleteTestCourier();
    }

    @Step("Remove created test-data")
    public void removeTestDataWithChangedLogin(String initialValue) {
        courierCard.setLogin(initialValue);
        deleteTestCourier();
    }
}
