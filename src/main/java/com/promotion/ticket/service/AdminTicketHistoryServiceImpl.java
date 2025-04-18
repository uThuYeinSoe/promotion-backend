package com.promotion.ticket.service;

import com.promotion.ticket.dto.AdminTicketHistoryResponse;
import com.promotion.ticket.entity.AdminTicketHistory;
import com.promotion.ticket.repo.AdminTicketHistoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminTicketHistoryServiceImpl implements AdminTicketHistoryService{

    @Autowired private final AdminTicketHistoryRepo adminTicketHistoryRepo;

    @Override
    @Transactional
    public AdminTicketHistoryResponse craetAdminTicketHistory(String randomId) {
        return null;
    }

    @Override
    @Transactional
    public AdminTicketHistoryResponse getAdminTicketHistory() {
        List<AdminTicketHistory> resObj = adminTicketHistoryRepo.findAll();
        return AdminTicketHistoryResponse
                .builder()
                .statusMessage("API Successfully")
                .status(true)
                .statusCode(200)
                .adminTicketHistories(resObj)
                .build();
    }
}
