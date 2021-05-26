package com.group9.NinjaGame.services;

import com.group9.NinjaGame.containers.GameContainer;
import com.group9.NinjaGame.entities.Game;
import com.group9.NinjaGame.entities.User;
import com.group9.NinjaGame.models.GameInfo;
import com.group9.NinjaGame.models.params.FinishGameParam;
import com.group9.NinjaGame.models.params.InitGameParam;
import com.group9.NinjaGame.repositories.GameRepository;
import com.group9.NinjaGame.repositories.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class GameService implements IGameService {
    private final GameRepository gameRepository;
    private final GameContainer gameContainer;
    private final UserRepository userRepository;
    private final IStatisticsService statisticsService;

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, IStatisticsService statisticsService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.statisticsService = statisticsService;
        this.gameContainer = GameContainer.getInstance();
    }

    @Override
    public Game initGame(InitGameParam param) {
        User user = createOrGetExisting(param.email);

        Game game = new Game(param.timeLimit, param.multiPlayer, param.playingAlone, user);

        game = gameRepository.save(game);

        if (param.multiPlayer) {
            GameInfo gameInfo = new GameInfo(game.getId(), param.lobbyCode);
            gameContainer.initGame(gameInfo);
        }

        return game;
    }

    public Game finishGame(FinishGameParam param) throws Exception {
        try {
            Optional<Game> gameEntityOptional = gameRepository.findById(param.gameId);

            if (gameEntityOptional.isPresent()) {
                Game game = gameEntityOptional.get();

                statisticsService.insertGameStatistics(param);

                // TODO: Mark game as finished - add new field for example, but don't delete
                gameRepository.delete(game);

                return game;
            } else {
                throw new NotFoundException("Game ID not found.");
            }
        } catch (Exception e) {
            //can't delete game
            throw e;
        }
    }

    public Iterable<Game> findAll() {
        try {
            return gameRepository.findAll();
        } catch (Exception e) {
            throw e;
        }

    }

    private User createOrGetExisting(String email) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            // User found
            user.setLast_visited(Instant.now());
            userRepository.save(user);
            return user;
        }
        // User NOT found
        User newUser = new User(email);

        newUser.setRegistered(Instant.now());
        newUser.setLast_visited(Instant.now());
        userRepository.save(newUser);

        return newUser;
    }
}
