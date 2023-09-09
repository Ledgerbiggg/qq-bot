package com.ledger.task;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ledger.entity.res.FetchMessage;
import com.ledger.entity.res.intserfaces.SendMessage;
import com.ledger.entity.resp.*;
import com.ledger.entity.resp.common.ResDataArr;
import com.ledger.entity.resp.common.ResMessage;
import com.ledger.enums.TypeEnum;
import com.ledger.utils.res.ResUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
     *
     * @param text 接受的消息文本
     */
    private static void switchTextMessage(String text) {
        // 新闻
        if (text.startsWith("w ")) {
            String city = text.replace("w ", "");
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
        } else if (text.startsWith("n1")) {
            // TODO 两个新闻
            try {
                NewsTopResponse newsTopResponse = ResUtils.getDataForCommon
                        ("http://v.juhe.cn/toutiao/index?type=top&key=d268884b9b07c0eb9d6093dc54116018", NewsTopResponse.class);
                List<SendMessage.MessageChain> newsTopContent = getNewsTopContent(newsTopResponse);
                SendMessageUtil.SendMessages(newsTopContent, true);
            } catch (Exception e) {
                log.error(e.getMessage());
                SendMessageUtil.SendMessages("接口失效或者出现其他异常", true);
            }
        } else if (text.startsWith("n2")) {
            try {
                NewsNetResponse newsNetResponse = ResUtils.getDataForCommon
                        ("https://v2.alapi.cn/api/new/toutiao?token=LwExDtUWhF3rH5ib", NewsNetResponse.class);
                List<SendMessage.MessageChain> newsNetContent = getNewsNetContent(newsNetResponse);
                SendMessageUtil.SendMessages(newsNetContent, true);
            } catch (Exception e) {
                log.error(e.getMessage());
                SendMessageUtil.SendMessages("接口失效或者出现其他异常", true);
            }
        } else if (text.startsWith("vb ")) {
            // TODO 一个微博热搜
            String count = text.replaceAll("vb ", "");
            try {
                VbHotWordsResponse vbHotWordsResponse = ResUtils.getDataForCommon
                        ("https://v2.alapi.cn/api/new/wbtop?token=LwExDtUWhF3rH5ib&num=" + count, VbHotWordsResponse.class);
                List<SendMessage.MessageChain> newsTopContent = getVbHotWordsContent(vbHotWordsResponse);
                SendMessageUtil.SendMessages(newsTopContent, true);
            } catch (Exception e) {
                // TODO 抛出错误写一下,避免返回状态是200但是实际请求是错误的
                log.error(e.getMessage());
                SendMessageUtil.SendMessages("接口失效或者出现其他异常", true);
            }
        } else if (text.startsWith("em ")) {
            try {
                EmojiResponse emojiResponse = ResUtils.getDataForCommon("https://test.harumoe.cn/api/emoji/all", EmojiResponse.class);
                List<SendMessage.MessageChain> emojiContent = getEmojiContent(emojiResponse);
                SendMessageUtil.SendMessages(emojiContent, true);
            } catch (Exception e) {
                log.error(e.getMessage());
                SendMessageUtil.SendMessages("接口失效或者出现其他异常", true);
            }
        }
    }

    private static List<SendMessage.MessageChain> getNewsNetContent(NewsNetResponse newsNetResponse) {
        List<NewsNetResponse.NewsItem> data = newsNetResponse.getData();
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        data.forEach(i -> {
            String title = i.getTitle();
            String imgsrc = i.getImgsrc();
            String source = i.getSource();
            String time = i.getTime();
            SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), title + "\n");
            SendMessage.MessageChain messageChain1 = new SendMessage.MessageChain(TypeEnum.IMAGE.getType(), imgsrc + "\n");
            SendMessage.MessageChain messageChain2 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), "新闻来源:" + source + "\n");
            SendMessage.MessageChain messageChain4 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), "发布时间:" + time + "\n");
            SendMessage.MessageChain messageChain5 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), "\n");
            Collections.addAll(messageChains, messageChain, messageChain1, messageChain2, messageChain4, messageChain5, messageChain5);
        });
        return messageChains;
    }

    private static List<SendMessage.MessageChain> getEmojiContent(EmojiResponse emojiResponse) {
        Map<String, String> data = emojiResponse.getData();
        //获取一个0-9的随机数
        int i = RandomUtil.randomInt(3, data.keySet().size());
        ArrayList<String> urls = new ArrayList<>();
        data.forEach((k, v) -> {
            urls.add(v);
        });
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.IMAGE.getType(), urls.get(i));
        messageChains.add(messageChain);
        return messageChains;
    }

    //TODO 链接显示问题
    private static List<SendMessage.MessageChain> getVbHotWordsContent(VbHotWordsResponse vbHotWordsResponse) {
        List<VbHotWordsResponse.HotWord> data = vbHotWordsResponse.getData();
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        data.forEach(i -> {
            String hot_word = i.getHot_word();
            long hot_word_num = i.getHot_word_num();
            String url = i.getUrl();
            String encodedText = null;
            String[] split = url.split("q=");
            String s = split[1];
            encodedText = URLEncoder.encode(s, StandardCharsets.UTF_8);
            SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), hot_word + "\n");
            SendMessage.MessageChain messageChain1 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), split[0] + "q=" + encodedText + "\n");
            messageChains.add(messageChain);
            messageChains.add(messageChain1);
        });

        return messageChains;
    }

    private static List<SendMessage.MessageChain> getNewsTopContent(NewsTopResponse newsTopResponse) {
        List<NewsTopResponse.NewsItem> data = newsTopResponse.getResult().getData();
        List<NewsTopResponse.NewsItem> newsItems = data.subList(0, 10);
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        newsItems.forEach(i -> {
            String title = i.getTitle();
            String url = i.getUrl();
            String date = i.getDate();
            String thumbnailPicS = i.getThumbnail_pic_s();
            SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), title + "\n");
            SendMessage.MessageChain messageChain1 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), "新闻链接:\n");
            SendMessage.MessageChain messageChain2 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), url);
            SendMessage.MessageChain messageChain3 = new SendMessage.MessageChain(TypeEnum.IMAGE.getType(), thumbnailPicS);
            SendMessage.MessageChain messageChain4 = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), date + "\n");
            Collections.addAll(messageChains, messageChain, messageChain1, messageChain2, messageChain3, messageChain4);
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
        //2023-09-08T22:54:21+08:00
        String[] ts = lastUpdate.split("T");
        String[] split = ts[1].split("\\+");
        String time = ts[0] + "  " + split[1];
        list.add("上次更新时间:" + "\n");
        list.add(" " + time + "\n");
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        list.forEach(i -> {
            SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.PLAIN.getType(), i);
            messageChains.add(messageChain);
        });
        addFace(messageChains, true, 4);
        return messageChains;
    }

    private static List<SendMessage.MessageChain> getRandomFace(boolean must, int maxFaceCount) {
        ArrayList<SendMessage.MessageChain> messageChains = new ArrayList<>();
        boolean haveFace = RandomUtil.randomBoolean();
        int facId = RandomUtil.randomInt(320);
        int faceCount = RandomUtil.randomInt(1, maxFaceCount);
        if (must || haveFace) {
            for (int i = 0; i < faceCount; i++) {
                SendMessage.MessageChain messageChain = new SendMessage.MessageChain(TypeEnum.FACE.getType(), String.valueOf(facId));
                messageChains.add(messageChain);
            }
        }
        return messageChains;
    }

    private static void addFace(List<SendMessage.MessageChain> messageChains, boolean must, int maxFaceCount) {
        messageChains.addAll(getRandomFace(must, maxFaceCount));
    }

    private static void switchItem(JSONObject jsonObject) {
        String type = (String) jsonObject.get("type");
        JSONArray messageChain = (JSONArray) jsonObject.get("messageChain");
        if (type.equals("FriendMessage")) {
            handleFriendMessageChain(messageChain);
        }
    }


}
