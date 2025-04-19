package com.promotion.game.service;

import com.promotion.game.dto.GameAndAgentJoinRequest;
import com.promotion.game.dto.GameAndAgentJoinResponse;
import com.promotion.game.dto.GameRequest;
import com.promotion.game.dto.GameResponse;
import com.promotion.game.entity.Game;
import com.promotion.game.repo.GameRepo;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    @Autowired private final GameRepo gameRepo;
    @Autowired private final UserRepo userRepo;

    @Override
    @Transactional
    public GameResponse saveGame(String randomId,GameRequest request) {

        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();

        if(!userGet.getRole().equals(Role.ADMIN)){
            return GameResponse
                    .builder()
                    .status(false)
                    .statusCode(403)
                    .statusMessage("Hello User You have no authorize for this api")
                    .build();
        }

        var gameObj = Game
                .builder()
                .gameName(request.getGameName())
                .gameCode(request.getGameCode())
                .conversationRate(request.getConversationRate())
                .gameStatus(true)
                .build();

        Game resObj = gameRepo.save(gameObj);
        System.out.println(resObj);

        return GameResponse
                .builder()
                .statusMessage("Created Game" + request.getGameName())
                .status(true)
                .statusCode(201)
                .build();
    }

    @Override
    @Transactional
    public GameAndAgentJoinResponse assignGameToAgent(String randomId, GameAndAgentJoinRequest request) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();

        if(!userGet.getRole().equals(Role.ADMIN)){
            return GameAndAgentJoinResponse
                    .builder()
                    .status(false)
                    .statusCode(403)
                    .statusMessage("Hello you haven't authorize to use this api")
                    .build();
        }

        System.out.println(userGet);

        Optional<User> userAgent = userRepo.findByRandomId(request.getRandomId());
        User userAgentGet = userAgent.get();
        if(!userAgentGet.getRole().equals(Role.AGENT)){
            return GameAndAgentJoinResponse
                    .builder()
                    .status(false)
                    .statusCode(403)
                    .statusMessage("Hello Selected User is not authorize to give game access")
                    .build();
        }

        System.out.println(userAgentGet);

        System.out.println(request);

        List<Game> gameList = request.getGameUpdateRequestList().stream()
                .map(game -> gameRepo.findByGameCode(game.getGameCode())
                        .orElseThrow(() -> new RuntimeException("Game not found")))
                .collect(Collectors.toList());
        System.out.println("Before game list");
        System.out.println(gameList);

        userAgentGet.setGames(gameList);

        userRepo.save(userAgentGet);

        return GameAndAgentJoinResponse
                .builder()
                .statusMessage("Finished Join Agent " + request.getRandomId() + "and selected games")
                .status(true)
                .statusCode(201)
                .build();
    }

    @Override
    @Transactional
    public GameResponse getGameAll(String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(userGet.getRole().equals(Role.ADMIN)){
            List<Game> gameList = gameRepo.findAll();
            System.out.println(gameList);
            return GameResponse
                    .builder()
                    .statusMessage("API Good Working")
                    .status(true)
                    .statusCode(200)
                    .gameList(gameList)
                    .build();
        }else if(userGet.getRole().equals(Role.AGENT)){
            System.out.println("AGENT Role");
        }else if(userGet.getRole().equals(Role.USER)){
            System.out.println("User Role");
        }

        return null;
    }

    @Override
    @Transactional
    public GameResponse updateGame(String randomId,GameRequest request) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(!userGet.getRole().equals(Role.ADMIN)){
            return GameResponse
                    .builder()
                    .statusCode(403)
                    .statusMessage("Hello you can't use this api, because your user level haven't authorize")
                    .status(false)
                    .build();
        }
       Optional<Game> game = gameRepo.findById(Long.valueOf(request.getId()));
       if(game.isEmpty()){
           return GameResponse
                   .builder()
                   .status(false)
                   .statusCode(400)
                   .statusMessage("Hello I can't find this Game Object , Something Wrong")
                   .build();
       }
       Game gameGet = game.get();
       Game updateGameObj = Game
               .builder()
               .id(gameGet.getId())
               .gameCode(request.getGameCode())
               .gameName(request.getGameName())
               .gameStatus(request.getGameStatus())
               .conversationRate(request.getConversationRate())
               .build();

        gameRepo.save(updateGameObj);

        return GameResponse
                .builder()
                .statusMessage("Update Game" + request.getGameName())
                .status(true)
                .statusCode(201)
                .build();
    }
}
