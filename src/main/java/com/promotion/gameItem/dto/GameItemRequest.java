package com.promotion.gameItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameItemRequest {
    private Integer id;
    private String gameCode;
    private String gameItem;
    private String gameItemDesc;
}
