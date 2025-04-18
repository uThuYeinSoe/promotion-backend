package com.promotion.user.service;

import com.promotion.user.dto.AgentResponse;
import com.promotion.user.dto.Profile;

public interface ProfileService {
    Profile getProfile(String randomId);
    AgentResponse getAgentAll(String randomId);
}
