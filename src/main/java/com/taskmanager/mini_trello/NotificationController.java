package com.taskmanager.mini_trello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    public String notifications(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        List<Notification> notifications =
                notificationRepository
                        .findByUserOrderByCreatedAtDesc(user);

        // Mark unread notifications as read
        for(Notification n : notifications) {

            if(!n.isRead()) {

                n.setRead(true);

                notificationRepository.save(n);
            }
        }

        model.addAttribute(
                "notifications",
                notifications);

        return "notifications";
    }

    @PostMapping("/notifications/read-all")
    public String markAllRead(
            HttpSession session) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        List<Notification> notifications =
                notificationRepository
                        .findByUserOrderByCreatedAtDesc(user);

        for(Notification n : notifications) {

            n.setRead(true);

            notificationRepository.save(n);
        }

        return "redirect:/notifications";
    }
}