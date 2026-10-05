import java.util.NoSuchElementException;

public class EmployeeManagementSystemTest {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {

        System.out.println("===== BLACK-BOX TESTS =====\n");

        // ---------- Add employee ----------
        blackBox("BB-01 Add valid employee", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean ok = s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            check(ok && s.getEmployeeCount() == 1);
        });

        blackBox("BB-02 Reject duplicate ID", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            boolean second = s.addEmployee("E001", "Bob Kamanzi", "HR", 2000);
            check(!second && s.getEmployeeCount() == 1);
        });

        blackBox("BB-03 Reject empty ID", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("", "Alice Uwase", "Engineering", 3000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-04 Reject negative salary", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E001", "Alice Uwase", "Engineering", -100); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-05 Reject zero salary", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E001", "Alice Uwase", "Engineering", 0); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-06 Reject empty name", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E001", "", "Engineering", 3000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-07 Reject invalid department", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E001", "Alice Uwase", "Marketing", 3000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-08 Reject ID with space", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E 001", "Alice Uwase", "Engineering", 3000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-09 Reject name with numbers", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E001", "Alice123", "Engineering", 3000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        // ---------- Remove employee ----------
        blackBox("BB-10 Remove existing employee", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            boolean ok = s.removeEmployee("E001");
            check(ok && s.getEmployeeCount() == 0);
        });

        blackBox("BB-11 Remove non-existing employee", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            check(!s.removeEmployee("E999"));
        });

        // ---------- Payment processing ----------
        blackBox("BB-12 Non-management net pay = 2550.00", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            double net = s.processPayment("E001", 160, 0);
            check(Math.abs(net - 2550.00) < 0.01);
        });

        blackBox("BB-13 Management net pay = 4950.00", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Management", 6000);
            double net = s.processPayment("E001", 160, 0);
            check(Math.abs(net - 4950.00) < 0.01);
        });

        blackBox("BB-14 Reject negative hours", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            boolean thrown = false;
            try { s.processPayment("E001", -10, 0); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-15 Reject hours > 168", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            boolean thrown = false;
            try { s.processPayment("E001", 200, 0); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-16 Reject overtime > 40", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            boolean thrown = false;
            try { s.processPayment("E001", 160, 50); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        blackBox("BB-17 Reject payment for unknown ID", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.processPayment("E999", 160, 0); }
            catch (NoSuchElementException e) { thrown = true; }
            check(thrown);
        });

        // ---------- Reports ----------
        blackBox("BB-18 Payroll report populated", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            String r = s.generatePayrollReport();
            check(r.contains("Total Payroll") && r.contains("Average Net Pay")
                    && r.contains("Top Earner"));
        });

        blackBox("BB-19 Payroll report empty", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            check("No employees to report.".equals(s.generatePayrollReport()));
        });

        blackBox("BB-20 Employee count after adds", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E001", "Alice Uwase", "Engineering", 3000);
            s.addEmployee("E002", "Bob Kamanzi", "HR", 2500);
            check(s.getEmployeeCount() == 2);
        });

        System.out.println("\n===== WHITE-BOX TESTS =====\n");

        // ---------- addEmployee branches ----------
        whiteBox("WB-01 Branch: empty ID throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("", "Alice", "Engineering", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-02 Branch: short ID throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("A", "Alice", "Engineering", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-03 Branch: ID with space throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E 1", "Alice", "Engineering", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-04 Branch: empty name throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "", "Engineering", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-05 Branch: short name throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "A", "Engineering", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-06 Branch: name with digits throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "Alice1", "Engineering", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-07 Branch: empty department throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "Alice", "", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-08 Branch: invalid department throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "Alice", "Marketing", 1000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-09 Branch: zero salary throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "Alice", "Engineering", 0); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-10 Branch: negative salary throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "Alice", "Engineering", -1); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-11 Branch: salary > 1,000,000 throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.addEmployee("E1", "Alice", "Engineering", 2000000); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-12 Branch: duplicate ID returns false", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 1000);
            check(!s.addEmployee("E1", "Bob", "HR", 1000));
        });

        // ---------- removeEmployee branches ----------
        whiteBox("WB-13 Branch: remove missing returns false", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            check(!s.removeEmployee("X"));
        });

        // ---------- processPayment branches ----------
        whiteBox("WB-14 Branch: unknown ID throws NoSuchElementException", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            boolean thrown = false;
            try { s.processPayment("X", 10, 0); }
            catch (NoSuchElementException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-15 Branch: hours < 0 throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 3000);
            boolean thrown = false;
            try { s.processPayment("E1", -1, 0); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-16 Branch: OT < 0 throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 3000);
            boolean thrown = false;
            try { s.processPayment("E1", 10, -5); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-17 Branch: hours > 168 throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 3000);
            boolean thrown = false;
            try { s.processPayment("E1", 169, 0); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        whiteBox("WB-18 Branch: OT > 40 throws", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 3000);
            boolean thrown = false;
            try { s.processPayment("E1", 160, 41); }
            catch (IllegalArgumentException e) { thrown = true; }
            check(thrown);
        });

        // ---------- Boundary tax bracket ----------
        whiteBox("WB-19 Boundary: gross = 5000 -> 25% tax", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 5000);
            double net = s.processPayment("E1", 160, 0);
            check(Math.abs(net - 3750.00) < 0.01);
        });

        whiteBox("WB-20 Boundary: gross just below 5000 -> 15% tax", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 4999);
            double net = s.processPayment("E1", 160, 0);
            check(Math.abs(net - 4249.15) < 0.01);
        });

        // ---------- Bonus & overtime paths ----------
        whiteBox("WB-21 Branch: Management bonus applied", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Management", 3000);
            double net = s.processPayment("E1", 160, 0);
            check(Math.abs(net - 2805.00) < 0.01);
        });

        whiteBox("WB-22 Branch: overtime 1.5x applied", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            s.addEmployee("E1", "Alice", "Engineering", 1600);
            double net = s.processPayment("E1", 160, 10);
            check(Math.abs(net - 1487.50) < 0.01);
        });

        // ---------- Report empty branch ----------
        whiteBox("WB-23 Branch: empty report message", () -> {
            EmployeeManagementSystem s = new EmployeeManagementSystem();
            check("No employees to report.".equals(s.generatePayrollReport()));
        });

        System.out.println("\n===== SUMMARY =====");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
    }

    interface TestBlock { void run() throws Exception; }

    static void blackBox(String name, TestBlock block) { run(name, block); }
    static void whiteBox(String name, TestBlock block) { run(name, block); }

    static void run(String name, TestBlock block) {
        try {
            block.run();
            System.out.println("[PASS] " + name);
            passed++;
        } catch (Throwable t) {
            System.out.println("[FAIL] " + name + " -> " + t);
            failed++;
        }
    }

    static void check(boolean condition) {
        if (!condition) throw new AssertionError("Condition failed");
    }
}