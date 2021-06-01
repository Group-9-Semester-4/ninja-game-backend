package com.group9.NinjaGame.repositories;

import com.group9.NinjaGame.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(@NotBlank(message = "Email is mandatory") String email);

    @Query(value = "SELECT * FROM users ORDER BY last_visited DESC LIMIT 25 OFFSET ?1", nativeQuery = true)
    List<Object[]> findAllPaginated(int pageNo);
}
