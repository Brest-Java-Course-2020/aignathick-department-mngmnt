DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS department;

CREATE TABLE department (
  departmentId INT NOT NULL AUTO_INCREMENT,
  departmentName VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (departmentId)
);

-- employee
CREATE TABLE employee (
  employeeId    INTEGER NOT NULL AUTO_INCREMENT,
  firstname VARCHAR(45) NULL,
  lastname  VARCHAR(45) NULL,
  departmentId   INTEGER NOT NULL,
  salary    DECIMAL(10,2) NOT NULL DEFAULT 0,
  PRIMARY KEY (employeeId),
  CONSTRAINT emp_to_dept_fk
	FOREIGN KEY (departmentId)
	REFERENCES department (departmentId)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
);
