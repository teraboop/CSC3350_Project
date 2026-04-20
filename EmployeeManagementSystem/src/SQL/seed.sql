insert into states (state_abbr)
	values
		('AL'),
		('AK'),
		('AZ'),
		('AR'),
		('CA'),
		('CO'),
		('CT'),
		('DE'),
		('FL'),
		('GA'),
		('HI'),
		('ID'),
		('IL'),
		('IN'),
		('IA'),
		('KS'),
		('KY'),
		('LA'),
		('ME'),
		('MD'),
		('MA'),
		('MI'),
		('MN'),
		('MS'),
		('MO'),
		('MT'),
		('NE'),
		('NV'),
		('NH'),
		('NJ'),
		('NM'),
		('NY'),
		('NC'),
		('ND'),
		('OH'),
		('OK'),
		('OR'),
		('PA'),
		('RI'),
		('SC'),
		('SD'),
		('TN'),
		('TX'),
		('UT'),
		('VT'),
		('VA'),
		('WA'),
		('WV'),
		('WI'),
		('WY');	

INSERT INTO cities (city_name, state_id)
	VALUES
		('Saint Paul', (SELECT state_id FROM states WHERE state_abbr = 'MN')),
		('Coolsville', (SELECT state_id FROM states WHERE state_abbr = 'OH')),
		('Acme Acres', (SELECT state_id FROM states WHERE state_abbr = 'AL')),
		('Atlanta', (SELECT state_id FROM states WHERE state_abbr = 'GA')),
		('New York', (SELECT state_id FROM states WHERE state_abbr = 'NY'));

INSERT INTO addresses (street, city_id, zip, country)
	VALUES
		('Peanut Street', (SELECT city_id FROM cities WHERE city_name = 'Saint Paul'), '55101', 'USA'),
		('Cool Street', (SELECT city_id FROM cities WHERE city_name = 'Coolsville'), '45723', 'USA'),
		('Toon Street', (SELECT city_id FROM cities WHERE city_name = 'Acme Acres'), '12345', 'USA'),
		('200 17th Street NW', (SELECT city_id FROM cities WHERE city_name = 'Atlanta'), '30363', 'USA'),
		('45 West 57th Street', (SELECT city_id FROM cities WHERE city_name = 'New York'), '00034', 'USA');
		
		
INSERT INTO employees (first_name, last_name, email, hire_date, salary, ssn, address_id, dob, phone, emergency_contact_name, emergency_contact_phone)
	VALUES 
		('Snoopy', 'Beagle', 		'Snoopy@example.com', 	'2022-08-01', 45000.00, '111-11-1111', (SELECT address_id FROM addresses WHERE street = 'Peanut Street'), '1950-10-02', '111-111-1111', 'Charlie Brown', '111-111-1112'),
		('Charlie', 'Brown', 		'Charlie@example.com', 	'2022-07-01', 48000.00, '111-22-1111', (SELECT address_id FROM addresses WHERE street = 'Peanut Street'), '1950-10-02', '111-111-1112', 'Snoopy Beagle', '111-111-1111'),
		('Lucy', 'Doctor', 		'Lucy@example.com', 	'2022-07-03', 55000.00, '111-33-1111', (SELECT address_id FROM addresses WHERE street = 'Peanut Street'), '1950-10-02', '111-111-1113', 'Snoopy Beagle', '111-111-1111'),
		('Pepermint', 'Patti', 	'Peppermint@example.com', '2022-08-02', 98000.00, '111-44-1111', (SELECT address_id FROM addresses WHERE street = 'Peanut Street'), '1950-10-02', '111-111-1114', 'Snoopy Beagle', '111-111-1111'),
		('Linus', 'Blanket', 		'Linus@example.com', 	'2022-09-01', 43000.00, '111-55-1111', (SELECT address_id FROM addresses WHERE street = 'Peanut Street'), '1950-10-02', '111-111-1115', 'Snoopy Beagle', '111-111-1111'),
		('PigPin', 'Dusty', 		'PigPin@example.com', 	'2022-10-01', 33000.00, '111-66-1111', (SELECT address_id FROM addresses WHERE street = 'Peanut Street'), '1950-10-02', '111-111-1116', 'Snoopy Beagle', '111-111-1111'),
		('Scooby', 'Doo', 			'Scooby@example.com', 	'1973-07-03', 78000.00, '111-77-1111', (SELECT address_id FROM addresses WHERE street = 'Cool Street'), '1950-10-02', '111-111-1117', 'Shaggy Rodgers', '111-111-1118'),
		('Shaggy', 'Rodgers', 		'Shaggy@example.com', 	'1973-07-11', 77000.00, '111-88-1111', (SELECT address_id FROM addresses WHERE street = 'Cool Street'), '1950-10-02', '111-111-1118', 'Scooby Doo', '111-111-1117'),
		('Velma', 'Dinkley', 		'Velma@example.com', 	'1973-07-21', 82000.00, '111-99-1111', (SELECT address_id FROM addresses WHERE street = 'Cool Street'), '1969-09-13', '111-111-1119', 'Scooby Doo', '111-111-1117'),
		('Daphne', 'Blake', 		'Daphne@example.com', 	'1973-07-30', 59000.00, '111-00-1111', (SELECT address_id FROM addresses WHERE street = 'Cool Street'), '1950-01-01', '111-111-1120', 'Scooby Doo', '111-111-1117'),
		('Bugs', 'Bunny', 		'Bugs@example.com', 	'1934-07-01', 18000.00, '222-11-1111', (SELECT address_id FROM addresses WHERE street = 'Toon Street'), '1969-09-13', '111-111-1121', 'Daffy Duck', '111-111-1122'),
		('Daffy', 'Duck', 		'Daffy@example.com', 	'1935-04-01', 16000.00, '333-11-1111', (SELECT address_id FROM addresses WHERE street = 'Toon Street'), '1950-01-01', '111-111-1122', 'Bugs Bunny', '111-111-1121'),
		('Porky', 'Pig', 		'Porky@example.com', 	'1935-08-12', 16550.00, '444-11-1111', (SELECT address_id FROM addresses WHERE street = 'Toon Street'), '1950-01-01', '111-111-1123', 'Bugs Bunny', '111-111-1121'),
		('Elmer', 'Fudd', 		'Elmer@example.com', 	'1934-08-01', 15500.00, '555-11-1111', (SELECT address_id FROM addresses WHERE street = 'Toon Street'), '1950-01-01', '111-111-1124', 'Bugs Bunny', '111-111-1121'),
		('Marvin', 'Martian', 	'Marvin@example.com', 	'1937-05-01', 28000.00, '777-11-1111', (SELECT address_id FROM addresses WHERE street = 'Toon Street'), '1950-01-01', '111-111-1125', 'Bugs Bunny', '111-111-1121');


