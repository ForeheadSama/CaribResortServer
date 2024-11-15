package com.caribresort.server;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String message;
    private Object data;

    // Constructor
    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
