package com.example.withdb.dao;


import com.example.withdb.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class ProductDAO implements BaseProductDAO {
    private final EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);
    @Autowired
    public ProductDAO(EntityManager em) {
        this.entityManager = em;

    }

    @Override
    @Transactional
    public void save(Product product) {
        logger.info("Trying to save product " + product.toString());
        product.setId(0);
        entityManager.persist(product);
    }

    @Override
    public Product findById(int id) {
        logger.info("Trying to find product with id = " + id);
        return entityManager.find(Product.class, id);
    }

    @Override
    public List<Product> findAll() {
        logger.info("Trying ton get all products");
        TypedQuery<Product> typedQuery = entityManager.createQuery("select p from Product p", Product.class);
        return typedQuery.getResultList();
    }

    @Override
    @Transactional
    public void update(Product product) {
        logger.info("Trying to update product with name = " + product.getName() + " with product: " + product.toString());
        TypedQuery<Product> typedQuery = entityManager.createQuery("From Product p where p.name = :name", Product.class);
        typedQuery.setParameter("name", product.getName());
        Product product1 = typedQuery.getSingleResult();

        product1.setMinAmount(product.getMinAmount());
        product1.setMaxAmount(product.getMaxAmount());

        entityManager.merge(product1);
    }

    @Override
    @Transactional
    public boolean delete(String  productName) {
        logger.info("Trying to delete product with name " + productName);
        TypedQuery<Product> typedQuery = entityManager.createQuery("from Product p where p.name = :productName", Product.class);
        typedQuery.setParameter("productName", productName);
        List<Product> products = typedQuery.getResultList();
        if(products.isEmpty()) {
            return false;
        }
        // it impossible here the list length be more than 1 but just in case :)

        for(Product product : products) {
            entityManager.remove(product);
        }
        return true;
    }

    @Override
    public Optional<Product> findByName(String name) {
        logger.info("Trying to get product with name = " + name);
        TypedQuery<Product> typedQuery = entityManager.createQuery("from Product where name=:n", Product.class);
        typedQuery.setParameter("n", name);
        try {
            Product product = typedQuery.getSingleResult();
            return Optional.of(product);
        }catch(NoResultException e) {
            return Optional.empty();
        }
    }
}
