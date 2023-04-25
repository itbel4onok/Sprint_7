import base.BaseCourierTest;
import base.BaseOrderTest;
import constants.CourierFields;
import constants.ErrorMessage;
import constants.OrderListParams;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import resources.TrackCard;

import java.util.Map;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@Feature("Accept order by track number - PUT /api/v1/orders/accept/:id")
public class AcceptOrderTest extends BaseOrderTest {
    private BaseCourierTest baseCourierTest = new BaseCourierTest();

    @Test
    @DisplayName("Send correct PUT request to /api/v1/orders/accept")
    @Description("Happy path for /api/v1/orders/accept")
    public void putAcceptOrderHappyPathTest() {
        int orderId = getIdCreatedTestOrder();
        String courierId = getTestCourierId();
        Response response = orderAction.putRequestAcceptOrder(courierId, orderId);
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(SC_OK);
        deleteTestCourier();
        finishTestOrder(orderId);
    }

    @Test
    @DisplayName("Send accept order request without Courier id")
    @Description("Order cannot be accepted without Courier id")
    public void putAcceptOrderWithoutCourierIdTest() {
        int orderId = getIdCreatedTestOrder();
        Response response = orderAction.putRequestAcceptOrderWithoutCourierID(orderId);
        response.then().assertThat().body("message", equalTo(
                ErrorMessage.NOT_ENOUGH_DATA_FOR_SEARCHING_ORDER))
                .and().statusCode(SC_BAD_REQUEST);
        finishTestOrder(orderId);
    }

    @Test
    @DisplayName("Send accept order request with random Courier id")
    @Description("Order cannot be accepted without correct Courier id")
    public void putAcceptOrderIncorrectCourierIdTest() {
        int orderId = getIdCreatedTestOrder();
        Response response = orderAction.putRequestAcceptOrder(CourierFields.RANDOM_COURIER_ID, orderId);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_FOUND_COURIER_ID))
                .and().statusCode(SC_NOT_FOUND);
        finishTestOrder(orderId);
    }

    @Test
    @DisplayName("Send accept order request without Order id")
    @Description("Order cannot be accepted without Order id")
    public void putAcceptOrderWithoutOrderIdTest() {
        String courierId = getTestCourierId();
        Response response = orderAction.putRequestAcceptOrderWithoutOrderID(courierId);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_ENOUGH_DATA_FOR_SEARCHING_ORDER))
                .and().statusCode(SC_BAD_REQUEST);
        deleteTestCourier();
    }

    @Test
    @DisplayName("Send accept order request with random Order id")
    @Description("Order cannot be accepted without correct Order id")
    public void putAcceptOrderIncorrectOrderIdTest() {
        String courierId = getTestCourierId();
        Response response = orderAction.putRequestAcceptOrder(courierId, OrderListParams.INCORRECT_ORDER_NUMBER);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_EXIST_ORDER))
                .and().statusCode(SC_NOT_FOUND);
        deleteTestCourier();
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

    @Step("Get id of created courier")
    private String getTestCourierId(){
        baseCourierTest.createNewTestCourier();
        return baseCourierTest.getCourierId();
    }

    private void deleteTestCourier(){
        baseCourierTest.deleteTestCourier();
    }
}
