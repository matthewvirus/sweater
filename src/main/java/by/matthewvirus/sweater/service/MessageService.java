package by.matthewvirus.sweater.service;

import by.matthewvirus.sweater.entity.Message;
import by.matthewvirus.sweater.entity.User;
import by.matthewvirus.sweater.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MessageService {

    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> allMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessageByTag(String tag) {
        return messageRepository.findByTag(tag);
    }

    public void saveMessage(Message message, MultipartFile file) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String filename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + filename));
            message.setFilename(filename);
        }
        messageRepository.save(message);
    }
}