package com.ledger.entity.resp;

import lombok.Data;

@Data
public class SessionInfoResp {
    private String sessionKey;
    private QQ qq;

    @Data
    public static class QQ {
        private long id;
        private String nickname;
        private String remark;
    }
}

