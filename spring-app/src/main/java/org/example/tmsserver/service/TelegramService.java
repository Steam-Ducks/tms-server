// No seu TelegramService.java existente, adicione:
package org.example.tmsserver.service;

import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class TelegramService {

    private final TelegramLongPollingBot bot;
    private final UserRepository userRepository;
    private final SecureRandom random;

    public TelegramService(@Lazy TelegramLongPollingBot bot, UserRepository userRepository) {
        this.bot = bot;
        this.userRepository = userRepository;
        this.random = new SecureRandom();
    }

    // Seus métodos existentes...
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

    public String generateTelegramCode() {
        // Gera um número aleatório entre 1000 e 9999 (4 dígitos)
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    public String generateAndSaveTelegramCodeForUser(String username) {
        try {
            System.out.println("🔍 Buscando usuário: " + username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));

            System.out.println("✅ Usuário encontrado - ID: " + user.getId() + ", Nome: " + user.getUsername());

            // Gera um código único que não está sendo usado por outro usuário
            String code;
            do {
                code = generateTelegramCode();
            } while (userRepository.findByTelegramId(Integer.parseInt(code)).isPresent());

            // Salva o código no campo telegramId do usuário
            user.setTelegramId(Integer.parseInt(code));
            userRepository.save(user);

            System.out.println("🎯 Código gerado e salvo: " + code + " para usuário: " + username);

            return code;

        } catch (Exception e) {
            System.err.println("💥 ERRO em generateAndSaveTelegramCodeForUser: " + e.getMessage());
            throw new RuntimeException("Erro ao gerar código: " + e.getMessage());
        }
    }

    public Optional<String> getTelegramCodeForUser(String username) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);

            if (userOpt.isPresent() && userOpt.get().getTelegramId() != null) {
                String code = String.valueOf(userOpt.get().getTelegramId());
                System.out.println("📋 Código encontrado para " + username + ": " + code);
                return Optional.of(code);
            }

            System.out.println("ℹ️  Nenhum código encontrado para: " + username);
            return Optional.empty();

        } catch (Exception e) {
            System.err.println("💥 ERRO em getTelegramCodeForUser: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar código: " + e.getMessage());
        }
    }

    public void clearTelegramCodeForUser(String username) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setTelegramId(null);
                userRepository.save(user);
                System.out.println("🧹 Código removido para usuário: " + username);
            } else {
                System.out.println("⚠️  Usuário não encontrado para limpar código: " + username);
            }

        } catch (Exception e) {
            System.err.println("💥 ERRO em clearTelegramCodeForUser: " + e.getMessage());
            throw new RuntimeException("Erro ao limpar código: " + e.getMessage());
        }
    }

}