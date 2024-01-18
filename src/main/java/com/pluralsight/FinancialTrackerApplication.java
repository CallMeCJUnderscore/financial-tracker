package com.pluralsight;

import com.pluralsight.colors.ConsoleColors;
import com.pluralsight.models.Transaction;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FinancialTrackerApplication {

    private static ArrayList<Transaction> transactions = new ArrayList<>();
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
            System.out.println("Welcome to " + ConsoleColors.GREEN_BOLD_BRIGHT + "CallMeFinance!" + ConsoleColors.RESET);
            System.out.println("Your Options Are:");
            System.out.println(ConsoleColors.GREEN_BOLD + "D) " + ConsoleColors.RESET + "Add Deposit");
            System.out.println(ConsoleColors.GREEN_BOLD + "P) " + ConsoleColors.RESET + "Make Payment (Debit)");
            System.out.println(ConsoleColors.GREEN_BOLD + "L) " + ConsoleColors.RESET + "Ledger");
            System.out.println(ConsoleColors.GREEN_BOLD + "X) " + ConsoleColors.RESET + "Exit");
            System.out.print("Please make a choice: ");

            String input = scanner.nextLine().toUpperCase().trim();

            switch (input) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> {
                    running = false;
                    System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "Thank you for using this app! Goodbye!");
                }
                default ->
                        System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Invalid option" + ConsoleColors.RESET);
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
        // For Example:" + ConsoleColors.BOLD_UNDERLINE + "2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
        try {
            File myFile = new File(fileName);
            if (myFile.createNewFile()) {
                System.out.println("Inventory does not exist! Creating file...\n");
            } else {
                System.out.println("Inventory loaded!\n");
            }
        } catch (IOException e) {
            System.out.println(ConsoleColors.ERROR +"ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not run file creation!" + ConsoleColors.RESET);
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String input;
            bufferedReader.readLine();
            while ((input = bufferedReader.readLine()) != null) {
                String[] tokens = input.split("\\|");
                LocalDate date = LocalDate.parse(tokens[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(tokens[1], TIME_FORMATTER);
                String description = tokens[2];
                String vendor = tokens[3];
                double price = Double.parseDouble(tokens[4]);
                transactions.add(new Transaction(date, time, description, vendor, price));
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not create reader!" + ConsoleColors.RESET);
        } catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not parse date/time!" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not load inventory!" + ConsoleColors.RESET);
        }

        System.out.println();
    }

    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Deposit` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
            System.out.print("Please add the date of the deposit (Example: " + ConsoleColors.BOLD + "2023-03-14" + ConsoleColors.RESET + "): ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.print("Please add the time of the deposit (Example: " + ConsoleColors.BOLD + "14:12:55" + ConsoleColors.RESET + "): ");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.print("Please enter the reason of the deposit: ");
            String reason = scanner.nextLine();

            System.out.print("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Please enter the amount of the deposit: $");
            double depositAmount = scanner.nextDouble();
            scanner.nextLine(); //Consume NewLine character to not cause issues

            if (depositAmount <= 0) {
                System.out.println(ConsoleColors.ERROR +"ERROR" + ConsoleColors.ERROR_MESSAGE + ": Deposit must be positive! Defaulting to $1..." + ConsoleColors.RESET);
                depositAmount = 1.0;
            }
            Transaction deposit = new Transaction(date, time, reason + " (Deposit)", vendor, depositAmount);
            transactions.add(deposit);
            String output = "\n" + deposit.getDate() + "|" + deposit.getTime() + "|" + deposit.getDescription() + "|" + deposit.getVendor() + "|" + deposit.getValue();
            bufferedWriter.write(output);
            System.out.println("\nThe following transaction has been added to the ledger:");
            System.out.println(deposit);
            bufferedWriter.close();
        } catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR +"ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not parse date/time!" + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not instantiate writer!" + ConsoleColors.RESET);
        } catch (InputMismatchException e){
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Improper type input!" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Unspecified issue with adding deposit!" + ConsoleColors.RESET);
        }

        System.out.println();
    }

    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Payment` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
            System.out.print("Please add the date of the payment (Example: " + ConsoleColors.BOLD + "2023-03-14" + ConsoleColors.RESET + "): ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.print("Please add the time of the payment (Example: " + ConsoleColors.BOLD + "14:12:55" + ConsoleColors.RESET + "): ");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.print("Please enter the reason of the payment: ");
            String reason = scanner.nextLine();

            System.out.print("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Please enter the amount of the deposit: $");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine(); //Consume NewLine character to not cause issues
            if (paymentAmount <= 0) {
                System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Payment must be positive! Defaulting to $1..." +ConsoleColors.RESET);
                paymentAmount = 1.0;
            }


            Transaction payment = new Transaction(date, time, reason + " (Payment)", vendor, paymentAmount * -1);
            transactions.add(payment);

            String output = "\n" + payment.getDate() + "|" + payment.getTime() + "|" + payment.getDescription() + "|" + payment.getVendor() + "|" + payment.getValue();
            System.out.println("The following transaction has been added to the ledger:");
            System.out.println(payment);
            bufferedWriter.write(output);
            bufferedWriter.close();
        } catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not parse date/time!" + ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not instantiate writer!" + ConsoleColors.RESET);
        } catch (InputMismatchException e){
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Improper type input!" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Unspecified issue with adding deposit!" + ConsoleColors.RESET);
        }

        System.out.println();
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the " + ConsoleColors.GREEN_BOLD_BRIGHT + "Ledger!" + ConsoleColors.RESET);
            System.out.println("Your options are:");
            System.out.println(ConsoleColors.GREEN_BOLD + "A) " + ConsoleColors.RESET + "All");
            System.out.println(ConsoleColors.GREEN_BOLD + "D) " + ConsoleColors.RESET + "Deposits");
            System.out.println(ConsoleColors.GREEN_BOLD + "P) " + ConsoleColors.RESET + "Payments");
            System.out.println(ConsoleColors.GREEN_BOLD + "R) " + ConsoleColors.RESET + "Reports");
            System.out.println(ConsoleColors.GREEN_BOLD + "S) " + ConsoleColors.RESET + "Custom Search");
            System.out.println(ConsoleColors.GREEN_BOLD + "H) " + ConsoleColors.RESET + "Home");
            System.out.print("Please make a choice: ");

            String input = scanner.nextLine().toUpperCase().trim();

            switch (input) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "S" -> customSearch(scanner);
                case "H" -> running = false;
                default -> System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Invalid option" + ConsoleColors.RESET);
            }

        }

        System.out.println();
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
        System.out.println("\nShowing all transactions of type: " + ConsoleColors.BOLD + "ANY" + ConsoleColors.RESET +":");

        generateTable(transactions);
        System.out.println();
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        System.out.println("\nShowing all transactions of type: " + ConsoleColors.BOLD + "DEPOSIT" + ConsoleColors.RESET +":");
        ArrayList<Transaction> found = new ArrayList<>();
        for (Transaction transaction : transactions) {

            if (transaction.getValue() >= 0) {
                found.add(transaction);
            }
        }
        if (found.isEmpty()) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": No deposits found!" + ConsoleColors.RESET);
        } else {
            generateTable(found);
        }

        System.out.println();
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        System.out.println("\nShowing all transactions of type: " + ConsoleColors.BOLD + "PAYMENT" + ConsoleColors.RESET +":");
        ArrayList<Transaction> found = new ArrayList<>();
        for (Transaction transaction : transactions) {

            if (transaction.getValue() <= 0) {
                found.add(transaction);
            }
        }
        if (found.isEmpty()) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": No payments found!" + ConsoleColors.RESET);
        } else {
            generateTable(found);
        }

        System.out.println();
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("Welcome to the " + ConsoleColors.GREEN_BOLD_BRIGHT + "Reports!" + ConsoleColors.RESET);
            System.out.println("Your options are:");
            System.out.println(ConsoleColors.GREEN_BOLD + "1) " + ConsoleColors.RESET + "Month To Date");
            System.out.println(ConsoleColors.GREEN_BOLD + "2) " + ConsoleColors.RESET + "Previous Month");
            System.out.println(ConsoleColors.GREEN_BOLD + "3) " + ConsoleColors.RESET + "Year To Date");
            System.out.println(ConsoleColors.GREEN_BOLD + "4) " + ConsoleColors.RESET + "Previous Year");
            System.out.println(ConsoleColors.GREEN_BOLD + "5) " + ConsoleColors.RESET + "Search by Vendor");
            System.out.println(ConsoleColors.GREEN_BOLD + "0) " + ConsoleColors.RESET + "Back");
            System.out.print("Please make a choice: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisMonth = LocalDate.now();
                    System.out.println("Displaying all transactions for the month of " + ConsoleColors.BOLD + thisMonth.getMonth() + ConsoleColors.RESET);
                    filterTransactionsByDate(thisMonth.withDayOfMonth(1), thisMonth);
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastMonth = LocalDate.now().minusMonths(1);
                    System.out.println("Displaying all transactions for the month of " + ConsoleColors.BOLD + lastMonth.getMonth() + ConsoleColors.RESET);
                    filterTransactionsByDate(lastMonth.withDayOfMonth(1), lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()));
                    //lengthOfMonth is used because there is no way to concretely know how many days last month has, but you need to ensure every day is checked
                    break;
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisYear = LocalDate.now();
                    System.out.println("Displaying all transactions for the year of " + ConsoleColors.BOLD + thisYear.getYear() + ConsoleColors.RESET + " so far:");
                    filterTransactionsByDate(thisYear.withDayOfYear(1), thisYear);
                    break;
                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastYear = LocalDate.now().minusYears(1);
                    System.out.println("Displaying all transactions for the year of " + ConsoleColors.BOLD + lastYear.getYear() + ConsoleColors.RESET + ":");
                    filterTransactionsByDate(lastYear.withMonth(1).withDayOfMonth(1), lastYear.withMonth(12).withDayOfMonth(31));
                    break;
                case "5":
                    // Generate a report for all transactions from a given vendor,
                    // including the date, vendor, and amount for each transaction.
                    System.out.print("Please type the name of the vendor you would like to check for: ");
                    String vendorName = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendorName);
                case "0":
                    running = false;
                default:
                    System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Invalid option" + ConsoleColors.RESET);
                    break;
            }
        }
        System.out.println();
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
        ArrayList<Transaction> found = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate dateToCheck = transaction.getDate();
            //Search method is exclusive, so the inputted dates must be shifted to include the start and end dates input by the user in the search.
            if (dateToCheck.isAfter(startDate.minusDays(1)) && dateToCheck.isBefore(endDate.plusDays(1))) {
                found.add(transaction);
            }
        }
        if (found.isEmpty()) { //Does nothing match the search terms?
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": No transactions found with given date range!" + ConsoleColors.RESET);
        } else { //Does nothing match the search terms?
            generateTable(found);
        }

        System.out.println();
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        ArrayList<Transaction> found = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                found.add(transaction);
            }
        }
        if (found.isEmpty()) { //Does nothing match the search terms?
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": No transactions found with given vendor!" + ConsoleColors.RESET);
        } else { //Something DOES match
            generateTable(found);
        }

        System.out.println();
    }

    public static void customSearch(Scanner scanner){
        System.out.println("Welcome to the " + ConsoleColors.GREEN_BOLD_BRIGHT + "Custom Search!" + ConsoleColors.RESET);
        System.out.println("If you would not like to search by a given term, feel free to leave it blank.");
        try{
            System.out.print("Please add the start date of the search (Example: " + ConsoleColors.BOLD + "2023-03-14" + ConsoleColors.RESET + "): ");
            String input = scanner.nextLine().trim();
            LocalDate startDate;
            if(!input.isEmpty()){ //Parse input if present, create Null case if not
                startDate = LocalDate.parse(input, DATE_FORMATTER);
            } else {
                startDate = LocalDate.of(1, 1, 1);
            }

            System.out.print("Please add the end date of the search (Example: " + ConsoleColors.BOLD + "2023-03-14" + ConsoleColors.RESET + "): ");
            input = scanner.nextLine().trim();
            LocalDate endDate;
            if(!input.isEmpty()){  //Parse input if present, create Null case if not
                endDate = LocalDate.parse(input, DATE_FORMATTER);
            } else {
                endDate = LocalDate.of(9999, 12, 31);
            }

            System.out.print("Please enter the reason of the transaction: ");
            input = scanner.nextLine().trim();
            String description;
            if(!input.isEmpty()){  //Parse input if present, create Null case if not
                description = input;
            } else {
                description = "NONE";
            }

            System.out.print("Please enter the vendor of the transaction: ");
            input = scanner.nextLine().trim();
            String vendor;
            if(!input.isEmpty()){  //Parse input if present, create Null case if not
                vendor = input;
            } else {
                vendor = "NONE";
            }

            System.out.print("Please enter the price of the transaction: ");
            input = scanner.nextLine().trim();
            double value;
            //Check if input is valid (Not empty, contains only digits, and does not only contain 0). Parse input if yes, create Null case if not.
            if (input.isEmpty() || input.matches("\\D+") || input.matches("^0+$")) {
                System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Payment be a nonzero number! Ignoring filter..." + ConsoleColors.RESET);
                value = 0;
            }
            else {
                value = Double.parseDouble(input);
            }

            ArrayList<Transaction> found = new ArrayList<>(transactions); //Build array to search with

            if(!startDate.equals(LocalDate.of(1, 1, 1))){ //If term is not Null case...
                //Removes transaction from found if date is before the start day, including the start day itself
                found.removeIf(item -> !item.getDate().isAfter(startDate.minusDays(1)));
            }

            if(!endDate.equals(LocalDate.of(9999,12,31))){ //If term is not Null case...
                //Removes transaction from found if date is after the end day, including the end day itself
                found.removeIf(item -> !item.getDate().isBefore(endDate.plusDays(1)));
            }

            if(!description.equals("NONE")){ //If term is not Null case...
                //Removes transaction from found if the description does not match what was supplied by user
                found.removeIf(item -> !item.getDescription().equals(description));
            }

            if(!vendor.equals("NONE")){ //If term is not Null case...
                //Removes transaction from found if the vendor does not match what was supplied by user
                found.removeIf(item -> !item.getVendor().equals(vendor));
            }

            if(value>0){ //If term is not Null case...
                //Removes transaction from found if the value does not match what was supplied by user
                found.removeIf(item -> item.getValue()!= value);
            }

            if (found.isEmpty()) { //Does nothing match the search terms?
                System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": No transactions found with given search terms!" + ConsoleColors.RESET);
            } else { //Something DOES match
                generateTable(found);
            }

            System.out.println();
        }
        catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not parse date/time!" + ConsoleColors.RESET);
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Could not parse value!" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR" + ConsoleColors.ERROR_MESSAGE + ": Unspecified issue with search! Check formatting of inputs!" + ConsoleColors.RESET);
        }
    }
    public static void generateTable(ArrayList<Transaction> transactions){
        transactions.sort(Transaction.multiField());

        //INSERT ALL THE ANSI ESCAPE CODES
        System.out.println("""
                    +----------+--------+-------------------------+--------------------+---------+
                    |   \033[4;1mDATE\033[0m   |  \033[4;1mTIME\033[0m  │       \033[4;1mDESCRIPTION\033[0m       |       \033[4;1mVENDOR\033[0m       |  \033[4;1mVALUE\033[0m  |
                    +----------+--------+-------------------------+--------------------+---------+""");
        for (Transaction transaction : transactions) {
            //negative sign left justifies, first number sets padding, second number sets max width
            String formattedDate = String.format("%-10.10s", transaction.getDate());
            String formattedDesc = String.format("%-25.25s",transaction.getDescription());
            String formattedVendor = String.format("%-20.20s",transaction.getVendor());
            String formattedTime = String.format("%-8.8s", transaction.getTime());
            String formattedValue = String.format("%6.2f", transaction.getValue()); //Max of 6 numbers before decimal, 2 after
            formattedValue = String.format("$%8.8s", formattedValue); //Done twice to add needed padding to value after converting from double
            String output = String.format("|%s|%s|%s|%s|%s|", formattedDate,formattedTime,formattedDesc,formattedVendor,formattedValue);
            System.out.println(output);
            System.out.println("+----------+--------+-------------------------+--------------------+---------+");
        }
    }
}
