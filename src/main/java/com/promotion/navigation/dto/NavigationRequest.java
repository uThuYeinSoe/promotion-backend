package com.promotion.navigation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NavigationRequest {
    private String navName;
    private String navCode;
    private List<Long> roleIds;
}
