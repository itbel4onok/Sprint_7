import actions.*;
import constants.CourierFields;
import constants.ErrorMessage;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import resources.CourierCard;

import static org.hamcrest.Matchers.equalTo;

@Feature("Create courier - POST /api/v1/courier")
@DisplayName("Create courier with empty value of field")
@RunWith(Parameterized.class)
public class CreateCourierEmptyValueTest {
    private String courierField;
    private String courierFieldValue;
    private CourierCard courierCard;

    public CreateCourierEmptyValueTest(String courierField, String courierFieldValue) {
        this.courierField = courierField;
        this.courierFieldValue = courierFieldValue;
    }

    @Parameterized.Parameters(name = "{1} value for field: {0}")
    public static Object[][] getCourierFieldValue() {
        return new Object[][] {
                { CourierFields.LOGIN, CourierFields.EMPTY_VALUE },
                { CourierFields.LOGIN, CourierFields.NULL_VALUE },
                { CourierFields.PASSWORD, CourierFields.EMPTY_VALUE },
                { CourierFields.PASSWORD, CourierFields.NULL_VALUE },
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        GenerateCourierData generateCourierData = new GenerateCourierData();
        generateCourierData.generateLoginPassName();
        courierCard = new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword(),
                generateCourierData.getCourierFirstName());
    }

    @Test
    public void createCourierWithEmptyValueTest() {
        switch(courierField) {
            case CourierFields.LOGIN: courierCard.setLogin(courierFieldValue); break;
            case CourierFields.PASSWORD: courierCard.setPassword(courierFieldValue); break;
        }
        CourierAction courierAction = new CourierAction(courierCard);
        Response response = courierAction.postRequestCreateCourierByCard();
        response.then().assertThat().body("message",
                        equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_CREATE))
                .and().statusCode(400);
    }
}
