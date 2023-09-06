package com.ledger.utils;

import com.ledger.entity.res.Bind;
import com.ledger.entity.res.Verify;
import com.ledger.entity.resp.WeatherResponse;
import com.ledger.entity.resp.common.Res;
import com.ledger.entity.resp.common.ResSession;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class ResUtilsTest {

    @Test
    void fetchData() {
//        Verify verify = new Verify();
//        verify.setVerifyKey("ledger");
//        ResSession resSession = ResUtils.postData(verify, ResSession.class);
//        Bind bind = new Bind();
//        bind.setSessionKey(resSession.getSession());
//        bind.setQq(427945607);
//        Res res = ResUtils.postData(bind);
//        log.info(res.toString());
    }

    @Test
    void get() {
//        ResUtils.getData();
    }

    @Test
    void name() {




    }
}