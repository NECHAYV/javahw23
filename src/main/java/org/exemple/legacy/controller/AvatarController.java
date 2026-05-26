package org.exemple.legacy.controller;

import org.exemple.legacy.model.Avatar;
import org.exemple.legacy.service.AvatarService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/student")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    /**
     * Загрузка аватара для студента.
     * POST /student/{studentId}/avatar
     */
    @PostMapping("/{studentId}/avatar")
    public ResponseEntity<Long> uploadAvatar(@PathVariable Long studentId,
                                             @RequestParam MultipartFile file) throws IOException {
        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(avatar.getId());
    }

    /**
     * Получение аватара из базы данных.
     * GET /student/{studentId}/avatar/db
     */
    @GetMapping("/{studentId}/avatar/db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long studentId) {
        Avatar avatar = avatarService.getAvatarFromDb(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.ok().headers(headers).body(avatar.getData());
    }

    /**
     * Получение аватара с локального диска.
     * GET /student/{studentId}/avatar/disk
     */
    @GetMapping("/{studentId}/avatar/disk")
    public ResponseEntity<byte[]> getAvatarFromDisk(@PathVariable Long studentId) throws IOException, IOException {
        byte[] data = avatarService.getAvatarFromDisk(studentId);
        Avatar avatar = avatarService.getAvatarFromDb(studentId); // для определения mediaType
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(data.length);
        return ResponseEntity.ok().headers(headers).body(data);
    }
}
