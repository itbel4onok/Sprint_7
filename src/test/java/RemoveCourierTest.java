import base.BaseCourierTest;
import constants.CourierFields;
import constants.ErrorMessage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@Feature("Remove courier - DELETE /api/v1/courier/:id")
public class RemoveCourierTest extends BaseCourierTest {
    @Test
    @DisplayName("Send correct DELETE request to /api/v1/courier/:id")
    @Description("Happy path for /api/v1/courier/:id")
    public void removeCourierHappyPathTest() {
        createNewTestCourier();
        Response response = courierAction.deleteRequestRemoveCourier(courierCard);
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(SC_OK);
    }

    @Test
    @DisplayName("Send DELETE request to /api/v1/courier/:id without id")
    @Description("Impossible to remove courier without sending id")
    public void removeCourierWithoutIdTest(){
        Response response = courierAction.deleteRequestRemoveCourierWithoutId();
        response.then().assertThat().body("message", equalTo(ErrorMessage.INCORRECT_REMOVE_REQUEST))
                .and().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Send DELETE request to /api/v1/courier/:id with random id")
    @Description("Impossible to remove courier wit incorrect id")
    public void removeCourierIncorrectIdTest(){
        Response response = courierAction.deleteRequestRemoveCourierById(CourierFields.INCORRECT_ID_FOR_REMOVE);
        response.then().assertThat().body("message", equalTo(ErrorMessage.NOT_FOUND_ID_FOR_REMOVE))
                .and().statusCode(SC_NOT_FOUND);
    }
}
