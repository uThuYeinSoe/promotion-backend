package com.promotion.gameTicket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promotion.game.entity.Game;
import com.promotion.gameItem.entity.GameItem;
import com.promotion.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.apache.logging.log4j.util.Lazy;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id" , nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private User agent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="game_id" , nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_item_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private GameItem gameItem;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @Column(name = "use_status")
    private Boolean useStatus;
    @Column(name = "remark")
    private String remark;
}
