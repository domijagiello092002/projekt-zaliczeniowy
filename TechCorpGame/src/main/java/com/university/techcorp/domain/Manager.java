package com.university.techcorp.domain;

public class Manager extends Employee {
    public Manager(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        // ZMIANA: Uzupełniono uciętą metodę. Manager koordynuje, więc pracuje z 50% efektywności.
        // Możesz zmienić na `skill` lub `skill * 1.5` w zależności od balansu gry.
        return skill / 2;
    }
}