package com.promotion.dice.service;

import com.promotion.dice.dto.DiceResponse;
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

@Service
@RequiredArgsConstructor
public class DiceServiceImpl implements DiceService{

    @Autowired private final UserRepo userRepo;
    @Autowired private final GameRepo gameRepo;
    @Autowired private final GameItemRepo gameItemRepo;
    @Autowired private final GameTicketRepo gameTicketRepo;
    @Autowired private final WinnerRepo winnerRepo;

    @Override
    @Transactional
    public DiceResponse getWinObj(String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        if(!userGet.getRole().equals(Role.USER)){
            return DiceResponse
                    .builder()
                    .status(false)
                    .statusCode(403)
                    .statusMessage("Hello you haven't authorize for this api")
                    .build();
        }

        Optional<User> agent = userRepo.findByRandomId(userGet.getParentId());
        if(agent.isEmpty()){
            return DiceResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello your agent have some issue please connect with your agent")
                    .build();
        }
        User agentGet = agent.get();

        Optional<Game> gameObj = gameRepo.findById(4L);
        Game gameObjGet = gameObj.get();

        if(gameObjGet.getConversationRate() > userGet.getTicket().getTicketAmt()) {
            return DiceResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello User Your Ticket Amount is not Enough")
                    .build();
        }

        if(gameObjGet.getConversationRate() > agentGet.getTicket().getTicketAmt()) {
            return DiceResponse
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
                        game -> "0300".equals(game.getGameCode())
                )
                .findFirst();

        if(selectedGame.isEmpty()){
            return DiceResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello your agent haven't authorize this game.")
                    .build();
        }

        //game Item တွေသည် true သာဖြစ်ရမည်။
        List<GameItem> gameItems = gameItemRepo.findByAgentAndGameAndGameItemStatusTrue(agentGet,selectedGame.get());

        for (GameItem item : gameItems) {
            List<GameTicket> tickets = item.getGameTicketList();

            boolean allUnused = tickets.isEmpty() ||
                    tickets.stream().allMatch(t -> Boolean.FALSE.equals(t.getUseStatus()));

            if (allUnused && Boolean.TRUE.equals(item.getGameItemStatus())) {
                item.setGameItemStatus(false);
                gameItemRepo.save(item);
            }
        }

        List<GameItem> gameItemList = gameItemRepo.findByAgentAndGameAndGameItemStatusTrue(agentGet,selectedGame.get());

        //ရလာတဲ့ gameItem တွေရဲ့ ticket true တွေကို ဆွဲထုတ်တာဖြစ်ပါတယ်။
        List<GameItem> gameItemListWithGameTicketTrue = gameTicketRepo.findActiveGameItemsByAgentAndGame(agentGet,selectedGame.get());

        List<GameItem> finalGameItemList = new ArrayList<>(gameItemList.stream().filter(
                game -> gameItemListWithGameTicketTrue.stream().anyMatch(
                        otherGame -> otherGame.getId().equals(game.getId())
                )
        ).toList());

        if(finalGameItemList.isEmpty()) {
            return DiceResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Hello No Ticket Value Now Please Contact With Your agent.")
                    .build();
        }

        System.out.println("Before Collection Shuffle");
        System.out.println(finalGameItemList);

        Collections.shuffle(finalGameItemList);

        GameItem selectedGameItem = finalGameItemList.get(0);
        System.out.println("Selected Game Item");
        System.out.println(selectedGameItem);

        List<GameTicket> gameTickets = gameTicketRepo.findByAgentAndGameAndGameItemAndUseStatusTrue(agentGet,selectedGame.get(),selectedGameItem);
        gameTickets.getFirst().setUseStatus(false);
        gameTickets.getFirst().setRemark("Already Finished" + userGet.getRandomId());
        gameTicketRepo.save(gameTickets.getFirst());

        List<Integer> pair = diceRandomTwoValue(Integer.valueOf(selectedGameItem.getGameItem()));
        System.out.println("Pair Value");
        System.out.println(pair);

        var winObj = DiceResponse.DiceWinObj
                .builder()
                .gameId(selectedGame.get().getId())
                .gameName(selectedGame.get().getGameName())
                .gameItemId(selectedGameItem.getId())
                .gameItemName(selectedGameItem.getGameItem())
                .gameItemDesc(selectedGameItem.getGameItemDesc())
                .gameTicketNumber(gameTickets.getFirst().getTicketNumber())
                .diceNumberObjList(pair)
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

        return DiceResponse
                .builder()
                .status(true)
                .statusCode(200)
                .statusMessage("You get reward " + gameItemList.getFirst().getGameItemDesc() + " and Ticket Number " + gameTickets.getFirst().getTicketNumber())
                .diceWinObj(winObj)
                .build();
    }

    public List<Integer> diceRandomTwoValue(Integer inputValue) {
        if (inputValue == null || inputValue < 2 || inputValue > 18) {
            System.out.println("Value must be between 2 and 18 (1 + 1 to 9 + 9)");
            return Collections.emptyList();
        }

        List<int[]> validPairs = new ArrayList<>();

        for (int a = 1; a <= 9; a++) {
            int b = inputValue - a;
            if (b >= 1 && b <= 9) {
                validPairs.add(new int[]{a, b});
            }
        }

        if (validPairs.isEmpty()) {
            System.out.println("No valid pairs found.");
            return Collections.emptyList();
        }

        Random random = new Random();
        int[] selectedPair = validPairs.get(random.nextInt(validPairs.size()));

        return Arrays.asList(selectedPair[0], selectedPair[1]);
    }
}
