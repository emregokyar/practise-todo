package com.todo.repository;

import com.todo.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(
            value = "SELECT COUNT(u.id) FROM users u JOIN user_authorities a ON u.id = a.user_id WHERE a.authority = 'ROLE_ADMIN'",
            nativeQuery = true
    )
    long countAdmins();
}
