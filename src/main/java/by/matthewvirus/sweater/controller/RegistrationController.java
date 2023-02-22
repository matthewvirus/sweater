package by.matthewvirus.sweater.controller;

import by.matthewvirus.sweater.domain.User;
import by.matthewvirus.sweater.domain.dto.CaptchaResponseDto;
import by.matthewvirus.sweater.service.UserService;
import by.matthewvirus.sweater.util.ControllerUtils;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Value("${secret.reCaptcha}")
    String secretKey;

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    private final RestTemplate restTemplate;
    private final UserService userService;

    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @NotNull @Valid User user,
            BindingResult bindingResult,
            Model model)
    {
        String url = String.format(CAPTCHA_URL, secretKey, captchaResponse);
        CaptchaResponseDto captchaResponseDto = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (captchaResponseDto != null && !captchaResponseDto.isSuccess()) {
            model.addAttribute("captchaError", "Please, fill the captcha!");
        }
        if (user.getPasswordConf() == null || user.getPasswordConf().isEmpty()) {
            model.addAttribute("passwordConfError", "Password confirmation can't be empty!");
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getPasswordConf())) {
            model.addAttribute("passwordsError", "Passwords are not equals!");
            System.out.println(user.getPasswordConf() + user.getPassword());
        }
        if (captchaResponseDto != null && (bindingResult.hasErrors() || !captchaResponseDto.isSuccess())) {
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