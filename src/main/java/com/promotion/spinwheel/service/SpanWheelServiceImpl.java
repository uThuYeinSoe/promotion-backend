package com.promotion.spinwheel.service;

import com.promotion.game.entity.Game;
import com.promotion.game.repo.GameRepo;
import com.promotion.gameItem.entity.GameItem;
import com.promotion.gameItem.repo.GameItemRepo;
import com.promotion.gameTicket.entity.GameTicket;
import com.promotion.gameTicket.repo.GameTicketRepo;
import com.promotion.spinwheel.dto.SpanWheelResponse;
import com.promotion.ticket.entity.Ticket;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import com.promotion.winner.entity.Winner;
import com.promotion.winner.repo.WinnerRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpanWheelServiceImpl implements SpanWheelService{

    @Autowired private final UserRepo userRepo;
    @Autowired private final GameItemRepo gameItemRepo;
    @Autowired private final GameTicketRepo gameTicketRepo;
    @Autowired private final GameRepo gameRepo;
    @Autowired private final WinnerRepo winnerRepo;


    @Override
    public SpanWheelResponse getWinObj(String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(!userGet.getRole().equals(Role.USER)){
            return SpanWheelResponse
                    .builder()
                    .status(false)
                    .statusCode(403)
                    .statusMessage("Hello you haven't authorize for this api")
                    .build();
        }

        Optional<User> agent = userRepo.findByRandomId(userGet.getParentId());
        if(agent.isEmpty()){
            return SpanWheelResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello your agent have some issue please connect with your agent")
                    .build();
        }
        User agentGet = agent.get();

        Optional<Game> gameObj = gameRepo.findById(1L);
        Game gameObjGet = gameObj.get();

        if(gameObjGet.getConversationRate() > userGet.getTicket().getTicketAmt()) {
            return SpanWheelResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello User Your Ticket Amount is not Enough")
                    .build();
        }

        if(gameObjGet.getConversationRate() > agentGet.getTicket().getTicketAmt()) {
            return SpanWheelResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello User Your Agent Ticket Amount is not Enough Please contact with your agent")
                    .build();
        }

        //Agent သည် Game ကို သုံးခွင့် ရ / မရ ဆိုတာကို စစ်တာ
        Optional<Game> selectedGame = agentGet.getGames()
                .stream()
                .filter(
                        game -> "0100".equals(game.getGameCode())
                )
                .findFirst();

        if(selectedGame.isEmpty()){
            return SpanWheelResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello your agent haven't authorize this game.")
                    .build();
        }

        //game Item တွေသည် true သာဖြစ်ရမည်။
        List<GameItem> gameItemList = gameItemRepo.findByAgentAndGameAndGameItemStatusTrue(agentGet,selectedGame.get());
        //ရလာတဲ့ gameItem တွေရဲ့ ticket true တွေကို ဆွဲထုတ်တာဖြစ်ပါတယ်။
        List<GameItem> gameItemListWithGameTicketTrue = gameTicketRepo.findActiveGameItemsByAgentAndGame(agentGet,selectedGame.get());

        List<GameItem> finalGameItemList = new ArrayList<>(gameItemList.stream().filter(
                game -> gameItemListWithGameTicketTrue.stream().anyMatch(
                        otherGame -> otherGame.getId().equals(game.getId())
                )
        ).toList());

        if(finalGameItemList.isEmpty()) {
            return SpanWheelResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello No Ticket Value Now Please Contact With Your agent.")
                    .build();
        }

        Collections.shuffle(finalGameItemList);

        List<GameTicket> gameTickets = gameTicketRepo.findByAgentAndGameAndGameItemAndUseStatusTrue(agentGet,selectedGame.get(),finalGameItemList.getFirst());
        gameTickets.getFirst().setUseStatus(false);
        gameTickets.getFirst().setRemark("Already Finished" + userGet.getRandomId());
        gameTicketRepo.save(gameTickets.getFirst());


        var winObj = SpanWheelResponse.SpinWheelWinObj
                .builder()
                .gameId(selectedGame.get().getId())
                .gameName(selectedGame.get().getGameName())
                .gameItemId(gameItemList.getFirst().getId())
                .gameItemName(gameItemList.getFirst().getGameItem())
                .gameItemDesc(gameItemList.getFirst().getGameItemDesc())
                .gameTicketNumber(gameTickets.getFirst().getTicketNumber())
                .build();

        Ticket userTicketObj = userGet.getTicket();
        userTicketObj.setTicketAmt(userTicketObj.getTicketAmt() - gameObj.get().getConversationRate());
        userGet.setTicket(userTicketObj);
        userRepo.save(userGet);

        Ticket agentTicketObj = agentGet.getTicket();
        agentTicketObj.setTicketAmt(agentTicketObj.getTicketAmt() - gameObj.get().getConversationRate());
        agentGet.setTicket(agentTicketObj);
        userRepo.save(agentGet);

        Winner winnerObj = Winner
                .builder()
                .winnerUser(userGet)
                .winnerAgent(agentGet)
                .gameItem(winObj.getGameItemName())
                .winValue(winObj.getGameItemDesc())
                .gameName(winObj.getGameName())
                .useTicketAmt(gameObj.get().getConversationRate())
                .winningDate(LocalDateTime.now())
                .build();

        winnerRepo.save(winnerObj);

        return SpanWheelResponse
                .builder()
                .status(true)
                .statusCode(200)
                .statusMessage("You get reward " + gameItemList.getFirst().getGameItemDesc() + " and Ticket Number " + gameTickets.getFirst().getTicketNumber())
                .spinWheelWinObj(winObj)
                .build();
    }
}
