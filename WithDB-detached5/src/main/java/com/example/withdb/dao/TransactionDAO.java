package com.example.withdb.dao;

import com.example.withdb.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionDAO implements BaseTransactionDAO {
    private final EntityManager entityManager;

    public TransactionDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    @Transactional
    public boolean saveTransaction(Transaction transaction) {
        entityManager.persist(transaction);
        return true;
    }

    @Override
    public boolean deleteTransaction(Transaction transaction) {
        if (isPresent(transaction)) {
            entityManager.detach(transaction);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTransaction(Transaction transaction) {
        if (isPresent(transaction)) {
            entityManager.merge(transaction);
            return true;
        }
        return false;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        String sqlCommand = "FROM Transaction ";
        TypedQuery<Transaction> query = entityManager.createQuery(sqlCommand, Transaction.class);
        return query.getResultList();
    }

    private boolean isPresent(Transaction transaction) {
        return entityManager.contains(transaction);
    }
}
