package com.ledger.entity.res.intserfaces;

import com.ledger.utils.res.ResUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public abstract class SendMessage {
    protected String sessionKey;
    protected long target;
    private List<MessageChain> messageChain;

    @Data
    public static class MessageChain {
        private String type;
        private String text;
        private String url;

        public MessageChain(String type, String content, boolean isText) {
            this.type = type;
            if (isText) {
                this.text = content;
            } else {
                this.url = content;
            }
        }
    }

    public SendMessage() {
        this.sessionKey = ResUtils.config.getSessionKey();
        this.target = ResUtils.config.getOwnerQq();
    }

}
