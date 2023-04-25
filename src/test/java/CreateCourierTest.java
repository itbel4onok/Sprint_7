import base.BaseCourierTest;
import constants.CourierFields;
import constants.ErrorMessage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@Feature("Create courier - POST /api/v1/courier")
public class CreateCourierTest extends BaseCourierTest {
    @Test
    @DisplayName("Send correct POST request to /api/v1/courier")
    @Description("Happy path for /api/v1/courier")
    public void createCourierHappyPathTest() {
        generateCourierData();
        Response response = courierAction.postRequestCreateCourier(courierCard);
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(SC_CREATED);
        deleteTestCourier();
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier twice")
    @Description("Impossible to create the same Couriers twice")
    public void createCourierTwiceTest(){
        generateCourierData();
        courierAction.postRequestCreateCourier(courierCard);
        Response response = courierAction.postRequestCreateCourier(courierCard);
        response.then().assertThat().body("message", equalTo(ErrorMessage.EXIST_LOGIN))
                .and().statusCode(SC_CONFLICT);
        deleteTestCourier();
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier with login null value")
    @Description("Impossible to create courier without login value")
    public void createCourierLoginNullValueTest(){
        generateCustomCourierData(CourierFields.PASSWORD, CourierFields.FIRST_NAME);
        Response response = courierAction.postRequestCreateCourier(courierCard);
        response.then().assertThat().body("message",
                        equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_CREATE))
               .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier with login empty value")
    @Description("Impossible to create courier without login value")
    public void createCourierLoginEmptyValueTest() {
        generateCourierData();
        courierCard.setLogin("");
        Response response = courierAction.postRequestCreateCourier(courierCard);
        response.then().assertThat().body("message",
                        equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_CREATE))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier with password null value")
    @Description("Impossible to create courier without password")
    public void createCourierPassNullValueTest(){
        generateCustomCourierData(CourierFields.LOGIN, CourierFields.FIRST_NAME);
        Response response = courierAction.postRequestCreateCourier(courierCard);
        response.then().assertThat().body("message",
                        equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_CREATE))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier with password null value")
    @Description("Impossible to create courier without password")
    public void createCourierPassEmptyValueTest() {
        generateCourierData();
        courierCard.setPassword("");
        Response response = courierAction.postRequestCreateCourier(courierCard);
        response.then().assertThat().body("message",
                        equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_CREATE))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier with firstName null value")
    @Description("Possible to create courier without firstName")
    public void createCourierNameNullValueTest(){
        generateCustomCourierData(CourierFields.LOGIN, CourierFields.PASSWORD);
        Response response = courierAction.postRequestCreateCourier(courierCard);
        response.then().assertThat().body("ok", equalTo(true))
                    .and().statusCode(SC_CREATED);
        deleteTestCourier();
    }
}
