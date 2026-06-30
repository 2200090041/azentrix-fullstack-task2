package com.taskmanager.mini_trello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user) {

        user.setRole("MEMBER");

        userRepository.save(user);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && user.getPassword().equals(password)) {

            session.setAttribute("loggedInUser", user);

            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid Email or Password");

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }
}