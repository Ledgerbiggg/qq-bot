package com.ledger.entity.resp.common;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ResMessage {
    private int code;
    private String msg;
    private int messageId;
}
