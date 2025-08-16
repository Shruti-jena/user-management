package com.shruti.user_management.Repository;

import com.shruti.user_management.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;// This annotation marks this interface as repository bean

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}