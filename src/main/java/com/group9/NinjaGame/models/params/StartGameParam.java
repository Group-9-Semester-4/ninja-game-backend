package com.group9.NinjaGame.models.params;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public class StartGameParam {

    @NotBlank(message = "gameId is mandatory")
    public UUID gameId;

    public UUID cardSetId;

    public List<UUID> unwantedCards;
}