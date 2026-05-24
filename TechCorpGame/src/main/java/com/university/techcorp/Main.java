package com.university.techcorp;

import com.university.techcorp.domain.Company;
import com.university.techcorp.domain.Developer;
import com.university.techcorp.domain.Manager;
import com.university.techcorp.domain.Project;
import com.university.techcorp.domain.Tester;
import com.university.techcorp.engine.GameEngine;
import com.university.techcorp.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // 1. Tworzymy firmę
        Company company = new Company("TechCorp", 50000);

        // 2. Rekrutujemy pracowników (tworzymy obiekty RAZ!)
        Developer anna = new Developer("Anna", 8, 7000);
        Tester piotr = new Tester("Piotr", 6, 6000);
        Manager ewa = new Manager("Ewa", 7, 9000); // Zgodnie z wytycznymi

        // 3. Zatrudniamy ich w firmie
        company.hireEmployee(anna, anna.getSkill());
        company.hireEmployee(piotr, piotr.getSkill());
        company.hireEmployee(ewa, ewa.getSkill());

        // 4. Tworzymy projekt i przypisujemy TYCH SAMYCH pracowników
        Project mobileApp = new Project("Mobile App", 100, 5000, 15000);
        mobileApp.addEmployee(anna);
        mobileApp.addEmployee(piotr);
        company.addProject(mobileApp);

        // 5. Drugi projekt (WEBSITE) + Manager
        Project website = new Project("Company Website", 60, 3000, 8000);
        website.addEmployee(ewa);
        company.addProject(website);

        // 6. Uruchomienie silnika gry
        ConsoleUI ui = new ConsoleUI();
        GameEngine engine = new GameEngine(company, ui);
        engine.run();
    }
}