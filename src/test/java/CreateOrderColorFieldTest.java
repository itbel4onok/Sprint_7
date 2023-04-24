import actions.*;
import constants.OrderColorFields;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import resources.OrderCard;
import resources.TrackCard;

import static org.hamcrest.Matchers.notNullValue;

@Feature("Color order field, POST /api/v1/orders")
@DisplayName("Create order with options of color field")
@RunWith(Parameterized.class)
public class CreateOrderColorFieldTest {
    private String[] color;
    private OrderCard orderCard;
    private OrderAction orderAction;
    private String testName;
    private GenerateOrderCardData generateOrderCardData = new GenerateOrderCardData();
    public CreateOrderColorFieldTest(String testName, String[] color) {

        this.testName = testName;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Color field value: {0}")
    public static Object[][] getColorValue() {
        return new Object[][] {
                { "Empty color list", OrderColorFields.NO_COLOR },
                { "Black color", OrderColorFields.BLACK },
                { "Gray color", OrderColorFields.GRAY },
                { "Black & Gray color", OrderColorFields.BOTH_COLOR },
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        generateOrderCardData.generateRandomDataField();
        orderCard = new OrderCard(
                generateOrderCardData.getFirstName(), generateOrderCardData.getLastName(),
                generateOrderCardData.getAddress(), generateOrderCardData.getMetroStation(),
                generateOrderCardData.getPhone(), generateOrderCardData.getRentTime(),
                generateOrderCardData.getDeliveryDate(), generateOrderCardData.getComment(),
                color);
    }

    @Test
    public void createOrderDiffColorValue() {
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

