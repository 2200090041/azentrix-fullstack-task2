package com.taskmanager.mini_trello;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(
            String resetToken);
}