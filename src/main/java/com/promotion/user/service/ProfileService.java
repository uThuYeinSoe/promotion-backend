package com.promotion.user.service;

import com.promotion.user.dto.AgentResponse;
import com.promotion.user.dto.Profile;
import com.promotion.user.dto.UserLevelDataRequest;
import com.promotion.user.dto.UserLevelDataResponse;

public interface ProfileService {
    Profile getProfile(String randomId);
    AgentResponse getAgentAll(String randomId);
    UserLevelDataResponse saveUserLevelData(String randomId,UserLevelDataRequest request);
}
