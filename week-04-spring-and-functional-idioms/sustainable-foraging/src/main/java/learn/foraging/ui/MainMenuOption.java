package learn.foraging.ui;

public enum MainMenuOption {

    EXIT(0, "Exit", false),
    VIEW_FORAGES_BY_DATE(1, "View Forages By Date", false),
    VIEW_FORAGES_BY_FORAGER(2, "View Forages By Forager", false),
    VIEW_FORAGERS_BY_STATE(3, "View Foragers By State", false),
    VIEW_ITEMS(4, "View Items", false),
    ADD_FORAGE(5, "Add a Forage", false),
    ADD_FORAGER(6, "Add a Forager", false),
    ADD_ITEM(7, "Add an Item", false),
    UPDATE_ITEM(8, "Update an Item", false),
    REPORT_KG_PER_ITEM(9, "Report: Kilograms of Item", false),
    REPORT_CATEGORY_VALUE(10, "Report: Item Category Value", false),
    GENERATE(11, "Generate Random Forages", true);

    private int value;
    private String message;
    private boolean hidden;

    private MainMenuOption(int value, String message, boolean hidden) {
        this.value = value;
        this.message = message;
        this.hidden = hidden;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public boolean isHidden() {
        return hidden;
    }
}
