package org.example.tmsserver.bot;

import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.Role;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.RegionRepository;
import org.example.tmsserver.repository.RoleRepository;
import org.example.tmsserver.repository.UserRepository;
import org.example.tmsserver.service.AlertService;
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
    private final AlertService alertService;
    private final String botToken;
    private final String botUsername;

    public TelegramPollingBot(@Value("${telegram.bot.token}") String botToken,
                              @Value("${telegram.bot.username}") String botUsername,
                              UserRepository userRepository,
                              RoleRepository roleRepository,
                              RegionRepository regionRepository,
                              TelegramService telegramService,
                              AlertService alertService) {
        super(botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.regionRepository = regionRepository;
        this.telegramService = telegramService;
        this.alertService = alertService;
        System.out.println("🚀 TelegramPollingBot inicializado com sucesso!");
        System.out.println("🤖 Bot Username: " + botUsername);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("\n=== 📨 NOVA MENSAGEM RECEBIDA ===");

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getUserName();

            System.out.println("💬 Mensagem: " + text);
            System.out.println("👤 Usuário Telegram: @" + userName);
            System.out.println("🆔 Chat ID: " + chatId);

            if (text.startsWith("/start")) {
                System.out.println("🔹 Comando /start detectado");
                handleStartCommand(chatId);
            } else if (text.startsWith("/register")) {
                System.out.println("🔹 Comando /register detectado");
                handleRegisterCommand(chatId, text);
            } else {
                System.out.println("⚠️ Comando não reconhecido: " + text);
            }
        } else {
            System.out.println("❌ Mensagem sem texto ou update inválido");
        }

        System.out.println("=== FIM DA MENSAGEM ===\n");
    }

    private void handleStartCommand(Long chatId) {
        System.out.println("🎯 Executando handleStartCommand para chatId: " + chatId);

        String message = "🤖 Bot TMS Alertas\n\n" +
                "Para se registrar e receber alertas de tráfego, use:\n" +
                "/register <código>\n\n" +
                "Obtenha o código de 4 dígitos no sistema web.";

        telegramService.sendMessage(chatId, message);
        System.out.println("✅ Mensagem de start enviada para chatId: " + chatId);
    }

    private void handleRegisterCommand(Long chatId, String text) {
        System.out.println("🎯 Executando handleRegisterCommand");
        System.out.println("📝 Texto completo: " + text);
        System.out.println("🆔 Chat ID: " + chatId);

        try {
            String[] parts = text.split(" ");
            System.out.println("🔍 Partes do comando: " + parts.length);

            if (parts.length != 2) {
                System.out.println("❌ Formato incorreto - esperadas 2 partes, encontradas: " + parts.length);
                telegramService.sendMessage(chatId,
                        "❌ Formato incorreto. Use: /register <código>");
                return;
            }

            String code = parts[1].trim();
            System.out.println("🔢 Código recebido: '" + code + "'");

            // Valida se o código tem 4 dígitos
            if (!code.matches("\\d{4}")) {
                System.out.println("❌ Código inválido - não possui 4 dígitos: " + code);
                telegramService.sendMessage(chatId,
                        "❌ Código inválido. O código deve ter 4 dígitos.");
                return;
            }

            // ✅ CORREÇÃO: Converter para Integer antes de buscar
            Integer telegramCode = Integer.parseInt(code);
            System.out.println("🔍 Buscando usuário com telegramId: " + telegramCode);

            // ✅ CORREÇÃO: Buscar por Integer, não String
            User user = userRepository.findByTelegramId(telegramCode)
                    .orElse(null);

            if (user == null) {
                System.out.println("❌ Nenhum usuário encontrado com telegramId: " + telegramCode);
                telegramService.sendMessage(chatId,
                        "❌ Código não encontrado. Verifique o código e tente novamente.");
                return;
            }

            System.out.println("✅ Usuário encontrado: " + user.getUsername() + " (ID: " + user.getId() + ")");
            System.out.println("🔍 Verificando se usuário já possui chatId...");

            // Verifica se já está registrado (já tem chatId)
            if (user.getChatId() != null) {
                System.out.println("⚠️ Usuário já registrado - chatId existente: " + user.getChatId());
                telegramService.sendMessage(chatId,
                        "⚠️ Este código já foi utilizado.");
                return;
            }

            System.out.println("📝 Registrando chatId " + chatId + " para usuário: " + user.getUsername());

            // Registra o chatId no usuário
            user.setChatId(chatId);
            userRepository.save(user);

            System.out.println("💾 Usuário salvo no banco de dados com sucesso");

            String successMessage = "✅ Registrado com sucesso! Você agora receberá alertas de tráfego.\n\n" +
                    "Usuário: " + user.getUsername();

            telegramService.sendMessage(chatId, successMessage);
            System.out.println("✅ Mensagem de sucesso enviada para chatId: " + chatId);

            System.out.println("🎉 REGISTRO CONCLUÍDO - Usuário " + user.getUsername() +
                    " registrado no Telegram com chatId: " + chatId);

        } catch (NumberFormatException e) {
            System.err.println("❌ Erro de conversão numérica: " + e.getMessage());
            telegramService.sendMessage(chatId, "❌ Código inválido. Use apenas números.");
        } catch (Exception e) {
            System.err.println("💥 ERRO CRÍTICO no handleRegisterCommand:");
            System.err.println("📝 Mensagem de erro: " + e.getMessage());
            System.err.println("🔍 Stack trace:");
            e.printStackTrace();

            telegramService.sendMessage(chatId, "❌ Erro no registro: " + e.getMessage());
        }
    }

    public void sendAlertForRegion(Region region, String alertMessage) {
        System.out.println("\n=== 🚨 ENVIANDO ALERTA PARA REGIÃO ===");
        System.out.println("📍 Região: " + region.getName() + " (ID: " + region.getIdRegion() + ")");
        System.out.println("📢 Mensagem: " + alertMessage);

        try {
            List<Role> roles = roleRepository.findByRegionsContaining(region.getIdRegion());
            System.out.println("👥 Roles encontradas: " + roles.size());

            int usersNotified = 0;
            int totalUsers = 0;
            int alertsCreated = 0;

            for (Role role : roles) {
                List<User> users = userRepository.findByRole(role);
                totalUsers += users.size();

                for (User user : users) {
                    if (user.getChatId() != null) {
                        System.out.println("📤 Enviando alerta para: " + user.getUsername() +
                                " (chatId: " + user.getChatId() + ")");
                        telegramService.sendMessage(user.getChatId(), alertMessage);

                        // Create alert record in database
                        alertService.createAlert(region, user);
                        alertsCreated++;
                        System.out.println("💾 Alerta registrado no banco de dados para: " + user.getUsername());

                        usersNotified++;
                    } else {
                        System.out.println("⏭️  Pulando usuário " + user.getUsername() + " - chatId não configurado");
                    }
                }
            }

            System.out.println("📊 RESUMO DO ALERTA:");
            System.out.println("✅ Usuários notificados: " + usersNotified);
            System.out.println("💾 Alertas criados no banco: " + alertsCreated);
            System.out.println("📋 Total de usuários na região: " + totalUsers);
            System.out.println("📍 Região: " + region.getName());

        } catch (Exception e) {
            System.err.println("💥 ERRO em sendAlertForRegion:");
            System.err.println("📝 " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIM DO ALERTA ===\n");
    }

    public void sendAlertToAll(String alertMessage) {
        System.out.println("\n=== 📢 ENVIANDO ALERTA PARA TODOS ===");
        System.out.println("📢 Mensagem: " + alertMessage);

        List<User> users = userRepository.findAll();
        System.out.println("👥 Total de usuários no sistema: " + users.size());

        int usersNotified = 0;
        int usersSkipped = 0;

        for (User user : users) {
            if (user.getChatId() != null) {
                System.out.println("📤 Enviando para: " + user.getUsername() +
                        " (chatId: " + user.getChatId() + ")");
                telegramService.sendMessage(user.getChatId(), alertMessage);
                usersNotified++;
            } else {
                System.out.println("⏭️  Pulando: " + user.getUsername() + " - sem chatId");
                usersSkipped++;
            }
        }

        System.out.println("📊 RESUMO DO ALERTA GERAL:");
        System.out.println("✅ Usuários notificados: " + usersNotified);
        System.out.println("⏭️  Usuários sem chatId: " + usersSkipped);
        System.out.println("📋 Total de usuários: " + users.size());

        System.out.println("=== FIM DO ALERTA GERAL ===\n");
    }
}