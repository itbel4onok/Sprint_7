import actions.CourierAction;
import actions.GenerateCourierData;
import constants.CourierFields;
import constants.ErrorMessage;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import resources.CourierCard;

import static org.hamcrest.Matchers.equalTo;

@Feature("Log in courier - POST /api/v1/courier/login")
@DisplayName("Log in courier with empty value of field")
@RunWith(Parameterized.class)
public class LogInCourierEmptyValueTest {
    private String courierField;
    private String courierFieldValue;
    private CourierCard courierCard;
    private String loginValue;
    private String passwordValue;
    private CourierAction courierAction;

    public LogInCourierEmptyValueTest(String courierField, String courierFieldValue) {
        this.courierField = courierField;
        this.courierFieldValue = courierFieldValue;
    }

    @Parameterized.Parameters(name = "{1} value for field: {0}")
    public static Object[][] getCourierFieldValue() {
        return new Object[][] {
                { CourierFields.LOGIN, CourierFields.EMPTY_VALUE },
                { CourierFields.LOGIN, CourierFields.NULL_VALUE },
                { CourierFields.PASSWORD, CourierFields.EMPTY_VALUE }
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        GenerateCourierData generateCourierData = new GenerateCourierData();
        generateCourierData.generateLoginPass();
        loginValue = generateCourierData.getCourierLogin();
        passwordValue = generateCourierData.getCourierPassword();
        courierCard = new CourierCard(loginValue, passwordValue);
        courierAction = new CourierAction(courierCard);
        courierAction.postRequestCreateCourierByCard();
    }

    @Test
    public void logInCourierWithEmptyValueTest() {
        switch(courierField) {
            case CourierFields.LOGIN: courierCard.setLogin(courierFieldValue); break;
            case CourierFields.PASSWORD: courierCard.setPassword(courierFieldValue); break;
        }
        Response response = courierAction.postRequestLogInByCard();
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_LOG_IN))
                .and().statusCode(400);
    }

    @After
    public void removeTestData() {
        switch(courierField) {
            case CourierFields.LOGIN: courierCard.setLogin(loginValue); break;
            case CourierFields.PASSWORD: courierCard.setPassword(passwordValue); break;
        }
        courierAction.deleteRequestRemoveCourier();
    }
}
