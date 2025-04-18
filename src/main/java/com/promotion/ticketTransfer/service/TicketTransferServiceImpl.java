package com.promotion.ticketTransfer.service;

import com.promotion.ticket.entity.Ticket;
import com.promotion.ticket.repo.TicketRepo;
import com.promotion.ticketTransfer.dto.TicketTransactionHistoryResponse;
import com.promotion.ticketTransfer.dto.TicketTransferRequest;
import com.promotion.ticketTransfer.dto.TicketTransferResponse;
import com.promotion.ticketTransfer.entity.TicketTransactionHistory;
import com.promotion.ticketTransfer.repo.TicketTransactionHistoryRepo;
import com.promotion.user.entity.Role;
import com.promotion.user.entity.User;
import com.promotion.user.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketTransferServiceImpl implements TicketTransferService{

    @Autowired private final UserRepo userRepo;
    @Autowired private final TicketRepo ticketRepo;
    @Autowired private final TicketTransactionHistoryRepo ticketTransactionHistoryRepo;

    @Override
    @Transactional
    public TicketTransferResponse saveTicketTransfer(String senderRandomId, TicketTransferRequest request) {
            Optional<User> senderUser = userRepo.findByRandomId(senderRandomId);
            User senderUserGet = senderUser.get();

            if(!senderUserGet.getRole().equals(Role.ADMIN)){
                return TicketTransferResponse
                        .builder()
                        .status(false)
                        .statusCode(403)
                        .statusMessage("Hello User you haven't authorized for this api")
                        .build();
            }

            if(request.getTicketAmt() > senderUserGet.getTicket().getTicketAmt()){
                return TicketTransferResponse
                        .builder()
                        .status(false)
                        .statusCode(403)
                        .statusMessage("Hello User you haven't authorized for this api")
                        .build();
            }

            Optional<User> receiverUser = userRepo.findByRandomId(request.getReceiverRandomId());
            User receiverUserGet = receiverUser.get();
            if(!receiverUserGet.getRole().equals(Role.AGENT)){
                return TicketTransferResponse
                        .builder()
                        .status(false)
                        .statusCode(400)
                        .statusMessage("You can't transfer ticket to this user role")
                        .build();
            }

            Ticket senderTicketObj = senderUserGet.getTicket();
            senderTicketObj.setTicketAmt(senderTicketObj.getTicketAmt() - request.getTicketAmt());
            System.out.println(senderTicketObj);

            Ticket receiverTicketObj = receiverUserGet.getTicket();

            if(ObjectUtils.isEmpty(receiverTicketObj)){
                Ticket recTicketObj = Ticket
                        .builder()
                        .ticketAmt(request.getTicketAmt())
                        .user(receiverUserGet)
                        .build();
                Ticket resObj = ticketRepo.save(recTicketObj);
                receiverUserGet.setTicket(resObj);
                System.out.println(resObj);
            }else{
                System.out.println(receiverTicketObj);
                receiverTicketObj.setTicketAmt(receiverTicketObj.getTicketAmt() + request.getTicketAmt());
                ticketRepo.save(receiverTicketObj);
            }

            ticketTransactionHistoryRepo.save(TicketTransactionHistory
                    .builder()
                            .ticketAmt(request.getTicketAmt())
                            .receiverRandomId(receiverUserGet.getRandomId())
                            .senderRandomId(senderUserGet.getRandomId())
                            .transferDateTime(LocalDateTime.now())
                    .build());

        return TicketTransferResponse
                .builder()
                .status(true)
                .statusCode(200)
                .statusMessage("Successfully Transfer Ticket from " + senderUserGet.getRandomId() + " to " + receiverUserGet.getRandomId() + " and success amount " + request.getTicketAmt())
                .build();
    }

    @Override
    @Transactional
    public TicketTransactionHistoryResponse getTicketTransactionHistory(String randomId) {
        Optional<User> currentUser = userRepo.findByRandomId(randomId);
        User currentUserGet = currentUser.get();
        System.out.println(currentUserGet);

        if(currentUserGet.getRole().equals(Role.ADMIN)){
            List<TicketTransactionHistory> ticketTransactionHistoryList = ticketTransactionHistoryRepo.findAll();
            return TicketTransactionHistoryResponse
                    .builder()
                    .status(true)
                    .statusCode(200)
                    .statusMessage("API Successfully")
                    .ticketTransactionHistories(ticketTransactionHistoryList)
                    .build();
        }else if(currentUserGet.getRole().equals(Role.AGENT)){
            List<TicketTransactionHistory> ticketTransactionHistoryList = ticketTransactionHistoryRepo.findByReceiverRandomId(randomId);
            return TicketTransactionHistoryResponse
                    .builder()
                    .status(true)
                    .statusCode(200)
                    .statusMessage("API Successfully")
                    .ticketTransactionHistories(ticketTransactionHistoryList)
                    .build();
        }else{
            return TicketTransactionHistoryResponse
                    .builder()
                    .status(false)
                    .statusCode(400)
                    .statusMessage("Not Found Data")
                    .build();
        }

    }
}
