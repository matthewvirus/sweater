package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.domain.Message;
import by.matthewvirus.sweater.domain.User;
import by.matthewvirus.sweater.service.MessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.Set;

@Controller
public class UserMessageController {

    private final MessageService messageService;

    public UserMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal @NotNull User currentUser,
            @PathVariable @NotNull User user,
            @NotNull Model model,
            Message message
    ) {
        Set<Message> messages = user.getMessages();
        model.addAttribute("message", message);
        model.addAttribute("messages", messages);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("userChannel", user);
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") @NotNull Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            if (!file.isEmpty()) {
                messageService.saveMessage(message, file);
            } else {
                messageService.saveMessage(message);
            }
        }
        return "redirect:/user-messages/" + user;
    }
}