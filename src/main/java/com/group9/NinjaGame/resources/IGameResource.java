package com.group9.NinjaGame.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

public interface IGameResource {
    ResponseEntity<?> initGame(ObjectNode json);
    ResponseEntity<?> drawCard(UUID gameId);
    ResponseEntity<?> startGame(@PathVariable UUID gameId, @RequestBody List<String> unwantedCards);
    ResponseEntity<?> cardDone(@PathVariable UUID gameId, @RequestBody ObjectNode json);
    ResponseEntity<?> finishGame(@PathVariable UUID gameId);
}
