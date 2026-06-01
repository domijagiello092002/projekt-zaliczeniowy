package com.university.techcorp.engine;

import com.university.techcorp.domain.*;
import com.university.techcorp.events.*;
import com.university.techcorp.ui.ConsoleUI;
import java.util.Random;

public class GameEngine {
    private final Company company;
    private final ConsoleUI ui;
    private int turn;
    private boolean running;
    private GameResult result;
    private static final int MAX_TURNS = 20;
    private final Random random = new Random();

    public GameEngine(Company company, ConsoleUI ui) {
        if (company == null || ui == null) {
            throw new IllegalArgumentException("Company and UI cannot be null.");
        }
        this.company = company;
        this.ui = ui;
        this.turn = 1;
        this.running = true;
        this.result = GameResult.IN_PROGRESS;
    }

    public void run() {
        while (running) {
            ui.showTurn(turn);
            ui.showCompanyStatus(company);
            ui.showMenu();
            int choice = ui.readChoice();

            switch (choice) {
                case 1: workOneTurn(); break;
                case 2: startPlannedProjects(); break;
                case 3: hireEmployee(); break;
                case 4: addNewProject(); break;
                case 5: assignEmployeeToProject(); break;
                case 0: running = false; ui.showMessage("Exiting game..."); break;
                default: ui.showMessage("Invalid choice."); break;
            }

            if (running) {
                turn++; 
                processRandomEvent();
                evaluateGameResult();
            }
        }
        showFinalResult();
    }

    private void workOneTurn() {
        boolean anyWorked = false;
        for (Project p : company.getProjects()) {
            if (p.getStatus() == ProjectStatus.IN_PROGRESS) {
                p.workOneTurn(company); 
                anyWorked = true;
            }
        }
        if (anyWorked) {
            company.paySalaries();
            collectFinishedBonuses();
            ui.showMessage("Work completed for this turn.");
        } else {
            ui.showMessage("No projects are IN_PROGRESS. Start a project first.");
        }
    }

    private void collectFinishedBonuses() {
        for (Project p : company.getProjects()) {
            if (p.isFinished() && !p.isBonusClaimed()) {
                company.collectProjectBonus(p);
            }
        }
    }

    private void startPlannedProjects() {
        boolean started = false;
        for (Project p : company.getProjects()) {
            if (p.getStatus() == ProjectStatus.PLANNED) {
                try {
                    p.start();
                    started = true;
                    ui.showMessage("Started project: " + p.getName());
                } catch (IllegalStateException e) {
                    ui.showMessage("Could not start project: " + p.getName() + " (" + e.getMessage() + ")");
                }
            }
        }
        if (!started) ui.showMessage("No planned projects to start.");
    }

    private void hireEmployee() {
        ConsoleUI.HireRequest req = ui.promptHireEmployee();
        if (req == null) { ui.showMessage("Hiring cancelled."); return; }
        
        Employee newEmp = null;
        switch (req.roleChoice) {
            case 1: newEmp = new Developer(req.name, req.skill, req.salary); break;
            case 2: newEmp = new Tester(req.name, req.skill, req.salary); break;
            case 3: newEmp = new Manager(req.name, req.skill, req.salary); break;
        }
        if (newEmp != null && company.hireEmployee(newEmp, req.skill)) {
            ui.showMessage("Successfully hired " + newEmp.getName());
        }
    }

    private void addNewProject() {
        Project selected = ui.promptAddProject();
        if (selected == null) { ui.showMessage("Project selection cancelled."); return; }
        if (company.addProject(selected)) {
            ui.showMessage("Project added: " + selected.getName());
        }
    }

    private void assignEmployeeToProject() {
        ui.promptAssignEmployeeToProject(company);
    }

    private void processRandomEvent() {
    if (random.nextDouble() < 0.3) {
        GameEvent event = getRandomEvent();
        ui.showMessage("\n*** RANDOM EVENT: " + event.getClass().getSimpleName() + " ***");
        try {
            event.apply(company);
        } catch (Exception e) {
            ui.showMessage(" Event failed: " + e.getMessage());
        }
        ui.showMessage("*** END EVENT ***");
    }
}

    private GameEvent getRandomEvent() {
        GameEvent[] events = {
            new MarketSlowdownEvent(), new ProductivityBoostEvent(),
            new ClientBonusEvent(), new EquipmentFailureEvent()
        };
        return events[random.nextInt(events.length)];
    }

    private void evaluateGameResult() {
        double target = Company.getTargetValue();
        
        if (company.isBankrupt()) {
            result = GameResult.PLAYER_LOSES; running = false;
            ui.showMessage("GAME OVER: Company is bankrupt!"); return;
        }
        if (company.reachedTargetValue()) {
            result = GameResult.PLAYER_WINS; running = false;
            ui.showMessage("GAME OVER: Target value " + target + " reached!"); return;
        }
        if (turn >= MAX_TURNS) {
            double val = company.calculateCompanyValue();
            if (val >= target) {
                result = GameResult.PLAYER_WINS;
                ui.showMessage("GAME OVER: Target company value reached!");
            } else {
                result = GameResult.PLAYER_LOSES;
                ui.showMessage("GAME OVER: Not enough value after " + MAX_TURNS + " turns. Final: " + val);
            }
            running = false;
        }
    }

    private void showFinalResult() {
        System.out.println("\n========== GAME OVER ==========");
        System.out.println("Result: " + result);
        System.out.println("Turns played: " + turn);
        System.out.println("Final cash: " + company.getCash());
        System.out.println("Company value: " + company.calculateCompanyValue());
        System.out.println("Target value: " + Company.getTargetValue());
        System.out.println("================================");
    }
}