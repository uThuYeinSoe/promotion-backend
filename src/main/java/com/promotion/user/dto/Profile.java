package com.promotion.user.dto;

import com.promotion.utility.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends BaseResponse {
    private Integer id;
    private String randomId;
    private String parentRandomId;
    private Integer ticketAmt;
    private String role;
    private List<SideMenu> sideMenus;
}
