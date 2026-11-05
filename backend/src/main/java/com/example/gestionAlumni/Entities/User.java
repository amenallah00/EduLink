package com.example.gestionAlumni.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false, unique = true)
    String email;

    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    String password;

    @Column(columnDefinition = "boolean default true")
    Boolean active = true;

    @Column(columnDefinition = "boolean default false")
    Boolean verified = false;

    String verificationToken;

    String avatarUrl;

    @Column(name = "graduation_year")
    Integer graduationYear;
    @JsonIgnore
    @OneToMany(mappedBy = "receiver")
    List<Message> receivedMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "sender")
    List<Message> sentMessages;

    @JsonIgnore
    @OneToMany(mappedBy = "host")
    List<Event> hostedEvents;

    @JsonIgnore
    @ManyToMany
    List<Event> events;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active != null && active;
    }

    public boolean isVerified() {
        return verified != null && verified;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Long getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}