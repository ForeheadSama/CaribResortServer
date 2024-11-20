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
        PULL_ORDER,
        PULL_ORDERS_BY_Date,
        PULL_ORDERS_BY_Drink,
        GENERATE_ORDER_ID,
        PULL_DRINK_BY_NAME
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

