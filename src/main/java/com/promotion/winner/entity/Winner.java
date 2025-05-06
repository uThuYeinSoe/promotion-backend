package com.promotion.winner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promotion.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "winner")
public class Winner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winnerUser")
    @ToString.Exclude
    @JsonIgnore
    private User winnerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winnerAgent")
    @ToString.Exclude
    @JsonIgnore
    private User winnerAgent;

    private String gameName;
    private String gameItem;
    private String winValue;
    private LocalDateTime winningDate;
    private Integer useTicketAmt;
}
