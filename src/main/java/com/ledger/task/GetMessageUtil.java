package com.ledger.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ledger.entity.res.FetchMessage;
import com.ledger.entity.resp.common.ResDataArr;
import com.ledger.entity.resp.common.ResMessage;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetMessageUtil {

    @Scheduled(fixedRate = 3000)
    public static void getMessage() {
        FetchMessage fetchMessage = new FetchMessage();
        ResDataArr res = ResUtils.getData(fetchMessage);
        JSONArray data = res.getData();
        data.forEach(i -> {
            JSONObject item = (JSONObject) i;
            switchItem(item);
        });
    }

    private static void handleFriendMessageChain(JSONArray messageChain) {
        messageChain.forEach(i -> {
            JSONObject item = (JSONObject) i;
            String type = (String) item.get("type");
            if (type.equals("Plain")) {
                log.info("消息是--文本--格式,消息文本是===={}", item.get("text"));
                ResMessage resMessage = SendMessageUtil.SendMessages(item.get("text"), true, true);
                log.info(resMessage.getMsg());
            } else if (type.equals("Image")) {
                log.info("消息是--图片--格式,图片地址是===={}", item.get("url"));
                ResMessage resMessage = SendMessageUtil.SendMessages(item.get("url"), true, true);
                log.info(resMessage.getMsg());
            }
        });
    }

    private static void switchItem(JSONObject jsonObject) {
        String type = (String) jsonObject.get("type");
        JSONArray messageChain = (JSONArray) jsonObject.get("messageChain");
        if (type.equals("FriendMessage")) {
            handleFriendMessageChain(messageChain);
        }
    }


}
