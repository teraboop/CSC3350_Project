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

INSERT INTO cities (city_name)
	VALUES
		('Saint Paul'),
		('Coolsville'),
		('Acme Acres');

INSERT INTO addresses (street, city_id, state_id, zip, dob, phone, emergency_contact_name, emergency_contact_phone)
	VALUES
		('Peanut Street', 1, 23, '55101', '1950-10-02', '111-111-1111', 'Charlie Brown', '111-111-1112'),
		('Peanut Street', 1, 23, '55101', '1950-10-02', '111-111-1112', 'Snoopy Beagle', '111-111-1111'),
		('Peanut Street', 1, 23, '55101', '1950-10-02', '111-111-1113', 'Snoopy Beagle', '111-111-1111'),
		('Peanut Street', 1, 23, '55101', '1950-10-02', '111-111-1114', 'Snoopy Beagle', '111-111-1111'),
		('Peanut Street', 1, 23, '55101', '1950-10-02', '111-111-1115', 'Snoopy Beagle', '111-111-1111'),
		('Peanut Street', 1, 23, '55101', '1950-10-02', '111-111-1116', 'Snoopy Beagle', '111-111-1111'),
		('Cool Street', 2, 35, '45723', '1969-09-13', '111-111-1117', 'Shaggy Rodgers', '111-111-1118'),
		('Cool Street', 2, 35, '45723', '1969-09-13', '111-111-1118', 'Scooby Doo', '111-111-1117'),
		('Cool Street', 2, 35, '45723', '1969-09-13', '111-111-1119', 'Scooby Doo', '111-111-1117'),
		('Cool Street', 2, 35, '45723', '1969-09-13', '111-111-1120', 'Scooby Doo', '111-111-1117'),
		('Toon Street', 3, 1, '12345', '1950-01-01', '111-111-1121', 'Daffy Duck', '111-111-1118'),
		('Toon Street', 3, 1, '12345', '1950-01-01', '111-111-1121', 'Bugs Bunny', '111-111-1119'),
		('Toon Street', 3, 1, '12345', '1950-01-01', '111-111-1121', 'Bugs Bunny', '111-111-1119'),
		('Toon Street', 3, 1, '12345', '1950-01-01', '111-111-1121', 'Bugs Bunny', '111-111-1119'),
		('Toon Street', 3, 1, '12345', '1950-01-01', '111-111-1121', 'Bugs Bunny', '111-111-1119');
		
		
INSERT INTO employees (first_name, last_name, email, hire_date, salary, ssn, address_id)
	VALUES 
		('Snoopy', 'Beagle', 		'Snoopy@example.com', 	'2022-08-01', 45000.00, '111-11-1111', 1),
		('Charlie', 'Brown', 		'Charlie@example.com', 	'2022-07-01', 48000.00, '111-22-1111', 1),
		('Lucy', 'Doctor', 		'Lucy@example.com', 	'2022-07-03', 55000.00, '111-33-1111', 2),
		('Pepermint', 'Patti', 	'Peppermint@example.com', '2022-08-02', 98000.00, '111-44-1111', 3),
		('Linus', 'Blanket', 		'Linus@example.com', 	'2022-09-01', 43000.00, '111-55-1111', 1),
		('PigPin', 'Dusty', 		'PigPin@example.com', 	'2022-10-01', 33000.00, '111-66-1111', 3),
		('Scooby', 'Doo', 			'Scooby@example.com', 	'1973-07-03', 78000.00, '111-77-1111', 4),
		('Shaggy', 'Rodgers', 		'Shaggy@example.com', 	'1973-07-11', 77000.00, '111-88-1111', 5),
		('Velma', 'Dinkley', 		'Velma@example.com', 	'1973-07-21', 82000.00, '111-99-1111', 6),
		('Daphne', 'Blake', 		'Daphne@example.com', 	'1973-07-30', 59000.00, '111-00-1111', 4),
		('Bugs', 'Bunny', 		'Bugs@example.com', 	'1934-07-01', 18000.00, '222-11-1111', 5),
		('Daffy', 'Duck', 		'Daffy@example.com', 	'1935-04-01', 16000.00, '333-11-1111', 2),
		('Porky', 'Pig', 		'Porky@example.com', 	'1935-08-12', 16550.00, '444-11-1111', 7),
		('Elmer', 'Fudd', 		'Elmer@example.com', 	'1934-08-01', 15500.00, '555-11-1111', 8),
		('Marvin', 'Martian', 	'Marvin@example.com', 	'1937-05-01', 28000.00, '777-11-1111', 9);


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


/* You can copy the above two INSERT statements for pay_id=13 
     and emp_id=7, then pay_id=14, and emp_id=7, pay_id=15 ... etc.
*/

INSERT INTO division (name, city, address_line1, address_line2, state, country, postal_code) 
	VALUES
		('Technology Engineering', 'Atlanta', '200 17th Street NW', '', 'GA', 'USA', '30363'),
		('Marketing', 'Atlanta', '200 17th Street NW', '', 'GA', 'USA', '30363'),
		('Human Resources','New York', '45 West 57th Street', '', 'NY', 'USA', '00034'),
		('HQ','New York', '45 West 57th Street', '', 'NY', 'USA', '00034');

INSERT INTO employee_division (emp_id, div_id)
	VALUES
		(1, 1),
		(2, 3),
		(3, 2),
		(7, 4),
		(10,1);
			
INSERT INTO employee_job_titles (emp_id, job_title_id)
	VALUES
		(1, 1),
		(2, 9),
		(3, 10),
		(4, 4),
		(5, 3),
		(6, 1),
		(7, 5),
		(8, 2),
		(9, 8),
		(10, 2),
		(11, 4),
		(12, 1),
		(13, 2),
		(14, 7),
		(15, 6);