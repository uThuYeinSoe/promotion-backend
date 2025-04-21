package com.promotion.gameItem.dto;

import com.promotion.gameItem.entity.GameItem;
import com.promotion.utility.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class GameItemResp extends BaseResponse {

    private String randomAgentId;
    private List<gameItemDto> gameItemDtos;

    @Data
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class gameItemDto {
        private Integer id;
        private String gameCode;
        private String gameName;
        private String gameItemName;
        private String gameItemDesc;
        private Boolean gameItemStatus;
    }

}
