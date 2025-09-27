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
        this.name = capitalizeWords(name);
        this.level = level;
    }

    private String capitalizeWords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }

        // Special case mapping
        String normalizedInput = input.toLowerCase().trim();
        if (normalizedInput.equals("centro")) {
            return "Zona Central";
        }

        String[] words = normalizedInput.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }

            String word = words[i];
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1));
            }
        }

        return result.toString();
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