package com.group9.NinjaGame.models.params;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public class FinishGameParam {

    @NotBlank(message = "gameId is mandatory")
    public UUID gameId;
    //might not be needed?
    public UUID cardSetId;
    public int percentageOfDoneCards;
    public int timeInSeconds;
    //TODO: check if really needed - used just to check if game is in game container
    public UUID playerId;
    public List<UUID> listOfRedrawnCards;
}
