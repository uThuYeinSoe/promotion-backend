package com.promotion.ticketTransfer.controller;

import com.promotion.config.JWTService;
import com.promotion.ticketTransfer.dto.TicketTransactionHistoryResponse;
import com.promotion.ticketTransfer.dto.TicketTransferRequest;
import com.promotion.ticketTransfer.dto.TicketTransferResponse;
import com.promotion.ticketTransfer.service.TicketTransferService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Data
@Builder
@RequestMapping("/api/v1/promotion/tt")
public class TicketTransfer {

    @Autowired private final JWTService jwtService;
    @Autowired private final TicketTransferService ticketTransferService;

    @PostMapping("/ticketTransfer")
    public ResponseEntity<TicketTransferResponse> sendTicket(@RequestHeader("Authorization") String token,@RequestBody TicketTransferRequest request){
        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        TicketTransferResponse resObj = ticketTransferService.saveTicketTransfer(randomId,request);
        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }

    @GetMapping("/ticketTransfer")
    public ResponseEntity<TicketTransactionHistoryResponse> getTicketTransferHistory(@RequestHeader("Authorization") String token){

        String jwtToken = token.substring(7);
        String randomId = jwtService.extractUsername(jwtToken);

        TicketTransactionHistoryResponse resObj = ticketTransferService.getTicketTransactionHistory(randomId);

        return ResponseEntity.status(resObj.getStatusCode()).body(resObj);
    }
}
