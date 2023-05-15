package actions;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Random;

public class GenerateOrderCardData {
    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private Random random = new Random();

    public void generateRandomDataField() {
        firstName = generateRandomString();
        lastName = generateRandomString();
        address = generateRandomString();
        metroStation = random.nextInt(50);
        phone = generatePhone();
        rentTime = random.nextInt(10);
        deliveryDate = generateData();
        comment = generateRandomString();
    }

    private String generateRandomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    private String generateData() {
        String day = String.valueOf(random.nextInt(28));
        String month = String.valueOf(random.nextInt(13));
        String year = String.valueOf(random.nextInt((2023 - 2020) + 1) + 2020);
        return year + "-" + month + "-" + day;
    }

    private String generatePhone() {
        String phoneFormat3NumberCod = String.valueOf(random.nextInt((999 - 100) + 1) + 100);
        String phoneFormat3Number = String.valueOf(random.nextInt((999 - 100) + 1) + 100);
        String phoneFormat2Number = String.valueOf(random.nextInt((99 - 10) + 1) + 10);
        String phoneFormat2NumberLast = String.valueOf(random.nextInt((99 - 10) + 1) + 10);
        return "+7" + " " + phoneFormat3NumberCod + " " + phoneFormat3Number + " " +
                phoneFormat2Number + " " + phoneFormat2NumberLast;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public int getMetroStation() {
        return metroStation;
    }

    public String getPhone() {
        return phone;
    }

    public int getRentTime() {
        return rentTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getComment() {
        return comment;
    }
}
