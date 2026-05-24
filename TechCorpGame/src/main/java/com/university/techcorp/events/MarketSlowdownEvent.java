package com.university.techcorp.events;

import com.university.techcorp.domain.Company;

public class MarketSlowdownEvent implements GameEvent {
    private static final double LOSS_AMOUNT = 5000.0;

    @Override
    public void apply(Company company) {
        if (company == null) throw new IllegalArgumentException("Company cannot be null.");
        try {
            company.reduceCash(LOSS_AMOUNT);
            System.out.println(" Market slowdown! Company lost " + LOSS_AMOUNT + " cash.");
        } catch (IllegalStateException e) {
            System.out.println(" Market slowdown! Insufficient funds. Debt incurred (-" + LOSS_AMOUNT + ").");
        }
    }
}