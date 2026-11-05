package com.example.gestionAlumni.Services;

import com.example.gestionAlumni.Entities.Event;
import com.example.gestionAlumni.Entities.EventType;
import com.example.gestionAlumni.Entities.User;
import com.example.gestionAlumni.Repos.EventRepository;
import com.example.gestionAlumni.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getApprovedEvents() {
        return eventRepository.findByStatusOrderByStartDateAsc("APPROVED");
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        if ("RECRUITER".equalsIgnoreCase(event.getCreatedByRole())) {
            event.setStatus("PENDING");
        } else {
            event.setStatus("APPROVED");
        }
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updated) {
        Event existing = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setDuration(updated.getDuration());
        existing.setType(updated.getType());
        existing.setLocation(updated.getLocation());
        existing.setFormat(updated.getFormat());
        existing.setVirtualLink(updated.getVirtualLink());
        existing.setSeats(updated.getSeats());
        existing.setDomain(updated.getDomain());
        existing.setSpeakers(updated.getSpeakers());
        existing.setSchedule(updated.getSchedule());
        existing.setPrerequisites(updated.getPrerequisites());
        existing.setTargetAudience(updated.getTargetAudience());
        existing.setCompanies(updated.getCompanies());
        existing.setBannerUrl(updated.getBannerUrl());
        existing.setHighlighted(updated.isHighlighted());

        return eventRepository.save(existing);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Event registerParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (event.getSeats() > 0 && event.getRegisteredCount() >= event.getSeats()) {
            throw new RuntimeException("Event is full");
        }

        if (event.getParticipants().stream().anyMatch(p -> p.getId().equals(userId))) {
            throw new RuntimeException("Already registered");
        }

        event.getParticipants().add(user);
        event.setRegisteredCount(event.getRegisteredCount() + 1);
        return eventRepository.save(event);
    }

    public Event unregisterParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        boolean removed = event.getParticipants().removeIf(p -> p.getId().equals(userId));
        if (removed) {
            event.setRegisteredCount(Math.max(0, event.getRegisteredCount() - 1));
        }
        return eventRepository.save(event);
    }

    public Event approveEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus("APPROVED");
        return eventRepository.save(event);
    }

    public Event rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus("REJECTED");
        return eventRepository.save(event);
    }

    public List<Event> getEventsByType(EventType type) {
        return eventRepository.findByType(type);
    }

    public List<Event> getUpcomingEvents() {
        return eventRepository.findByStartDateAfter(LocalDateTime.now());
    }

    public List<Event> getEventsByParticipant(Long userId) {
        return eventRepository.findEventsByParticipantId(userId);
    }

    public List<Event> getPendingEvents() {
        return eventRepository.findByStatus("PENDING");
    }

    public List<Event> getEventsByHost(Long userId) {
        return eventRepository.findByHost_Id(userId);
    }
}
