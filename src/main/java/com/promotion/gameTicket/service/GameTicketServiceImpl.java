package com.promotion.gameTicket.service;

import com.promotion.game.entity.Game;
import com.promotion.game.repo.GameRepo;
import com.promotion.gameItem.entity.GameItem;
import com.promotion.gameItem.repo.GameItemRepo;
import com.promotion.gameTicket.dto.GameTicketRequest;
import com.promotion.gameTicket.dto.GameTicketResp;
import com.promotion.gameTicket.entity.GameTicket;
import com.promotion.gameTicket.repo.GameTicketRepo;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import com.promotion.utility.RandomId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GameTicketServiceImpl implements GameTicketService{

    @Autowired private final UserRepo userRepo;
    @Autowired private final GameRepo gameRepo;
    @Autowired private final GameItemRepo gameItemRepo;
    @Autowired private final GameTicketRepo gameTicketRepo;


    @Override
    @Transactional
    public GameTicketResp saveGameTicket(String randomId, GameTicketRequest request) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(!userGet.getRole().equals(Role.AGENT)){
            return GameTicketResp
                    .builder()
                    .status(false)
                    .statusCode(403)
                    .statusMessage("Hello you haven't authorize to use this api")
                    .build();
        }

        Optional<Game> resGameObj = gameRepo.findByIdAndAgentsContains(request.getGameId(),userGet);
        if(resGameObj.isEmpty()){
            return GameTicketResp
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello some issue you and game relationship")
                    .build();
        }
        Game resGameObjGet = resGameObj.get();
        System.out.println(resGameObj);

        Optional<GameItem> resGameIteObj = gameItemRepo.findByIdAndGame(request.getGameItemId(),resGameObjGet);
        if(resGameIteObj.isEmpty()){
            return GameTicketResp
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello insert data of game and game item are issue")
                    .build();
        }
        GameItem resGameItemObjGet = resGameIteObj.get();

        List<GameTicket> gameTicketList = new ArrayList<>();

        for(int i = 0; i < request.getTicketAmt(); i++) {
            GameTicket ticket = GameTicket
                    .builder()
                    .agent(userGet)
                    .game(resGameObjGet)
                    .gameItem(resGameItemObjGet)
                    .ticketNumber(RandomId.generateRandomId())
                    .useStatus(true)
                    .build();

            gameTicketList.add(ticket);
        }

        gameTicketRepo.saveAll(gameTicketList);

        return GameTicketResp
                .builder()
                .status(true)
                .statusCode(201)
                .statusMessage("Completed Created Game Ticket for " + resGameObjGet.getGameName() +"'s "+resGameItemObjGet.getGameItem())
                .build();
    }

    @Override
    public GameTicketResp getTicketAllByAgent(String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();

        List<GameTicket> gameTicketList = gameTicketRepo.findByAgent(userGet);

        List<GameTicketResp.GameTicketResObj> resObjList = gameTicketList.stream().map(
                ticket -> GameTicketResp.GameTicketResObj
                        .builder()
                        .id(ticket.getId())
                        .gameCode(ticket.getGame().getGameCode())
                        .gameName(ticket.getGame().getGameName())
                        .gameItem(ticket.getGameItem().getGameItem())
                        .gameItemDesc(ticket.getGameItem().getGameItemDesc())
                        .gameItemStatus(ticket.getGameItem().getGameItemStatus())
                        .ticketNumber(ticket.getTicketNumber())
                        .ticketStatus(ticket.getUseStatus())
                        .remark(ticket.getRemark())
                        .build()
        ).toList();
        return GameTicketResp
                .builder()
                .statusMessage("API Good Working")
                .statusCode(200)
                .status(true)
                .gameTicketResObjList(resObjList)
                .build();
    }
}
