package com.example.gestionAlumni.Repos;

import com.example.gestionAlumni.Entities.Conversation;
import com.example.gestionAlumni.Entities.Message;
import com.example.gestionAlumni.Entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiverOrderBySentDateAsc(User sender, User receiver);
    List<Message> findByConversationOrderBySentDateAsc(Conversation conversation);

    @Query("SELECT m FROM Message m WHERE m.conversation = :conv ORDER BY m.sentDate ASC")
    List<Message> findAllByConversation(@Param("conv") Conversation conv);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.isRead = false")
    long countUnreadForUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.conversation.id = :convId AND m.receiver.id = :userId AND m.isRead = false")
    long countUnreadInConversation(@Param("convId") Long convId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.conversation.id = :convId AND m.receiver.id = :userId AND m.isRead = false")
    int markConversationAsRead(@Param("convId") Long convId, @Param("userId") Long userId);
}