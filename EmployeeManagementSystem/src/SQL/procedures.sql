drop procedure if exists AddEmployee;
drop procedure if exists AddAddress;
drop procedure if exists AddCity;
drop procedure if exists AddState;
drop procedure if exists AddEmployeeFull;
DROP PROCEDURE IF EXISTS AddCredentials;
DROP PROCEDURE IF EXISTS AddPayroll;
DROP PROCEDURE IF EXISTS AddJobTitle;
DROP PROCEDURE IF EXISTS AddDivision;
DROP PROCEDURE IF EXISTS AddEmployeeDivision;
DROP PROCEDURE IF EXISTS AddEmployeeJobTitle;

DELIMITER //

-- =====================================================
-- PROCEDURE: AddEmployee
-- Purpose: Inserts an employee into the employees table
-- =====================================================

create procedure AddEmployee(
	in v_first_name varchar(50),
	in v_last_name varchar(50),
	in v_email varchar(100),
	in v_hire_date date,
	in v_salary DECIMAL(10, 2),
	in v_ssn varchar(12),
	in v_address_id int,
	OUT o_emp_id int
)

BEGIN
	
	insert into employees(
		first_name,
		last_name,
		email,
		hire_date,
		salary,
		ssn,
		address_id
	)
	
	values(
		v_first_name,
		v_last_name,
		v_email,
		v_hire_date,
		v_salary,
		v_ssn,
		v_address_id
	);
	
	SET o_emp_id = LAST_INSERT_ID();
	
end //

-- =====================================================
-- PROCEDURE: AddAddress
-- Purpose: Inserts an address into the addresses table
-- =====================================================

create procedure AddAddress(
	in v_street varchar(50),
	in v_city_id int,
	in v_state_id int,
	in v_zip varchar(10),
	in v_dob date,
	in v_phone varchar(15),
	in v_emergency_contact_name varchar(100),
	in v_emergency_contact_phone varchar(15),
	OUT o_address_id INT
)

BEGIN
	
	insert into addresses(
		street,
		city_id,
		state_id,
		zip,
		dob,
		phone,
		emergency_contact_name,
		emergency_contact_phone
	)
	
	values(	
		v_street,
		v_city_id,
		v_state_id,
		v_zip,
		v_dob,
		v_phone,
		v_emergency_contact_name,
		v_emergency_contact_phone
	);
	
	SET o_address_id = LAST_INSERT_ID();
	
end //

-- =====================================================
-- PROCEDURE: AddCity
-- Purpose: Inserts a city into the cities table
-- =====================================================

create procedure AddCity(
	in v_city_name varchar(100),
	IN v_state_id int,
	OUT o_city_id int
)

BEGIN
	
	insert into cities(city_name, state_id)
	
	values(v_city_name, v_state_id);

	SET o_city_id = LAST_INSERT_ID();
	
end //

-- =====================================================
-- PROCEDURE: AddState
-- Purpose: Inserts a state into the states table
-- =====================================================

create procedure AddState(
	in v_state_abbr varchar(2),
	OUT o_state_id INT
)

BEGIN
	
	insert into states(state_abbr)
	
	values(v_state_abbr);
	
	SET o_state_id = LAST_INSERT_ID();

end //

-- =====================================================
-- PROCEDURE: AddEmployeeFull
-- Purpose: Inserts a full employee into related tables
-- =====================================================

create procedure AddEmployeeFull(
	in v_first_name varchar(50),
	in v_last_name varchar(50),
	in v_email varchar(100),
	in v_hire_date date,
	in v_salary DECIMAL(10, 2),
	in v_ssn varchar(12),
	
	in v_street varchar(50),
	in v_city_name varchar(100),
	IN v_state_abbr VARCHAR(2),
	in v_zip varchar(10),
	in v_dob date,
	in v_phone varchar(15),
	in v_emergency_contact_name varchar(100),
	in v_emergency_contact_phone varchar(15)
)

BEGIN
	
	DECLARE v_state_id INT;
	DECLARE v_city_id INT;
	DECLARE v_address_id INT;
	DECLARE v_emp_id INT;

	-- 1. Insert state
	CALL AddState(v_state_abbr, v_state_id);
	
	-- 2. Insert city
	CALL AddCity(v_city_name, v_state_id, v_city_id);
	
	-- 3. Insert address
	CALL AddAddress(
		v_street,
		v_city_id,
		v_state_id,
		v_zip,
		v_dob,
		v_phone,
		v_emergency_contact_name,
		v_emergency_contact_phone,
		v_address_id
	);
	
	-- 4. Insert employee
	CALL AddEmployee(
		v_first_name,
		v_last_name,
		v_email,
		v_hire_date,
		v_salary,
		v_ssn,
		v_address_id,
		v_emp_id
	);
	
