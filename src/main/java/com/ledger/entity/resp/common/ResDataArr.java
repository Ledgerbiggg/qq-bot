package com.ledger.entity.resp.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class ResDataArr {
    private int code;
    private String msg;
    private JSONArray data;

}
