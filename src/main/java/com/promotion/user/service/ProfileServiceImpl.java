package com.promotion.user.service;

import com.promotion.game.dto.GameResponse;
import com.promotion.game.dto.GameResponseObj;
import com.promotion.game.entity.Game;
import com.promotion.game.repo.GameRepo;
import com.promotion.game.service.GameService;
import com.promotion.navigation.entity.Navigation;
import com.promotion.navigation.repo.NavigationRepo;
import com.promotion.ticket.dto.AgentGameAuthority;
import com.promotion.ticket.entity.Ticket;
import com.promotion.user.dto.Agent;
import com.promotion.user.dto.AgentResponse;
import com.promotion.user.dto.Profile;
import com.promotion.user.dto.SideMenu;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService{

    @Autowired private final UserRepo userRepo;
    @Autowired private final NavigationRepo navigationRepo;
    @Autowired private final GameRepo gameRepo;
    @Autowired private final GameService gameService;

    @Override
    @Transactional
    public Profile getProfile(String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        Ticket userTicket = userGet.getTicket();

        List<GameResponseObj> gameResponseObjList = new ArrayList<>();


        if(userGet.getRole().equals(Role.ADMIN)){

            List<User> agents = userRepo.findByRole(Role.AGENT);

            List<AgentGameAuthority> agentGameAuthorityList = new ArrayList<>();

            for(User agent : agents ){
                List<AgentGameAuthority.GameAuthorityInfo> authorityInfos = agent.getGames().stream()
                        .map(authority -> new AgentGameAuthority.GameAuthorityInfo(
                                authority.getId(),
                                authority.getGameName(),
                                authority.getGameCode()
                        ))
                        .collect(Collectors.toList());

                agentGameAuthorityList.add(new AgentGameAuthority(
                        agent.getRandomId(),
                        authorityInfos
                ));
            }

            List<Navigation> navByRole = navigationRepo.findAllByRoleName(String.valueOf(userGet.getRole()));
            List<Game> games = gameRepo.findAll();
            System.out.println(games);
            gameResponseObjList = games.stream().map(
                    game -> GameResponseObj
                            .builder()
                            .gameCode(game.getGameCode())
                            .gameId(game.getId())
                            .gameName(game.getGameName())
                            .gameConversion(String.valueOf(game.getConversationRate()))
                            .gameRoute(game.getGameRoute())
                            .build()
            ).collect(Collectors.toList());

            return Profile
                    .builder()
                    .status(true)
                    .statusCode(200)
                    .statusMessage("Profile Get By " + randomId)
                    .parentRandomId(userGet.getParentId())
                    .randomId(userGet.getRandomId())
                    .ticketAmt(userTicket.getTicketAmt())
                    .role(String.valueOf(userGet.getRole()))
                    .gameResponseObjList(gameResponseObjList)
                    .agentGameAuthorityList(agentGameAuthorityList)
                    .sideMenus(navByRole.stream().map(
                            nav -> new SideMenu(
                                    nav.getId().intValue(),nav.getNavName(),nav.getNavCode()
                            )
                    ).collect(Collectors.toList()))
                    .build();

        }else if(userGet.getRole().equals(Role.AGENT)){
            List<Navigation> navByRole = navigationRepo.findAllByRoleName(String.valueOf(userGet.getRole()));

            return Profile
                    .builder()
                    .status(true)
                    .statusCode(200)
                    .statusMessage("Profile Get By " + randomId)
                    .parentRandomId(userGet.getParentId())
                    .randomId(userGet.getRandomId())
                    .ticketAmt(userTicket.getTicketAmt())
                    .role(String.valueOf(userGet.getRole()))
                    .gameResponseObjList(
                            userGet.getGames()
                                    .stream()
                                    .map(
                                            game -> {
                                                return GameResponseObj
                                                        .builder()
                                                        .gameCode(game.getGameCode())
                                                        .gameId(game.getId())
                                                        .gameRoute(game.getGameRoute())
                                                        .gameConversion(String.valueOf(game.getConversationRate()))
                                                        .gameName(game.getGameName())
                                                        .build();
                                            }
                                    )
                                    .collect(Collectors.toList()))
                    .sideMenus(navByRole.stream().map(
                            nav -> new SideMenu(
                                    nav.getId().intValue(),nav.getNavName(),nav.getNavCode()
                            )
                    ).collect(Collectors.toList()))
                    .build();
        }else if(userGet.getRole().equals(Role.USER)){
            GameResponse resObj = gameService.getGameAll(userGet.getRandomId());

            List<Navigation> navByRole = navigationRepo.findAllByRoleName(String.valueOf(userGet.getRole()));

            return Profile
                    .builder()
                    .status(true)
                    .statusCode(200)
                    .statusMessage("Profile Get By " + randomId)
                    .parentRandomId(userGet.getParentId())
                    .randomId(userGet.getRandomId())
                    .role(String.valueOf(userGet.getRole()))
                    .gameResponseObjList(
                            resObj.getGameList()
                                    .stream()
                                    .map(
                                            game -> {
                                                return GameResponseObj
                                                        .builder()
                                                        .gameCode(game.getGameCode())
                                                        .gameId(game.getId())
                                                        .gameRoute(game.getGameRoute())
                                                        .gameConversion(String.valueOf(game.getConversationRate()))
                                                        .gameName(game.getGameName())
                                                        .build();
                                            }
                                    )
                                    .collect(Collectors.toList()))
                    .sideMenus(navByRole.stream().map(
                            nav -> new SideMenu(
                                    nav.getId().intValue(),nav.getNavName(),nav.getNavCode()
                            )
                    ).collect(Collectors.toList()))
                    .build();
        }
    return null;
    }

    @Override
    public AgentResponse getAgentAll(String randomId) {
       List<User> usersList = userRepo.findByParentId(randomId);
        List<Agent> agentList = usersList.stream()
                .map(user -> Agent.builder()
                        .agentRandomId(user.getRandomId())
                        .agentAmt(
                                user.getTicket() != null && user.getTicket().getTicketAmt() != null
                                        ? user.getTicket().getTicketAmt()
                                        : 0
                        )
                        .build()
                )
                .collect(Collectors.toList());

        return AgentResponse
                .builder()
                .status(true)
                .statusCode(200)
                .statusMessage("API Good Working")
                .agents(agentList)
                .build();
    }
}
