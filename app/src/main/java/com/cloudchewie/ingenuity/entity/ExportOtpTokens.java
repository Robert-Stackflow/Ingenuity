package com.cloudchewie.ingenuity.entity;

import java.util.List;

public class ExportOtpTokens {
    List<OtpToken> tokens;
    List<String> order;

    public ExportOtpTokens(List<OtpToken> tokens, List<String> order) {
        this.tokens = tokens;
        this.order = order;
    }

    public List<OtpToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<OtpToken> tokens) {
        this.tokens = tokens;
    }

    public List<String> getOrder() {
        return order;
    }

    public void setOrder(List<String> order) {
        this.order = order;
    }
}
