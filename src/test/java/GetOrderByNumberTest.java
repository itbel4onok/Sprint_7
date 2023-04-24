import actions.*;
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
import resources.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@Feature("Get order by track number - GET /api/v1/orders/track")
public class GetOrderByNumberTest {
    private OrderAction orderAction = new OrderAction();
    private TrackCard trackCard;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }


    @Test
    @DisplayName("Get order by track number")
    @Description("Happy path for /api/v1/orders/track")
    public void getOrderByNumberTest() {
        int trackNumber = getTrackNumber();
        Response response = orderAction.getRequestGetOrderByNumber(trackNumber);
        response.then().assertThat().body("order", notNullValue())
                .and().statusCode(200);
        cancelTestOrder(trackNumber);
    }

    @Test
    @DisplayName("Get order without track number")
    public void getOrderWithoutNumberTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders/track");
        response.then().assertThat().body("message", equalTo(
                ErrorMessage.NOT_ENOUGH_DATA_FOR_SEARCHING_ORDER))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("Get order by incorrect track number")
    public void getOrderByIncorrectNumberTest() {
        Response response = orderAction.getRequestGetOrderByNumber(
                OrderListParams.INCORRECT_ORDER_NUMBER);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_FOUND_ORDER))
                .and().statusCode(404);
    }


    @Step("Create test order and get track number")
    private int getTrackNumber() {
        Response response = createTestOrder();
        return getTrackNumber(response);
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

    @Step("Get track number of test order")
    private int getTrackNumber (Response response) {
        trackCard = response.as(TrackCard.class);
        return trackCard.getTrack();
    }

    @Step("Cancel test order")
    private void cancelTestOrder(int trackNumber) {
        orderAction.putRequestCancelOrderByTrack(trackNumber);
    }
}
