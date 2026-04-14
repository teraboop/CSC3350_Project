
CREATE DATABASE projectEmployeeData;
USE projectEmployeeData;


/***********************************************************************/ 

DROP TABLE IF EXISTS cities;
CREATE TABLE cities (
  cityID INT NOT NULL PRIMARY KEY,
  cityName varchar(25) NOT NULL
);

/***********************************************************************/ 

DROP TABLE IF EXISTS states;
CREATE TABLE states (
  stateID INT NOT NULL PRIMARY KEY,
  stateAbbr varchar(2) NOT NULL
);

/***********************************************************************/ 

DROP TABLE IF EXISTS addresses;
CREATE TABLE addresses (
  addressID INT NOT NULL PRIMARY KEY,
  street varchar(50) NOT NULL,
  cityID INT NOT NULL,
  stateID INT NOT NULL,
  zip VARCHAR(10) NOT NULL,
  DOB varchar(10) NOT NULL,
  phone varchar(10) NOT NULL,
  emergencyContactName varchar(100) NOT NULL,
  emergencyContactPhone varchar(10) NOT NULL,
  FOREIGN KEY (cityID) REFERENCES cities(cityID),
  FOREIGN KEY (stateID) REFERENCES states(stateID)
);

/***********************************************************************/ 

DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
  empid INT NOT NULL PRIMARY KEY,
  Fname VARCHAR(65) NOT NULL,
  Lname VARCHAR(65) NOT NULL,
  email VARCHAR(65) NOT NULL,
  HireDate DATE,
  Salary DECIMAL(10,2) NOT NULL,
  SSN VARCHAR(12),
  addressID INT NOT NULL,
  FOREIGN KEY (addressID) REFERENCES addresses(addressID)
);

/***********************************************************************/

DROP TABLE IF EXISTS payroll;
CREATE TABLE payroll (
  payID INT,
  pay_date DATE,
  earnings DECIMAL(8,2),
  fed_tax DECIMAL(7,2),
  fed_med DECIMAL(7,2),
  fed_SS DECIMAL(7,2),
  state_tax DECIMAL(7,2),
  retire_401k DECIMAL(7,2),
  health_care DECIMAL(7,2),
  empid INT,
  FOREIGN KEY (empid) REFERENCES employees(empid)
);


/***********************************************************************/ 

DROP TABLE IF EXISTS job_titles;
CREATE TABLE job_titles (
  job_title_id INT PRIMARY KEY,
  job_title VARCHAR(125) NOT NULL
);

/***********************************************************************/ 

DROP TABLE IF EXISTS employee_job_titles ;
CREATE TABLE employee_job_titles (
  empid INT NOT NULL,
  job_title_id INT NOT NULL,
  FOREIGN KEY (empid) REFERENCES employees(empid),
  FOREIGN KEY (job_title_id) REFERENCES job_titles(job_title_id)
);

/***********************************************************************/

DROP TABLE IF EXISTS division;
CREATE TABLE division (
  divID int NOT NULL PRIMARY KEY,
  Name varchar(100) DEFAULT NULL,
  city varchar(50) NOT NULL,
  addressLine1 varchar(50) NOT NULL,
  addressLine2 varchar(50) DEFAULT NULL,
  state varchar(50) DEFAULT NULL,
  country varchar(50) NOT NULL,
  postalCode varchar(15) NOT NULL
) COMMENT='company divisions';

/***********************************************************************/ 

DROP TABLE IF EXISTS employee_division;
CREATE TABLE employee_division (
  empid int NOT NULL,
  divID int NOT NULL,
  FOREIGN KEY (empid) REFERENCES employees(empid),
  FOREIGN KEY (divId) REFERENCES division(divID)
) COMMENT='links employee to a division';

/***********************************************************************/
/* Edward: I want to add this table to track employee credential/authorization information. */

DROP TABLE IF EXISTS credentials;
CREATE TABLE credentials (
  empid INT NOT NULL PRIMARY KEY,
  username VARCHAR(65) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  password_salt VARCHAR(255) NOT NULL,
  classification ENUM('Employee', 'Admin') NOT NULL,
  last_login DATETIME,
  failed_attempts INT DEFAULT 0,
  locked_until DATETIME,
  FOREIGN KEY (empid) REFERENCES employees(empid)
);


