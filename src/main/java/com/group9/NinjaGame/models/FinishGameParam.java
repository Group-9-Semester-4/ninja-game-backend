package com.group9.NinjaGame.models;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public class FinishGameParam {

    @NotBlank(message = "gameId is mandatory")
    public UUID gameId;

}
