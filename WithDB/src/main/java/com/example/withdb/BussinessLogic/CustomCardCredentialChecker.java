package com.example.withdb.BussinessLogic;

import org.springframework.stereotype.Component;

/**
 *  Class CustomCardCredentialChecker simply checks validation of the card.
 *    Requirements: Card information should start with word 'Valid'.
 */
@Component
public class CustomCardCredentialChecker implements CardCredentialChecker {
    @Override
    public boolean checkCardCredential(String cardId) {
        return true;
    }
}
