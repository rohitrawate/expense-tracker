package com.rohit.expensetracker.repository;

import com.rohit.expensetracker.entity.User;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    long countByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    boolean existsByEmail(String email);

    void deleteByUuid(UUID uuid);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findWithRolesByEmail(String email);
//    @Query("""
//        SELECT DISTINCT u
//        FROM User u
//        LEFT JOIN FETCH u.roles
//        WHERE u.email = :email
//        """)
//    Optional<User> findByEmailWithRoles( @Param("email") String email );

}