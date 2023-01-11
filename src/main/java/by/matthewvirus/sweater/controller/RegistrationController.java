package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.domain.Role;
import by.matthewvirus.sweater.domain.User;
import by.matthewvirus.sweater.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;

    public RegistrationController(UserRepository repository) {
        this.userRepository = repository;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@NotNull User user, Model model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "User is exists");
            return "registration";
        }
        user.setActive(true);
        user.setRoleSet(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}