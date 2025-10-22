package org.example.tmsserver.controller;

import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final UserRepository userRepository;

    public TelegramController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestParam Long chatId, @RequestParam String nome) {
        User user = userRepository.findByUsername(nome)
                .orElseGet(() -> new User(nome, chatId));
        user.setChatId(chatId);
        userRepository.save(user);
        return ResponseEntity.ok("Responsável registrado!");
    }
}