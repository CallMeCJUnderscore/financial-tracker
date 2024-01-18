# ---------------------------------------------------------------------- #
# Target DBMS:           MySQL                                           #
# Project name:          financial-tracker                               #
# Author:                Team 'Name TBD'                                 #
# ---------------------------------------------------------------------- #

# ---------------------------------------------------------------------- #
# STEP 1                                                                 #
# Create Database    													 #
# ---------------------------------------------------------------------- #
DROP DATABASE IF EXISTS `financial-tracker`;
CREATE DATABASE IF NOT EXISTS `financial-tracker`;
USE `financial-tracker`;


# ---------------------------------------------------------------------- #
# STEP 2																 #
# Create Database Tables												 #
# ---------------------------------------------------------------------- #
CREATE TABLE transactions(
TrID INT AUTO_INCREMENT PRIMARY KEY,
TrDate DATE DEFAULT CURDATE(),
TrTime TIME DEFAULT CURTIME(),
Description VARCHAR(50) NOT NULL,
Vendor VARCHAR(30) NOT NULL,
Amount DOUBLE NOT NULL);


# ---------------------------------------------------------------------- #
# STEP 3                                                                 #
# Populate tables with sample data                    					 #
# ---------------------------------------------------------------------- #
INSERT INTO transactions(TrDate, TrTime, Description, Vendor, Amount)
VALUES
("2023-04-15", "10:13:25", "Ergonomic keyboard", "Amazon", -89.50),
("2023-04-15", "11:15:00", "Invoice 1001 paid", "Joe", 1500.00),
("2023-04-16", "14:30:45", "Grocery shopping", "Walmart", -120.35),
("2023-04-17", "09:05:10", "Gasoline", "Shell", -45.00),
("2023-04-18", "12:30:00", "Monthly rent payment", "ABC Apartments", -2000.00),
("2023-04-19", "15:20:30", "Dinner with friends", "Cheesecake Factory", -85.20),
("2023-04-21", "08:45:00", "Salary deposit", "ABC Company", 5000.00),
("2023-04-23", "14:00:15", "Haircut", "Mario's Barber Shop", -35.00),
("2023-04-24", "16:10:00", "Online course subscription", "Udemy", -70.00);