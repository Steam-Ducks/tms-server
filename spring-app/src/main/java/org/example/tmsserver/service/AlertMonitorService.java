package org.example.tmsserver.service;

import org.example.tmsserver.entity.Level;
import org.example.tmsserver.entity.Region;
import org.example.tmsserver.entity.Role;
import org.example.tmsserver.entity.User;
import org.example.tmsserver.repository.RegionRepository;
import org.example.tmsserver.repository.RoleRepository;
import org.example.tmsserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(AlertMonitorService.class);

    private final LevelService levelService;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final RoleRepository roleRepository;
    private final TelegramService telegramService;

    public AlertMonitorService(LevelService levelService,
                               UserRepository userRepository,
                               RegionRepository regionRepository,
                               RoleRepository roleRepository,
                               TelegramService telegramService) {
        this.levelService = levelService;
        this.userRepository = userRepository;
        this.regionRepository = regionRepository;
        this.telegramService = telegramService;
        this.roleRepository = roleRepository;
    }

    @Scheduled(fixedDelayString = "${alert.monitor.interval.ms:60000}")
    public void verificarEEnviarAlertas() {
        logger.info("=== Início da rotina de verificação de alertas ===");

        List<Region> regioes = regionRepository.findAll();
        for (Region region : regioes) {
            try {
                Level level = levelService.calculateLevelForRegion(region.getIdRegion());
                logger.info("Região {} nível calculado: {}", region.getName(), level.getValue());

                if (level.getValue() >= 4) {
                    List<Role> rolesDaRegiao = roleRepository.findByRegion_IdRegion(region.getIdRegion());

                    for (Role role : rolesDaRegiao) {
                        List<User> users = role.getUsers();
                        for (User user : users) {
                            if (user.getChatId() != null) {
                                String mensagem = String.format(
                                        "⚠️ ALERTA: Região %s atingiu nível %d!",
                                        region.getName(),
                                        level.getValue()
                                );
                                telegramService.sendMessage(user.getChatId(), mensagem);
                                logger.info("Mensagem enviada para {}", user.getUsername());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Erro ao processar região {}: {}", region.getName(), e.getMessage());
            }
        }

        logger.info("=== Fim da rotina de verificação de alertas ===");
    }
}
