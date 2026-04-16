drop procedure if exists AddEmployee;
drop procedure if exists AddAddress;
drop procedure if exists AddCity;
drop procedure if exists AddState;
drop procedure if exists AddEmployeeFull;

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

DELIMITER ;