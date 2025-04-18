package com.promotion.navigation.service;

import com.promotion.navigation.dto.NavigationRequest;
import com.promotion.navigation.dto.NavigationResponse;

public interface NavigationService {
    NavigationResponse saveNavigation(NavigationRequest request);
}
