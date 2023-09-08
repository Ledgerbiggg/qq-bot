package com.ledger.task;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ledger.entity.res.SendFriendMessage;
import com.ledger.entity.res.SendGroupMessage;
import com.ledger.entity.res.intserfaces.SendMessage;
import com.ledger.entity.resp.common.ResMessage;
import com.ledger.enums.TypeEnum;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class SendMessageUtil {


    public static ResMessage SendMessages(String content , boolean isToFriend) {
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(),content,true);
        messageChains.add(messageChain);
        return SendMessages(messageChains, isToFriend);
    }
    public static ResMessage SendMessages(List<SendMessage.MessageChain> messageChains, boolean isToFriend) {
        ResMessage resMessage = null;
        if (isToFriend) {
            resMessage= send(messageChains, new SendFriendMessage());
        } else {
            resMessage= send(messageChains, new SendGroupMessage());
        }
        return resMessage;
    }
    // TODO 表情包看着用
    private static ResMessage send(List<SendMessage.MessageChain> messageChains, SendMessage sendMessage) {
        if(sendMessage instanceof SendFriendMessage){
            SendFriendMessage sendFriendMessage = new SendFriendMessage();
            sendFriendMessage.setMessageChain(messageChains);
            return ResUtils.postData(sendFriendMessage, ResMessage.class);
        }else if (sendMessage instanceof SendGroupMessage){
            SendGroupMessage sendGroupMessage = new SendGroupMessage();
            sendGroupMessage.setMessageChain(messageChains);
            return ResUtils.postData(sendGroupMessage, ResMessage.class);
        }
        ResMessage resMessage = new ResMessage();
        resMessage.setCode(1);
        resMessage.setMessageId(-1);
        return resMessage;
    }


}
