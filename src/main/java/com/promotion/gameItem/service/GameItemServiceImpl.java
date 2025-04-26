package com.promotion.gameItem.service;

import com.promotion.game.entity.Game;
import com.promotion.game.repo.GameRepo;
import com.promotion.gameItem.dto.GameItemRequest;
import com.promotion.gameItem.dto.GameItemResp;
import com.promotion.gameItem.entity.GameItem;
import com.promotion.gameItem.repo.GameItemRepo;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GameItemServiceImpl implements GameItemService{

    @Autowired private final UserRepo userRepo;
    @Autowired private final GameRepo gameRepo;
    @Autowired private final GameItemRepo gameItemRepo;

    @Override
    @Transactional
    public GameItemResp saveGameItem(String randomId, GameItemRequest request) {
        System.out.println(randomId);
        System.out.println(request);

        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(!userGet.getRole().equals(Role.AGENT)) {
            return GameItemResp
                    .builder()
                    .status(false)
                    .statusCode(403)
                    .statusMessage("Hello User you haven't authorize use for this api")
                    .build();
        }

        Optional<Game> game = gameRepo.findByGameCode(request.getGameCode());
        if(game.isEmpty()) {
            return GameItemResp
                    .builder()
                    .statusMessage("Hello " + request.getGameCode() + " can't find")
                    .status(false)
                    .statusCode(400)
                    .build();
        }

        Game gameGet = game.get();

        GameItem saveObj = GameItem
                .builder()
                .game(gameGet)
                .agent(userGet)
                .gameItem(request.getGameItem())
                .gameItemDesc(request.getGameItemDesc())
                .gameItemStatus(true)
                .build();

       GameItem resObj = gameItemRepo.save(saveObj);

       return GameItemResp
               .builder()
               .statusCode(200)
               .status(true)
               .statusMessage("Complete Create Game Item")
               .build();
    }

    @Override
    public GameItemResp getGameItemAll(String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(userGet.getRole().equals(Role.ADMIN)){
            System.out.println("ADMIN Role");
        }if(userGet.getRole().equals(Role.AGENT)){
            List<GameItem> gameItemList = userGet.getGameItemList();

            List<GameItemResp.gameItemDto> dtoList = gameItemList.stream().map(item -> {
                GameItemResp.gameItemDto dto = new GameItemResp.gameItemDto();
                dto.setId(item.getId());
                dto.setGameCode(item.getGame().getGameCode());
                dto.setGameName(item.getGame().getGameName());
                dto.setGameItemName(item.getGameItem());
                dto.setGameItemDesc(item.getGameItemDesc());
                dto.setGameItemStatus(item.getGameItemStatus());
                return dto;
            }).toList();

            return GameItemResp
                    .builder()
                    .statusMessage("API Good Working")
                    .status(true)
                    .statusCode(200)
                    .randomAgentId(userGet.getRandomId())
                    .gameItemDtos(dtoList)
                    .build();
        }
        return null;
    }

    @Override
    public GameItemResp updateStatusGameItemS(String randomId, GameItemRequest request) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(!userGet.getRole().equals(Role.AGENT)){
            return GameItemResp
                    .builder()
                    .statusCode(403)
                    .status(false)
                    .statusMessage("Hello you haven't authorize for this api")
                    .build();
        }

        Optional<GameItem> resObj = gameItemRepo.findByIdAndAgent(request.getId(),userGet.getRandomId());
        if(resObj == null || resObj.isEmpty()){
            System.out.println("Hello is present is working");
            return GameItemResp
                    .builder()
                    .statusCode(403)
                    .status(false)
                    .statusMessage("Hello I can't find any game item")
                    .build();
        }

        GameItem updateObj = resObj.get();
        updateObj.setGameItemStatus(!updateObj.getGameItemStatus());
        GameItem updateResObj = gameItemRepo.save(updateObj);

        return GameItemResp
                .builder()
                .statusCode(201)
                .status(true)
                .statusMessage("Completed Game Item " + updateObj.getGameItem())
                .build();
    }

    @Override
    @Transactional
    public GameItemResp getGameItemByGame(Long id, String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();

        if(userGet.getRole().equals(Role.USER)){
            Optional<User> agent = userRepo.findByRandomId(userGet.getParentId());
            if(agent.isEmpty()){
                return GameItemResp
                        .builder()
                        .statusMessage("Can't Find Agent")
                        .status(false)
                        .statusCode(400)
                        .build();
            }
            User agentGet = agent.get();
            Optional<Game> game = gameRepo.findById(id);
            Game gameGet = game.get();

            List<GameItem> gameItemList = gameItemRepo.findByAgentAndGame(agentGet,gameGet);

            List<GameItemResp.gameItemDto> dtoList = gameItemList.stream().map(item -> {
                GameItemResp.gameItemDto dto = new GameItemResp.gameItemDto();
                dto.setId(item.getId());
                dto.setGameCode(item.getGame().getGameCode());
                dto.setGameName(item.getGame().getGameName());
                dto.setGameItemName(item.getGameItem());
                dto.setGameItemDesc(item.getGameItemDesc());
                dto.setGameItemStatus(item.getGameItemStatus());
                return dto;
            }).toList();

            return GameItemResp
                    .builder()
                    .statusMessage("API Good Working")
                    .status(true)
                    .statusCode(200)
                    .randomAgentId(userGet.getRandomId())
                    .gameItemDtos(dtoList)
                    .build();
        }else if(userGet.getRole().equals(Role.AGENT)){
            Optional<Game> game = gameRepo.findById(id);
            Game gameGet = game.get();

            System.out.println(gameGet);

            List<GameItem> gameItemList = gameItemRepo.findByAgentAndGame(userGet,gameGet);

            List<GameItemResp.gameItemDto> dtoList = gameItemList.stream().map(item -> {
                GameItemResp.gameItemDto dto = new GameItemResp.gameItemDto();
                dto.setId(item.getId());
                dto.setGameCode(item.getGame().getGameCode());
                dto.setGameName(item.getGame().getGameName());
                dto.setGameItemName(item.getGameItem());
                dto.setGameItemDesc(item.getGameItemDesc());
                dto.setGameItemStatus(item.getGameItemStatus());
                return dto;
            }).toList();

            return GameItemResp
                    .builder()
                    .statusMessage("API Good Working")
                    .status(true)
                    .statusCode(200)
                    .randomAgentId(userGet.getRandomId())
                    .gameItemDtos(dtoList)
                    .build();
        }

        Optional<Game> game = gameRepo.findById(id);
        Game gameGet = game.get();

        System.out.println(gameGet);

        List<GameItem> gameItemList = gameItemRepo.findByAgentAndGame(userGet,gameGet);

        List<GameItemResp.gameItemDto> dtoList = gameItemList.stream().map(item -> {
            GameItemResp.gameItemDto dto = new GameItemResp.gameItemDto();
            dto.setId(item.getId());
            dto.setGameCode(item.getGame().getGameCode());
            dto.setGameName(item.getGame().getGameName());
            dto.setGameItemName(item.getGameItem());
            dto.setGameItemDesc(item.getGameItemDesc());
            dto.setGameItemStatus(item.getGameItemStatus());
            return dto;
        }).toList();

        return GameItemResp
                .builder()
                .statusMessage("API Good Working")
                .status(true)
                .statusCode(200)
                .randomAgentId(userGet.getRandomId())
                .gameItemDtos(dtoList)
                .build();
    }
}
