package resources;

public enum OrderColor {
    NO_COLOR(new String[]{}),
    BLACK(new String[]{"BLACK"}),
    GRAY(new String[]{"GRAY"}),
    BOTH_COLOR(new String[]{"BLACK", "GRAY"});

    private String[] value;

    OrderColor(String[] value) {
        this.value = value;
    }

    public String[] getValue() {
        return value;
    }
}
