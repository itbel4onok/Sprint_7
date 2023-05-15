package actions;
import org.apache.commons.lang3.RandomStringUtils;

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
