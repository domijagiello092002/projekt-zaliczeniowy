package com.university.techcorp.events;

import com.university.techcorp.domain.Company;

public class EquipmentFailureEvent implements GameEvent {
    private static final double REPAIR_COST = 3000.0;

    @Override
    public void apply(Company company) {
        if (company == null) throw new IllegalArgumentException("Company cannot be null.");
        try {
            company.reduceCash(REPAIR_COST);
            System.out.println(" Equipment failure! Repair cost: -" + REPAIR_COST + " cash.");
        } catch (IllegalStateException e) {
            System.out.println(" Equipment failure! Insufficient funds. Debt incurred (-" + REPAIR_COST + ").");
            // Jeśli chcesz pozwolić na ujemne saldo (bankructwo), możesz dodać w Company:
            // company.setCash(company.getCash() - REPAIR_COST);
        }
    }
}