package com.example.withdb.controllers;


import com.example.withdb.BussinessLogic.OperationValidation;
import com.example.withdb.entity.Product;
import com.example.withdb.entity.Transaction;
import com.example.withdb.BussinessLogic.BaseProductManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *    ProductController class - for managing products.
 *    supports functions:
 *      for admins: add product
 *                  update product
 *                  delete product
 *      for customers: pay in cash
 *                     pay by card
 *                     get transactions history
 *     Every endpoint has its constraint that refers to the role that has access to do operation
 *
 *     For managing product CRUD operations here is BaseProductManager attribute that provides suitable methods
 *
 *     Some implementation specifications (if needed) will be written above methods or in README.md file
 */
@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private final BaseProductManager productManager;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(BaseProductManager userManager) {
        System.out.println(userManager);
        this.productManager = userManager;
    }

    // must authorized role = administrtator
    @PostMapping("/addProduct")
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addProduct(Product product){
        OperationValidation operationValidation = productManager.addProduct(product);
        if (operationValidation.success()) logger.info("Product " + product.toString() + "has been added successfully");
        else logger.info("Product can't be added. Error Message: " + operationValidation.errorMessage());
        return responseEntity(operationValidation,"Successfully added product");
    }

    // Role = admin
    @PostMapping("/deleteProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(String productName){
        OperationValidation operationValidation = productManager.deleteProduct(productName);
        if (operationValidation.success()) logger.info("Product with name = " + productName + "has been deleted successfully");
        else logger.info("Product can't be deleted. Error Message: " + operationValidation.errorMessage());
        return responseEntity(operationValidation,"Successfully deleted product");
    }

    // Role = admin
    @PostMapping("/updateProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProduct(Product product){
        OperationValidation operationValidation = productManager.updateProduct(product);
        if (operationValidation.success()) logger.info("Product with name = " + product.getName() + "has been updated with product " + product.toString() + " successfully");
        else logger.info("Product can't be updated. Error Message: " + operationValidation.errorMessage());
        return responseEntity(operationValidation,"Successfully Updated");
    }


    @PostMapping("/payInCash")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> payWithCash(String productName, int money, int user_id){
        OperationValidation operationValidation = productManager.payInCash(productName,money,user_id);
        if (operationValidation.success()) logger.info("Paying with cash has been completed successfully");
        else logger.info("Payment failed. Error Message: " + operationValidation.errorMessage());
        return responseEntity(operationValidation,"Successfully payed in cash");
    }

    @PostMapping("/PayByCard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<String> payByCard(String productName, String credentials, int money, int user_id){
        OperationValidation operationValidation = productManager.payByCard(productName,credentials,money,user_id);
        if (operationValidation.success()) logger.info("Paying by card has been completed successfully");
        else logger.info("Payment failed. Error Message: " + operationValidation.errorMessage());
        return responseEntity(operationValidation,"Successfully payed by card");
    }

    @GetMapping("/GetHistory")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<Transaction> getHistory(int userId){
        logger.info("All transactions loaded successfully");
        return productManager.getAllTransactions(userId);
    }

    private ResponseEntity<String> responseEntity(OperationValidation operationValidation, String successMessage){
        if(operationValidation.success()){
            return ResponseEntity.status(HttpStatus.OK).body(successMessage);
        }else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(operationValidation.errorMessage());
        }
    }

}
