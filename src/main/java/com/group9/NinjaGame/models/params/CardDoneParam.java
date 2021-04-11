package com.group9.NinjaGame.models.params;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class CardDoneParam {

    @NotBlank(message = "cardId is mandatory")
    public UUID cardId;

    @NotBlank(message = "gameId is mandatory")
    public UUID gameId;

}
