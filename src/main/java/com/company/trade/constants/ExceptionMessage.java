package com.company.trade.constants;

public enum ExceptionMessage {
    INVALID_MATURITY_DATE("Maturity date should not less than today's date!" ),
    INVALID_TRADE_VERSION("Invalid trade version");
    private String errorMessage;

    ExceptionMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
