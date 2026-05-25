package com.university.techcorp.domain;

import java.util.ArrayList;
import java.util.List;

public class Company {
    private String name;
    private double cash;
    private List<Employee> employees;
    private List<Project> projects;
    private static final double TARGET_VALUE = 100000;

    public Company(String name, double cash) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be blank.");
        }
        if (cash < 0) {
            throw new IllegalArgumentException("Initial cash cannot be negative.");
        }
        this.name = name;
        this.cash = cash;
        this.employees = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    // ZMIANA: Walidacja zgodnie z wytycznymi – cash nie spada poniżej 0
    public void reduceCash(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount to reduce cannot be negative.");
        if (cash < amount) {
            throw new IllegalStateException("Insufficient funds! Cash would drop below zero.");
        }
        cash -= amount;
    }

    public void addCash(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount to add cannot be negative.");
        cash += amount;
    }

    public boolean hireEmployee(Employee employee, int skill) {
        if (employee == null) throw new IllegalArgumentException("Employee cannot be null.");
        int recruitmentCost = 1000 + (skill * 500);
        
        if (cash >= recruitmentCost) {
            reduceCash(recruitmentCost);
            employees.add(employee);
            System.out.println("Hired " + employee.getName() + " for " + recruitmentCost + " cash.");
            return true;
        } else {
            System.out.println("Not enough cash! Need " + recruitmentCost + " cash.");
            return false;
        }
    }

    public boolean addProject(Project project) {
        if (project == null) throw new IllegalArgumentException("Project cannot be null.");
        int setupCost = project.getSetupCost();
        if (cash >= setupCost) {
            reduceCash(setupCost);
            projects.add(project);
            System.out.println("Started project: " + project.getName() + " (cost: " + setupCost + " cash)");
            return true;
        } else {
            System.out.println("Not enough cash for project setup! Need " + setupCost + " cash.");
            return false;
        }
    }

    public void collectProjectBonus(Project project) {
        int bonus = project.getCashBonus();
        addCash(bonus);
        project.setBonusClaimed(true);
        System.out.println("Project '" + project.getName() + "' completed! Bonus: +" + bonus + " cash.");
    }

    public double paySalaries() {
        double totalSalaries = 0;
        for (Employee e : employees) {
            totalSalaries += e.getSalary() * 0.5;
        }
        cash -= totalSalaries;
        System.out.println("Salaries paid (50% rate): " + totalSalaries + " cash.");
        return totalSalaries;
    }

    public boolean isBankrupt() { return cash < 0; }
    public boolean reachedTargetValue() { return calculateCompanyValue() >= TARGET_VALUE; }

    public boolean allProjectsFinished() {
        if (projects.isEmpty()) return false;
        for (Project p : projects) {
            if (!p.isFinished()) return false;
        }
        return true;
    }

    public double calculateCompanyValue() {
        double projectValue = 0;
        for (Project p : projects) {
            if (p.isFinished()) projectValue += p.getCashBonus();
        }
        return cash + projectValue;
    }

    public void showStatus() {
        System.out.println("\n===  " + name + " Status ===");
        System.out.printf("Cash: $%.2f%n", cash);
        System.out.println("Employees: " + employees.size());
        System.out.println("Projects:");
        if (projects.isEmpty()) {
            System.out.println("   (no projects)");
        } else {
            for (Project p : projects) {
                System.out.printf("   - %-15s | Progress: %3d%% | Status: %s%n",
                    p.getName(), p.getProgress(), p.getStatus());
            }
        }
        System.out.println("==============================\n");
    }

    // Gettery
    public String getName() { return name; }
    public double getCash() { return cash; }
    public List<Employee> getEmployees() { return new ArrayList<>(employees); }
    public List<Project> getProjects() { return new ArrayList<>(projects); }
    public static double getTargetValue() { return TARGET_VALUE; }
}