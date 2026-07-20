package com.needleeye.auth_service.Repository;

import com.needleeye.auth_service.Entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepo extends JpaRepository<AuthUser,Long> {
    Optional<AuthUser> findTopByOrderByIdDesc();
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByUserId(String userId);
}
