package com.university.techcorp.domain;

public class Manager extends Employee {
    public Manager(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        return skill / 2;
    }
}