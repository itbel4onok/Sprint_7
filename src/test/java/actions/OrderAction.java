package actions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import resources.OrderCard;

import static io.restassured.RestAssured.given;

public class OrderAction {
    private OrderCard orderCard;
    public OrderAction(OrderCard orderCard){
        this.orderCard = orderCard;
    }

    public OrderAction(){
    }

    // Create order part
    @Step("Create order, POST /api/v1/orders")
    public Response postRequestCreateOrder() {
        return given()
                .header("Content-type", "application/json")
                .body(orderCard)
                .when()
                .post("/api/v1/orders");
    }

    // Ger order by track number
    @Step("Get order by track number, GET /api/v1/orders")
    public Response getRequestGetOrderByNumber(int trackNumber) {
        return  given()
                .header("Content-type", "application/json")
                .queryParam("t", trackNumber)
                .get("/api/v1/orders/track");
    }

    // Cancel order by track number
    @Step("Cancel order by number, PUT /api/v1/orders/cancel")
    public void putRequestCancelOrderByTrack(int trackNumber) {
          given()
                .header("Content-type", "application/json")
                .queryParam("track", trackNumber)
                .put("/api/v1/orders/cancel");
    }


    // Finish order by id
    @Step("Finish order by id, PUT /api/v1/orders/finish/:id")
    public void putRequestFinishOrderById(int orderId) {
        given()
                .header("Content-type", "application/json")
                .put("/api/v1/orders/finish/{:id}", orderId);
    }
}
