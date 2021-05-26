package com.group9.NinjaGame.entities;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Game> games;

    @Column(name = "registered", nullable = false)
    @NotBlank(message = "System error - registered date doesn't exist")
    private Instant registered;

    @Column(name = "last_visited", nullable = false)
    @NotBlank(message = "System error - last_visited date doesn't exist")
    private Instant last_visited;

    public User() {
    }

    public User(@NotBlank(message = "Email is mandatory") String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getRegistered() {
        return registered;
    }

    public void setRegistered(Instant registered) {
        this.registered = registered;
    }

    public Instant getLast_visited() {
        return last_visited;
    }

    public void setLast_visited(Instant last_visited) {
        this.last_visited = last_visited;
    }
}
