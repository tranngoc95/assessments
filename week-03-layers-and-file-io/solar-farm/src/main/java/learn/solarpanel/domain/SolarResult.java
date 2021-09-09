package learn.solarpanel.domain;

import learn.solarpanel.models.SolarPanel;

import java.util.ArrayList;
import java.util.List;

public class SolarResult {
    private final List<String> messages = new ArrayList<>();
    private SolarPanel panel;
    private String action;

    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String msg) {
        this.messages.add(msg);
    }

    public SolarPanel getPanel() {
        return panel;
    }

    public void setPanel(SolarPanel panel) {
        this.panel = panel;
    }

    public boolean isSuccess(){
        return messages.size()==0;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
