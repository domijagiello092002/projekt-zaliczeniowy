package com.university.techcorp.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project {
    private String name;
    private int requiredWork;
    private int progress;
    private List<Employee> team;
    private ProjectStatus status;
    private int cashBonus;
    private int setupCost;
    private boolean bonusClaimed;

    public Project(String name, int requiredWork, int setupCost, int cashBonus) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be blank.");
        }
        if (requiredWork <= 0) {
            throw new IllegalArgumentException("Required work must be positive.");
        }
        if (setupCost < 0) {
            throw new IllegalArgumentException("Setup cost cannot be negative.");
        }
        if (cashBonus < 0) {
            throw new IllegalArgumentException("Cash bonus cannot be negative.");
        }
        this.name = name;
        this.requiredWork = requiredWork;
        this.setupCost = setupCost;
        this.cashBonus = cashBonus;
        this.progress = 0;
        this.team = new ArrayList<>();
        this.status = ProjectStatus.PLANNED;
        this.bonusClaimed = false;
    }

    public void addEmployee(Employee employee) {
        if (employee == null) throw new IllegalArgumentException("Employee cannot be null.");
        if (status != ProjectStatus.PLANNED) {
            throw new IllegalStateException("Cannot modify team after project has started.");
        }
        team.add(employee);
    }

    public void start() {
        if (status != ProjectStatus.PLANNED) {
            throw new IllegalStateException("Project can only be started from PLANNED state. Current: " + status);
        }
        status = ProjectStatus.IN_PROGRESS;
    }

    public void putOnHold() {
        if (status != ProjectStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only put IN_PROGRESS projects on hold.");
        }
        status = ProjectStatus.ON_HOLD;
    }

    public void resume() {
        if (status != ProjectStatus.ON_HOLD) {
            throw new IllegalStateException("Can only resume projects that are ON_HOLD.");
        }
        status = ProjectStatus.IN_PROGRESS;
    }

    public void workOneTurn() {
        if (status != ProjectStatus.IN_PROGRESS) return;

        for (Employee e : team) {
            progress += e.work();
        }
        if (progress >= requiredWork) {
            progress = requiredWork;
            status = ProjectStatus.FINISHED;
        }
    }

    public boolean isFinished() { return status == ProjectStatus.FINISHED; }
    public boolean isBonusClaimed() { return bonusClaimed; }
    public void setBonusClaimed(boolean claimed) { this.bonusClaimed = claimed; }

    public String getName() { return name; }
    public int getProgress() { return progress; }
    public int getRequiredWork() { return requiredWork; }
    public int getCashBonus() { return cashBonus; }
    public int getSetupCost() { return setupCost; }
    public ProjectStatus getStatus() { return status; }

    public List<Employee> getTeam() { return Collections.unmodifiableList(team); }

    public int getCompletionPercentage() {
        if (requiredWork == 0) return 0;
        return (progress * 100) / requiredWork;
    }
}