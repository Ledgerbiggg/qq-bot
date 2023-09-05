package com.ledger.utils.res;

import com.ledger.entity.res.SessionInfo;
import com.ledger.entity.resp.Res;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResUtilsTest {

    @Test
    void getData() {
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setSessionKey("zr3qgZdU");
        Res data = ResUtils.getData(sessionInfo, Res.class);
        System.out.println(data+"12222222222222");

    }

    @Test
    void postData() {
    }

    @Test
    void testPostData() {
    }

    @Test
    void setUp() {
    }

    @Test
    void getUrl() {
    }
}