package com.university.techcorp.domain;

public class Tester extends Employee {
    public Tester(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        // Tester pracuje zgodnie z bazowym skillem
        return skill;
    }
}