package com.ledger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ledger.config.Config;
import com.ledger.entity.res.Bind;
import com.ledger.entity.res.SessionInfo;
import com.ledger.entity.res.Verify;
import com.ledger.entity.resp.NowWeatherResponse;
import com.ledger.entity.resp.SessionInfoResp;
import com.ledger.entity.resp.WeatherResponse;
import com.ledger.entity.resp.common.Res;
import com.ledger.entity.resp.common.ResDataObj;
import com.ledger.entity.resp.common.ResSession;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class QqBotApplication {


    public static void main(String[] args) {
        Config config = ResUtils.SetUp();
        ResUtils.config = config;
        Res bind = bind(config);
        if (bind.getCode() == 0 && bind.getMsg().equals("success")) {
            SpringApplication.run(QqBotApplication.class, args);
            ResUtils.config.setFinishStart(1);
            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.setSessionKey(ResUtils.config.getSessionKey());
            ResDataObj data = ResUtils.getData(sessionInfo, ResDataObj.class);
            JSONObject json = data.getData();
            SessionInfoResp sessionInfoResp = JSON.parseObject(json.toJSONString(), SessionInfoResp.class);
            SessionInfoResp.QQ qq = sessionInfoResp.getQq();
            Object nickname = qq.getNickname();
            Object remark = qq.getRemark();
            log.info("账号绑定成功,qq账号是{}", config.getQq());
            log.info("账号信息:qq用户名称{},qq签名:{}", nickname, remark);
            log.info("======================================");
            // TODO 天气设置成每天早上播报
            WeatherResponse weatherResponse = ResUtils.postDataForCommon("http://apis.juhe.cn/simpleWeather/query?city=宁波&key=251518e073ef6c3c9504dd286c3f6a86", null, WeatherResponse.class);

            log.info(weatherResponse.toString());
        } else {
            log.error("账号绑定失败,qq账号是{},失败的原因是{}", config.getQq(), bind.getMsg());
            log.error("错误码{}", bind.getCode());
        }
    }

    public static Res bind(Config config) {
        long qq = config.getQq();
        Verify verify = new Verify();
        verify.setVerifyKey(config.getVerifyKey());
        ResSession resSession = ResUtils.postData(verify, ResSession.class);
        Bind bind = new Bind();
        bind.setSessionKey(resSession.getSession());
        bind.setQq(qq);
        ResUtils.config.setSessionKey(resSession.getSession());
        return ResUtils.postData(bind, Res.class);
    }

}
