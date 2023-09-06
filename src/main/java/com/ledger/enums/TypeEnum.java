package com.ledger.enums;


public enum TypeEnum {
    PLAIN("Plain", "text"),
    IMAGE("Image", "url");

    private final String type;
    private final String contentType;

    TypeEnum(String type, String contentType) {
        this.type = type;
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public String getContentType() {
        return contentType;
    }

}
