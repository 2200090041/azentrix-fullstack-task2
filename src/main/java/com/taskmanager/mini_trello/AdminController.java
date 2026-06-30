package com.taskmanager.mini_trello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

@Autowired
private UserRepository userRepository;

@GetMapping("/admin/users")
public String manageUsers(
        HttpSession session,
        Model model) {

    User loggedUser =
            (User) session.getAttribute("loggedInUser");

    if(loggedUser == null) {
        return "redirect:/login";
    }

    if(!"ADMIN".equals(loggedUser.getRole())) {
        return "redirect:/dashboard";
    }

    model.addAttribute(
            "users",
            userRepository.findAll());

    return "admin-users";
}

@PostMapping("/admin/promote/{id}")
public String promoteUser(
        @PathVariable Long id) {

    User user =
            userRepository.findById(id)
                    .orElse(null);

    if(user != null) {

        user.setRole("ADMIN");

        userRepository.save(user);
    }

    return "redirect:/admin/users";
}

@PostMapping("/admin/member/{id}")
public String makeMember(
        @PathVariable Long id) {

    User user =
            userRepository.findById(id)
                    .orElse(null);

    if(user != null) {

        user.setRole("MEMBER");

        userRepository.save(user);
    }

    return "redirect:/admin/users";
}

@PostMapping("/admin/delete/{id}")
public String deleteUser(
        @PathVariable Long id) {

    userRepository.deleteById(id);

    return "redirect:/admin/users";
}


}
