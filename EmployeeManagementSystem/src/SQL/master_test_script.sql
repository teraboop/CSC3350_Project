-- =====================================================
-- MASTER TEST SCRIPT
-- Purpose: Test all stored procedures end-to-end
-- =====================================================

-- Clean session variables
SET @state_id = NULL;
SET @city_id = NULL;
SET @address_id = NULL;
SET @emp_id = NULL;
SET @div_id = NULL;
SET @job_title_id = NULL;

-- =====================================================
-- 1. TEST STATE
-- =====================================================
CALL AddState('GA', @state_id);
SELECT @state_id AS state_id;

-- =====================================================
-- 2. TEST CITY
-- =====================================================
CALL AddCity('Testville', @state_id, @city_id);
SELECT @city_id AS city_id;

-- =====================================================
-- 3. TEST ADDRESS
-- =====================================================
CALL AddAddress(
    '123 Test Street',
    @city_id,
    '30301',
    'USA',
    @address_id
);
SELECT @address_id AS address_id;

-- =====================================================
-- 4. TEST EMPLOYEE
-- =====================================================
CALL AddEmployee(
    'Test',
    'User',
    'test.user@email.com',
    '2026-01-01',
    65000.00,
    '123-45-6789',
    @address_id,
    '1995-05-05',
    '404-111-2222',
    'Emergency Contact',
    '404-999-8888',
    @emp_id
);

SELECT @emp_id AS emp_id;

-- =====================================================
-- 5. TEST EMPLOYEE FULL (optional full-chain test)
-- =====================================================
CALL AddEmployeeFull(
    'Full',
    'Pipeline',
    'full.pipeline@email.com',
    '2026-01-01',
    75000.00,
    '999-88-7777',
    '2000-01-01',
    '123-345-5412',
    'Joey Chestnuts',
    '123-645-8732',
    '500 Full St',
    'Atlanta',
    'GA',
    '30303',
    'USA',
    @emp_id
);

-- =====================================================
-- 6. TEST JOB TITLE
-- =====================================================
CALL AddJobTitle('Test Engineer');

-- get job_title_id
SELECT job_title_id
INTO @job_title_id
FROM job_titles
WHERE job_title = 'Test Engineer'
LIMIT 1;

SELECT @job_title_id AS job_title_id;

-- =====================================================
-- 7. TEST DIVISION
-- =====================================================
CALL AddDivision('Test Division', @address_id);

-- get div_id
SELECT div_id
INTO @div_id
FROM divisions
WHERE div_name = 'Test Division'
LIMIT 1;

SELECT @div_id AS div_id;

-- =====================================================
-- 8. TEST EMPLOYEE ↔ DIVISION
-- =====================================================
CALL AddEmployeeDivision(@emp_id, @div_id);

SELECT * 
FROM employee_division
WHERE emp_id = @emp_id;

-- =====================================================
-- 9. TEST EMPLOYEE ↔ JOB TITLE
-- =====================================================
CALL AddEmployeeJobTitle(@emp_id, @job_title_id);

SELECT *
FROM employee_job_titles
WHERE emp_id = @emp_id;

-- =====================================================
-- 10. TEST PAYROLL
-- =====================================================
CALL AddPayroll(
    '2026-01-31',
    1250.00,
    400.00,
    18.13,
    77.50,
    150.00,
    5.00,
    40.00,
    @emp_id
);

SELECT *
FROM payroll
WHERE emp_id = @emp_id;

-- =====================================================
-- 11. TEST CREDENTIALS
-- =====================================================
CALL AddCredentials(
    @emp_id,
    'testuser',
    'hashed_pw_example',
    'salt_example',
    'Employee',
    NOW(),
    NULL
);

SELECT *
FROM credentials
WHERE emp_id = @emp_id;

-- =====================================================
-- FINAL VERIFICATION: EMPLOYEE SNAPSHOT
-- =====================================================
SELECT 
    e.emp_id,
    e.first_name,
    e.last_name,
    e.email,
    a.street,
    c.city_name,
    s.state_abbr
FROM employees e
JOIN addresses a ON e.address_id = a.address_id
JOIN cities c ON a.city_id = c.city_id
JOIN states s ON c.state_id = s.state_id
WHERE e.emp_id = @emp_id;