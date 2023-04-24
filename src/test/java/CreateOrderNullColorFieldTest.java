import actions.GenerateOrderCardData;
import actions.OrderAction;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import resources.OrderCard;
import resources.TrackCard;

import static org.hamcrest.Matchers.notNullValue;

@Feature("Color order field, POST /api/v1/orders")
@DisplayName("Create order without color field")
public class CreateOrderNullColorFieldTest {
    private OrderCard orderCard;
    private OrderAction orderAction;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        GenerateOrderCardData generateOrderCardData = new GenerateOrderCardData();
        generateOrderCardData.generateRandomDataField();
        orderCard = new OrderCard (
                generateOrderCardData.getFirstName(), generateOrderCardData.getLastName(),
                generateOrderCardData.getAddress(), generateOrderCardData.getMetroStation(),
                generateOrderCardData.getPhone(), generateOrderCardData.getRentTime(),
                generateOrderCardData.getDeliveryDate(), generateOrderCardData.getComment());
    }

    @Test
    public void createOrderNullColorValue() {
        orderAction = new OrderAction(orderCard);
        Response response = orderAction.postRequestCreateOrder();
        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(201);
        cancelTestOrder(response.as(TrackCard.class));
    }

    @Step("Cancel test order")
    private void cancelTestOrder(TrackCard trackCard) {
        orderAction.putRequestCancelOrderByTrack(trackCard.getTrack());
    }
}

