package com.university.techcorp.ui;

import com.university.techcorp.domain.Company;
import com.university.techcorp.domain.Employee;
import com.university.techcorp.domain.Project;
import com.university.techcorp.domain.ProjectStatus;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private static final String[] PROJECT_NAMES = {
        "Mobile App", "Website", "API Integration", "Data Dashboard",
        "Cloud Migration", "Security Audit", "AI Chatbot", "CRM System"
    };

    public void showTurn(int turn) {
        System.out.println("\n══════════ TURN " + turn + " / 20 ══════════");
    }

    public void showCompanyStatus(Company company) {
        System.out.println("\n══════════ COMPANY STATUS ══════════");
        System.out.printf("    Name:  %s%n", company.getName());
        System.out.printf("    Cash:  %.2f%n", company.getCash());
        System.out.printf("    Value: %.2f / %.0f%n", company.calculateCompanyValue(), Company.getTargetValue());
        System.out.printf("    Employees: %d%n", company.getEmployees().size());

        double totalSalaries = company.getEmployees().stream()
                .mapToDouble(e -> e.getSalary() * 0.5)
                .sum();
        if (totalSalaries > 0) {
            System.out.printf("    Salaries/turn (50%%): %.2f%n", totalSalaries);
        }

        if (!company.getProjects().isEmpty()) {
            System.out.println("    Projects: ");
            for (Project p : company.getProjects()) {
                String bonusInfo = p.isBonusClaimed() ? "" : " (Bonus: +" + p.getCashBonus() + ")";
                System.out.printf("      • %-20s | Progress: %3d/%d | %s%s%n",
                        p.getName(), p.getProgress(), p.getRequiredWork(), p.getStatus(), bonusInfo);
            }
        }

        if (!company.getEmployees().isEmpty()) {
            System.out.println("    Team: ");
            for (Employee e : company.getEmployees()) {
                String role = e.getClass().getSimpleName();
                System.out.printf("      - %-15s | %-10s | Skill: %2d | Work/Turn: %2d | Salary: %.0f%n",
                        e.getName(), role, e.getSkill(), e.work(), e.getSalary());
            }
        }
        System.out.println("═══════════════════════════════════\n");
    }

    public void showMenu() {
        System.out.println("🎮 MENU:");
        System.out.println("  1. Work one turn");
        System.out.println("  2. Start all planned projects");
        System.out.println("  3. Hire Employee");
        System.out.println("  4. Add New Project");
        System.out.println("  5. Assign Employee to Project");
        System.out.println("  0. Exit Game");
        System.out.print("   ▶ Choose: ");
    }

    public int readChoice() {
        while (!scanner.hasNextInt()) {
            System.out.print("    Invalid input. Enter a number: ");
            scanner.nextLine();
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return choice;
    }

    public HireRequest promptHireEmployee() {
        System.out.println("\n---  Hire Employee ---");
        System.out.println("  1. Developer (2x work efficiency)");
        System.out.println("  2. Tester    (1x work efficiency)");
        System.out.println("  3. Manager   (0.5x work efficiency)");
        System.out.print("  ▶ Select Role (1-3): ");
        
        int roleChoice = readChoice();
        if (roleChoice < 1 || roleChoice > 3) return null;

        System.out.print("  ▶ Enter Name: ");
        String name = scanner.nextLine().trim();
        if (name.isBlank()) return null;

        System.out.print("  ▶ Enter Skill (1-10): ");
        int skill = readChoice();
        if (skill < 1 || skill > 10) {
            System.out.println("   Skill out of range.");
            return null;
        }

        int salary = 1000 + (skill * 300);
        int recruitmentCost = 1000 + (skill * 500);
        System.out.printf("   Expected Salary: %d/turn (50%% charged) | Recruitment: %d cash%n", salary, recruitmentCost);
        return new HireRequest(roleChoice, name, skill, salary);
    }

    public Project promptAddProject() {
        System.out.println("\n---  Available Projects ---");
        Set<String> usedNames = new HashSet<>();
        Project[] options = new Project[3];

        for (int i = 0; i < 3; i++) {
            String name;
            do {
                name = PROJECT_NAMES[(int)(Math.random() * PROJECT_NAMES.length)];
            } while (usedNames.contains(name));
            usedNames.add(name);

            int requiredWork = 20 + (int)(Math.random() * 61);
            int setupCost = 2000 + (requiredWork * 50);
            int cashBonus = requiredWork * 600;
            options[i] = new Project(name, requiredWork, setupCost, cashBonus);
            System.out.printf("  %d. %-20s | Work: %3d | Setup: %5d | Bonus: %5d%n",
                    i + 1, name, requiredWork, setupCost, cashBonus);
        }
        System.out.println("  0. Cancel");
        System.out.print("  ▶ Select project: ");
        
        int choice = readChoice();
        return (choice >= 1 && choice <= 3) ? options[choice - 1] : null;
    }

    public void promptAssignEmployeeToProject(Company company) {
        System.out.println("\n--- Assign Employee to Project ---");
        if (company.getEmployees().isEmpty()) {
        showMessage("   No employees available."); return;
        }
        if (company.getProjects().isEmpty()) {
            showMessage("   No projects available."); return;
        }

        System.out.println("   Employees: ");
        for (int i = 0; i < company.getEmployees().size(); i++) {
            Employee e = company.getEmployees().get(i);
            System.out.printf("    %d. %-15s (Skill: %d)%n", i + 1, e.getName(), e.getSkill());
        }
        System.out.print("  ▶ Select employee:  ");
        int empChoice = readChoice();
        if (empChoice < 1 || empChoice > company.getEmployees().size()) {
            showMessage("   Invalid selection."); return;
        }
        Employee selectedEmp = company.getEmployees().get(empChoice - 1);

        System.out.println("   Projects: ");
        for (int i = 0; i < company.getProjects().size(); i++) {
            Project p = company.getProjects().get(i);
            System.out.printf("    %d. %-20s [%s]%n", i + 1, p.getName(), p.getStatus());
        }
        System.out.print("  ▶ Select project:  ");
        int projChoice = readChoice();
        if (projChoice < 1 || projChoice > company.getProjects().size()) {
            showMessage("   Invalid selection."); return;
        }
        Project selectedProj = company.getProjects().get(projChoice - 1);

        if (selectedProj.getStatus() == ProjectStatus.FINISHED || selectedProj.getStatus() == ProjectStatus.CANCELLED) {
            showMessage("   Cannot assign employees to a project that is already " + selectedProj.getStatus() + ".");
            return;
        }

        selectedProj.addEmployee(selectedEmp);
        showMessage("   Assigned " + selectedEmp.getName() + " to '" + selectedProj.getName() + "'.");
    }

    public void showMessage(String msg) {
        System.out.println(msg);
    }

    public static final class HireRequest {
        public final int roleChoice, skill, salary;
        public final String name;
        public HireRequest(int roleChoice, String name, int skill, int salary) {
            this.roleChoice = roleChoice;
            this.name = name;
            this.skill = skill;
            this.salary = salary;
        }
    }
}