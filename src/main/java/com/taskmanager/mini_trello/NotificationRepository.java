package com.taskmanager.mini_trello;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    long countByUserAndIsReadFalse(User user);

}