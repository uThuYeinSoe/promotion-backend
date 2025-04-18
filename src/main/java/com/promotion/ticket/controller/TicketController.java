package com.promotion.ticket.controller;

import com.promotion.config.JWTService;
import com.promotion.ticket.dto.*;
import com.promotion.ticket.service.AdminTicketHistoryService;
import com.promotion.ticket.service.TicketService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/promotion/tc")
@RequiredArgsConstructor
public class TicketController {

    @Autowired private final JWTService jwtService;

    @Autowired private final TicketService ticketService;

    @Autowired private final AdminTicketHistoryService adminTicketHistoryService;

    @PostMapping("/ticket")
    public ResponseEntity<TicketResponse> saveTicket(@RequestHeader("Authorization") String token, @RequestBody TicketRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);
        TicketResponse resObj =ticketService.saveTicket(randomId,request);
        System.out.println(resObj);

        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/ticketHistory")
    public ResponseEntity<AdminTicketHistoryResponse> getCreateTicketHistory(){
        AdminTicketHistoryResponse resObj = adminTicketHistoryService.getAdminTicketHistory();
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

}
