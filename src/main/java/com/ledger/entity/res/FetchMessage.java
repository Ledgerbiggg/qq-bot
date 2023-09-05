package com.ledger.entity.res;

import com.ledger.utils.res.ResUtils;
import lombok.Data;

@Data
public class FetchMessage {
    private String url="/fetchMessage";
    private String sessionKey;
    private int count=10;

    public FetchMessage() {
        this.sessionKey= ResUtils.config.getSessionKey();
    }
}
