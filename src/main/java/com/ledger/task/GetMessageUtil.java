package com.ledger.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ledger.entity.res.FetchMessage;
import com.ledger.entity.res.intserfaces.SendMessage;
import com.ledger.entity.resp.NewsTopResponse;
import com.ledger.entity.resp.NowWeatherResponse;
import com.ledger.entity.resp.common.ResDataArr;
import com.ledger.entity.resp.common.ResMessage;
import com.ledger.enums.TypeEnum;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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
                ResMessage resMessage = SendMessageUtil.SendMessages((String) item.get("url"), true);
                log.info(resMessage.getMsg());
            }
        });
    }

    /**
     * 针对不同的指令消息去回应不同的消息
     * @param text 接受的消息文本
     */
    private static void switchTextMessage(String text) {
        if (text.startsWith("/w ")) {
            String city = text.replace("/w ", "");
            //现在的天气
            try {
                NowWeatherResponse nowWeatherResponse =
                        ResUtils.getDataForCommon
                                ("https://api.seniverse.com/v3/weather/now.json?location=" + city + "&key=SCYrvkytJze9qyzOh", NowWeatherResponse.class);
                List<SendMessage.MessageChain> weatherContent = getWeatherContent(nowWeatherResponse);
                SendMessageUtil.SendMessages(weatherContent, true);
            } catch (Exception e) {
                log.error(e.getMessage());
                SendMessageUtil.SendMessages("输入的城市有误或者接口失效", true);
            }
        }else if(text.startsWith("/n")){
            // TODO 两个新闻
            try {
                NewsTopResponse newsTopResponse = ResUtils.getDataForCommon
                        ("http://v.juhe.cn/toutiao/index?type=top&key=d268884b9b07c0eb9d6093dc54116018", NewsTopResponse.class);
                List<SendMessage.MessageChain> newsTopContent = getNewsContent(newsTopResponse);
                SendMessageUtil.SendMessages(newsTopContent, true);
            }catch (Exception e){
                log.error(e.getMessage());
                SendMessageUtil.SendMessages("接口失效或者出现其他异常", true);
            }
        }else if(text.startsWith("/vb")){
            // TODO 一个微博热搜

        }
    }

    private static List<SendMessage.MessageChain> getNewsContent(NewsTopResponse newsTopResponse) {
        List<NewsTopResponse.NewsItem> data = newsTopResponse.getResult().getData();
        List<NewsTopResponse.NewsItem> newsItems = data.subList(0, 10);
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        newsItems.forEach(i -> {
            String title = i.getTitle();
            String url = i.getUrl();
            String date = i.getDate();
            String thumbnailPicS = i.getThumbnail_pic_s();
            SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), title+"\n", true);
            SendMessage.MessageChain messageChain1 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), "新闻链接:\n", true);
            SendMessage.MessageChain messageChain2 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), url, true);
            SendMessage.MessageChain messageChain3 = new SendMessage.MessageChain(TypeEnum.IMAGE.getType(), thumbnailPicS, false);
            SendMessage.MessageChain messageChain4 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), date+"\n", true);
            Collections.addAll(messageChains, messageChain, messageChain1, messageChain2, messageChain3,messageChain4);
            messageChains.add(messageChain);
        });
        return messageChains;
    }

    private static List<SendMessage.MessageChain> getWeatherContent(NowWeatherResponse nowWeatherResponse) {
        NowWeatherResponse.Result result = nowWeatherResponse.getResults()[0];
        String name = result.getLocation().getName();
        String weather = result.getNow().getText();
        String temperature = result.getNow().getTemperature();
        String lastUpdate = result.getLast_update();
        ArrayList<String> list = new ArrayList<>();
        list.add("城市:" + name + "\n");
        list.add("天气:" + weather + "\n");
        list.add("温度:" + temperature + "°C\n");
        list.add("上次更新时间:" + lastUpdate + "\n");
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), list.get(0), true);
        SendMessage.MessageChain messageChain1 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), list.get(1), true);
        SendMessage.MessageChain messageChain2 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), list.get(2), true);
        SendMessage.MessageChain messageChain3 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), list.get(3), true);
        Collections.addAll(messageChains, messageChain, messageChain1, messageChain2, messageChain3);
        return messageChains;
    }


    private static void switchItem(JSONObject jsonObject) {
        String type = (String) jsonObject.get("type");
        JSONArray messageChain = (JSONArray) jsonObject.get("messageChain");
        if (type.equals("FriendMessage")) {
            handleFriendMessageChain(messageChain);
        }
    }


}
