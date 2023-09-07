package com.ledger.entity.resp;

import lombok.Data;

@Data
public class NowWeatherResponse {
    private Result[] results;

    @Data
    public static class Result {
        private Location location;
        private Now now;
        private String last_update;
    }

    @Data
    public static class Location {
        private String id;
        private String name;
        private String country;
        private String path;
        private String timezone;
        private String timezone_offset;
    }

    @Data
    public static class Now {
        private String text;
        private String code;
        private String temperature;
    }

}
