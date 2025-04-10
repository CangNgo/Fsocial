package com.fsocial.timelineservice.services.impl;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;

import java.time.Duration;

public class ChatService {

    public  static void should_send_messages_and_receive_response() {

        HuggingFaceChatModel model = HuggingFaceChatModel.builder()
                .accessToken(System.getenv("HF_API_KEY"))
                .modelId("microsoft/Phi-3.5-mini-instruct")
                .timeout(Duration.ofSeconds(60))
                .temperature(0.7)
                .maxNewTokens(20)
                .waitForModel(true)
                .build();

        AiMessage aiMessage = model.chat(
                        SystemMessage.systemMessage("You are a good friend of mine, who likes to answer with jokes"),
                        UserMessage.userMessage("Hey Bro, what are you doing?"))
                .aiMessage();

        System.out.println(aiMessage);
    }

    public static void main(String[] args) {
        should_send_messages_and_receive_response();
    }

}
