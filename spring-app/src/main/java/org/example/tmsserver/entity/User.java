package org.example.tmsserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_phone", columnNames = "phone_number"),
                @UniqueConstraint(name = "uk_username", columnNames = "username")
        })

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role")
    private Role role;

    @Column(name="email", unique = true)
    private String email;

    @Column(name = "phone_number", columnDefinition = "CHAR(11)")
    private String phoneNumber;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name= "chat_id", nullable = true)
    private Long chatId;

    @Column(name="telegram_id", nullable = true)
    private Integer telegramId;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    public User() {
    }

    public User(Integer id, Role role, String email, String phoneNumber, String username, String password, Long chatId, Integer telegramId, Boolean enabled) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.chatId = chatId;
        this.telegramId = telegramId;
        this.enabled = enabled;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() { return username; }
    
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
   
    public void setPassword(String password) { this.password = password; }

    public Boolean getEnabled() { return enabled; }
    
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }


    public Integer getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(Integer telegramId) {
        this.telegramId = telegramId;
    }
}