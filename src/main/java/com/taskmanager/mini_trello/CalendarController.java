package com.taskmanager.mini_trello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class CalendarController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/calendar")
    public String calendar(
            HttpSession session,
            Model model) {

        User user =
            (User) session.getAttribute("loggedInUser");

        if(user == null) {
            return "redirect:/login";
        }

        List<Task> tasks =
            taskRepository.findByBoardUser(user);

        model.addAttribute("tasks", tasks);

        return "calendar";
    }
}