package actions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import resources.CourierCard;
import resources.CourierId;

import static io.restassured.RestAssured.given;

public class CourierAction {
    private CourierCard courierCard;
    public CourierAction(CourierCard courierCard){
        this.courierCard = courierCard;
    }
    public CourierAction(){
    }

    // Create courier part
    private Response postRequestCreateCourier(Object obj) {
        return given()
                .header("Content-type", "application/json")
                .body(obj)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Create courier by CourierCard, POST request - /api/v1/courier")
    public Response postRequestCreateCourierByCard() {
        return postRequestCreateCourier(courierCard);
    }

    @Step("Create courier by Json, POST request - /api/v1/courier")
    public Response postRequestCreateCourierByJson(String json) {
        return postRequestCreateCourier(json);
    }

    // Log-in courier part
    private Response postRequestLogIn(Object obj){
        return given()
                        .header("Content-type", "application/json")
                        .body(obj)
                        .when()
                        .post("/api/v1/courier/login");
    }

    @Step("Log in by CourierCard, POST request - /api/v1/courier/login")
    public Response postRequestLogInByCard(){
        return postRequestLogIn(courierCard);
    }

    @Step("Log in by Json, POST request - /api/v1/courier/login")
    public Response postRequestLogInByJson(String json){
        return postRequestLogIn(json);
    }

    // Remove courier part
    @Step("Remove courier")
    public Response deleteRequestRemoveCourier(){
        return deleteRequestRemoveCourierById(getCourierId());
    }

    @Step("Find out Courier ID by Login request")
    public String getCourierId(){
        Response response = postRequestLogInByCard();
        CourierId courierId = response.as(CourierId.class);
        return courierId.getId();
    }

    @Step("Remove courier, DELETE request - /api/v1/courier/{:id}")
    public Response deleteRequestRemoveCourierById(String courierID){
        int courierId = Integer.parseInt(courierID);
        return given()
                        .header("Content-type", "application/json")
                        .delete("/api/v1/courier/{:id}", courierId);
    }
}
