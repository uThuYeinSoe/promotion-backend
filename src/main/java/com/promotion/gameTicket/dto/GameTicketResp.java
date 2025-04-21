package com.promotion.gameTicket.dto;

import com.promotion.utility.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class GameTicketResp extends BaseResponse {

    private List<GameTicketResObj> gameTicketResObjList;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GameTicketResObj {
        private Integer id;
        private String gameCode;
        private String gameName;
        private String gameItem;
        private String gameItemDesc;
        private Boolean gameItemStatus;
        private String ticketNumber;
        private Boolean ticketStatus;
        private String remark;
    }
}
