package com.promotion.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "game")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="game_code")
    private String gameCode;
    @Column(name="game_name")
    private String gameName;
    @Column(name="game_status")
    private Boolean gameStatus;
    @Column(name="conversation_rate")
    private Integer conversationRate;

    @ManyToMany(mappedBy = "games")
    @ToString.Exclude
    @JsonIgnore
    private List<User> agents;

    @OneToMany(mappedBy = "game" , cascade = CascadeType.ALL)
    private List<GameTicket> gameTicketList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id != null && id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
