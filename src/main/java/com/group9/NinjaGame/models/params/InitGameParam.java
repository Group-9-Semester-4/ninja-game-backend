package com.group9.NinjaGame.models.params;

import javax.validation.constraints.NotBlank;

public class InitGameParam {

    @NotBlank(message = "timeLimit is mandatory")
    public int timeLimit;

    public boolean multiPlayer;

    // TODO: Remove this useless field
    public boolean playingAlone;

    public String lobbyCode;

    public String email;

}
