package com.pluralsight.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

public class Transaction {


    /*---------------VARIABLES---------------*/

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double value;

    /*--------------CONSTRUCTORS-------------*/

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double value) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.value = value;
    }

    /*------------GETTERS/SETTERS------------*/

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /*---------------FUNCTIONS---------------*/

    @Override
    public String toString() {
        return String.format("""
                                    DATE: %s
                                    TIME: %s
                                    DESCRIPTION: %s
                                    VENDOR: %s
                                    VALUE: $%.2f
                                    """, date, time, description, vendor, value);
    }

    public static Comparator<Transaction> multiField(){ //sorter
        return Comparator.comparing(Transaction::getDate)
                .thenComparing(Transaction::getVendor);
    }
}
