package com.ledger.entity.res.chat;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    private String model="gpt-3.5-turbo-0613";
    private List<Message> messages;

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}
