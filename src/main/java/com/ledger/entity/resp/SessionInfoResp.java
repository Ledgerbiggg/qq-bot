package com.ledger.entity.resp;

import lombok.Data;

@Data
public class SessionInfoResp {
    private String sessionKey;
    private QQ qq;

    // 构造函数

    // Getter 和 Setter 方法
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public QQ getQq() {
        return qq;
    }

    public void setQq(QQ qq) {
        this.qq = qq;
    }

    // 内部类表示 "qq" 字段
    @Data
    public static class QQ {
        private long id;
        private String nickname;
        private String remark;
    }
}

