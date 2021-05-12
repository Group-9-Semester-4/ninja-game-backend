package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(@NotBlank(message = "Email is mandatory") String email);
}
