package com.ledger.entity.resp;

import lombok.Data;
@Data
public class WeatherResponse {
    private String reason;
    private WeatherResult result;
    private int error_code;

    @Data
    public static class WeatherResult {
        private String city;
        private Realtime realtime;
        private Future[] future;
    }

    @Data
    public static class Realtime {
        private String temperature;
        private String humidity;
        private String info;
        private String wid;
        private String direct;
        private String power;
        private String aqi;
    }

    @Data
    public static class Future {
        private String date;
        private String temperature;
        private String weather;
        private Wid wid;
        private String direct;
    }

    @Data
    public static class Wid {
        private String day;
        private String night;
    }

}
