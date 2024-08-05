package com.example.withdb.BussinessLogic;

import com.example.withdb.entity.Product;
import com.example.withdb.entity.Transaction;

import java.util.List;

public interface BaseProductManager {

    OperationValidation addProduct(Product product);
    OperationValidation deleteProduct(String productName);
    OperationValidation updateProduct(Product product);
    OperationValidation payInCash(String productName, int money,int user_id);
    OperationValidation payByCard(String productName, String credential, int money,int user_id);
    List<Transaction> getAllTransactions(int userId);



}
