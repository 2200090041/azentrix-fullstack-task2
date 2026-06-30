package com.taskmanager.mini_trello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

@Autowired
private BoardRepository boardRepository;

@GetMapping("/boards")
public String boards(
        HttpSession session,
        Model model) {

    User user =
            (User) session.getAttribute("loggedInUser");

    if(user == null) {
        return "redirect:/login";
    }

    List<Board> boards =
            boardRepository.findByUser(user);

    model.addAttribute("boards", boards);

    return "boards";
}

@PostMapping("/board/create")
public String createBoard(
        @RequestParam String boardName,
        HttpSession session) {

    User user =
            (User) session.getAttribute("loggedInUser");

    if(user == null) {
        return "redirect:/login";
    }

    if(boardName == null ||
       boardName.trim().isEmpty()) {

        return "redirect:/boards";
    }

    Board board = new Board();

    board.setBoardName(
            boardName.trim());

    board.setUser(user);

    boardRepository.save(board);

    return "redirect:/boards";
}

@PostMapping("/board/delete/{id}")
public String deleteBoard(
        @PathVariable Long id,
        HttpSession session) {

    User loggedUser =
            (User) session.getAttribute("loggedInUser");

    if(loggedUser == null) {
        return "redirect:/login";
    }

    Board board =
            boardRepository.findById(id)
                    .orElse(null);

    if(board == null) {
        return "redirect:/boards";
    }

    boolean admin =
            "ADMIN".equals(
                    loggedUser.getRole());

    boolean owner =
            board.getUser() != null &&
            board.getUser().getId()
                    .equals(
                            loggedUser.getId());

    if(admin || owner) {

        boardRepository.delete(
                board);
    }

    return "redirect:/boards";
}


}
