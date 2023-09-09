package com.ledger.enums;


import lombok.Getter;

@Getter
public enum TypeEnum {
    PLAIN("Plain", "text"),
    IMAGE("Image", "url"),
    FACE("Face", "faceId");

    private final String type;
    private final String contentType;

    TypeEnum(String type, String contentType) {
        this.type = type;
        this.contentType = contentType;
    }

}
