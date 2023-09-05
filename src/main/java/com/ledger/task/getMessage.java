package com.ledger.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ledger.entity.res.FetchMessage;
import com.ledger.entity.resp.common.ResDataArr;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class getMessage {


    @Scheduled(fixedRate = 3000)
    public void getMethod() {
        FetchMessage fetchMessage = new FetchMessage();
        ResDataArr res = ResUtils.getData(fetchMessage);
        JSONArray data = res.getData();
        data.forEach(i->{
            JSONObject item = (JSONObject) i;
            switchItem(item);
        });
    }

    private void handleFriendMessageChain(JSONArray messageChain){
        messageChain.forEach(i->{
            JSONObject item = (JSONObject) i;
            String type = (String)item.get("type");
            if(type.equals("Plain")){
                log.info("消息是文本格式");
                log.info("消息文本是{}",item.get("text"));
            }else if(type.equals("Image")){
                log.info("消息是图片格式");
                log.info("图片地址是{}",item.get("url"));
            }
        });
    }
    private void switchItem(JSONObject jsonObject){
        String type = (String)jsonObject.get("type");
        JSONArray messageChain = (JSONArray)jsonObject.get("messageChain");
        if (type.equals("FriendMessage")) {
            handleFriendMessageChain(messageChain);
        }
    }


}
