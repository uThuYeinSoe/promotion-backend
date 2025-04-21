package com.promotion.gameItem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promotion.game.entity.Game;
import com.promotion.gameTicket.entity.GameTicket;
import com.promotion.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private User agent;

    @Column(name ="game_item")
    private String gameItem;

    @Column(name = "game_item_desc")
    private String gameItemDesc;

    @Column(name = "game_item_status")
    private Boolean gameItemStatus;

    @OneToMany(mappedBy = "gameItem" , cascade = CascadeType.ALL)
    private List<GameTicket> gameTicketList = new ArrayList<>();
}
