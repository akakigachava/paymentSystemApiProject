package com.example.withdb.dao;

import com.example.withdb.entity.Transaction;

import java.util.List;

public interface BaseTransactionDAO {
    boolean saveTransaction(Transaction transaction);
    boolean deleteTransaction(Transaction transaction);
    boolean updateTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
}
