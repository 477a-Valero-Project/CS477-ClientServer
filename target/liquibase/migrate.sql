--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: src/main/java/Logic/Resources/liquibase/db.master.xml
--  Ran at: 2/10/15 12:16 AM
--  Against: root@localhost@jdbc:mysql://127.0.0.1:3306/dev
--  Liquibase version: 3.2.2
--  *********************************************************************

--  Lock Database
--  Changeset src/main/java/Logic/Resources/liquibase/db.changelog-0.0.1.xml::0.0.2::Martin Borstad
CREATE TABLE Doctors (doctorId INT NOT NULL, `key` VARCHAR(255) NULL, password VARCHAR(255) NULL, CONSTRAINT PK_DOCTORS PRIMARY KEY (doctorId));

ALTER TABLE Doctors MODIFY doctorId INT AUTO_INCREMENT;

CREATE TABLE Patients (patientId INT NOT NULL, `key` VARCHAR(255) NULL, password VARCHAR(255) NULL, CONSTRAINT PK_PATIENTS PRIMARY KEY (patientId));

ALTER TABLE Patients MODIFY patientId INT AUTO_INCREMENT;

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('0.0.2', 'Martin Borstad', 'src/main/java/Logic/Resources/liquibase/db.changelog-0.0.1.xml', NOW(), 2, '7:65ae60f0cb90b93012727e4391909ba2', 'createTable, addAutoIncrement, createTable, addAutoIncrement', '', 'EXECUTED', '3.2.2');

--  Release Database Lock
--  Release Database Lock
