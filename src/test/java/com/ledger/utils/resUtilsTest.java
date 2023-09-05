package com.ledger.utils;

import com.ledger.entity.res.Verify;
import com.ledger.entity.resp.Res;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class ResUtilsTest {

    @Test
    void fetchData() {
        Verify verify = new Verify();
        Res res = ResUtils.postData(verify);
    }
}