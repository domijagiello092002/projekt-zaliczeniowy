package com.university.techcorp.domain;

public class Developer extends Employee {
    public Developer(String name, int skill, double salary) {
        super(name, skill, salary);
    }

    @Override
    public int work() {
        // ZMIANA: Dokończono urwaną metodę. Developer pracuje 2x szybciej niż bazowy skill
        return skill * 2;
    }
}