end //

-- =====================================================
-- PROCEDURE: AddCredentials
-- Purpose: Inserts credentials into the credentials table
-- =====================================================


CREATE PROCEDURE AddCredentials(
	IN v_emp_id INT,
	IN v_username VARCHAR(65),
	IN v_password_hash VARCHAR(255),
	IN v_password_salt VARCHAR(255),
	IN v_classification ENUM('Employee', 'Admin'),
	IN v_last_login DATETIME,
	IN v_locked_until DATETIME
)

BEGIN
	
	INSERT INTO credentials(
		emp_id,
		username,
		password_hash,
		password_salt,
		classification,
		last_login,
		locked_until
	)
	
	VALUES(
		v_emp_id,
		v_username,
		v_password_hash,
		v_password_salt,
		v_classification,
		NOW(),
		v_locked_until
	);
	
END //

-- =====================================================
-- PROCEDURE: AddPayroll
-- Purpose: Inserts payroll info into the payroll table
-- =====================================================


CREATE PROCEDURE AddPayroll(
	IN v_pay_date DATE,
	IN v_earnings DECIMAL(8, 2),
	IN v_fed_tax DECIMAL(7, 2),
	IN v_fed_med DECIMAL(7, 2),
	IN v_fed_ss DECIMAL(7, 2),
	IN v_state_tax DECIMAL(7, 2),
	IN v_retirement_401k DECIMAL(7, 2),
	IN v_health_care DECIMAL(7, 2),
	IN v_emp_id INT
)
	
BEGIN
		
	INSERT INTO payroll(
		pay_date,
		earnings,
		fed_tax,
		fed_med,
		fed_ss,
		state_tax,
		retirement_401k,
		health_care,
		emp_id
		)
		
	VALUES(
		v_pay_date,
		v_earnings,
		v_fed_tax,
		v_fed_med,
		v_fed_ss,
		v_state_tax,
		v_retirement_401k,
		v_health_care,
		v_emp_id
	);
		
END //

-- =====================================================
-- PROCEDURE: AddJobTitle
-- Purpose: Inserts a job title into the job titles table
-- =====================================================

CREATE PROCEDURE AddJobTitle(
	IN v_job_title VARCHAR(125)
)

BEGIN
	
	INSERT INTO job_titles(job_title)
	
	VALUES(v_job_title);
	
END //

-- =====================================================
-- PROCEDURE: AddDivision
-- Purpose: Inserts a division into the divisions table
-- =====================================================

CREATE PROCEDURE AddDivision(
	IN v_name varchar(100),
	IN v_city varchar(50),
	IN v_address_line1 varchar(50),
	IN v_address_line2 varchar(50),
	IN v_state varchar(50),
	IN v_country varchar(50),
	IN v_postal_code varchar(15)
)

BEGIN
	
	INSERT INTO division(
		name,
		city,
		address_line1,
		address_line2,
		state,
		country,
		postal_code
	)
	
	VALUES(
		v_name,
		v_city,
		v_address_line1,
		v_address_line2,
		v_state,
		v_country,
		v_postal_code
	);

END //
	
-- =====================================================
-- PROCEDURE: AddEmployeeDivision
-- Purpose: Assigns an employee to a division
-- =====================================================

CREATE PROCEDURE AddEmployeeDivision(
	IN v_emp_id INT,
	IN v_div_id INT
)

BEGIN
	
	INSERT INTO employee_division(emp_id, div_id)
	
	VALUES(v_emp_id, v_div_id);
	
END //

-- =====================================================
-- PROCEDURE: AddEmployeeJobTitle
-- Purpose: Assigns an job title to an employee
-- =====================================================

CREATE PROCEDURE AddEmployeeJobTitle(
	IN v_emp_id INT,
	IN v_job_title_id INT
)

BEGIN
	
	INSERT INTO employee_job_titles(emp_id, job_title_id)
	
	VALUES(v_emp_id, v_job_title_id);
	
END //

DELIMITER ;