package com.ledger.entity.resp;

import lombok.Data;

import java.util.List;

@Data
public class VbHotWordsResponse  {
    private int code;
    private String msg;
    private List<HotWord> data;
    private long time;
    private int usage;
    private String log_id;

    @Data
    public static class HotWord {
        private String hot_word;
        private long hot_word_num;
        private String url;
    }
}
