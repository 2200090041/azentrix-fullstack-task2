package com.taskmanager.mini_trello;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
@Controller
public class PasswordController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        if(!user.getPassword().equals(currentPassword)) {

            model.addAttribute(
                    "error",
                    "Current password is incorrect");

            return "change-password";
        }

        if(!newPassword.equals(confirmPassword)) {

            model.addAttribute(
                    "error",
                    "Passwords do not match");

            return "change-password";
        }

        user.setPassword(newPassword);

        userRepository.save(user);

        model.addAttribute(
                "success",
                "Password changed successfully");

        return "change-password";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestParam String email,
            Model model) {

        User user =
                userRepository.findByEmail(email)
                        .orElse(null);

        if(user == null) {

            model.addAttribute(
                    "error",
                    "Email not found");

            return "forgot-password";
        }

        String token =
                UUID.randomUUID().toString();

        user.setResetToken(token);

        user.setTokenExpiry(
                java.time.LocalDateTime.now()
                        .plusMinutes(30));

        userRepository.save(user);

        String resetLink =
                "http://localhost:8082/reset-password?token="
                + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Password Reset");

        message.setText(
                "Click link to reset password:\n\n"
                + resetLink);

        mailSender.send(message);

        model.addAttribute(
                "success",
                "Reset link sent to your email");

        return "forgot-password";
    }
    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam String token,
            Model model) {

        model.addAttribute("token", token);

        return "reset-password";
    }
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {

        User user =
                userRepository.findByResetToken(token)
                        .orElse(null);

        if(user == null) {

            model.addAttribute(
                    "error",
                    "Invalid token");

            return "reset-password";
        }

        if(!newPassword.equals(confirmPassword)) {

            model.addAttribute(
                    "error",
                    "Passwords do not match");

            return "reset-password";
        }

        user.setPassword(newPassword);

        user.setResetToken(null);
        user.setTokenExpiry(null);

        userRepository.save(user);

        return "redirect:/login";
    }
}