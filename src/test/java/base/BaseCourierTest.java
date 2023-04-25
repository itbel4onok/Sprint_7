package base;

import actions.CourierAction;
import actions.GenerateCourierData;
import constants.CourierFields;
import io.qameta.allure.Step;
import resources.CourierCard;

public class BaseCourierTest {
    public CourierAction courierAction = new CourierAction();
    private GenerateCourierData generateCourierData = new GenerateCourierData();
    public CourierCard courierCard;

    @Step("Create courier by Random data")
    public void createNewTestCourier()
    {
        generateLogPassCourierData();
        courierAction.postRequestCreateCourier(courierCard);
    }

    @Step("Get courier id")
    public String getCourierId(){
        return courierAction.getCourierId(courierCard);
    }

    @Step("Fill CourierCard by Random data")
    public void generateCourierData()
    {
        generateCourierData.generateLoginPassName();
        courierCard = new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword(),
                generateCourierData.getCourierFirstName());
    }

    @Step("Create & Fill CourierCard by Random data")
    private void generateLogPassCourierData()
    {
        generateCourierData.generateLoginPassName();
        courierCard = new CourierCard(
                generateCourierData.getCourierLogin(),
                generateCourierData.getCourierPassword());
    }

    @Step("Create & Fill CourierCard by Random data (with part of field)")
    public void generateCustomCourierData(String firstField, String secondField)
    {
        generateCourierData.generateLoginPassName();
        courierCard = new CourierCard();
        fillField(firstField);
        fillField(secondField);
    }

    private void fillField(String field)
    {
        switch(field) {
            case CourierFields.LOGIN: courierCard.setLogin(generateCourierData.getCourierLogin());
            break;
            case CourierFields.PASSWORD: courierCard.setPassword(generateCourierData.getCourierPassword());
            break;
            case CourierFields.FIRST_NAME: courierCard.setFirstName(generateCourierData.getCourierFirstName());
            break;
        }
    }

    @Step("Remove created test-data")
    public void deleteTestCourier() {
        courierAction.deleteRequestRemoveCourier(courierCard);
    }
}
