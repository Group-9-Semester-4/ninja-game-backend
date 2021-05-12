package com.group9.NinjaGame.entities.statisics;

import javax.persistence.*;
import java.util.UUID;

/**
 * Helper class to make creation of repository possible
 *
 * Repositories can not function without primary key in entity
 */

@MappedSuperclass
public abstract class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
