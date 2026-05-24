package com.university.techcorp.events;

import com.university.techcorp.domain.Company;

public class ClientBonusEvent implements GameEvent {
    private static final double BONUS_AMOUNT = 8000.0;

    @Override
    public void apply(Company company) {
        if (company == null) throw new IllegalArgumentException("Company cannot be null.");
        company.addCash(BONUS_AMOUNT);
        System.out.println(" Happy client! Received bonus payment: +" + BONUS_AMOUNT + " cash.");
    }
}