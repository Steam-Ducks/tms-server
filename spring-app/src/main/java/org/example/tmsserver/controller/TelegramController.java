package org.example.tmsserver.controller;

import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.example.tmsserver.service.TelegramService;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final UserRepository userRepository;
    private final TelegramService telegramService;

    public TelegramController(UserRepository userRepository, TelegramService telegramService) {
        this.userRepository = userRepository;
        this.telegramService = telegramService;
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

    @PostMapping("/my-telegram/generate-code")
    public ResponseEntity<Map<String, String>> generateMyTelegramCode() {
        try {
            // Abordagem alternativa para pegar o usuário autenticado
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("❌ Nenhuma autenticação encontrada");
                return ResponseEntity.status(401).body(Map.of("error", "Usuário não autenticado"));
            }

            String username = authentication.getName();
            System.out.println("🔐 Usuário autenticado: " + username);

            if (username == null || "anonymousUser".equals(username)) {
                System.out.println("❌ Usuário é anonymous ou null");
                return ResponseEntity.status(401).body(Map.of("error", "Usuário não identificado"));
            }

            String code = telegramService.generateAndSaveTelegramCodeForUser(username);

            Map<String, String> response = new HashMap<>();
            response.put("code", code);
            response.put("message", "Código gerado com sucesso");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar código: " + e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao gerar código: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/my-telegram/get-code")
    public ResponseEntity<Map<String, String>> getMyTelegramCode() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuário não autenticado"));
            }

            String username = authentication.getName();
            System.out.println("🔐 Buscando código para usuário: " + username);

            if (username == null || "anonymousUser".equals(username)) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuário não identificado"));
            }

            var codeOpt = telegramService.getTelegramCodeForUser(username);

            Map<String, String> response = new HashMap<>();
            if (codeOpt.isPresent()) {
                response.put("code", codeOpt.get());
                response.put("message", "Código encontrado");
            } else {
                response.put("message", "Nenhum código gerado");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar código: " + e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao buscar código: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/my-telegram/clear-code")
    public ResponseEntity<Map<String, String>> clearMyTelegramCode() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuário não autenticado"));
            }

            String username = authentication.getName();
            System.out.println("🔐 Limpando código para usuário: " + username);

            if (username == null || "anonymousUser".equals(username)) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuário não identificado"));
            }

            telegramService.clearTelegramCodeForUser(username);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Código removido com sucesso");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Erro ao limpar código: " + e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Erro ao limpar código: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/debug-auth")
    public ResponseEntity<Map<String, Object>> debugAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("authentication", authentication != null ? authentication.getClass().getSimpleName() : "null");
        debugInfo.put("authenticated", authentication != null && authentication.isAuthenticated());
        debugInfo.put("name", authentication != null ? authentication.getName() : "null");
        debugInfo.put("principal", authentication != null ? authentication.getPrincipal().getClass().getSimpleName() : "null");
        debugInfo.put("authorities", authentication != null ? authentication.getAuthorities() : "null");

        System.out.println("🔍 DEBUG AUTH: " + debugInfo);

        return ResponseEntity.ok(debugInfo);
    }
}
