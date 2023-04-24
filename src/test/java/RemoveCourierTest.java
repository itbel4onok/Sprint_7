import actions.CourierAction;
import actions.GenerateCourierData;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Feature("Remove courier - DELETE /api/v1/courier/:id")
public class RemoveCourierTest {
    private CourierCard courierCard;
    private CourierAction courierAction;
    private boolean clearTestDataFlag = true;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        GenerateCourierData generateCourierData = new GenerateCourierData();
        generateCourierData.generateLoginPass();
        courierCard = new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword());
        courierAction = new CourierAction(courierCard);
        courierAction.postRequestCreateCourierByCard();
    }

    @Test
    @DisplayName("Send correct DELETE request to /api/v1/courier/:id")
    @Description("Happy path for /api/v1/courier/:id")
    public void removeCourierHappyPathTest() {
        Response response = courierAction.deleteRequestRemoveCourier();
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(200);
        clearTestDataFlag = false;
    }

    @Test
    @DisplayName("Send DELETE request to /api/v1/courier/:id without id")
    @Description("Impossible to remove courier without sending id")
    public void removeCourierWithoutIdTest(){
        Response response =
                given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/:id");
        response.then().assertThat().body("message", equalTo(ErrorMessage.INCORRECT_REMOVE_REQUEST))
                .and().statusCode(500);
    }

    @Test
    @DisplayName("Send DELETE request to /api/v1/courier/:id with random id")
    @Description("Impossible to remove courier wit incorrect id")
    public void removeCourierIncorrectIdTest(){
        Response response = courierAction.deleteRequestRemoveCourierById(CourierFields.INCORRECT_ID_FOR_REMOVE);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_FOUND_ID_FOR_REMOVE))
                .and().statusCode(404);
    }

    @After
    public void removeTestData() {
        if (clearTestDataFlag) {
            courierAction.deleteRequestRemoveCourier();
        }
    }
}
