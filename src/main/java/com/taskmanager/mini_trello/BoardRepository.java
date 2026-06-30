package com.taskmanager.mini_trello;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByUser(User user);

    long countByUser(User user);

}