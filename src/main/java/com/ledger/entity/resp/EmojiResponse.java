package com.ledger.entity.resp;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EmojiResponse {
    private int code;
    private String msg;
    private Map<String,String> data;
}
