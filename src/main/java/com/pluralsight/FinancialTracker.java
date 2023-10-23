package com.pluralsight;

import javax.print.attribute.standard.DateTimeAtProcessing;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;


        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Your Options Are:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Please make a choice: ");

            String input = scanner.nextLine().toUpperCase().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String input;
            while((input = bufferedReader.readLine()) != null){
                String[] tokens = input.split("\\|");
                LocalDate date = LocalDate.parse(tokens[0],DATE_FORMATTER);
                LocalTime time = LocalTime.parse(tokens[1],TIME_FORMATTER);
                String description = tokens[2];
                String vendor = tokens[3];
                double price = Double.parseDouble( tokens[4]);
                transactions.add(new Transaction(date, time, description, vendor, price));
            }
        }
        catch (IOException e){
            System.out.println("ERROR: Could not create reader!");
        }
        catch (DateTimeParseException e){
            System.out.println("ERROR: Could not parse date/time!");
        }
        catch (Exception e){
            System.out.println("ERROR: Could not load inventory!");
        }
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME));
            System.out.print("Please add the date of the deposit (Example: 2023-03-14): ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.print("Please add the time of the deposit (Example: 14:12:55): ");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.print("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Please enter the amount of the deposit: $");
            double depositAmount = scanner.nextDouble();
            scanner.nextLine();
            if(depositAmount <= 0){
                System.out.println("ERROR: Deposit must be positive! Defaulting to $1...");
                depositAmount = 1.0;
            }
            Transaction deposit = new Transaction(date,time,"Deposit",vendor,depositAmount);
            transactions.add(deposit);
            bufferedWriter.write((deposit +"\n"));
        }
        catch (DateTimeParseException e){
            System.out.println("ERROR: Could not parse date/time!");
        }
        catch (IOException e){
            System.out.println("ERROR: Could not instantiate writer!");
        }
        catch (Exception e){
            System.out.println("ERROR: Unspecified issue with adding deposit! Check formatting of inputs!");
        }
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME));
            System.out.print("Please add the date of the payment (Example: 2023-03-14): ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.print("Please add the time of the payment (Example: 14:12:55): ");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.print("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Please enter the amount of the deposit: $");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();
            if(paymentAmount <= 0){
                System.out.println("ERROR: Payment must be positive! Defaulting to $1...");
                paymentAmount = 1.0;
            }
            Transaction payment = new Transaction(date,time,"Payment",vendor,paymentAmount * -1);
            transactions.add(payment);
            bufferedWriter.write(payment +"\n");
        }
        catch (DateTimeParseException e){
            System.out.println("ERROR: Could not parse date/time!");
        }
        catch (IOException e){
            System.out.println("ERROR: Could not instantiate writer!");
        }
        catch (Exception e){
            System.out.println("ERROR: Unspecified issue with adding deposit! Check formatting of inputs!");
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().toUpperCase().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
        for (Transaction transaction : transactions){
            System.out.println(transaction);

        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        ArrayList<Transaction> found = new ArrayList<Transaction>();
        for (Transaction transaction : transactions){

            if(transaction.getPrice() >= 0){
                found.add(transaction);
            }
        }
        if (found.isEmpty()){
            System.out.println("ERROR: No deposits found!");
        }
        else{
            for(Transaction transaction : found){
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        ArrayList<Transaction> found = new ArrayList<Transaction>();
        for (Transaction transaction : transactions){

            if(transaction.getPrice() <= 0){
                found.add(transaction);
            }
        }
        if (found.isEmpty()){
            System.out.println("ERROR: No payments found!");
        }
        else{
            for(Transaction transaction : found){
                System.out.println(transaction);
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisMonth = LocalDate.now();
                    System.out.println("Displaying all transactions for the month of " + thisMonth.getMonth()+"...");
                    filterTransactionsByDate(thisMonth.withDayOfMonth(1).minusDays(1), thisMonth.plusDays(1));
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastMonth = LocalDate.now().minusMonths(1);
                    System.out.println("Displaying all transactions for the month of " + lastMonth.getMonth()+"...");
                    filterTransactionsByDate(lastMonth.withDayOfMonth(1).minusDays(1), lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()));
                    break;
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisYear = LocalDate.now();
                    System.out.println("Displaying all transactions for the year of " + thisYear.getYear()+" so far...");
                    filterTransactionsByDate(thisYear.withDayOfYear(1),thisYear.plusDays(1));
                    break;
                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastYear = LocalDate.now().minusYears(1);
                    System.out.println("Displaying all transactions for the year of " + lastYear.getYear()+"...");
                    filterTransactionsByDate(lastYear.withMonth(1).withDayOfMonth(31), lastYear.withMonth(12).withDayOfMonth(31));
                    break;
                case "5":
                    System.out.print("Please type the name of the vendor you would like to check for: ");
                    String vendorName = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendorName);
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        ArrayList<Transaction> found = new ArrayList<Transaction>();
        for (Transaction transaction : transactions){
            LocalDate dateToCheck = transaction.getDate();
            if(dateToCheck.isAfter(startDate.minusDays(1)) && dateToCheck.isBefore(endDate.plusDays(1))){
                found.add(transaction);
            }
        }
        if (found.isEmpty()){
            System.out.println("ERROR: No transactions found with given date range!");
        }
        else{
            for(Transaction transaction : found){
                System.out.println(transaction);
            }
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        ArrayList<Transaction> found = new ArrayList<Transaction>();
        for (Transaction transaction : transactions){
            if(transaction.getVendor().equalsIgnoreCase(vendor)){
                found.add(transaction);
            }
        }
        if (found.isEmpty()){
            System.out.println("ERROR: No transactions found with given vendor!");
        }
        else{
            for(Transaction transaction : found){
                System.out.println(transaction);
            }
        }
    }
}