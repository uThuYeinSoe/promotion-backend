package com.promotion.ticketTransfer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ticket_transaction_history")
public class TicketTransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="sender_random_id")
    private String senderRandomId;
    @Column(name="receiver_random_id")
    private String receiverRandomId;
    @Column(name="ticket_amt")
    private Integer ticketAmt;
    @Column(name="transfer_date_time")
    private LocalDateTime transferDateTime;
}
