package com.promotion.dice.service;


import com.promotion.dice.dto.DiceResponse;

public interface DiceService {
    DiceResponse getWinObj(String randomId);
}
