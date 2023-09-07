package com.ledger.entity.resp;

import lombok.Data;

import java.util.List;

@Data
public class NewsTopResponse {
    private String reason;
    private Result result;
    private int error_code;

    @Data
    public static class Result {
        private String stat;
        private List<NewsItem> data;
        private String page;
        private String pageSize;
    }

    @Data
    public static class NewsItem {
        private String uniquekey;
        private String title;
        private String date;
        private String category;
        private String author_name;
        private String url;
        private String thumbnail_pic_s;
        private String thumbnail_pic_s02;
        private String thumbnail_pic_s03;
        private String is_content;
    }
}
