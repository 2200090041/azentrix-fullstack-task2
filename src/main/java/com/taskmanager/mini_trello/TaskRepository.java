package com.taskmanager.mini_trello;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByBoardAndStatus(
            Board board,
            String status);

    long countByBoardUserAndStatus(
            User user,
            String status);

    long countByBoardUserAndStatusNot(
            User user,
            String status);
    List<Task> findByAssignedUser(User user);
    List<Task> findByBoardUser(User user);
}