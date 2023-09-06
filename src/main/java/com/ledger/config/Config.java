package com.ledger.config;

import lombok.Data;

@Data
public class Config {
    private String http = "http://";
    private String verifyKey = "ledger";
    private String sessionKey;
    private int finishStart = 0;
    private String addr;
    private int port;
    private long qq;
    private long ownerQq;
    private long group;

}