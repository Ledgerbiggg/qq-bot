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

import java.util.ArrayList;
import java.util.List;

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
                switchTextMessage((String) item.get("text"));
            } else if (type.equals("Image")) {
                log.info("消息是--图片--格式,图片地址是===={}", item.get("url"));
                ResMessage resMessage = SendMessageUtil.SendMessages(item.get("url"), true, true);
                log.info(resMessage.getMsg());
            }
        });
    }
    private static void switchTextMessage(String text) {
        if(text.startsWith("/w ")){
            String city = text.replace("/w ", "");
            //现在的天气
            try {
                NowWeatherResponse nowWeatherResponse =
                        ResUtils.getDataForCommon
                                ("https://api.seniverse.com/v3/weather/now.json?location="+city+"&key=SCYrvkytJze9qyzOh", NowWeatherResponse.class);
                List<String> weatherContent = getWeatherContent(nowWeatherResponse);

                SendMessageUtil.SendMessages(weatherContent.toString(),true,true);
            }catch (Exception e){
                log.error(e.getMessage());
                SendMessageUtil.SendMessages("输入的城市有误或者接口失效",true,true);
            }


        }
    }

    private static List<String> getWeatherContent(NowWeatherResponse nowWeatherResponse) {
        NowWeatherResponse.Result[] results = nowWeatherResponse.getResults();
        NowWeatherResponse.Result result = results[0];
        String name = result.getLocation().getName();
        String weather = result.getNow().getText();
        String temperature = result.getNow().getTemperature();
        String lastUpdate = result.getLast_update();
        ArrayList<String> list = new ArrayList<>();

        list.add("城市:"+name+"\\n");
        list.add("天气:"+weather+"\\n");
        list.add("温度:"+temperature+"°C\\n");
        list.add("上次更新时间:"+lastUpdate+"\\n");
        return list;
    }


    private static void switchItem(JSONObject jsonObject) {
        String type = (String) jsonObject.get("type");
        JSONArray messageChain = (JSONArray) jsonObject.get("messageChain");
        if (type.equals("FriendMessage")) {
            handleFriendMessageChain(messageChain);
        }
    }


}
