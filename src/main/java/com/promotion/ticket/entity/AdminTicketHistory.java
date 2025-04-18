package com.promotion.ticket.entity;

import com.promotion.user.entity.Role;
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
public class AdminTicketHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "random_id")
    private String randomId;
    private Role role;
    @Column(name="ticket_amt")
    private Integer ticketAmt;
    @Column(name="create_time")
    private LocalDateTime crateTime;
}
