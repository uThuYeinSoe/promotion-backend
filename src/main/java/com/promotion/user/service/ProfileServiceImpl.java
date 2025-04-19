package com.promotion.user.service;

import com.promotion.game.entity.Game;
import com.promotion.game.repo.GameRepo;
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

    @Override
    @Transactional
    public Profile getProfile(String randomId) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();
        Ticket userTicket = userGet.getTicket();

        List<Game> gameList = null;
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

            System.out.println("Hello World");
            System.out.println(agentGameAuthorityList);

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
                    .gameList(gameList)
                    .agentGameAuthorityList(agentGameAuthorityList)
                    .sideMenus(navByRole.stream().map(
                            nav -> new SideMenu(
                                    nav.getId().intValue(),nav.getNavName(),nav.getNavCode()
                            )
                    ).collect(Collectors.toList()))
                    .build();

        }else if(userGet.getRole().equals(Role.AGENT)){
            gameList = gameRepo.findGamesByRandomId(randomId);
            List<Navigation> navByRole = navigationRepo.findAllByRoleName(String.valueOf(userGet.getRole()));
            System.out.println(gameList);
            return Profile
                    .builder()
                    .status(true)
                    .statusCode(200)
                    .statusMessage("Profile Get By " + randomId)
                    .parentRandomId(userGet.getParentId())
                    .randomId(userGet.getRandomId())
                    .ticketAmt(userTicket.getTicketAmt())
                    .role(String.valueOf(userGet.getRole()))
                    .gameList(gameList)
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
