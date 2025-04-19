package com.promotion.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentGameAuthority {
    private String agentRandomId;
    private List<GameAuthorityInfo> gameAuthorityInfos;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public  static class GameAuthorityInfo {
        private Integer gameId;
        private String gameName;
        private String gameCode;
    }
}
