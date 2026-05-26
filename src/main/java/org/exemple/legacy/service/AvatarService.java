package org.exemple.legacy.service;

import org.exemple.legacy.exeption.NotFoundException;
import org.exemple.legacy.model.Avatar;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.repository.AvatarRepository;
import org.exemple.legacy.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student with id = {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = {}", studentId);
                    return new NotFoundException("Студент с id=" + studentId + " не найден");
                });

        // Создать папку, если её нет
        Path folder = Paths.get(avatarsDir);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        // Уникальное имя файла
        String originalFilename = file.getOriginalFilename();
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                : "";
        String fileName = studentId + "_" + System.currentTimeMillis() + extension;
        Path filePath = folder.resolve(fileName);

        // Сохраняем на диск
        file.transferTo(filePath.toFile());

        // Сохраняем метаданные в БД
        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    public Avatar getAvatarFromDb(Long studentId) {
        logger.info("Was invoked method for get avatar from DB for student with id = {}", studentId);
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("Avatar not found for student with id = {}", studentId);
                    return new NotFoundException("Аватар для студента с id=" + studentId + " не найден");
                });
    }

    public byte[] getAvatarFromDisk(Long studentId) throws IOException {
        logger.info("Was invoked method for get avatar from disk for student with id = {}", studentId);
        Avatar avatar = getAvatarFromDb(studentId); // вызовет NotFoundException, если нет аватара
        Path path = Paths.get(avatar.getFilePath());
        if (!Files.exists(path)) {
            logger.error("File not found on disk: {}", avatar.getFilePath());
            throw new NotFoundException("Файл аватара не найден на диске: " + avatar.getFilePath());
        }
        return Files.readAllBytes(path);
    }

    public Page<Avatar> getAllAvatars(Pageable pageable) {
        logger.info("Was invoked method for get all avatars with pagination");
        return avatarRepository.findAll(pageable);
    }
}
