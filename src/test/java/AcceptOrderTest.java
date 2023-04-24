import actions.CourierAction;
import actions.GenerateCourierData;
import actions.GenerateOrderCardData;
import actions.OrderAction;
import constants.CourierFields;
import constants.ErrorMessage;
import constants.OrderListParams;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import resources.CourierCard;
import resources.OrderCard;
import resources.TrackCard;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Feature("Accept order by track number - PUT /api/v1/orders/accept/:id")
public class AcceptOrderTest {
    private OrderAction orderAction;
    private CourierAction courierAction;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Send correct PUT request to /api/v1/orders/accept")
    @Description("Happy path for /api/v1/orders/accept")
    public void putAcceptOrderHappyPathTest() {
        int orderId = getIdCreatedTestOrder();
        String courierId = getIdCreatedCourier();
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/{orderId}", orderId);
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(200);
        removeTestCourier();
        finishTestOrder(orderId);
    }

    @Test
    @DisplayName("Send accept order request without Courier id")
    @Description("Order cannot be accepted without Courier id")
    public void putAcceptOrderWithoutCourierIdTest() {
        int orderId = getIdCreatedTestOrder();
        Response response = given()
                .header("Content-type", "application/json")
                .put("/api/v1/orders/accept/{orderId}", orderId);
        response.then().assertThat().body("message", equalTo(
                ErrorMessage.NOT_ENOUGH_DATA_FOR_SEARCHING_ORDER))
                .and().statusCode(400);
        finishTestOrder(orderId);
    }

    @Test
    @DisplayName("Send accept order request with random Courier id")
    @Description("Order cannot be accepted without correct Courier id")
    public void putAcceptOrderIncorrectCourierIdTest() {
        int orderId = getIdCreatedTestOrder();
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam("courierId", CourierFields.RANDOM_COURIER_ID)
                .put("/api/v1/orders/accept/{orderId}", orderId);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_FOUND_COURIER_ID))
                .and().statusCode(404);
        finishTestOrder(orderId);
    }

    @Test
    @DisplayName("Send accept order request without Order id")
    @Description("Order cannot be accepted without Order id")
    public void putAcceptOrderWithoutOrderIdTest() {
        String courierId = getIdCreatedCourier();
        Response response = given()
                .header("Content-type", "application/json")
                .put("/api/v1/orders/accept/{courierId}", courierId);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_ENOUGH_DATA_FOR_SEARCHING_ORDER))
                .and().statusCode(400);
        removeTestCourier();
    }

    @Test
    @DisplayName("Send accept order request with random Order id")
    @Description("Order cannot be accepted without correct Order id")
    public void putAcceptOrderIncorrectOrderIdTest() {
        String courierId = getIdCreatedCourier();
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/{orderId}", OrderListParams.INCORRECT_ORDER_NUMBER);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_EXIST_ORDER))
                .and().statusCode(404);
        removeTestCourier();
    }

    @Step("Get id of test order")
    private int getIdCreatedTestOrder() {
        Response response = createTestOrder();
        TrackCard trackCard = response.as(TrackCard.class);
        int trackNumber =  trackCard.getTrack();
        Response responseGetOrder = orderAction.getRequestGetOrderByNumber(trackNumber);
        Map<String, Integer> map = responseGetOrder.jsonPath().getMap("order");
        return map.get("id");
    }

    @Step("Create test order")
    private Response createTestOrder () {
        GenerateOrderCardData generateOrderCardData = new GenerateOrderCardData();
        generateOrderCardData.generateRandomDataField();
        OrderCard orderCard = new OrderCard(
                generateOrderCardData.getFirstName(), generateOrderCardData.getLastName(),
                generateOrderCardData.getAddress(), generateOrderCardData.getMetroStation(),
                generateOrderCardData.getPhone(), generateOrderCardData.getRentTime(),
                generateOrderCardData.getDeliveryDate(), generateOrderCardData.getComment());
        orderAction = new OrderAction(orderCard);
        return orderAction.postRequestCreateOrder();
    }

    @Step("Get id of created courier")
    private String getIdCreatedCourier(){
        createCourier();
        return courierAction.getCourierId();
    }

    @Step("Create test courier")
    private void createCourier(){
        GenerateCourierData generateCourierData = new GenerateCourierData();
        generateCourierData.generateLoginPass();
        CourierCard courierCard = new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword());
        courierAction = new CourierAction(courierCard);
        courierAction.postRequestCreateCourierByCard();
    }

    @Step("Remove test courier")
    private void removeTestCourier() {
        courierAction.deleteRequestRemoveCourier();
    }

    @Step("Finish test order")
    private void finishTestOrder(int orderId) {
        orderAction.putRequestFinishOrderById(orderId);
    }
}
