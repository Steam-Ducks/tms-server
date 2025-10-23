package org.example.tmsserver.webhook;

import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.UserRepository;
import org.example.tmsserver.service.TelegramService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/telegram")
public class TelegramWebhook {

    private final UserRepository userRepository;
    private final TelegramService telegramService;

    public TelegramWebhook(UserRepository userRepository, TelegramService telegramService) {
        this.userRepository = userRepository;
        this.telegramService = telegramService;
    }

    @PostMapping("/webhook/update")
    @Transactional
    public ResponseEntity<Void> receberUpdate(@RequestBody Map<String, Object> update) {
        Map<String, Object> message = (Map<String, Object>) update.get("message");
        if (message == null) return ResponseEntity.ok().build();

        String text = (String) message.get("text");
        Map<String, Object> chat = (Map<String, Object>) message.get("chat");
        if (chat == null || text == null) return ResponseEntity.ok().build();

        Long chatId = ((Number) chat.get("id")).longValue();

        if (text.startsWith("/registrar")) {
            String[] parts = text.split("\\s+");
            if (parts.length >= 2) {
                String token = parts[1].trim();

                userRepository.findByTelegramToken(token).ifPresent(user -> {
                    user.setChatId(chatId);
                    user.setTelegramToken(null); // opcional: invalidar token após vincular
                    userRepository.save(user);

                    // confirma para o usuário via Telegram
                    telegramService.sendMessage(chatId, "✅ Registro concluído! Você vai receber alertas desta região.");
                });
            } else {
                telegramService.sendMessage(chatId, "Uso: /registrar <CÓDIGO>. Peça o código no sistema.");
            }
        } else if (text.startsWith("/start")) {
            telegramService.sendMessage(chatId, "Olá! Para vincular sua conta, use o comando /registrar <CÓDIGO> no bot.");
        }

        return ResponseEntity.ok().build();
    }
}
