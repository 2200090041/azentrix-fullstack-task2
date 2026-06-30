package com.taskmanager.mini_trello;
import org.springframework.mail.javamail.JavaMailSender;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.mail.SimpleMailMessage;
@Controller
public class TaskController {

@Autowired
private BoardRepository boardRepository;

@Autowired
private TaskRepository taskRepository;

@Autowired
private UserRepository userRepository;

@Autowired
private NotificationRepository notificationRepository;

@Autowired
private JavaMailSender mailSender;

@Autowired
private ActivityRepository activityRepository;

@GetMapping("/board/{id}")
public String boardDetails(
        @PathVariable Long id,
        Model model,
        HttpSession session) {

    Board board =
            boardRepository.findById(id).orElse(null);

    List<Task> todo =
            taskRepository.findByBoardAndStatus(
                    board,
                    "TODO");

    List<Task> inProgress =
            taskRepository.findByBoardAndStatus(
                    board,
                    "IN_PROGRESS");

    List<Task> done =
            taskRepository.findByBoardAndStatus(
                    board,
                    "DONE");

    User loggedUser =
            (User) session.getAttribute("loggedInUser");

    model.addAttribute("board", board);
    model.addAttribute("todo", todo);
    model.addAttribute("inProgress", inProgress);
    model.addAttribute("done", done);
    model.addAttribute("users",
            userRepository.findAll());
    model.addAttribute("loggedUser",
            loggedUser);

    return "board-details";
}

@PostMapping("/task/move/{taskId}")
public String moveTask(
        @PathVariable Long taskId,
        @RequestParam String status) {

    Task task =
            taskRepository.findById(taskId)
                    .orElse(null);

    if(task != null) {

        task.setStatus(status);

        taskRepository.save(task);
        if(status.equals("DONE")) {

            Activity activity =
                    new Activity();

            activity.setUser(
                    task.getAssignedUser());

            activity.setAction(
                    "Completed task : "
                    + task.getTitle());

            activity.setCreatedAt(
                    LocalDateTime.now());

            activityRepository.save(
                    activity);
        }

        if(status.equals("DONE")
                && task.getAssignedUser() != null) {

            SimpleMailMessage email =
                    new SimpleMailMessage();

            email.setTo(
                    task.getAssignedUser()
                            .getEmail());

            email.setSubject(
                    "Task Completed - Mini Trello");

            email.setText(
                    "Hello "
                    + task.getAssignedUser()
                            .getName()

                    + "\n\nYour task '"
                    + task.getTitle()
                    + "' has been marked as completed."

                    + "\n\nCongratulations!"
                    + "\n\nMini Trello Team");

            mailSender.send(email);
        }

        return "redirect:/board/" +
                task.getBoard().getId();
    }

    return "redirect:/boards";
}

@PostMapping("/task/delete/{taskId}")
public String deleteTask(
        @PathVariable Long taskId,
        HttpSession session) {

    User loggedUser =
            (User) session.getAttribute("loggedInUser");

    Task task =
            taskRepository.findById(taskId)
                    .orElse(null);

    if(task == null) {
        return "redirect:/boards";
    }

    boolean admin =
            "ADMIN".equals(
                    loggedUser.getRole());

    boolean owner =
            task.getAssignedUser() != null &&
            task.getAssignedUser()
                    .getId()
                    .equals(
                            loggedUser.getId());

    if(admin || owner) {

        Long boardId =
                task.getBoard().getId();

        taskRepository.delete(task);

        return "redirect:/board/" +
                boardId;
    }

    return "redirect:/dashboard";
}

@GetMapping("/task/edit/{id}")
public String editTaskPage(
        @PathVariable Long id,
        Model model,
        HttpSession session) {

    User loggedUser =
            (User) session.getAttribute("loggedInUser");

    Task task =
            taskRepository.findById(id)
                    .orElse(null);

    if(task == null) {
        return "redirect:/boards";
    }

    boolean admin =
            "ADMIN".equals(
                    loggedUser.getRole());

    boolean owner =
            task.getAssignedUser() != null &&
            task.getAssignedUser()
                    .getId()
                    .equals(
                            loggedUser.getId());

    if(!(admin || owner)) {
        return "redirect:/dashboard";
    }

    model.addAttribute("task", task);
    model.addAttribute("users",
            userRepository.findAll());

    return "edit-task";
}

@PostMapping("/task/update")
public String updateTask(
        @RequestParam Long taskId,
        @RequestParam String title,
        @RequestParam String description,
        @RequestParam String priority,
        @RequestParam String dueDate,
        @RequestParam(required = false) Long userId,
        HttpSession session) {

    User loggedUser =
            (User) session.getAttribute("loggedInUser");

    Task task =
            taskRepository.findById(taskId)
                    .orElse(null);

    if(task == null) {
        return "redirect:/boards";
    }

    boolean admin =
            "ADMIN".equals(
                    loggedUser.getRole());

    boolean owner =
            task.getAssignedUser() != null &&
            task.getAssignedUser()
                    .getId()
                    .equals(
                            loggedUser.getId());

    if(!(admin || owner)) {
        return "redirect:/dashboard";
    }

    task.setTitle(title);
    task.setDescription(description);
    task.setPriority(priority);

    task.setDueDate(
            java.time.LocalDate.parse(dueDate));

    if(userId != null) {

        User user =
                userRepository.findById(userId)
                        .orElse(null);

        task.setAssignedUser(user);
    }

    taskRepository.save(task);

    return "redirect:/board/" +
            task.getBoard().getId();
}
@PostMapping("/task/create")
public String createTask(
        @RequestParam Long boardId,
        @RequestParam String title,
        @RequestParam String description,
        @RequestParam String priority,
        @RequestParam String dueDate,
        @RequestParam(required = false) Long userId) {

    Board board =
            boardRepository.findById(boardId)
                    .orElse(null);

    Task task = new Task();

    task.setTitle(title);
    task.setDescription(description);
    task.setPriority(priority);
    task.setStatus("TODO");

    task.setDueDate(
            java.time.LocalDate.parse(dueDate));

    task.setBoard(board);

    User assignedUser = null;

    if(userId != null) {

        assignedUser =
                userRepository.findById(userId)
                        .orElse(null);

        task.setAssignedUser(
                assignedUser);
    }

    taskRepository.save(task);
    Activity activity =
            new Activity();

    activity.setUser(
            assignedUser);

    activity.setAction(
            "Created task : "
            + task.getTitle());

    activity.setCreatedAt(
            LocalDateTime.now());

    activityRepository.save(
            activity);

    // CREATE NOTIFICATION
    if(assignedUser != null) {

        Notification notification =
                new Notification();

        notification.setUser(
                assignedUser);

        notification.setMessage(
                "📌 New Task Assigned : "
                + task.getTitle()

                + " | Priority : "
                + task.getPriority()

                + " | Due : "
                + task.getDueDate());

        notification.setRead(false);

        notification.setCreatedAt(
                java.time.LocalDateTime.now());

        notificationRepository.save(
                notification);

        System.out.println(
                "Notification Saved");

        /* SEND EMAIL */

        SimpleMailMessage email =
                new SimpleMailMessage();

        email.setTo(
                assignedUser.getEmail());

        email.setSubject(
                "New Task Assigned - Mini Trello");

        email.setText(
                "Hello " +
                assignedUser.getName() +

                "\n\nYou have been assigned a new task."

                + "\n\nTask Title : "
                + task.getTitle()

                + "\nDescription : "
                + task.getDescription()

                + "\nPriority : "
                + task.getPriority()

                + "\nDue Date : "
                + task.getDueDate()

                + "\n\nPlease login to Mini Trello to view task details."

                + "\n\nRegards,"
                + "\nMini Trello Team");

        mailSender.send(email);

        System.out.println(
                "Email Sent Successfully");
    }

    return "redirect:/board/" + boardId;
}
@GetMapping("/tasks")
public String viewTasks(
        HttpSession session,
        Model model) {

    User user =
            (User) session.getAttribute("loggedInUser");

    if(user == null) {
        return "redirect:/login";
    }

    List<Task> tasks;

    if("ADMIN".equals(user.getRole())) {

        tasks = taskRepository.findAll();

    } else {

        tasks =
            taskRepository.findByAssignedUser(user);
    }

    model.addAttribute("tasks", tasks);

    return "tasks";
}
@PostMapping("/task/drag/{id}")
@ResponseBody
public String dragTask(
        @PathVariable Long id,
        @RequestParam String status) {

    Task task =
            taskRepository.findById(id)
            .orElseThrow();

    task.setStatus(status);

    taskRepository.save(task);

    return "success";
}
}
