package org.example.tmsserver.config;

import org.example.tmsserver.bot.TelegramPollingBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.springframework.stereotype.Component;

@Configuration
public class TelegramBotConfig {

    private final TelegramPollingBot telegramPollingBot;

    public TelegramBotConfig(TelegramPollingBot telegramPollingBot) {
        this.telegramPollingBot = telegramPollingBot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramPollingBot);
            System.out.println("TelegramPollingBot registrado com sucesso!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}