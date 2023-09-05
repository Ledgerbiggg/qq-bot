package com.ledger.utils.res;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.ledger.entity.resp.Res;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.StringJoiner;

public class ResUtils {

    static RestTemplate restTemplate;

    static Config config;

    static String url;

    static {
        SetUp();
    }


    public static <T, K> K getData(T type, Class<K> resClazz) {

        Map<String, Object> map = BeanUtil.beanToMap(type, false, true);

        StringJoiner sj = new StringJoiner("&", "?", "");
        map.forEach((k, v) -> {
            if (!k.equals("url")) {
                sj.add(k + "=" + v);
            }
        });
        ResponseEntity<K> res = restTemplate.getForEntity(url + getUrl(type) + sj, resClazz);

        //TODO 消息不正确就发送给我
        HttpStatus statusCode = res.getStatusCode();

        return res.getBody();
    }

    public static <T> Res postData(T type) {
        return postData(type, Res.class);
    }

    public static <T, K> K postData(T type, Class<K> resClazz) {

        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(type), null);

        ResponseEntity<K> res = restTemplate.postForEntity(url + getUrl(type), requestEntity, resClazz);

        //TODO 消息不正确就发送给我
        HttpStatus statusCode = res.getStatusCode();

        return res.getBody();
    }


    private static void SetUp() {
        restTemplate = new RestTemplate();
        File file = new File("./Config.json");
        String c = FileUtil.readString(file, Charset.defaultCharset());
        config = JSON.parseObject(c, Config.class);
        String addr = config.getAddr();
        int port = config.getPort();
        String http = config.getHttp();
        url = http + addr + ":" + port;
    }


    private static <T> String getUrl(T type) {
        Class<?> aClass = type.getClass();
        try {
            Field field = aClass.getDeclaredField("url");
            field.setAccessible(true);
            Object o = field.get(type);
            return (String) o;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    private static class Config {
        private String http = "http://";
        private String addr;
        private int port;
        private long qq;

    }


}
