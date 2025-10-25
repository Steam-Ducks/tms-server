package org.example.tmsserver.service;

import org.example.tmsserver.bot.TelegramPollingBot;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertMonitorService {
    private final TelegramPollingBot telegramBot;
    private final UserRepository userRepository;
    private final TelegramService telegramService;

    private final Map<Integer, LocalDateTime> lastAlertTimeByRegion = new HashMap<>();

    public AlertMonitorService(TelegramPollingBot telegramBot,
                               UserRepository userRepository,
                               TelegramService telegramService) {
        this.telegramBot = telegramBot;
        this.userRepository = userRepository;
        this.telegramService = telegramService;
    }

    public void sendCriticalAlert(Region region, int level, String severity, String description) {
        System.out.println("🔥 DEBUG: AlertMonitorService.sendCriticalAlert chamado");
        System.out.println("🔥 DEBUG: Região: " + region.getName() + ", Nível: " + level);

        if (shouldSendAlert(region.getIdRegion())) {
            System.out.println("🔥 DEBUG: Deve enviar alerta - passou no controle de spam");

            String alertMessage = buildCriticalAlertMessage(region, level, severity, description);
            System.out.println("🔥 DEBUG: Mensagem construída: " + alertMessage);

            telegramBot.sendAlertForRegion(region, alertMessage);
            lastAlertTimeByRegion.put(region.getIdRegion(), LocalDateTime.now());

            System.out.println("✅ DEBUG: Alerta crítico processado para região: " + region.getName());
        } else {
            System.out.println("⏰ DEBUG: Alerta ignorado - controle de spam ativo");
        }
    }

    public void triggerCriticalAlert(Region region, String indicator, String currentLevel) {
        String alertMessage = String.format(
                "🔴 ALERTA CRÍTICO 🔴\n\n" +
                        "Indicador: %s\n" +
                        "Região: %s\n" +
                        "Nível Atual: %s\n\n" +
                        "Nível crítico atingido! Ação imediata necessária.",
                indicator, region.getName(), currentLevel
        );
        telegramBot.sendAlertForRegion(region, alertMessage);
    }

    public void triggerTrafficAlert(Region region, String severity, String description) {
        String alertMessage = String.format(
                "🚨 ALERTA DE TRÁFEGO 🚨\n\n" +
                        "Região: %s\n" +
                        "Gravidade: %s\n" +
                        "Descrição: %s\n\n" +
                        "Tome as devidas precauções.",
                region.getName(), severity, description
        );
        telegramBot.sendAlertForRegion(region, alertMessage);
    }

    public void sendNormalizationAlert(Region region, int previousLevel, int currentLevel) {
        if (previousLevel >= 4 && currentLevel <= 3) {
            String message = String.format(
                    "✅ *SITUAÇÃO NORMALIZADA*\n\n" +
                            "📍 *Região:* %s\n" +
                            "📊 *Nível atual:* %d/5\n" +
                            "📉 *Melhoria:* Nível %d → %d\n\n" +
                            "_Tráfego voltando ao normal na região._",
                    region.getName(), currentLevel, previousLevel, currentLevel
            );
            telegramBot.sendAlertForRegion(region, message);
            System.out.println("✅ Alerta de normalização enviado para região: " + region.getName());
        }
    }

    public void sendDirectAlert(Long chatId, String message) {
        System.out.println("🎯 DEBUG: sendDirectAlert chamado para chatId: " + chatId);
        try {
            telegramService.sendMessage(chatId, message);
            System.out.println("✅ DEBUG: Mensagem direta enviada com SUCESSO para chatId: " + chatId);
        } catch (Exception e) {
            System.err.println("❌ ERRO em sendDirectAlert: " + e.getMessage());
        }
    }

    public void sendDirectCriticalAlert(Long chatId) {
        String alertMessage = "🔴 *ALERTA CRÍTICO DE TESTE* 🔴\n\n📍 *Região:* Centro\n📊 *Nível:* 5/5 🔴\n⚠️ *Gravidade:* MUITO ALTO\n📝 *Situação:* Congestionamento crítico detectado\n\n_Este é um teste do sistema de alertas._";
        sendDirectAlert(chatId, alertMessage);
    }

    public void sendToSpecificUsers(List<Long> chatIds, String message) {
        System.out.println("🎯 DEBUG: Enviando para " + chatIds.size() + " chatIds específicos");
        for (Long chatId : chatIds) {
            sendDirectAlert(chatId, message);
        }
    }

    public void sendTestAlert(Long chatId) {
        String testMessage = "🧪 *TESTE DE ALERTA*\n\nEste é um teste do sistema de alertas de tráfego.\nSe você recebeu esta mensagem, o sistema está funcionando!";
        System.out.println("TESTE: " + testMessage + " para chatId: " + chatId);
    }

    private boolean shouldSendAlert(Integer regionId) {
        LocalDateTime lastAlert = lastAlertTimeByRegion.get(regionId);
        if (lastAlert == null) return true;
        LocalDateTime now = LocalDateTime.now();
        return lastAlert.plusMinutes(5).isBefore(now);
    }

    private String buildCriticalAlertMessage(Region region, int level, String severity, String description) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String levelEmoji = level == 5 ? "🔴" : "🟠";
        String severityEmoji = level == 5 ? "🚨" : "⚠️";
        return String.format(
                "%s *ALERTA DE TRÁFEGO CRÍTICO* %s\n\n%s *Região:* %s\n📊 *Nível:* %d/5 %s\n⚠️ *Gravidade:* %s\n📝 *Situação:* %s\n🕒 *Horário:* %s\n\n_Recomenda-se evitar a região e buscar rotas alternativas._",
                severityEmoji, severityEmoji, levelEmoji, region.getName(), level, levelEmoji, severity, description, timestamp
        );
    }
}