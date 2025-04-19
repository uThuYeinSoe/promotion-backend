package com.promotion.ticket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promotion.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer ticketAmt;

    @OneToOne(mappedBy = "ticket")
    @ToString.Exclude
    @JsonIgnore
    private User user;
}
