package org.example.tmsserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "APP_LEVEL") // Diz ao Spring que esta classe representa a tabela APP_LEVEL
public class AppLevel {

    @Id // Marca a chave primária (mesmo que não a usemos diretamente na query)
    private Long id;

    @Column(name = "ID_REGION") // Mapeia a coluna ID_REGION
    private Long idRegion;

    @Column(name = "VALUE") // Mapeia a coluna VALUE (que já é o nível final)
    private int value;

    @Column(name = "TIME") // Mapeia a coluna TIME, para podermos ordenar
    private LocalDateTime time;

    // Getters e Setters para que o Spring possa acessar os dados
    public Long getIdRegion() { return idRegion; }
    public int getValue() { return value; }
    // ... outros getters e setters
}
