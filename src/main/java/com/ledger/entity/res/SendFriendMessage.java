package com.ledger.entity.res;

import com.alibaba.fastjson.JSONArray;
import com.ledger.entity.res.intserfaces.SendMessage;
import com.ledger.utils.res.ResUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SendFriendMessage extends SendMessage {
    protected String url="/sendFriendMessage";
    public SendFriendMessage() {
        super();
    }

}
