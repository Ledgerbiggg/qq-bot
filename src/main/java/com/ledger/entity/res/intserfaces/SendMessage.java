package com.ledger.entity.res.intserfaces;

import com.ledger.enums.TypeEnum;
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
        private String faceId;

        public MessageChain(String type, String content) {
            this.type = type;
            if (TypeEnum.PLAIN.getType().equals(type)) {
                this.text = content;
            } else if (TypeEnum.IMAGE.getType().equals(type)){
                this.url = content;
            } else if (TypeEnum.FACE.getType().equals(type)){
                this.faceId = content;
            }
        }
    }

    public SendMessage() {
        this.sessionKey = ResUtils.config.getSessionKey();
        this.target = ResUtils.config.getOwnerQq();
    }

}
