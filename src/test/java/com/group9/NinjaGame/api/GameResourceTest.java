package com.group9.NinjaGame.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.resources.api.GameResource;
import com.group9.NinjaGame.services.ICardService;
import com.group9.NinjaGame.services.ICardSetService;
import com.group9.NinjaGame.services.IGameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameResource.class)
public class GameResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ICardService cardService;

    @MockBean
    private IGameService gameService;

    @MockBean
    private ICardSetService cardSetService;

    @Test
    void initGameTest() throws Exception {
        InitGameParam testParam = new InitGameParam();
        testParam.lobbyCode = "FLOP";
        testParam.multiPlayer = true;
        testParam.playingAlone = false;
        testParam.timeLimit =  420;
        mockMvc.perform(post("/api/game/init")
        .contentType(MediaType.valueOf("application/json"))
        .content(objectMapper.writeValueAsString(testParam)))
        .andExpect(status().isOk());
    }
}

