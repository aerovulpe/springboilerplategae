package com.namespace.util;

/**
 * Created by Aaron on 08/03/2016.
 */
public class WrappedBoolean {

    private final Boolean result;
    private final String reason;

    public WrappedBoolean(Boolean result) {
        this.result = result;
        this.reason = "";
    }

    public WrappedBoolean(Boolean result, String reason) {
        this.result = result;
        this.reason = reason;
    }

    public Boolean getResult() {
        return result;
    }

    public String getReason() {
        return reason;
    }
}