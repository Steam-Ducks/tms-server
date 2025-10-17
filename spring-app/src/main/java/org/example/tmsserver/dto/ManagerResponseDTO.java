package org.example.tmsserver.dto;

public class ManagerResponseDTO {

    private Integer id;
    private String email;
    private String phoneNumber;
    private String roleDescription;

    // Construtor
    public ManagerResponseDTO(Integer id, String email, String phoneNumber, String roleDescription) {
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleDescription = roleDescription;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}