package com.group9.NinjaGame.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.UUID;

public class Player {

    public String name;

    //@JsonIgnore
    public UUID sessionId;

    public Player(String name, UUID sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return sessionId.equals(player.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }
}
