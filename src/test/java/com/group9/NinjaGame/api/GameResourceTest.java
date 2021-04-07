package com.group9.NinjaGame.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.repositories.CardRepository;
import com.group9.NinjaGame.repositories.CardSetRepository;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.resources.api.GameResource;
import com.group9.NinjaGame.services.GameService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GameResource.class)
public class GameResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    GameService gameService;

    @MockBean
    GameRepository gameRepository;

    @MockBean
    CardRepository cardRepository;

    @MockBean
    CardSetRepository cardSetRepository;


    // TODO: check with ERIK, not working; empty response
    @Test
    @Order(1)
    void initGameTest() throws Exception {
        InitGameParam testParam = new InitGameParam();
        testParam.lobbyCode = "123456";
        testParam.multiPlayer = true;
        testParam.playingAlone = true;
        testParam.timeLimit = 60;

        mockMvc.perform(post("/api/game/init")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testParam)))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.gameInfo").isNotEmpty());


    }

//    @Test
//    void startGameTest() throws Exception {
//        StartGameParam testParam = new StartGameParam();
//
//        testParam.gameId = ;
//        testParam.cardSetId = ;
//        testParam.unwantedCards = ;
//        mockMvc.perform(post("/api/game/init")
//                .contentType(MediaType.valueOf("application/json"))
//                .content(objectMapper.writeValueAsString(testParam)))
//                .andExpect(status().isOk());
//    }

    @Test
    void getCardsTest() throws Exception {
        mockMvc.perform(get("/api/game/cards"))
                .andExpect(status().isOk());
    }

    @Test
    void getCardSetsTest() throws Exception {
        mockMvc.perform(get("/api/game/cardsets"))
                .andExpect(status().isOk());
    }

    @Test
    void getGamesTest() throws Exception {
        mockMvc.perform(get("/api/game/games"))
                .andDo(MockMvcResultHandlers.print()).andExpect(status().isOk());
    }

    //TODO: almost working
    @Test
    @Order(2)
    void getGameObjectsTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/game/games")).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        System.out.print(json);
        //Object actualObject = objectMapper.readValue(json, Game.class);
        assertThat(json).isEqualToIgnoringWhitespace("[]");
    }

}






