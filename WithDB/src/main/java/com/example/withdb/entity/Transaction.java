package com.example.withdb.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionId;

    private boolean isValid;

    private double amount;

    private Date localDate;

    // foreign key
    private int product_id;

    private int customer_id;

    public Transaction(){}

    public Transaction( boolean isValid, double amount, Date localDate, int product_id, int customer_id) {
        this.isValid = isValid;
        this.amount = amount;
        this.localDate = localDate;
        this.product_id = product_id;
        this.customer_id = customer_id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public boolean isValid() {
        return isValid;
    }

    public double getAmount() {
        return amount;
    }

    public Date getLocalDate() {
        return localDate;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }
}
