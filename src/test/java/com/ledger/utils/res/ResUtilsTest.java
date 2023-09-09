package com.ledger.utils.res;

import com.ledger.entity.res.chat.ChatRequest;
import com.ledger.entity.resp.ChatCompletionResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;

@Slf4j
@SpringBootTest
class ResUtilsTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void BeforeEach() {
        ResUtils.SetUp();
    }

    @Test
    void getData() {

    }

    @Test
    void postData() {

    }

    @Test
    void testPostData() {
    }

    @Test
    void setUp() {
//        log.info(nowWeatherResponse.toString());
    }

    @Test
    void getUrl() {
    }

    @Test
    void testChat() {
        ChatRequest chatRequest = new ChatRequest();
        ArrayList<ChatRequest.Message> messages = new ArrayList<>();
        ChatRequest.Message message = new ChatRequest.Message();
        message.setRole("user");
        message.setContent("Hello");
        messages.add(message);
        chatRequest.setMessages(messages);
        ChatCompletionResponse chatCompletionResponse =
                ResUtils.postDataForChat("/chat/completions", chatRequest, ChatCompletionResponse.class);
        ChatCompletionResponse.Choice choice = chatCompletionResponse.getChoices()[0];
        String content = choice.getMessage().getContent();
        log.info(content);
    }


    @Test
    void name() {
        stringRedisTemplate.opsForValue().set("test", "test");

    }
}