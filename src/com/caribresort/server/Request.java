package com.caribresort.server;


import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Action { 
        ADD_DRINK, 
        REMOVE_DRINK, 
        MODIFY_DRINK, 
        PULL_DRINKS,
        NEW_ORDER,  
        MODIFY_ORDER, 
        REMOVE_ORDER,
        PULL_ORDERS,
        GENERATE_ORDER_ID
    }
    
    private Action action;
    private Object data;

    // Constructor
    public Request(Action action, Object data) {
        this.action = action;
        this.data = data;
    }

    // Getters
    public Action getAction() {
        return action;
    }

    public Object getData() {
        return data;
    }
}

