package com.pluralsight;

import java.io.*;
import java.sql.Array;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

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
            System.out.println("Welcome to "+ConsoleColors.GREEN_BOLD_BRIGHT+"TransactionApp"+ConsoleColors.RESET);
            System.out.println("Your Options Are:");
            System.out.println(ConsoleColors.GREEN_BOLD+"D) "+ConsoleColors.RESET+"Add Deposit");
            System.out.println(ConsoleColors.GREEN_BOLD+"P) "+ConsoleColors.RESET+"Make Payment (Debit)");
            System.out.println(ConsoleColors.GREEN_BOLD+"L) "+ConsoleColors.RESET+"Ledger");
            System.out.println(ConsoleColors.GREEN_BOLD+"X) "+ConsoleColors.RESET+"Exit");
            System.out.print("Please make a choice: ");

            String input = scanner.nextLine().toUpperCase().trim();
            System.out.println();

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
                    System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT+"Thank you for using this app! Goodbye!");
                    break;
                default:
                    System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Invalid option"  + ConsoleColors.RESET);
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
            System.out.println(ConsoleColors.ERROR +"ERROR"+ConsoleColors.ERROR_MESSAGE+": Could not run file creation!"+ConsoleColors.RESET);
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
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not create reader!" + ConsoleColors.RESET);
        } catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not parse date/time!" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not load inventory!" + ConsoleColors.RESET);
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
            System.out.print("Please add the date of the deposit (Example:" + ConsoleColors.BOLD_UNDERLINE + "2023-03-14): ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.print("Please add the time of the deposit (Example:" + ConsoleColors.BOLD_UNDERLINE + "14:12:55): ");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.print("Please enter the reason of the deposit: ");
            String reason = scanner.nextLine();

            System.out.print("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Please enter the amount of the deposit: $");
            double depositAmount = scanner.nextDouble();
            scanner.nextLine();
            if (depositAmount <= 0) {
                System.out.println(ConsoleColors.ERROR +"ERROR"+ConsoleColors.ERROR_MESSAGE+": Deposit must be positive! Defaulting to $1..."+ConsoleColors.RESET);
                depositAmount = 1.0;
            }
            Transaction deposit = new Transaction(date, time, reason + " (Deposit)", vendor, depositAmount);
            transactions.add(deposit);
            String output = "\n" + deposit.getDate() + "|" + deposit.getTime() + "|" + deposit.getDescription() + "|" + deposit.getVendor() + "|" + deposit.getPrice();
            bufferedWriter.write(output);
            bufferedWriter.close();
        } catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR +"ERROR"+ConsoleColors.ERROR_MESSAGE+": Could not parse date/time!"+ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not instantiate writer!"+ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Unspecified issue with adding deposit!"+ConsoleColors.RESET);
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
            System.out.print("Please add the date of the payment (Example:" + ConsoleColors.BOLD_UNDERLINE + "2023-03-14" + ConsoleColors.RESET + "): ");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input, DATE_FORMATTER);

            System.out.print("Please add the time of the payment (Example:" + ConsoleColors.BOLD_UNDERLINE + "14:12:55" + ConsoleColors.RESET + "): ");
            input = scanner.nextLine();
            LocalTime time = LocalTime.parse(input, TIME_FORMATTER);

            System.out.print("Please enter the reason of the payment: ");
            String reason = scanner.nextLine();

            System.out.print("Please enter the name of the vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Please enter the amount of the deposit: $");
            double paymentAmount = scanner.nextDouble();
            scanner.nextLine();
            if (paymentAmount <= 0) {
                System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Payment must be positive! Defaulting to $1..."+ConsoleColors.RESET);
                paymentAmount = 1.0;
            }
            Transaction payment = new Transaction(date, time, reason + " (Payment)", vendor, paymentAmount * -1);
            transactions.add(payment);
            String output = "\n" + payment.getDate() + "|" + payment.getTime() + "|" + payment.getDescription() + "|" + payment.getVendor() + "|" + payment.getPrice();
            bufferedWriter.write(output);
            bufferedWriter.close();
        } catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not parse date/time!"+ConsoleColors.RESET);
        } catch (IOException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not instantiate writer!"+ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Unspecified issue with adding deposit! Check formatting of inputs!"+ConsoleColors.RESET);
        }

        System.out.println();
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the "+ConsoleColors.GREEN_BOLD_BRIGHT+"Ledger!"+ConsoleColors.RESET);
            System.out.println("Your options are:");
            System.out.println(ConsoleColors.GREEN_BOLD+"A) "+ConsoleColors.RESET+"All");
            System.out.println(ConsoleColors.GREEN_BOLD+"D) "+ConsoleColors.RESET+"Deposits");
            System.out.println(ConsoleColors.GREEN_BOLD+"P) "+ConsoleColors.RESET+"Payments");
            System.out.println(ConsoleColors.GREEN_BOLD+"R) "+ConsoleColors.RESET+"Reports");
            System.out.println(ConsoleColors.GREEN_BOLD+"S) "+ConsoleColors.RESET+"Custom Search");
            System.out.println(ConsoleColors.GREEN_BOLD+"H) "+ConsoleColors.RESET+"Home");
            System.out.print("Please make a choice: ");

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
                case "S":
                    customSearch(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Invalid option" + ConsoleColors.RESET);
                    break;
            }

        }

        System.out.println();
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
        System.out.println("\nShowing all transactions of type: "+ConsoleColors.BOLD_UNDERLINE+"ANY"+ConsoleColors.RESET+"...");
        transactions.sort(Transaction.TransDate);
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }

        System.out.println();
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        System.out.println("\nShowing all transactions of type: "+ConsoleColors.BOLD_UNDERLINE+"DEPOSIT"+ConsoleColors.RESET+"...");
        ArrayList<Transaction> found = new ArrayList<>();
        for (Transaction transaction : transactions) {

            if (transaction.getPrice() >= 0) {
                found.add(transaction);
            }
        }
        if (found.isEmpty()) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": No deposits found!"+ConsoleColors.RESET);
        } else {
            generateTable(found);
        }

        System.out.println();
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
        System.out.println("\nShowing all transactions of type: "+ConsoleColors.BOLD_UNDERLINE+"PAYMENT"+ConsoleColors.RESET+"...");
        ArrayList<Transaction> found = new ArrayList<>();
        for (Transaction transaction : transactions) {

            if (transaction.getPrice() <= 0) {
                found.add(transaction);
            }
        }
        if (found.isEmpty()) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": No payments found!"+ConsoleColors.RESET);
        } else {
            generateTable(found);
        }

        System.out.println();
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("Welcome to the "+ConsoleColors.GREEN_BOLD_BRIGHT+"Reports!"+ConsoleColors.RESET);
            System.out.println("Your options are:");
            System.out.println(ConsoleColors.GREEN_BOLD+"1) "+ConsoleColors.RESET+"Month To Date");
            System.out.println(ConsoleColors.GREEN_BOLD+"2) "+ConsoleColors.RESET+"Previous Month");
            System.out.println(ConsoleColors.GREEN_BOLD+"3) "+ConsoleColors.RESET+"Year To Date");
            System.out.println(ConsoleColors.GREEN_BOLD+"4) "+ConsoleColors.RESET+"Previous Year");
            System.out.println(ConsoleColors.GREEN_BOLD+"5) "+ConsoleColors.RESET+"Search by Vendor");
            System.out.println(ConsoleColors.GREEN_BOLD+"0) "+ConsoleColors.RESET+"Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisMonth = LocalDate.now();
                    System.out.println("Displaying all transactions for the month of " + ConsoleColors.BOLD_UNDERLINE + thisMonth.getMonth()+ConsoleColors.RESET + "...");
                    filterTransactionsByDate(thisMonth.withDayOfMonth(1), thisMonth);
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastMonth = LocalDate.now().minusMonths(1);
                    System.out.println("Displaying all transactions for the month of " + ConsoleColors.BOLD_UNDERLINE + lastMonth.getMonth()+ConsoleColors.RESET + "...");
                    filterTransactionsByDate(lastMonth.withDayOfMonth(1), lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()));
                    break;
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate thisYear = LocalDate.now();
                    System.out.println("Displaying all transactions for the year of " + ConsoleColors.BOLD_UNDERLINE + thisYear.getYear()+ConsoleColors.RESET + " so far...");
                    filterTransactionsByDate(thisYear.withDayOfYear(1), thisYear);
                    break;
                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    LocalDate lastYear = LocalDate.now().minusYears(1);
                    System.out.println("Displaying all transactions for the year of " + ConsoleColors.BOLD_UNDERLINE + lastYear.getYear()+ConsoleColors.RESET + "...");
                    filterTransactionsByDate(lastYear.withMonth(1).withDayOfMonth(1), lastYear.withMonth(12).withDayOfMonth(31));
                    break;
                case "5":
                    System.out.print("Please type the name of the vendor you would like to check for: ");
                    String vendorName = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendorName);
                case "0":
                    running = false;
                default:
                    System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Invalid option" + ConsoleColors.RESET);
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
        if (found.isEmpty()) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": No transactions found with given date range!" + ConsoleColors.RESET);
        } else {
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
        if (found.isEmpty()) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": No transactions found with given vendor!"+ConsoleColors.RESET);
        } else {
            generateTable(found);
        }

        System.out.println();
    }

    public static void customSearch(Scanner scanner){
        System.out.println("Welcome to the "+ConsoleColors.GREEN_BOLD_BRIGHT+"Custom Search!"+ConsoleColors.RESET);
        System.out.println("If you would not like to search by a given term, feel free to leave it blank.");
        try{
            System.out.print("Please add the start date of the search (Example:" + ConsoleColors.BOLD_UNDERLINE + "2023-03-14" + ConsoleColors.RESET + "): ");
            String input = scanner.nextLine().trim();
            LocalDate startDate;
            if(!input.isEmpty()){
                startDate = LocalDate.parse(input, DATE_FORMATTER);
            } else {
                startDate = LocalDate.of(1, 1, 1);
            }

            System.out.print("Please add the end date of the search (Example:" + ConsoleColors.BOLD_UNDERLINE + "2023-03-14" + ConsoleColors.RESET + "): ");
            input = scanner.nextLine().trim();
            LocalDate endDate;
            if(!input.isEmpty()){
                endDate = LocalDate.parse(input, DATE_FORMATTER);
            } else {
                endDate = LocalDate.of(9999, 12, 31);
            }

            System.out.print("Please enter the reason of the transaction: ");
            input = scanner.nextLine().trim();
            String description;
            if(!input.isEmpty()){
                description = input;
            } else {
                description = "NONE";
            }

            System.out.print("Please enter the vendor of the transaction: ");
            input = scanner.nextLine().trim();
            String vendor;
            if(!input.isEmpty()){
                vendor = input;
            } else {
                vendor = "NONE";
            }

            System.out.print("Please enter the price of the transaction: ");
            input = scanner.nextLine().trim();
            double price = 0;
            if(!input.isEmpty()){
                price = Double.parseDouble(input);
            }
            if (price < 0) {
                System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Payment must be positive! Ignoring term..."+ConsoleColors.RESET);
                price = 0;
            }

            ArrayList<Transaction> found = new ArrayList<>(transactions);
            if(!startDate.equals(LocalDate.of(1, 1, 1))){
                //Removes transaction from found if date is before the start day, including the start day itself
                found.removeIf(item -> !item.getDate().isAfter(startDate.minusDays(1)));
            }

            if(!endDate.equals(LocalDate.of(9999,12,31))){
                //Removes transaction from found if date is after the end day, including the end day itself
                found.removeIf(item -> !item.getDate().isBefore(endDate.plusDays(1)));
            }

            if(!description.equals("NONE")){
                //Removes transaction from found if the description does not match what was supplied by user
                found.removeIf(item -> !item.getDescription().equals(description));
            }

            if(!vendor.equals("NONE")){
                //Removes transaction from found if the vendor does not match what was supplied by user
                found.removeIf(item -> !item.getVendor().equals(vendor));
            }

            if(price!=0){
                //Removes transaction from found if the price does not match what was supplied by user
                double finalPrice = price;
                found.removeIf(item -> item.getPrice()!= finalPrice);
            }

            if (found.isEmpty()) {
                System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": No transactions found with given search terms!"+ConsoleColors.RESET);
            } else {
                generateTable(found);
            }

            System.out.println();
        }
        catch (DateTimeParseException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not parse date/time!"+ConsoleColors.RESET);
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Could not parse price!"+ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.ERROR + "ERROR"+ConsoleColors.ERROR_MESSAGE + ": Unspecified issue with search! Check formatting of inputs!"+ConsoleColors.RESET);
        }
    }
    public static void generateTable(ArrayList<Transaction> found){
        found.sort(Transaction.TransDate);
        System.out.println("""
                    +----------+--------+-------------------------+--------------------+---------+
                    |   \033[4;1mDATE\033[0m   |  \033[4;1mTIME\033[0m  │       \033[4;1mDESCRIPTION\033[0m       |       \033[4;1mVENDOR\033[0m       |  \033[4;1mPRICE\033[0m  |
                    +----------+--------+-------------------------+--------------------+---------+""");
        for (Transaction transaction : found) {
            String formattedDate = String.format("%-10.10s", transaction.getDate());
            String formattedDesc = String.format("%-25.25s",transaction.getDescription());
            String formattedVendor = String.format("%-20.20s",transaction.getVendor());
            String formattedTime = String.format("%-8.8s", transaction.getTime());
            String formattedPrice = String.format("%6.2f", transaction.getPrice());
            formattedPrice = String.format("$%8.8s", formattedPrice);
            String output = String.format("|%s|%s|%s|%s|%s|", formattedDate,formattedTime,formattedDesc,formattedVendor,formattedPrice);
            System.out.println(output);
            System.out.println("+----------+--------+-------------------------+--------------------+---------+");
        }
    }
}
