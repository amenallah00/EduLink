package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.Entities.Conversation;
import com.example.gestionAlumni.Entities.Message;
import com.example.gestionAlumni.Services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    private static final String UPLOAD_DIR = "uploads/chat/";

    @PostMapping("/send")
    public Message sendMessage(@RequestParam Long senderId,
                               @RequestParam Long receiverId,
                               @RequestParam String content) {
        return messageService.sendMessage(senderId, receiverId, content);
    }

    // Fixed: returns bidirectional conversation messages
    @GetMapping("/between")
    public List<Message> getMessagesBetweenUsers(@RequestParam Long senderId,
                                                 @RequestParam Long receiverId) {
        return messageService.getMessagesBetweenUsers(senderId, receiverId);
    }

    @GetMapping("/conversation/{conversationId}")
    public List<Message> getConversationMessages(@PathVariable Long conversationId) {
        return messageService.getConversationMessages(conversationId);
    }

    // Get or create conversation between two users
    @PostMapping("/conversations/get-or-create")
    public ResponseEntity<Map<String, Object>> getOrCreateConversation(@RequestParam Long userId1,
                                                                       @RequestParam Long userId2) {
        Conversation c = messageService.getOrCreateConversationByIds(userId1, userId2);
        return ResponseEntity.ok(Map.of(
            "conversationId", c.getId(),
            "user1", Map.of("id", c.getUser1().getId(), "firstName", c.getUser1().getFirstName(), "lastName", c.getUser1().getLastName()),
            "user2", Map.of("id", c.getUser2().getId(), "firstName", c.getUser2().getFirstName(), "lastName", c.getUser2().getLastName())
        ));
    }

    // List all conversations for a user
    @GetMapping("/conversations/{userId}")
    public List<Map<String, Object>> getUserConversations(@PathVariable Long userId) {
        return messageService.getUserConversations(userId);
    }

    // Mark messages as read
    @PostMapping("/read/{conversationId}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long conversationId, @RequestParam Long userId) {
        messageService.markAsRead(conversationId, userId);
        return ResponseEntity.ok().build();
    }

    // Get unread count
    @GetMapping("/unread/{userId}")
    public Map<String, Long> getUnreadCount(@PathVariable Long userId) {
        return Map.of("count", messageService.getUnreadCount(userId));
    }

    // Update conversation tags
    @PutMapping("/conversations/{conversationId}/tags")
    public ResponseEntity<Conversation> updateTags(@PathVariable Long conversationId, @RequestBody Map<String, String> body) {
        Conversation c = messageService.updateTags(conversationId, body.get("tags"));
        return ResponseEntity.ok(c);
    }

    // File upload endpoint for attachments
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalName = file.getOriginalFilename();
            String safeName = UUID.randomUUID() + "_" + (originalName != null ? originalName.replaceAll("[^a-zA-Z0-9._-]", "_") : "file");
            Path filePath = uploadPath.resolve(safeName);
            Files.write(filePath, file.getBytes());

            String fileType = "FILE";
            String contentType = file.getContentType();
            if (contentType != null && contentType.startsWith("image/")) {
                fileType = "IMAGE";
            }

            return ResponseEntity.ok(Map.of(
                "url", "/api/messages/files/" + safeName,
                "fileName", originalName != null ? originalName : "file",
                "fileType", fileType
            ));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Upload failed"));
        }
    }

    // Serve uploaded files
    @GetMapping("/files/{filename}")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        try {
            // Sanitize filename to prevent path traversal
            String safeName = Paths.get(filename).getFileName().toString();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(safeName);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }
            byte[] data = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            return ResponseEntity.ok()
                .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                .body(data);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
