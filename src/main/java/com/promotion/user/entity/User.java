package com.promotion.user.entity;


import com.promotion.game.entity.Game;
import com.promotion.gameItem.entity.GameItem;
import com.promotion.gameTicket.entity.GameTicket;
import com.promotion.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="random_id")
    private String randomId;

    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="parent_id")
    private String parentId;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ticket_id", referencedColumnName = "id")
    @ToString.Exclude
    private Ticket ticket;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "agent_games",
            joinColumns = @JoinColumn(name = "agent_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private List<Game> games = new ArrayList<>();

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<GameItem> gameItemList = new ArrayList<>();

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<GameTicket> gameTicketList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return randomId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
