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

            if (text.startsWith("/start")) {
                telegramService.sendMessage(chatId, "Bot TMS rodando! Você receberá alertas de tráfego.");
                registerUser(update.getMessage().getFrom(), chatId);
            }
        }
    }

    private void registerUser(org.telegram.telegrambots.meta.api.objects.User telegramUser, Long chatId) {
        // Implement logic to register user in the system (sprint 3)
    }

    public void sendAlertForRegion(Region region, String alertMessage) {
        try {
            List<Role> roles = roleRepository.findByRegionsContaining(region.getIdRegion());
            int usersNotified = 0;

            for (Role role : roles) {
                List<User> users = userRepository.findByRole(role);

                for (User user : users) {
                    if (user.getChatId() != null) {
                        telegramService.sendMessage(user.getChatId(), alertMessage);
                        System.out.println("BOT: Enviando mensagem para: " + user.getUsername() + ")");

                        usersNotified++;
                    }
                }
            }

            System.out.println("Total de " + usersNotified + " usuários notificados para região: " + region.getName());

        } catch (Exception e) {
            System.err.println("Erro em sendAlertForRegion: " + e.getMessage());
        }
    }

    public void sendAlertToAll(String alertMessage) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getChatId() != null) {
                telegramService.sendMessage(user.getChatId(), alertMessage);
            }
        }
    }
}