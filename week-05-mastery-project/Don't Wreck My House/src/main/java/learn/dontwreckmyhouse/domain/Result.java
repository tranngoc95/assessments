package learn.dontwreckmyhouse.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {
    private ArrayList<String> messages = new ArrayList<>();
    @Getter @Setter
    private T payLoad;

    public List<String> getErrorMessages() {
        return new ArrayList<>(messages);
    }

    public void addErrorMessage(String message) {
        messages.add(message);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

}
