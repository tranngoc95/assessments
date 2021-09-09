package learn.field_agent.controllers;

import lombok.Getter;

import java.time.LocalDateTime;

public class GlobalErrorResponse {
    private @Getter final LocalDateTime timestamp = LocalDateTime.now();
    private @Getter final String message;

    public GlobalErrorResponse(String message) {
        this.message = message;
    }
}
