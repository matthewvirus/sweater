package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.entity.Role;
import by.matthewvirus.sweater.entity.User;
import by.matthewvirus.sweater.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(@NotNull Model model) {
        model.addAttribute("users", userService.allUsers());
        return "userList";
    }

    @GetMapping("/{user}")
    public String userEdit(
            @PathVariable User user,
            Model model
    ) {
        model.addAttribute("selectedUser", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String updateUser(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        userService.updateUser(username, form, user);
        return "redirect:/admin";
    }
}