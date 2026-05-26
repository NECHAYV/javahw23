package org.exemple.legacy.service;

import org.exemple.legacy.exeption.NotFoundException;
import org.exemple.legacy.model.Avatar;
import org.exemple.legacy.model.Student;
import org.exemple.legacy.repository.AvatarRepository;
import org.exemple.legacy.repository.StudentRepository;
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
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }


    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Студент с id=" + studentId + " не найден"));

        // Создать папку, если её нет
        Path folder = Paths.get(avatarsDir);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        // Уникальное имя файла: studentId + timestamp + расширение
        String originalFilename = file.getOriginalFilename();
        String extension = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                : "";
        String fileName = studentId + "_" + System.currentTimeMillis() + extension;
        Path filePath = folder.resolve(fileName);

        // Сохраняем на диск
        file.transferTo(filePath.toFile());

        // Создаём объект Avatar
        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    /**
     * Возвращает аватар из БД по ID студента.
     * @param studentId ID студента
     * @return объект Avatar с данными
     */
    public Avatar getAvatarFromDb(Long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException("Аватар для студента с id=" + studentId + " не найден"));
    }

    /**
     * Читает аватар с локального диска.
     * @param studentId ID студента
     * @return массив байтов файла
     * @throws IOException если файл не найден или ошибка чтения
     */
    public byte[] getAvatarFromDisk(Long studentId) throws IOException {
        Avatar avatar = getAvatarFromDb(studentId);
        Path path = Paths.get(avatar.getFilePath());
        if (!Files.exists(path)) {
            throw new NotFoundException("Файл аватара не найден на диске: " + avatar.getFilePath());
        }
        return Files.readAllBytes(path);
    }

    public Page<Avatar> getAllAvatars(Pageable pageable) {
        return avatarRepository.findAll(pageable);
    }
}
