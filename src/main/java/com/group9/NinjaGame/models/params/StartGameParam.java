package com.group9.NinjaGame.models.params;

import java.util.List;
import java.util.UUID;

public class StartGameParam {
    public UUID gameId;
    public UUID cardSetId;
    public List<UUID> unwantedCards;
}
