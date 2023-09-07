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


@Component
@Slf4j
public class SendMessageUtil {


    public static ResMessage SendMessages(Object content, boolean isText, boolean isToFriend) {
        ResMessage resMessage = null;
        if (isToFriend) {
            resMessage= send(content, isText, new SendFriendMessage());
        } else {
            resMessage= send(content, isText, new SendGroupMessage());
        }
        return resMessage;
    }
    private static ResMessage send(Object content, boolean isText, SendMessage sendMessage) {
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        if (isText) {
            jsonObject.put("type", TypeEnum.PLAIN.getType());
            jsonObject.put(TypeEnum.PLAIN.getContentType(), content);
        } else {
            jsonObject.put("type", TypeEnum.IMAGE.getType());
            jsonObject.put(TypeEnum.IMAGE.getContentType(), content);
        }
        array.add(jsonObject);
        if(sendMessage instanceof SendFriendMessage){
            SendFriendMessage sendFriendMessage = new SendFriendMessage();
            sendFriendMessage.setMessageChain(array);
            return ResUtils.postData(sendFriendMessage, ResMessage.class);
        }else if (sendMessage instanceof SendGroupMessage){
            SendGroupMessage sendGroupMessage = new SendGroupMessage();
            sendGroupMessage.setMessageChain(array);
            return ResUtils.postData(sendGroupMessage, ResMessage.class);
        }
        ResMessage resMessage = new ResMessage();
        resMessage.setCode(1);
        resMessage.setMessageId(-1);
        return resMessage;
    }


}
