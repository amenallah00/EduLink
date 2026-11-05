package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.Event;
import com.example.gestionAlumni.Entities.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByHost_Id(Long userId);

    List<Event> findByAdmin_Id(Long adminId);

    List<Event> findByType(EventType type);

    List<Event> findByStatus(String status);

    List<Event> findByStartDateAfter(LocalDateTime now);

    List<Event> findByStartDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT e FROM Event e JOIN e.participants p WHERE p.id = :userId")
    List<Event> findEventsByParticipantId(@Param("userId") Long userId);

    List<Event> findByStatusOrderByStartDateAsc(String status);
}