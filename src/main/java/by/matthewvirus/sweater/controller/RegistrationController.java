package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.entity.Role;
import by.matthewvirus.sweater.entity.User;
import by.matthewvirus.sweater.repository.UserRepository;
import by.matthewvirus.sweater.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@NotNull User user, Model model) {
        if (!userService.saveUser(user)) {
            model.addAttribute("message", "User is exists");
            return "registration";
        }
        return "redirect:/login";
    }
}