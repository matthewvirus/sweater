package org.example.sweater.controller;

import org.example.sweater.domain.Message;
import org.example.sweater.domain.User;
import org.example.sweater.repository.MessageRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepository messageRepository;

    @Autowired
    public MainController(MessageRepository repository) {
        this.messageRepository = repository;
    }

    @GetMapping("/")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            @NotNull Model model
    ) {
        model.addAttribute("name", name);
        return "home";
    }

    @GetMapping( "/messages")
    public String messages(
            @RequestParam(required = false) String filter,
            @NotNull Model model
    ) {
        model.addAttribute("messages", messageRepository.findAll());
        if (filter == null || filter.isEmpty()) {
            model.addAttribute("messages", messageRepository.findAll());
        } else {
            model.addAttribute("messages", messageRepository.findByTag(filter));
            model.addAttribute("filter", filter);
        }
        return "messages";
    }

    @PostMapping("/messages")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file,
            @NotNull Model model
    ) throws IOException {
        Message message = new Message(text, tag, user);
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
        model.addAttribute("messages", messageRepository.findAll());
        return "messages";
    }
}