package org.exemple.legacy.controller;

import org.exemple.legacy.model.Avatar;
import org.exemple.legacy.service.AvatarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    // Загрузка аватара
    @PostMapping("/student/{studentId}/avatar")
    public ResponseEntity<Long> uploadAvatar(@PathVariable Long studentId,
                                             @RequestParam MultipartFile file) throws IOException {
        Avatar avatar = avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(avatar.getId());
    }

    // Получение аватара из БД
    @GetMapping("/student/{studentId}/avatar/db")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long studentId) {
        Avatar avatar = avatarService.getAvatarFromDb(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.ok().headers(headers).body(avatar.getData());
    }

    // Получение аватара с диска
    @GetMapping("/student/{studentId}/avatar/disk")
    public ResponseEntity<byte[]> getAvatarFromDisk(@PathVariable Long studentId) throws IOException {
        byte[] data = avatarService.getAvatarFromDisk(studentId);
        Avatar avatar = avatarService.getAvatarFromDb(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(data.length);
        return ResponseEntity.ok().headers(headers).body(data);
    }

    // Пагинация аватарок
    @GetMapping("/avatars")
    public ResponseEntity<Page<Avatar>> getAvatars(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Page<Avatar> avatarPage = avatarService.getAllAvatars(PageRequest.of(page, size));
        return ResponseEntity.ok(avatarPage);
    }


}
