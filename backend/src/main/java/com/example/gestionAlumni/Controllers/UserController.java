package com.example.gestionAlumni.Controllers;

import com.example.gestionAlumni.Entities.User;
import com.example.gestionAlumni.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Store avatars in a folder next to the running application
    private final Path avatarStorageDir = Paths.get("uploads/avatars").toAbsolutePath().normalize();

    public UserController() {
        try {
            Files.createDirectories(avatarStorageDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create avatar upload directory", e);
        }
    }

    /**
     * Upload a profile avatar image for a user.
     * POST /api/users/{id}/avatar  (multipart/form-data, field name = "file")
     * Returns the updated User object with avatarUrl set.
     */
    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id,
                                          @RequestParam("file") MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body("Only image files are allowed");
        }

        // Validate file size (max 5 MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body("File size must be under 5 MB");
        }

        try {
            // Generate a unique filename to avoid collisions
            String extension = getFileExtension(file.getOriginalFilename());
            String filename = "avatar_" + id + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            // Delete old avatar file if it exists
            String oldAvatarUrl = user.getAvatarUrl();
            if (oldAvatarUrl != null && !oldAvatarUrl.isBlank()) {
                try {
                    String oldFilename = oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf('/') + 1);
                    Path oldFilePath = avatarStorageDir.resolve(oldFilename).normalize();
                    Files.deleteIfExists(oldFilePath);
                } catch (Exception ignored) {
                    // Old file cleanup is best-effort
                }
            }

            // Save the new file
            Path targetPath = avatarStorageDir.resolve(filename).normalize();
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Store the URL that the frontend can use to fetch the image
            String avatarUrl = "/api/users/avatars/" + filename;
            user.setAvatarUrl(avatarUrl);
            userRepository.save(user);

            // Return a simple JSON response with the new avatar URL
            return ResponseEntity.ok(java.util.Map.of("avatarUrl", avatarUrl));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to save avatar: " + e.getMessage());
        }
    }

    /**
     * Serve an avatar image by filename.
     * GET /api/users/avatars/{filename}
     */
    @GetMapping("/avatars/{filename:.+}")
    public ResponseEntity<Resource> serveAvatar(@PathVariable String filename) {
        try {
            Path filePath = avatarStorageDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Determine content type
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) return ".jpg";
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex >= 0 ? filename.substring(dotIndex) : ".jpg";
    }
}
