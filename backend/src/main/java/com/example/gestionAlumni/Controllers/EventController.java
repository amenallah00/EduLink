package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.Entities.Event;
import com.example.gestionAlumni.Entities.EventType;
import com.example.gestionAlumni.Services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getApprovedEvents());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllIncludingPending() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(id, event));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/register/{userId}")
    public ResponseEntity<?> registerParticipant(@PathVariable Long id, @PathVariable Long userId) {
        try {
            return ResponseEntity.ok(eventService.registerParticipant(id, userId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/unregister/{userId}")
    public ResponseEntity<Event> unregisterParticipant(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok(eventService.unregisterParticipant(id, userId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Event> approveEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.approveEvent(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Event> rejectEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.rejectEvent(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Event>> getPendingEvents() {
        return ResponseEntity.ok(eventService.getPendingEvents());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Event>> getByType(@PathVariable EventType type) {
        return ResponseEntity.ok(eventService.getEventsByType(type));
    }

    @GetMapping("/participant/{userId}")
    public ResponseEntity<List<Event>> getByParticipant(@PathVariable Long userId) {
        return ResponseEntity.ok(eventService.getEventsByParticipant(userId));
    }

    @GetMapping("/host/{userId}")
    public ResponseEntity<List<Event>> getByHost(@PathVariable Long userId) {
        return ResponseEntity.ok(eventService.getEventsByHost(userId));
    }
}
