package org.example.tmsserver.dto;

public record RegisterRequest(String username, String email, String password, Integer roleId, String phoneNumber) {}