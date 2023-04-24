import actions.CourierAction;
import actions.GenerateCourierData;
import actions.GenerateOrderParamValue;
import constants.OrderListParams;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import resources.CourierCard;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Get order list - GET /api/v1/orders")
public class GetOrderListTest {
    private GenerateOrderParamValue generateOrderParamValue = new GenerateOrderParamValue();
    private CourierAction courierAction;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Get order list without params")
    public void getOrderListNoParamTest() {
        Response response = given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Get order list with params: Limit, Page")
    public void getOrderListParamLimitPageTest() {
        String paramLimitValue = generateOrderParamValue.GenerateParamValue(OrderListParams.LIMIT);
        String paramPageValue = generateOrderParamValue.GenerateParamValue(OrderListParams.PAGE);
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam(OrderListParams.LIMIT, paramLimitValue)
                .queryParam(OrderListParams.PAGE, paramPageValue)
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(200);
    }

    @Test
    @DisplayName("Get order list with params: Limit, Page, Nearest Station")
    public void getOrderListParamLimitPageStationTest() {
        String paramLimitValue = generateOrderParamValue.GenerateParamValue(OrderListParams.LIMIT);
        String paramPageValue = generateOrderParamValue.GenerateParamValue(OrderListParams.PAGE);
        String paramStationValue = generateOrderParamValue.GenerateParamValue(OrderListParams.NEAREST_STATION);
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam(OrderListParams.LIMIT, paramLimitValue)
                .queryParam(OrderListParams.PAGE, paramPageValue)
                .queryParam(OrderListParams.NEAREST_STATION, paramStationValue)
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(200);
    }


    @Test
    @DisplayName("Get order list with params: Courier ID")
    public void getOrderListParamCourierIdTest() {
        String courierId = getCourierId();
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam(OrderListParams.COURIER_ID, courierId)
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(200);
        removeGeneratedCourier(courierId);
    }

    @Test
    @DisplayName("Get order list with params: Courier ID, nearestStation")
    public void getOrderListParamCourierIdStationTest() {
        String courierId = getCourierId();
        String paramStationValue = generateOrderParamValue.GenerateParamValue(OrderListParams.NEAREST_STATION);
        Response response = given()
                .header("Content-type", "application/json")
                .queryParam(OrderListParams.COURIER_ID, courierId)
                .queryParam(OrderListParams.NEAREST_STATION, paramStationValue)
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue())
                .and().statusCode(200);
        removeGeneratedCourier(courierId);
    }

    private String getCourierId(){
        GenerateCourierData generateCourierData = new GenerateCourierData();
        generateCourierData.generateLoginPass();
        CourierCard courierCard = new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword());
        courierAction = new CourierAction(courierCard);
        courierAction.postRequestCreateCourierByCard();
        return courierAction.getCourierId();
    }

    private void removeGeneratedCourier(String courierId){
        courierAction.deleteRequestRemoveCourierById(courierId);
    }
}
