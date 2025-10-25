package org.example.tmsserver.controller;

import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final UserRepository userRepository;

    public TelegramController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/webhook/message")
    public ResponseEntity<?> receberMensagem(@RequestBody Map<String, Object> update) {
        try {
            Map<String, Object> message = (Map<String, Object>) update.get("message");
            if (message == null) return ResponseEntity.ok().build();

            String text = (String) message.get("text");
            Map<String, Object> chat = (Map<String, Object>) message.get("chat");
            Long chatId = ((Number) chat.get("id")).longValue();

            if (text != null && text.startsWith("/registrar")) {
                String[] partes = text.split(" ");
                if (partes.length == 2) {
                    String nome = partes[1].trim();

                    User user = userRepository.findByUsername(nome)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + nome));

                    user.setChatId(chatId);
                    userRepository.save(user);

                    return ResponseEntity.ok("Usuário registrado com sucesso!");
                }
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }
}
