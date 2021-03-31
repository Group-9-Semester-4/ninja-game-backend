package com.group9.NinjaGame.models;

import java.util.UUID;

public class KokotinaCard {
    public UUID id;
    public String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
