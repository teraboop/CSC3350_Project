DROP DATABASE IF EXISTS project_employee_data;
CREATE DATABASE project_employee_data;
USE project_employee_data;


/***********************************************************************/ 

DROP TABLE IF EXISTS states;
CREATE TABLE states (
  state_id INT auto_increment PRIMARY KEY,
  state_abbr varchar(2) NOT null unique
);

/***********************************************************************/ 

DROP TABLE IF EXISTS cities;
CREATE TABLE cities (
  city_id INT AUTO_INCREMENT PRIMARY KEY,
  city_name varchar(25) NOT NULL,
  state_id INT NOT NULL,
  FOREIGN KEY (state_id) REFERENCES states(state_id),
  UNIQUE (city_name, state_id)
);

/***********************************************************************/ 

DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
  address_id INT AUTO_INCREMENT PRIMARY KEY,
  street varchar(50) NOT NULL,
  city_id INT NOT NULL,
  zip VARCHAR(10) NOT NULL,
  country varchar(50) NOT NULL,
  FOREIGN KEY (city_id) REFERENCES cities(city_id)
);

/***********************************************************************/ 

DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
  emp_id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(65) NOT NULL,
  last_name VARCHAR(65) NOT NULL,
  email VARCHAR(65) NOT NULL UNIQUE,
  hire_date DATE,
  salary DECIMAL(10,2) NOT NULL,
  ssn VARCHAR(12) UNIQUE,
  address_id INT NOT NULL,
  dob DATE NOT NULL,
  phone varchar(15) NOT NULL,
  emergency_contact_name varchar(100) NOT NULL,
  emergency_contact_phone varchar(15) NOT NULL,
  FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

/***********************************************************************/

DROP TABLE IF EXISTS payroll;
CREATE TABLE payroll (
  pay_id INT auto_increment primary KEY,
  pay_date DATE NOT NULL,
  earnings DECIMAL(8,2),
  fed_tax DECIMAL(7,2),
  fed_med DECIMAL(7,2),
  fed_ss DECIMAL(7,2),
  state_tax DECIMAL(7,2),
  retirement_401k DECIMAL(7,2),
  health_care DECIMAL(7,2),
  emp_id INT NOT NULL,
  FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);


/***********************************************************************/ 

DROP TABLE IF EXISTS job_titles;
CREATE TABLE job_titles (
  job_title_id INT auto_increment PRIMARY KEY,
  job_title VARCHAR(125) NOT NULL UNIQUE
);

/***********************************************************************/ 

DROP TABLE IF EXISTS employee_job_titles ;
CREATE TABLE employee_job_titles (
  emp_id INT NOT NULL,
  job_title_id INT NOT NULL,
  PRIMARY KEY (emp_id, job_title_id),
  FOREIGN KEY (emp_id) REFERENCES employees(emp_id),
  FOREIGN KEY (job_title_id) REFERENCES job_titles(job_title_id)
);

/***********************************************************************/

DROP TABLE IF EXISTS divisions;
CREATE TABLE divisions (
  div_id int auto_increment PRIMARY KEY,
  div_name varchar(100) DEFAULT NULL,
  address_id INT not NULL,
  foreign key (address_id) references addresses(address_id)
) COMMENT='company divisions';

/***********************************************************************/ 

DROP TABLE IF EXISTS employee_division;
CREATE TABLE employee_division (
  emp_id int NOT NULL,
  div_id int NOT NULL,
  PRIMARY KEY (emp_id, div_id),
  FOREIGN KEY (emp_id) REFERENCES employees(emp_id),
  FOREIGN KEY (div_id) REFERENCES divisions(div_id)
) COMMENT='links employee to a division';

/***********************************************************************/
/* Edward: I want to add this table to track employee credential/authorization information. */

DROP TABLE IF EXISTS credentials;
CREATE TABLE credentials (
  emp_id INT NOT NULL PRIMARY KEY,
  username VARCHAR(65) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  password_salt VARCHAR(255) NOT NULL,
  classification ENUM('Employee', 'Admin') NOT NULL,
  last_login DATETIME,
  failed_attempts INT DEFAULT 0,
  locked_until DATETIME,
  FOREIGN KEY (emp_id) REFERENCES employees(emp_id)
);