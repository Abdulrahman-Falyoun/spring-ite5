package com.ite5year.repositories;

import com.ite5year.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<ApplicationUser, String> {
    ApplicationUser findUserByUsername(String username);
}
