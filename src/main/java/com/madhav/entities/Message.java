package com.madhav.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_id")
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    // One chat have many messages
    @ManyToOne
    private Chat chat;

    // one user can send many messages
    @ManyToOne
    private User sender;
}
