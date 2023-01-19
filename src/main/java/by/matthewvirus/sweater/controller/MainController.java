package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.entity.User;
import by.matthewvirus.sweater.service.MessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class MainController {

    private final MessageService messageService;

    public MainController(MessageService messageService) {
        this.messageService = messageService;
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
        model.addAttribute("messages", messageService.allMessages());
        if (filter == null || filter.isEmpty()) {
            model.addAttribute("messages", messageService.allMessages());
        } else {
            model.addAttribute("messages", messageService.getMessageByTag(filter));
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
        messageService.saveMessage(user, text, tag, file);
        model.addAttribute("messages", messageService.allMessages());
        return "messages";
    }
}