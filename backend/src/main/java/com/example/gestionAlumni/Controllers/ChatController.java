package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.Entities.Conversation;
import com.example.gestionAlumni.Entities.Message;
import com.example.gestionAlumni.Entities.User;
import com.example.gestionAlumni.Repos.ConversationRepository;
import com.example.gestionAlumni.Repos.MessageRepository;
import com.example.gestionAlumni.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage/{conversationId}")
    public void sendMessage(@DestinationVariable Long conversationId, @Payload Map<String, Object> payload) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        if (conversation == null) return;

        Long senderId;
        Long receiverId;
        try {
            Map<?, ?> senderMap = (Map<?, ?>) payload.get("sender");
            Map<?, ?> receiverMap = (Map<?, ?>) payload.get("receiver");
            if (senderMap == null || receiverMap == null) return;
            senderId = ((Number) senderMap.get("id")).longValue();
            receiverId = ((Number) receiverMap.get("id")).longValue();
        } catch (Exception e) {
            return; // Malformed payload — silently discard
        }
        String content = (String) payload.get("content");
        String attachmentUrl = (String) payload.get("attachmentUrl");
        String fileName = (String) payload.get("fileName");
        String fileType = (String) payload.get("fileType");

        User sender = userRepository.findById(senderId).orElse(null);
        User receiver = userRepository.findById(receiverId).orElse(null);
        if (sender == null || receiver == null) return;

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setSentDate(LocalDateTime.now());
        message.setConversation(conversation);
        message.setAttachmentUrl(attachmentUrl);
        message.setFileName(fileName);
        message.setFileType(fileType);

        Message saved = messageRepository.save(message);

        Map<String, Object> response = Map.of(
            "id", saved.getId(),
            "content", saved.getContent(),
            "sentDate", saved.getSentDate().toString(),
            "sender", Map.of("id", sender.getId(), "firstName", sender.getFirstName(), "lastName", sender.getLastName()),
            "receiver", Map.of("id", receiver.getId(), "firstName", receiver.getFirstName(), "lastName", receiver.getLastName()),
            "attachmentUrl", saved.getAttachmentUrl() != null ? saved.getAttachmentUrl() : "",
            "fileName", saved.getFileName() != null ? saved.getFileName() : "",
            "fileType", saved.getFileType() != null ? saved.getFileType() : "",
            "isRead", saved.isRead()
        );

        // Send to conversation topic — only users subscribed to this conversation receive it
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId, response);
    }

    @MessageMapping("/chat.typing/{conversationId}")
    public void typing(@DestinationVariable Long conversationId, @Payload Map<String, Object> payload) {
        messagingTemplate.convertAndSend("/topic/conversation/" + conversationId + "/typing", payload);
    }
}
