package actions;

import constants.CourierFields;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

import static java.lang.String.format;

public class GenerateCourierData {
    private String courierLogin;
    private String courierPassword;
    private String courierFirstName;

    private String generateRandomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public void generateLoginPassName() {
        courierLogin = generateRandomString();
        courierPassword = generateRandomString();
        courierFirstName = generateRandomString();
    }

    public void generateLoginPass() {
        courierLogin = generateRandomString();
        courierPassword = generateRandomString();
    }

    @Step("Generating custom Json with two required fields")
    public String generateCustomJson(String firstRequiredFields, String secondRequiredFields) {
        return "{" + buildJson(firstRequiredFields) + ", " + buildJson(secondRequiredFields) + "}";
    }

    @Step("Generating custom Json with one required field")
    public String generateCustomJson(String requiredField) {
        return "{" + buildJson(requiredField) + "}";
    }

    private String buildJson(String requiredField) {
        String tempLine = "";
        switch (requiredField) {
            case CourierFields.LOGIN: tempLine = courierLogin;
                break;
            case CourierFields.PASSWORD: tempLine = courierPassword;
                break;
            case CourierFields.FIRST_NAME: tempLine = courierFirstName;
                break;
        }
        return format("\"%s\": \"%s\"", requiredField, tempLine);
    }

    public String getCourierLogin() {
        return courierLogin;
    }

    public String getCourierPassword() {
        return courierPassword;
    }

    public String getCourierFirstName() {
        return courierFirstName;
    }
}
