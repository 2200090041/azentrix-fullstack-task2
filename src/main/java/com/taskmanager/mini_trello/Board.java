package com.taskmanager.mini_trello;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boardName;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Task> tasks;

    public Board() {
    }

    public Long getId() {
        return id;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}