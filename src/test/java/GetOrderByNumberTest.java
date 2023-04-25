import base.BaseOrderTest;
import constants.ErrorMessage;
import constants.OrderListParams;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import resources.*;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@Feature("Get order by track number - GET /api/v1/orders/track")
public class GetOrderByNumberTest extends BaseOrderTest {
    @Test
    @DisplayName("Get order by track number")
    @Description("Happy path for /api/v1/orders/track")
    public void getOrderByNumberTest() {
        int trackNumber = getTrackNumberOfNewTestOrder();
        Response response = orderAction.getRequestGetOrderByNumber(trackNumber);
        response.then().assertThat().body("order", notNullValue())
                .and().statusCode(SC_OK);
        cancelTestOrder(response.as(TrackCard.class));
    }

    @Test
    @DisplayName("Get order without track number")
    public void getOrderWithoutNumberTest() {
        Response response = orderAction.getRequestGetOrderWithoutParam();
        response.then().assertThat().body("message", equalTo(
                ErrorMessage.NOT_ENOUGH_DATA_FOR_SEARCHING_ORDER))
                .and().statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Get order by incorrect track number")
    public void getOrderByIncorrectNumberTest() {
        Response response = orderAction.getRequestGetOrderByNumber(
                OrderListParams.INCORRECT_ORDER_NUMBER);
        response.then().assertThat().body("message", equalTo(
                        ErrorMessage.NOT_FOUND_ORDER))
                .and().statusCode(SC_NOT_FOUND);
    }
}
