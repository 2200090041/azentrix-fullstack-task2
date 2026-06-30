package com.taskmanager.mini_trello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        User user =
                (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        long totalBoards =
                boardRepository.countByUser(user);

        long pendingTasks =
                taskRepository
                        .countByBoardUserAndStatusNot(
                                user,
                                "DONE");

        long completedTasks =
                taskRepository
                        .countByBoardUserAndStatus(
                                user,
                                "DONE");

        model.addAttribute("user", user);
        model.addAttribute("totalBoards", totalBoards);
        model.addAttribute("pendingTasks", pendingTasks);
        model.addAttribute("completedTasks", completedTasks);

        model.addAttribute(
                "todoCount",
                pendingTasks);

        model.addAttribute(
                "doneCount",
                completedTasks);

        long notificationCount =
                notificationRepository
                .countByUserAndIsReadFalse(user);

        model.addAttribute(
                "notificationCount",
                notificationCount);

        if("ADMIN".equals(user.getRole())) {

            model.addAttribute(
                    "activities",
                    activityRepository
                    .findTop10ByOrderByCreatedAtDesc());

        }
        else {

            model.addAttribute(
                    "activities",
                    activityRepository
                    .findTop10ByUserOrderByCreatedAtDesc(
                            user));
        }

        return "dashboard";
}
}