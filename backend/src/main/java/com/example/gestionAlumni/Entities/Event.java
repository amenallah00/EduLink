package com.example.gestionAlumni.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private String duration;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private String location;

    @Column(name = "event_format")
    private String format; // presentiel, distanciel, hybride

    @Column(name = "virtual_link")
    private String virtualLink;

    private int seats;

    @Column(name = "registered_count")
    private int registeredCount = 0;

    private String domain;

    private String speakers; // comma-separated

    @Column(length = 2000)
    private String schedule;

    @Column(length = 1000)
    private String prerequisites;

    @Column(name = "target_audience")
    private String targetAudience;

    private String companies; // comma-separated

    @Column(name = "banner_url")
    private String bannerUrl;

    private boolean highlighted = false;

    @Column(name = "event_status")
    private String status = "APPROVED";

    @Column(name = "created_by_role")
    private String createdByRole = "ADMIN";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
        name = "event_participants",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties({"sentMessages", "receivedMessages", "password"})
    private List<User> participants = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnoreProperties({"sentMessages", "receivedMessages", "password"})
    private Administrator admin;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User host;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
    }

    public void cancelEvent() {
        this.status = "CANCELLED";
        this.endDate = LocalDateTime.now();
    }
}