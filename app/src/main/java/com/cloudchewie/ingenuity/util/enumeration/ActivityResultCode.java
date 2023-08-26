package com.cloudchewie.ingenuity.util.enumeration;

import org.jetbrains.annotations.Contract;

public enum ActivityResultCode {
    SELECT_CITY(1, "select city");
    private final int code;
    private final String message;

    ActivityResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Contract(pure = true)
    public int getCode() {
        return code;
    }

    @Contract(pure = true)
    public String getMessage() {
        return message;
    }
}
