package com.ledger.entity.resp;

import lombok.Data;

import java.util.List;

@Data
public class NewsNetResponse {
    private int code;
    private String msg;
    private List<NewsItem> data;
    private long time;
    private int usage;
    private String log_id;


    @Data
    public static class NewsItem {
        private String title;
        private String type;
        private String digest;
        private String docid;
        private String pc_url;
        private String m_url;
        private String imgsrc;
        private String source;
        private String time;

    }
}
