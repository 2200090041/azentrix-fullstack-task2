package com.taskmanager.mini_trello;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository
        extends JpaRepository<Activity, Long>{

    List<Activity>
    findTop10ByOrderByCreatedAtDesc();

    List<Activity>
    findTop10ByUserOrderByCreatedAtDesc(
            User user);
}