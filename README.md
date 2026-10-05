# Employee Management System

A Java console application that manages employees, processes payments, and generates payroll reports — with comprehensive input validation and 43 passing unit tests.

**Author:** MUYOBOKE Emmanuel (Student ID: 26227)

## Functional Requirements

| ID  | Requirement                                                                                                                   |
| --- | ----------------------------------------------------------------------------------------------------------------------------- |
| FR1 | Add a new employee with ID, name, department, and base salary                                                                 |
| FR2 | Reject invalid registration (empty fields, non-positive salary, duplicate ID, invalid department)                             |
| FR3 | Remove an employee by ID; report when ID does not exist                                                                       |
| FR4 | Compute net payment from base salary, overtime (1.5x), 10% Management bonus, and tax bracket (25% if gross >= 5000, else 15%) |
| FR5 | Reject payment when employee ID is unknown, hours are negative, or weekly limits exceeded                                     |
| FR6 | Generate payroll report with each employee's net pay, total, average, and top earner                                          |
| FR7 | Report current number of registered employees                                                                                 |

## Business Rules

- **Overtime:** 1.5x hourly rate (base salary / 160)
- **Management bonus:** 10% on gross pay
- **Tax brackets:** 25% if gross >= 5000, 15% otherwise
- **Allowed departments:** Engineering, Management, HR, Finance, IT

## Input Validations

- ID: 2-10 characters, alphanumeric only
- Name: at least 2 characters, letters and spaces only
- Department: must be from the allowed list
- Salary: greater than 0 and at most 1,000,000
- Weekly hours: at most 168
- Overtime hours: at most 40
- Total hours: at most 200

## Test Suite

- **20 Black-Box Tests** (BB-01 to BB-20)
- **23 White-Box Tests** (WB-01 to WB-23)
- **Result: 43 / 43 passed**

## How to Run

### Compile

```bash
javac EmployeeManagementSystem.java EmployeeManagementSystemTest.java
```
