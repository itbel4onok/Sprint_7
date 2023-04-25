import base.BaseOrderTest;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import resources.TrackCard;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Color order field, POST /api/v1/orders")
@DisplayName("Create order without color field")
public class CreateOrderNullColorFieldTest extends BaseOrderTest {
    @Test
    public void createOrderNullColorValue() {
        generateOrderData();
        Response response = orderAction.postRequestCreateOrder(orderCard);
        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(SC_CREATED);
        cancelTestOrder(response.as(TrackCard.class));
    }
}

