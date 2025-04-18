package com.promotion.ticket.service;

import com.promotion.ticket.dto.TicketRequest;
import com.promotion.ticket.dto.TicketResponse;
import com.promotion.ticket.entity.AdminTicketHistory;
import com.promotion.ticket.entity.Ticket;
import com.promotion.ticket.repo.AdminTicketHistoryRepo;
import com.promotion.ticket.repo.TicketRepo;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService{

    @Autowired private UserRepo userRepo;
    @Autowired private TicketRepo ticketRepo;
    @Autowired private AdminTicketHistoryRepo adminTicketHistoryRepo;

    @Override
    @Transactional
    public TicketResponse saveTicket(String randomId, TicketRequest request) {
        Optional<User> user = userRepo.findByRandomId(randomId);
        User userGet = user.get();

        if(!userGet.getRole().equals(Role.ADMIN)){
            System.out.println("Error Start Working");
            return TicketResponse
                    .builder()
                    .statusMessage("Create Ticket Failed and Not Authorize For You")
                    .status(false)
                    .statusCode(403)
                    .build();
        }

        System.out.println(userGet);
        System.out.println(userGet.getTicket());

        if(userGet.getTicket() == null){
            Ticket ticketObj = Ticket
                    .builder()
                    .ticketAmt(request.getTicketAmt())
                    .user(userGet)
                    .build();
            Ticket ticketResObj = ticketRepo.save(ticketObj);
            userGet.setTicket(ticketResObj);
            userRepo.save(userGet);
        }else{
            Ticket exsitionTicketObj = userGet.getTicket();
            Ticket ticketObj = Ticket
                    .builder()
                    .id(exsitionTicketObj.getId())
                    .ticketAmt(request.getTicketAmt() + exsitionTicketObj.getTicketAmt())
                    .user(userGet)
                    .build();
            Ticket ticketResObj = ticketRepo.save(ticketObj);
        }

        LocalDateTime currentDateTime = LocalDateTime.now();

        AdminTicketHistory adminTicketHistory = AdminTicketHistory
                .builder()
                .crateTime(currentDateTime)
                .role(userGet.getRole())
                .ticketAmt(request.getTicketAmt())
                .randomId(userGet.getRandomId())
                .build();

        System.out.println(adminTicketHistory);

        adminTicketHistoryRepo.save(adminTicketHistory);

        return TicketResponse
                .builder()
                .statusMessage("Ticket Create Success " + request.getTicketAmt())
                .status(true)
                .statusCode(201)
                .build();
    }
}
