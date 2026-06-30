package com.taskmanager.mini_trello;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class ProfileController {
	@Autowired
	private UserRepository userRepository;
    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "profile";
    }
    @GetMapping("/profile/edit")
    public String editProfilePage(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "edit-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String email,
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        user.setName(name);
        user.setEmail(email);

        userRepository.save(user);

        session.setAttribute("loggedInUser", user);

        return "redirect:/profile";
    }
}