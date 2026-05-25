package com.university.techcorp.domain;

public class Developer extends Employee {
    public Developer(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return skill * 2;
    }
}