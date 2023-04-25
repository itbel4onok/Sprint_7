package base;

import actions.GenerateOrderCardData;
import actions.GenerateOrderValue;
import actions.OrderAction;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import resources.OrderCard;
import resources.TrackCard;

public class BaseOrderTest {
    public OrderAction orderAction = new OrderAction();
    public GenerateOrderCardData generateOrderCardData = new GenerateOrderCardData();
    public GenerateOrderValue generateOrderValue = new GenerateOrderValue();
    public OrderCard orderCard;

    @Step("Fill OrderCard by Random data")
    public void generateOrderData() {
        generateOrderCardData.generateRandomDataField();
        orderCard = new OrderCard(
                generateOrderCardData.getFirstName(), generateOrderCardData.getLastName(),
                generateOrderCardData.getAddress(), generateOrderCardData.getMetroStation(),
                generateOrderCardData.getPhone(), generateOrderCardData.getRentTime(),
                generateOrderCardData.getDeliveryDate(), generateOrderCardData.getComment());
    }

    @Step("Fill OrderCard by Random data")
    public void generateOrderData(String[] color) {
        generateOrderCardData.generateRandomDataField();
        orderCard = new OrderCard(
                generateOrderCardData.getFirstName(), generateOrderCardData.getLastName(),
                generateOrderCardData.getAddress(), generateOrderCardData.getMetroStation(),
                generateOrderCardData.getPhone(), generateOrderCardData.getRentTime(),
                generateOrderCardData.getDeliveryDate(), generateOrderCardData.getComment(), color);
    }

    @Step("Create test order")
    public Response createTestOrder () {
        generateOrderData();
        return orderAction.postRequestCreateOrder(orderCard);
    }

    @Step("Get track number of test order")
    public int getTrackNumberOfNewTestOrder () {
        TrackCard trackCard = createTestOrder().as(TrackCard.class);
        return trackCard.getTrack();
    }

    @Step("Cancel test order")
    public void cancelTestOrder(TrackCard trackCard) {
        orderAction.putRequestCancelOrderByTrack(trackCard.getTrack());
    }

    @Step("Finish test order")
    public void finishTestOrder(int orderId) {
        orderAction.putRequestFinishOrderById(orderId);
    }
}
