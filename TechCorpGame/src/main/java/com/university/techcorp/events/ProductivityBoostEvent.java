package com.university.techcorp.events;

import com.university.techcorp.domain.Company;

public class ProductivityBoostEvent implements GameEvent {
    private static final double BOOST_AMOUNT = 5000.0;

    @Override
    public void apply(Company company) {
        if (company == null) throw new IllegalArgumentException("Company cannot be null.");
        company.addCash(BOOST_AMOUNT);
        System.out.println(" Productivity boost! Company gained " + BOOST_AMOUNT + " cash.");
    }
}