package ru.practicum.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "items")

public class Item {
    @Id
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "url")
    private String url;

}