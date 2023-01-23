package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.domain.Message;
import by.matthewvirus.sweater.domain.User;
import by.matthewvirus.sweater.service.MessageService;
import by.matthewvirus.sweater.util.ControllerUtils;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            @Valid Message message,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            @NotNull Model model
    ) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorsMap", ControllerUtils.getErrors(bindingResult));
            model.addAttribute("message", message);
        } else {
            messageService.saveMessage(message, file);
            model.addAttribute("message", null);
        }
        model.addAttribute("messages", messageService.allMessages());
        return "messages";
    }
}