package com.ledger.utils.res;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.ledger.config.Config;
import com.ledger.entity.resp.common.ResDataArr;
import com.ledger.entity.resp.common.ResDataObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.StringJoiner;

@Slf4j
public class ResUtils {

    public static Config config;

    static RestTemplate restTemplate;

    static String url;


    public static <T> ResDataObj postData(T type) {
        return postData(type, ResDataObj.class);
    }
    public static <T> ResDataArr getData(T type) {
        return getData(type, ResDataArr.class);
    }

    public static <T, K> K getData(T type, Class<K> resClazz) {

        Map<String, Object> map = BeanUtil.beanToMap(type, false, true);

        StringJoiner sj = new StringJoiner("&", "?", "");
        map.forEach((k, v) -> {
            if (!k.equals("url")) {
                sj.add(k + "=" + v);
            }
        });
        ResponseEntity<String> res = restTemplate.getForEntity(url + getUrl(type) + sj, String.class);

        String body = res.getBody();

        JSON.toJSONString(body);

        K k = JSON.parseObject(res.getBody(), resClazz);

        //TODO 消息不正确就发送给我
        HttpStatus statusCode = res.getStatusCode();

        return k;
    }


    public static <T, K> K postData(T type, Class<K> resClazz) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(type), headers);

        ResponseEntity<K> res = restTemplate.postForEntity(url + getUrl(type), requestEntity, resClazz);

        //TODO 消息不正确就发送给我
        HttpStatus statusCode = res.getStatusCode();

        return res.getBody();
    }


    public static <T, K> K postDataForCommon(String url,T type, Class<K> resClazz){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");
        HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSONString(type), headers);
        ResponseEntity<K> res = restTemplate.postForEntity(url, requestEntity, resClazz);
//        String body = res.getBody();
//        K k = JSON.parseObject(body, resClazz);
        //TODO 消息不正确就发送给我
        HttpStatus statusCode = res.getStatusCode();

        return res.getBody();
    }

    public static <K> K getDataForCommon(String url, Class<K> resClazz){
        ResponseEntity<K> res = restTemplate.getForEntity(url, resClazz);
        //TODO 消息不正确就发送给我
        HttpStatus statusCode = res.getStatusCode();
        return res.getBody();
    }




    public static Config SetUp() {
        restTemplate = new RestTemplate();
        File file = new File("./Config.json");
        String c = FileUtil.readString(file, Charset.defaultCharset());
        Config config = JSON.parseObject(c, Config.class);
        String addr = config.getAddr();
        int port = config.getPort();
        String http = config.getHttp();
        url = http + addr + ":" + port;
        return config;
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


}
