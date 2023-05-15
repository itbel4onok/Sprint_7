import base.BaseCourierTest;
import base.BaseOrderTest;
import constants.OrderListParams;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Get order list - GET /api/v1/orders")
public class GetOrderListTest extends BaseOrderTest {
    private BaseCourierTest baseCourierTest = new BaseCourierTest();

    @Test
    @DisplayName("Get order list without params")
    public void getOrderListNoParamTest() {
        Response response = orderAction.getRequestGetOrderList();
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Get order list with params: Limit, Page")
    public void getOrderListParamLimitPageTest() {
        Response response = orderAction.getRequestGetOrderList(
                OrderListParams.LIMIT, generateOrderValue.GenerateParamValue(OrderListParams.LIMIT),
                OrderListParams.PAGE, generateOrderValue.GenerateParamValue(OrderListParams.PAGE));
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Get order list with params: Limit, Page, Nearest Station")
    public void getOrderListParamLimitPageStationTest() {
        Response response = orderAction.getRequestGetOrderList(
                OrderListParams.LIMIT, generateOrderValue.GenerateParamValue(OrderListParams.LIMIT),
                OrderListParams.PAGE, generateOrderValue.GenerateParamValue(OrderListParams.PAGE),
                OrderListParams.NEAREST_STATION, generateOrderValue.GenerateParamValue(OrderListParams.NEAREST_STATION));
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(SC_OK);
    }


    @Test
    @DisplayName("Get order list with params: Courier ID")
    public void getOrderListParamCourierIdTest() {
        Response response = orderAction.getRequestGetOrderList(OrderListParams.COURIER_ID, getNewTestCourierId());
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(SC_OK);
        deleteTestCourier();
    }

    @Test
    @DisplayName("Get order list with params: Courier ID, nearestStation")
    public void getOrderListParamCourierIdStationTest() {
        Response response = orderAction.getRequestGetOrderList(
                OrderListParams.COURIER_ID, getNewTestCourierId(),
                OrderListParams.NEAREST_STATION, generateOrderValue.GenerateParamValue(OrderListParams.NEAREST_STATION));
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(SC_OK);
        deleteTestCourier();
    }

    private String getNewTestCourierId(){
        baseCourierTest.createNewTestCourier();
        return baseCourierTest.courierAction.getCourierId(baseCourierTest.courierCard);
    }

    private void deleteTestCourier(){
        baseCourierTest.deleteTestCourier();
    }
}
