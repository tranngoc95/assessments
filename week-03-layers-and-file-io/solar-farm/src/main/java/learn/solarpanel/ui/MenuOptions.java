package learn.solarpanel.ui;

public enum MenuOptions {
    DISPLAY_PANELS_BY_SECTION("Display Panels In A Section"),
    ADD_PANEL("Add Panel"),
    UPDATE_PANEL("Update Panel"),
    REMOVE_PANEL("Remove Panel"),
    EXIT("Exit");
    private final String title;

    MenuOptions(String title){
        this.title=title;
    }

    public String getTitle() {
        return title;
    }
}
