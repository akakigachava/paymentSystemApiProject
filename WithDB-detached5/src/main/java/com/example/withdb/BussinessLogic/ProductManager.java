package com.example.withdb.BussinessLogic;

import com.example.withdb.controllers.ProductController;
import com.example.withdb.dao.BaseProductDAO;
import com.example.withdb.dao.BaseTransactionDAO;
import com.example.withdb.dao.ProductDAO;
import com.example.withdb.dao.TransactionDAO;
import com.example.withdb.entity.Product;
import com.example.withdb.entity.Transaction;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ProductManager implements BaseProductManager {

    private final BaseProductDAO baseProductDAO;
    private final BaseTransactionDAO baseTransactionDAO;
    private final CardCredentialChecker cardCredentialChecker;


    @Autowired
    public ProductManager(BaseProductDAO baseProductDAO, BaseTransactionDAO baseTransaction, CardCredentialChecker cardCredentialChecker, ProductDAO productDAO) {
        this.baseProductDAO = baseProductDAO;
        this.baseTransactionDAO = baseTransaction;
        this.cardCredentialChecker = cardCredentialChecker;
    }

    @Override
    public OperationValidation addProduct(Product product) {
            if(!validProduct(product)){
                return new OperationValidation("Invalid Product",false);
            }


            try {
                baseProductDAO.save(product);
                return new OperationValidation("no error",true);
            } catch (EntityExistsException e) {
                // Handle specific case where the entity already exists
                System.err.println("Product already exists: " + e.getMessage());
                return new OperationValidation("product already exists",false);
            } catch (IllegalArgumentException e) {
                // Handle specific case where the argument is not a valid entity
                System.err.println("Invalid product: " + e.getMessage());
                return new OperationValidation("Invalid product",false);
            } catch (TransactionRequiredException e) {
                // Handle specific case where there is no transaction
                System.err.println("Transaction required: " + e.getMessage());
                return new OperationValidation("Transaction required",false);
            } catch (PersistenceException e) {
                // Handle general persistence exceptions
                System.err.println("Persistence error: " + e.getMessage());
                return new OperationValidation("Persistence error",false);
            } catch (Exception e) {
                // Handle all other exceptions
                System.err.println("An unexpected error occurred: " + e.getMessage());
                return new OperationValidation("An unexpected error occurred" +e.getMessage(),false);
            }
    }



    @Override
    public OperationValidation deleteProduct(String productName) {
        try {
            if (baseProductDAO.delete(productName)) {
                return new OperationValidation("No error", true);
            } else {
                return new OperationValidation("Product does not exist", false);
            }
        } catch (IllegalArgumentException e) {
            // Handle specific case where the argument is not a valid entity
            System.err.println("Invalid product: " + e.getMessage());
            return new OperationValidation("Invalid product: " + e.getMessage(), false);
        } catch (TransactionRequiredException e) {
            // Handle specific case where there is no transaction
            System.err.println("Transaction required: " + e.getMessage());
            return new OperationValidation("Transaction required: " + e.getMessage(), false);
        } catch (PersistenceException e) {
            // Handle general persistence exceptions
            System.err.println("Persistence error: " + e.getMessage());
            return new OperationValidation("Persistence error: " + e.getMessage(), false);
        } catch (Exception e) {
            // Handle all other exceptions
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return new OperationValidation("An unexpected error occurred: " + e.getMessage(), false);
        }
    }

    @Override
    public OperationValidation updateProduct(Product product) {
        try {
            baseProductDAO.update(product);
            return new OperationValidation("No error", true);
        } catch (IllegalArgumentException e) {
            // Handle specific case where the argument is not a valid entity
            return new OperationValidation("Invalid product: " + e.getMessage(), false);
        } catch (TransactionRequiredException e) {
            // Handle specific case where there is no transaction
            return new OperationValidation("Transaction required: " + e.getMessage(), false);
        } catch (PersistenceException e) {
            // Handle general persistence exceptions
            return new OperationValidation("Persistence error: " + e.getMessage(), false);
        } catch (Exception e) {
            // Handle all other exceptions
            return new OperationValidation("An unexpected error occurred: " + e.getMessage(), false);
        }
    }





    @Override
    public OperationValidation payInCash(String productName, int money,int user_id) {

        Optional<Product> productOptional = baseProductDAO.findByName(productName);

        if(productOptional.isEmpty()){
            return new OperationValidation("Product does not exist", false);
        }

        Product product = productOptional.get();

        if(!validProduct(product)) {
            Transaction transaction=new Transaction(false,money,new Date(), product.getId(),user_id);
            baseTransactionDAO.saveTransaction(transaction);
            return new OperationValidation("Product does not exist", false);
        }

        if (!validMoney(product,money)){
            Transaction transaction=new Transaction(false,money,new Date(), product.getId(),user_id);
            baseTransactionDAO.saveTransaction(transaction);
            return new OperationValidation("Money is not sufficient", false);
        }

        // Some Business Logic
        Transaction transaction=new Transaction(true,money,new Date(), product.getId(),user_id);
        try {
            baseTransactionDAO.saveTransaction(transaction);
        } catch (Exception e) {
            return new OperationValidation("Transaction failed to save", false);
        }
        return new OperationValidation("No error", true);
    }



    @Override
    public OperationValidation payByCard(String productName, String cardId, int money,int user_id) {

        Optional<Product> productOptional = baseProductDAO.findByName(productName);

        if(productOptional.isEmpty()){
            return new OperationValidation("Product does not exist", false);
        }

        Product product = productOptional.get();

        if(!validProduct(product)){
            Transaction transaction=new Transaction(false,money,new Date(), product.getId(),user_id);
            baseTransactionDAO.saveTransaction(transaction);
            return new OperationValidation("Product does not exist", false);
        }

        if (!validMoney(product,money)){
            Transaction transaction=new Transaction(false,money,new Date(), product.getId(),user_id);
            baseTransactionDAO.saveTransaction(transaction);
            return new OperationValidation("Money is not sufficient", false);
        }
        if (!cardCredentialChecker.checkCardCredential(cardId)){
            Transaction transaction=new Transaction(false,money,new Date(), product.getId(),user_id);
            baseTransactionDAO.saveTransaction(transaction);
            return new OperationValidation("Card does not exist", false);
        }

        // Some Business Logic
        Transaction transaction=new Transaction(true,money,new Date(), product.getId(),user_id);
        try {
            baseTransactionDAO.saveTransaction(transaction);
        } catch (Exception e) {
            return new OperationValidation("Transaction failed to save", false);
        }
        return new OperationValidation("No error", true);


    }

    @Override
    public List<Transaction> getAllTransactions(int customerId) {
        return baseTransactionDAO.getAllTransactions().stream().filter(x->x.getCustomer_id() == customerId).toList();
    }



    private boolean validMoney(Product product, int money){
        return money > 0 && money <= product.getMaxAmount() && money >= product.getMinAmount();
    }

    private boolean validProduct(Product product){
        return product.getMinAmount()>0 && product.getMaxAmount()>0 && product.getMinAmount()<product.getMaxAmount() && !product.getName().isEmpty();
    }





}
