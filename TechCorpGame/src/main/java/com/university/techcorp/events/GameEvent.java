package com.university.techcorp.events;

import com.university.techcorp.domain.Company;

/**
 * Interfejs zdarzeń losowych wpływających na stan firmy.
 */
public interface GameEvent {
    void apply(Company company);
}