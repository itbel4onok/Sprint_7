import base.BaseOrderTest;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import resources.OrderColor;
import resources.TrackCard;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Color order field, POST /api/v1/orders")
@DisplayName("Create order with options of color field")
@RunWith(Parameterized.class)
public class CreateOrderColorFieldTest extends BaseOrderTest {
    private String testName;
    private String[] color;
    public CreateOrderColorFieldTest(String testName, String[] color) {
        this.testName = testName;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Color field value: {0}")
    public static Object[][] getColorValue() {
        Object[][] objects = {
                { OrderColor.NO_COLOR.name(), OrderColor.NO_COLOR.getValue()},
                { OrderColor.BLACK.name(), OrderColor.BLACK.getValue() },
                { OrderColor.GRAY.name(), OrderColor.GRAY.getValue() },
                { OrderColor.BOTH_COLOR.name(), OrderColor.BOTH_COLOR.getValue()},
        };
        return objects;
    }

    @Before
    public void setUp() {
        generateOrderData(color);
    }

    @Test
    public void createOrderDiffColorValue() {
        Response response = orderAction.postRequestCreateOrder(orderCard);
        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(SC_CREATED);
        cancelTestOrder(response.as(TrackCard.class));
    }
}

