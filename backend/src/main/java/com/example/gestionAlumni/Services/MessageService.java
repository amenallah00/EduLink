package com.example.gestionAlumni.Services;

import com.example.gestionAlumni.Entities.Conversation;
import com.example.gestionAlumni.Entities.Message;
import com.example.gestionAlumni.Entities.User;
import com.example.gestionAlumni.Repos.ConversationRepository;
import com.example.gestionAlumni.Repos.MessageRepository;
import com.example.gestionAlumni.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public Message sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Conversation conversation = getOrCreateConversation(sender, receiver);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentDate(LocalDateTime.now());
        message.setConversation(conversation);

        conversation.setLastMessageAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        return messageRepository.save(message);
    }

    /** Fixed: returns ALL messages in conversation (both directions) */
    public List<Message> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Optional<Conversation> convOpt = conversationRepository.findByUsers(sender, receiver);
        if (convOpt.isEmpty()) return Collections.emptyList();

        return messageRepository.findByConversationOrderBySentDateAsc(convOpt.get());
    }

    public List<Message> getConversationMessages(Long conversationId) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return messageRepository.findByConversationOrderBySentDateAsc(conv);
    }

    public Conversation getOrCreateConversation(User user1, User user2) {
        return conversationRepository.findByUsers(user1, user2)
                .orElseGet(() -> {
                    Conversation c = new Conversation();
                    c.setUser1(user1);
                    c.setUser2(user2);
                    return conversationRepository.save(c);
                });
    }

    public Conversation getOrCreateConversationByIds(Long userId1, Long userId2) {
        User u1 = userRepository.findById(userId1).orElseThrow(() -> new RuntimeException("User not found"));
        User u2 = userRepository.findById(userId2).orElseThrow(() -> new RuntimeException("User not found"));
        return getOrCreateConversation(u1, u2);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getUserConversations(Long userId) {
        List<Conversation> conversations = conversationRepository.findAllByUserId(userId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Conversation c : conversations) {
            User other = c.getUser1().getId().equals(userId) ? c.getUser2() : c.getUser1();
            long unread = messageRepository.countUnreadInConversation(c.getId(), userId);

            // Get last message
            List<Message> msgs = messageRepository.findByConversationOrderBySentDateAsc(c);
            String lastMsg = "";
            String lastDate = "";
            if (!msgs.isEmpty()) {
                Message last = msgs.get(msgs.size() - 1);
                lastMsg = last.getContent();
                lastDate = last.getSentDate().toString();
            }

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("conversationId", c.getId());
            entry.put("otherUser", Map.of(
                "id", other.getId(),
                "firstName", other.getFirstName(),
                "lastName", other.getLastName()
            ));
            entry.put("lastMessage", lastMsg);
            entry.put("lastMessageAt", lastDate);
            entry.put("unreadCount", unread);
            entry.put("tags", c.getTags() != null ? c.getTags() : "");
            entry.put("isGroup", c.isGroup());
            entry.put("name", c.getName() != null ? c.getName() : "");
            result.add(entry);
        }

        result.sort((a, b) -> {
            String dateA = (String) a.get("lastMessageAt");
            String dateB = (String) b.get("lastMessageAt");
            if (dateA.isEmpty() && dateB.isEmpty()) return 0;
            if (dateA.isEmpty()) return 1;
            if (dateB.isEmpty()) return -1;
            return dateB.compareTo(dateA);
        });

        return result;
    }

    @Transactional
    public void markAsRead(Long conversationId, Long userId) {
        messageRepository.markConversationAsRead(conversationId, userId);
    }

    public long getUnreadCount(Long userId) {
        return messageRepository.countUnreadForUser(userId);
    }

    @Transactional
    public Conversation updateTags(Long conversationId, String tags) {
        Conversation c = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        c.setTags(tags);
        return conversationRepository.save(c);
    }
}
