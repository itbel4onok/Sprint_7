package actions;

import constants.PathApi;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import resources.CourierCard;
import resources.CourierId;

import static io.restassured.RestAssured.given;

public class CourierAction extends BaseApi {
    public CourierAction(){
    }

    // Create courier part
    public Response postRequestCreateCourier(Object obj) {
        return given(RequestSpecification())
                .body(obj)
                .when()
                .post(PathApi.COURIER_BASE_URL);
    }

    // Log-in courier part
    public Response postRequestLogIn(Object obj){
        return given(RequestSpecification())
                        .body(obj)
                        .when()
                        .post(PathApi.COURIER_LOGIN);
    }

    // Remove courier part
    @Step("Remove courier")
    public Response deleteRequestRemoveCourier(CourierCard courierCard){
        return deleteRequestRemoveCourierById(getCourierId(courierCard));
    }

    @Step("Find out Courier ID by Login request")
    public String getCourierId(CourierCard courierCard){
        Response response = postRequestLogIn(courierCard);
        CourierId courierId = response.as(CourierId.class);
        return courierId.getId();
    }

    @Step("Remove courier, DELETE request - /api/v1/courier/{:id}")
    public Response deleteRequestRemoveCourierById(String courierID){
        int courierId = Integer.parseInt(courierID);
        return given(RequestSpecification())
                        .delete(PathApi.COURIER_BASE_URL +"/{:id}", courierId);
    }

    @Step("Remove courier without ID, DELETE request - /api/v1/courier/{:id}")
    public Response deleteRequestRemoveCourierWithoutId(){
        return given(RequestSpecification())
                .delete(PathApi.COURIER_BASE_URL + "/:id");
    }
}
