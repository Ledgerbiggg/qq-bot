package com.ledger.entity.res.intserfaces;

import com.alibaba.fastjson.JSONArray;
import com.ledger.utils.res.ResUtils;
import lombok.Data;

@Data
public abstract class SendMessage {
    protected String sessionKey;
    protected long target;
    protected JSONArray messageChain;


    public SendMessage() {
        this.sessionKey= ResUtils.config.getSessionKey();
        this.target=ResUtils.config.getOwnerQq();
    }

}
