package com.namespace.util;

/**
 * Created by Aaron on 08/03/2016.
 */
public class WrappedBoolean {

    private final boolean result;
    private final String reason;

    public WrappedBoolean(boolean result) {
        this.result = result;
        this.reason = "";
    }

    public WrappedBoolean(boolean result, String reason) {
        this.result = result;
        this.reason = reason;
    }

    public boolean getResult() {
        return result;
    }

    public String getReason() {
        return reason;
    }
}