INSERT INTO job_titles (job_title)
	VALUES 
		('software manager'),
		('software architect'),
		('software engineer'),
		('software developer'),
		('marketing manager'),
		('marketing associate'),
		('marketing assistant'),
		('Chief Exec. Officer'),
		('Chief Finn. Officer'),
		('Chief Info. Officer');


/* EMPLOYEE id=1 */
INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2026-01-31', 
		1, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145,
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031
	FROM employees
	WHERE emp_id=1;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		2, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=2;


/* EMPLOYEE id=2 */
INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2026-01-31', 
		2, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145,
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031
	FROM employees
	WHERE emp_id=2;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		2, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=2;


/* EMPLOYEE id=3 */
INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2026-01-31', 
		3, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145,
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031
	FROM employees
	WHERE emp_id=3;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		3, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=3;


/* EMPLOYEE id=4 */
INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2026-01-31', 
		4, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145,
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031
	FROM employees
	WHERE emp_id=4;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		5, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=5;


/* EMPLOYEE id=5 */
INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2026-01-31', 
		5, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145,
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031
	FROM employees
	WHERE emp_id=5;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		5, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=5;

/* EMPLOYEE id=6 */
INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2026-01-31', 
		6, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145,
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031
	FROM employees
	WHERE emp_id=6;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		6, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=6;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		7, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=7;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		8, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=8;


INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		9, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=9;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		10, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=10;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		11, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=11;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		12, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=12;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		13, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=13;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		14, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=14;

INSERT INTO payroll (pay_date, emp_id, earnings, fed_tax, fed_med, fed_ss, state_tax, retirement_401k, health_care)
	SELECT 
		'2025-12-31', 
		15, 
		salary/52.0, 
		(salary/52.0)*0.32, 
		(salary/52.0)*0.0145, 
		(salary/52.0)*0.062,
		(salary/52.0)*0.12,
		(salary/52.0)*0.004,
		(salary/52.0)*0.031 
	FROM employees
	WHERE emp_id=15;

/* You can copy the above two INSERT statements for pay_id=13 
     and emp_id=7, then pay_id=14, and emp_id=7, pay_id=15 ... etc.
*/

INSERT INTO divisions (div_name, address_id) 
	VALUES
		('Technology Engineering', (SELECT address_id FROM addresses WHERE street = '200 17th Street NW')),
		('Marketing', (SELECT address_id FROM addresses WHERE street = '200 17th Street NW')),
		('Human Resources', (SELECT address_id FROM addresses WHERE street = '45 West 57th Street')),
		('Headquarters', (SELECT address_id FROM addresses WHERE street = '45 West 57th Street'));

INSERT INTO employee_division (emp_id, div_id)
	VALUES
		(1, 1),
		(2, 3),
		(3, 2),
		(4, 4),
		(5,1),
		(6, 2),
		(7, 3),
		(8, 4),
		(9, 2),
		(10, 3),
		(11, 4),
		(12, 1),
		(13, 2),
		(14, 3),
		(15, 4);
			
INSERT INTO employee_job_titles (emp_id, job_title_id)
	VALUES
		(1, 1),
		(2, 2),
		(3, 3),
		(4, 4),
		(5, 3),
		(6, 1),
		(7, 5),
		(8, 2),
		(9, 8),
		(10, 10),
		(11, 4),
		(12, 1),
		(13, 2),
		(14, 7),
		(15, 6);

/******************************************************************************/

INSERT INTO credentials (emp_id, username, password_hash, password_salt, classification)
	VALUES
	  ((SELECT emp_id FROM employees WHERE email = 'Snoopy@example.com'),
	   'snoopy', TO_BASE64(UNHEX(SHA2('Password1!', 256))), '', 'Employee'),
	
	  ((SELECT emp_id FROM employees WHERE email = 'Charlie@example.com'),
	   'charlie', TO_BASE64(UNHEX(SHA2('Password1!', 256))), '', 'Employee'),
	
	  -- Add an Admin user for testing HR functionality
	  ((SELECT emp_id FROM employees WHERE email = 'Lucy@example.com'),
	   'admin', TO_BASE64(UNHEX(SHA2('AdminPass1!', 256))), '', 'Admin');