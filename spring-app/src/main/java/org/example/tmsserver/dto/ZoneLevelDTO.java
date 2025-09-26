package org.example.tmsserver.dto;

public class ZoneLevelDTO {

    private String id;
    private String name;
    private int level;

    // Construtor padrão (obrigatório para o Spring)
    public ZoneLevelDTO() {
    }

    // Construtor para facilitar a criação do objeto
    public ZoneLevelDTO(String id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}