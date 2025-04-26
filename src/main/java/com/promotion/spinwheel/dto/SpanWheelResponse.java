package com.promotion.spinwheel.dto;

import com.promotion.utility.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class SpanWheelResponse extends BaseResponse {

    private SpinWheelWinObj spinWheelWinObj;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpinWheelWinObj {
        private Integer gameId;
        private String gameName;
        private Integer gameItemId;
        private String gameItemName;
        private String gameItemDesc;
        private String gameTicketNumber;

    }
}
