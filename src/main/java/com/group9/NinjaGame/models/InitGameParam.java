package com.group9.NinjaGame.models;

import javax.validation.constraints.NotBlank;

public class InitGameParam {

    @NotBlank(message = "timeLimit is mandatory")
    public int timeLimit;

    public boolean multiPlayer;

    public boolean playingAlone;

}
