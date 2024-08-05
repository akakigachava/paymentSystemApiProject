package com.example.withdb.entity;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    private double minAmount;
    private double maxAmount;

    public Product(String name, double minAmount, double maxAmount) {
        this.name = name;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public Product(String name) {
        this.name = name;
        this.minAmount = 1.0;
        this.maxAmount = 2000.0;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Double.compare(minAmount, product.minAmount) == 0 &&
                Double.compare(maxAmount, product.maxAmount) == 0 &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, minAmount, maxAmount);
    }
}
