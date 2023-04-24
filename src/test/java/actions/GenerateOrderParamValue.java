package actions;

import constants.OrderListParams;

import java.util.Random;

public class GenerateOrderParamValue {
    Random random = new Random();
    public String GenerateParamValue(String param){
        switch (param) {
            case OrderListParams.NEAREST_STATION:
                return generateNearestStation();
            case OrderListParams.PAGE:
                return Integer.toString(random.nextInt(10));
            case OrderListParams.LIMIT:
                return Integer.toString(random.nextInt(30));
        }
        return "empty_param";
    }
    private String generateNearestStation(){
        return "[\"" + random.nextInt(120) + "\", \"" + random.nextInt(120) + "\"]";
    }
}
