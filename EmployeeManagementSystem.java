import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EmployeeManagementSystem {

    private static final List<String> VALID_DEPARTMENTS = Arrays.asList(
            "Engineering", "Management", "HR", "Finance", "IT");

    public static class Employee {
        private final String id;
        private final String name;
        private final String department;
        private final double baseSalary;

        public Employee(String id, String name, String department, double baseSalary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.baseSalary = baseSalary;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDepartment() { return department; }
        public double getBaseSalary() { return baseSalary; }
    }

    private final Map<String, Employee> employees = new LinkedHashMap<>();

    // ---------------------------------------------------------
    // 1. Add employee with full validation
    // ---------------------------------------------------------
    public boolean addEmployee(String id, String name, String department, double baseSalary) {
        // --- ID validation ---
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        if (id.trim().length() < 2 || id.trim().length() > 10)
            throw new IllegalArgumentException("Employee ID must be 2 to 10 characters.");
        if (!id.trim().matches("[A-Za-z0-9]+"))
            throw new IllegalArgumentException("Employee ID must contain only letters and digits (no spaces).");

        // --- Name validation ---
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Employee name cannot be empty.");
        if (name.trim().length() < 2)
            throw new IllegalArgumentException("Employee name must be at least 2 characters.");
        if (!name.trim().matches("[A-Za-z ]+"))
            throw new IllegalArgumentException("Employee name must contain only letters and spaces.");

        // --- Department validation ---
        if (department == null || department.trim().isEmpty())
            throw new IllegalArgumentException("Department cannot be empty.");
        if (!VALID_DEPARTMENTS.contains(department.trim()))
            throw new IllegalArgumentException(
                "Invalid department. Must be one of: " + VALID_DEPARTMENTS);

        // --- Salary validation ---
        if (baseSalary <= 0)
            throw new IllegalArgumentException("Base salary must be greater than zero.");
        if (baseSalary > 1_000_000)
            throw new IllegalArgumentException("Base salary cannot exceed 1,000,000.");

        // --- Duplicate check ---
        if (employees.containsKey(id.trim()))
            return false;

        employees.put(id.trim(),
                new Employee(id.trim(), name.trim(), department.trim(), baseSalary));
        return true;
    }

    // ---------------------------------------------------------
    // 2. Remove employee
    // ---------------------------------------------------------
    public boolean removeEmployee(String id) {
        if (id == null || !employees.containsKey(id))
            return false;
        employees.remove(id);
        return true;
    }

    // ---------------------------------------------------------
    // 3. Process payment with hour validation
    // ---------------------------------------------------------
    public double processPayment(String id, double hours, double overtimeHours) {
        if (!employees.containsKey(id))
            throw new NoSuchElementException("Employee not found: " + id);

        if (hours < 0 || overtimeHours < 0)
            throw new IllegalArgumentException("Hours cannot be negative.");
        if (hours > 168)
            throw new IllegalArgumentException("Regular hours cannot exceed 168 per week.");
        if (overtimeHours > 40)
            throw new IllegalArgumentException("Overtime hours cannot exceed 40 per week.");
        if (hours + overtimeHours > 200)
            throw new IllegalArgumentException("Total hours (regular + overtime) cannot exceed 200.");

        Employee emp = employees.get(id);

        double hourlyRate = emp.getBaseSalary() / 160.0;
        double standardPay = hours * hourlyRate;
        double overtimePay = overtimeHours * hourlyRate * 1.5;

        double grossPay = standardPay + overtimePay;

        if ("Management".equalsIgnoreCase(emp.getDepartment())) {
            grossPay = grossPay + (grossPay * 0.10);
        }

        double taxRate = (grossPay >= 5000) ? 0.25 : 0.15;
        double netPay = grossPay - (grossPay * taxRate);

        return Math.round(netPay * 100.0) / 100.0;
    }

    // ---------------------------------------------------------
    // 4. Payroll report
    // ---------------------------------------------------------
    public String generatePayrollReport() {
        if (employees.isEmpty())
            return "No employees to report.";

        StringBuilder sb = new StringBuilder();
        sb.append("===== PAYROLL REPORT =====\n");

        double total = 0;
        String topEarner = null;
        double topPay = -1;

        for (Employee emp : employees.values()) {
            double net = processPayment(emp.getId(), 160, 0);
            total += net;
            if (net > topPay) {
                topPay = net;
                topEarner = emp.getName();
            }
            sb.append(String.format("%s (%s) - %s -> Net Pay: %.2f%n",
                    emp.getName(), emp.getId(), emp.getDepartment(), net));
        }

        double average = total / employees.size();
        sb.append(String.format("Total Payroll: %.2f%n", total));
        sb.append(String.format("Average Net Pay: %.2f%n", average));
        sb.append("Top Earner: ").append(topEarner)
          .append(String.format(" (%.2f)%n", topPay));

        return sb.toString();
    }

    // ---------------------------------------------------------
    // 5. Count
    // ---------------------------------------------------------
    public int getEmployeeCount() {
        return employees.size();
    }

    // ---------------------------------------------------------
    // Console menu
    // ---------------------------------------------------------
    public static void main(String[] args) {
        EmployeeManagementSystem sys = new EmployeeManagementSystem();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Employee Management System =====");
            System.out.println("1. Add Employee");
            System.out.println("2. Remove Employee");
            System.out.println("3. Process Payment");
            System.out.println("4. Payroll Report");
            System.out.println("5. Employee Count");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Invalid input. Please enter a number.");
                choice = -1;
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("ID: ");
                        String id = sc.nextLine();
                        System.out.print("Name: ");
                        String nm = sc.nextLine();
                        System.out.print("Dept (Engineering/Management/HR/Finance/IT): ");
                        String dp = sc.nextLine();
                        System.out.print("Salary: ");
                        double sal = sc.nextDouble();
                        sc.nextLine();
                        boolean ok = sys.addEmployee(id, nm, dp, sal);
                        System.out.println(ok ? "Added." : "Duplicate ID - rejected.");
                        break;

                    case 2:
                        System.out.print("ID: ");
                        System.out.println(sys.removeEmployee(sc.nextLine())
                                ? "Removed." : "Not found.");
                        break;

                    case 3:
                        System.out.print("ID: ");
                        String pid = sc.nextLine();
                        System.out.print("Hours: ");
                        double h = sc.nextDouble();
                        System.out.print("OT Hours: ");
                        double ot = sc.nextDouble();
                        sc.nextLine();
                        System.out.printf("Net Pay: %.2f%n",
                                sys.processPayment(pid, h, ot));
                        break;

                    case 4:
                        System.out.println(sys.generatePayrollReport());
                        break;

                    case 5:
                        System.out.println("Employees: " + sys.getEmployeeCount());
                        break;

                    case 0:
                        System.out.println("Bye!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }
}