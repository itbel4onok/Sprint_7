package actions;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseApi {
    public RequestSpecification RequestSpecification()
    {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        return RestAssured.given()
                .header("Content-type", "application/json");
    }
}
