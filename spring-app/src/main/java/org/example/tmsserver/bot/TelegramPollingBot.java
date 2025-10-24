package org.example.tmsserver.bot;

import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.Role;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.RegionRepository;
import org.example.tmsserver.repository.RoleRepository;
import org.example.tmsserver.repository.UserRepository;
import org.example.tmsserver.service.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TelegramPollingBot extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RegionRepository regionRepository;
    private final TelegramService telegramService;
    private final String botToken;
    private final String botUsername;

    public TelegramPollingBot(@Value("${telegram.bot.token}") String botToken,
                              @Value("${telegram.bot.username}") String botUsername,
                              UserRepository userRepository,
                              RoleRepository roleRepository,
                              RegionRepository regionRepository,
                              TelegramService telegramService) {
        super(botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.regionRepository = regionRepository;
        this.telegramService = telegramService;
        System.out.println("TelegramPollingBot inicializado com sucesso!");
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println("Mensagem recebida de chatId " + chatId + ": " + text);

            if (text.startsWith("/start")) {
                telegramService.sendMessage(chatId, "Bot TMS rodando! Você receberá alertas de tráfego.");
                System.out.println("Mensagem /start respondida para chatId " + chatId);

                // Opcional: registrar o usuário no sistema
                registerUser(update.getMessage().getFrom(), chatId);
            }
        }
    }

    private void registerUser(org.telegram.telegrambots.meta.api.objects.User telegramUser, Long chatId) {
        // Implemente a lógica para registrar o usuário no seu sistema
        // Isso pode incluir salvar o chatId para enviar alertas futuros
        System.out.println("Usuário " + telegramUser.getFirstName() + " registrado com chatId: " + chatId);
    }

    // Enviar alertas para usuários de uma região
    public void sendAlertForRegion(Region region, String alertMessage) {
        try {
            System.out.println("🔥 DEBUG: TelegramPollingBot.sendAlertForRegion chamado");
            System.out.println("🔥 DEBUG: Região: " + region.getName() + ", ID: " + region.getIdRegion());

            List<Role> roles = roleRepository.findByRegion_IdRegion(region.getIdRegion());
            System.out.println("🔥 DEBUG: Encontrados " + roles.size() + " roles para a região");

            int usersNotified = 0;

            for (Role role : roles) {
                List<User> users = userRepository.findByRole(role);
                System.out.println("🔥 DEBUG: Role " + role.getDescription() + " tem " + users.size() + " usuários");

                for (User user : users) {
                    if (user.getChatId() != null) {
                        System.out.println("🔥 DEBUG: Enviando mensagem para: " + user.getUsername() + " (chatId: " + user.getChatId() + ")");

                        telegramService.sendMessage(user.getChatId(), alertMessage);
                        usersNotified++;

                        System.out.println("✅ DEBUG: Mensagem enviada para: " + user.getUsername());
                    } else {
                        System.out.println("⚠️ DEBUG: Usuário " + user.getUsername() + " não tem chatId");
                    }
                }
            }

            System.out.println("✅ DEBUG: Total de " + usersNotified + " usuários notificados para região: " + region.getName());

        } catch (Exception e) {
            System.err.println("❌ ERRO em sendAlertForRegion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendAlertToAll(String alertMessage) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getChatId() != null) {
                telegramService.sendMessage(user.getChatId(), alertMessage);
                System.out.println("Alerta geral enviado para: " + user.getUsername());
            }
        }
    }
}