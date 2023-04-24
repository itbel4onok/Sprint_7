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

import static org.hamcrest.Matchers.equalTo;

@Feature("Create courier - POST /api/v1/courier")
public class CreateCourierTest {
    private GenerateCourierData generateCourierData;
    private boolean clearTestDataFlag = true;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        generateCourierData = new GenerateCourierData();
        generateCourierData.generateLoginPassName();
    }

    @Test
    @DisplayName("Send correct POST request to /api/v1/courier")
    @Description("Happy path for /api/v1/courier")
    public void createCourierHappyPathTest() {
        CourierAction courierAction = new CourierAction(generateCourierData());
        Response response = courierAction.postRequestCreateCourierByCard();
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(201);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier twice")
    @Description("Impossible to create the same Couriers twice")
    public void createCourierTwiceTest(){
        CourierAction courierAction = new CourierAction(generateCourierData());
        courierAction.postRequestCreateCourierByCard();
        Response response = courierAction.postRequestCreateCourierByCard();
        response.then().assertThat().body("message", equalTo(ErrorMessage.EXIST_LOGIN))
                .and().statusCode(409);
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier without login")
    @Description("Impossible to create courier without login")
    public void createCourierMissedLoginFieldTest(){
        CourierAction courierAction = new CourierAction();
        String json = generateCourierData.generateCustomJson(CourierFields.PASSWORD, CourierFields.FIRST_NAME);
        Response response = courierAction.postRequestCreateCourierByJson(json);
        response.then().assertThat().body("message",
                        equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_CREATE))
               .and().statusCode(400);
        clearTestDataFlag = false;
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier without password")
    @Description("Impossible to create courier without password")
    public void createCourierMissedPasswordFieldTest(){
        CourierAction courierAction = new CourierAction();
        String json = generateCourierData.generateCustomJson(CourierFields.LOGIN, CourierFields.FIRST_NAME);
        Response response = courierAction.postRequestCreateCourierByJson(json);
        response.then().assertThat().body("message",
                        equalTo(ErrorMessage.NOT_ENOUGH_DATA_FOR_CREATE))
                .and().statusCode(400);
        clearTestDataFlag = false;
    }

    @Test
    @DisplayName("Send POST request to /api/v1/courier without firstName")
    @Description("Possible to create courier without firstName")
    public void createCourierMissedNameFieldTest(){
        CourierAction courierAction = new CourierAction();
        String json = generateCourierData.generateCustomJson(CourierFields.LOGIN, CourierFields.PASSWORD);
        Response response = courierAction.postRequestCreateCourierByJson(json);
        response.then().assertThat().body("ok", equalTo(true))
                    .and().statusCode(201);
        clearTestDataFlag = false;
    }

    @After
    public void removeTestData() {
        if(clearTestDataFlag) {
            CourierCard courierCard = new CourierCard(
                    generateCourierData.getCourierLogin(),
                    generateCourierData.getCourierPassword());
            CourierAction courierAction = new CourierAction(courierCard);
            courierAction.deleteRequestRemoveCourier();
        }
    }

    public CourierCard generateCourierData()
    {
        return new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword(),
                generateCourierData.getCourierFirstName());
    }
}
