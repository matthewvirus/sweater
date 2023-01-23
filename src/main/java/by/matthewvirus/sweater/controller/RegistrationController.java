package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.domain.User;
import by.matthewvirus.sweater.service.UserService;
import by.matthewvirus.sweater.util.ControllerUtils;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String addUser(
            @NotNull @Valid User user,
            BindingResult bindingResult,
            Model model)
    {
        if (user.getPasswordConf() == null || user.getPasswordConf().isEmpty()) {
            model.addAttribute("passwordConfError", "Password confirmation can't be empty!");
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getPasswordConf())) {
            model.addAttribute("passwordsError", "Passwords are not equals!");
            System.out.println(user.getPasswordConf() + user.getPassword());
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorsMap", ControllerUtils.getErrors(bindingResult));
            return "registration";
        }
        if (!userService.saveUser(user)) {
            model.addAttribute("usernameError", "User is exists");
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("messageSuccess", "User successfully activated!");
        } else {
            model.addAttribute("messageError", "User is not activated!");
        }
        return "login";
    }
}