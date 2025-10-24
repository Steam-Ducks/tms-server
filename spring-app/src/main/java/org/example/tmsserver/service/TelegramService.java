package org.example.tmsserver.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramService {

    private final TelegramLongPollingBot bot;

    public TelegramService(@Lazy TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void sendMessage(Long chatId, String text) {
        System.out.println("🔥 DEBUG: TelegramService.sendMessage chamado para chatId: " + chatId);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.enableMarkdown(true);

        try {
            System.out.println("🔥 DEBUG: Tentando executar sendMessage...");
            bot.execute(message);
            System.out.println("✅ DEBUG: Mensagem enviada com SUCESSO para chatId: " + chatId);
        } catch (TelegramApiException e) {
            System.err.println("❌ ERRO ao enviar mensagem para chatId " + chatId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}