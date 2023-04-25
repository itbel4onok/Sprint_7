package actions;

import constants.PathApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import resources.OrderCard;

import static io.restassured.RestAssured.given;

public class OrderAction extends BaseApi {
    public OrderAction(){
    }

    // Create order part
    @Step("Create order, POST /api/v1/orders")
    public Response postRequestCreateOrder(OrderCard orderCard) {
        return given(RequestSpecification())
                .body(orderCard)
                .when()
                .post(PathApi.ORDER_BASE_URL);
    }

    // Ger order list
    @Step("Get order list, GET /api/v1/orders")
    public Response getRequestGetOrderList() {
        return given(RequestSpecification())
                .get(PathApi.ORDER_BASE_URL);
    }

    @Step("Get order list, GET /api/v1/orders")
    public Response getRequestGetOrderList(String queryParam, String queryParamValue) {
        return given(RequestSpecification())
                .queryParam(queryParam, queryParamValue)
                .get(PathApi.ORDER_BASE_URL);
    }

    @Step("Get order list, GET /api/v1/orders")
    public Response getRequestGetOrderList(String firstQueryParam, String firstQueryParamValue,
                                           String secondQueryParam, String secondQueryParamValue) {
        return given(RequestSpecification())
                .queryParam(firstQueryParam, firstQueryParamValue)
                .queryParam(secondQueryParam, secondQueryParamValue)
                .get(PathApi.ORDER_BASE_URL);
    }

    @Step("Get order list, GET /api/v1/orders")
    public Response getRequestGetOrderList(String firstQueryParam, String firstQueryParamValue,
                                           String secondQueryParam, String secondQueryParamValue,
                                           String thirdQueryParam, String thirdQueryParamValue) {
        return given(RequestSpecification())
                .queryParam(firstQueryParam, firstQueryParamValue)
                .queryParam(secondQueryParam, secondQueryParamValue)
                .queryParam(thirdQueryParam, thirdQueryParamValue)
                .get(PathApi.ORDER_BASE_URL);
    }

    // Ger order by track number
    @Step("Get order by track number, GET /api/v1/orders")
    public Response getRequestGetOrderByNumber(int trackNumber) {
        return given(RequestSpecification())
                .queryParam("t", trackNumber)
                .get(PathApi.GET_ORDER_BY_TRACK);
    }

    @Step("Get order request without track number, GET /api/v1/orders")
    public Response getRequestGetOrderWithoutParam() {
        return given(RequestSpecification())
                .get(PathApi.GET_ORDER_BY_TRACK);
    }

    // Cancel order by track number
    @Step("Cancel order by number, PUT /api/v1/orders/cancel")
    public void putRequestCancelOrderByTrack(int trackNumber) {
        given(RequestSpecification())
                .queryParam("track", trackNumber)
                .put(PathApi.CANCEL_ORDER);
    }


    // Finish order by id
    @Step("Finish order by id, PUT /api/v1/orders/finish/:id")
    public void putRequestFinishOrderById(int orderId) {
        given(RequestSpecification())
                .put(PathApi.FINISH_ORDER + "{:id}", orderId);
    }

    // Accept order
    @Step("Accept order, PUT /api/v1/orders/accept/")
    public Response putRequestAcceptOrder(String courierId, int orderId) {
        return given(RequestSpecification())
                .queryParam("courierId", courierId)
                .put(PathApi.ACCEPT_ORDER + orderId);
    }

    @Step("Accept order without courier ID, PUT /api/v1/orders/accept/")
    public Response putRequestAcceptOrderWithoutCourierID(int orderId) {
        return given(RequestSpecification())
                .put(PathApi.ACCEPT_ORDER + orderId);
    }

    @Step("Accept order without order ID, PUT /api/v1/orders/accept/")
    public Response putRequestAcceptOrderWithoutOrderID(String courierId) {
        return given(RequestSpecification())
                .put(PathApi.ACCEPT_ORDER + "courierId={courierId}", courierId);
    }
}
