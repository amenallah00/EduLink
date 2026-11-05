package com.example.gestionAlumni.Entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(length = 1000, nullable = false)
    String content;
    LocalDateTime sentDate;
    
    @ManyToOne
    @JoinColumn(name="sender_id",nullable = false)
    User sender;
    
    @ManyToOne
    @JoinColumn(name="receiver_id",nullable = false)
    User receiver;

    String attachmentUrl;
    String fileName;
    String fileType; // TEXT, IMAGE, FILE
    boolean isRead = false;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="conversation_id", nullable = false)
    Conversation conversation;
}