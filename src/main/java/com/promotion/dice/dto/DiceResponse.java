package com.promotion.dice.dto;

import com.promotion.utility.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class DiceResponse extends BaseResponse {

    private DiceWinObj diceWinObj;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DiceWinObj {
        private Integer gameId;
        private String gameName;
        private Integer gameItemId;
        private String gameItemName;
        private String gameItemDesc;
        private String gameTicketNumber;
        private List<Integer> diceNumberObjList;
    }
}
