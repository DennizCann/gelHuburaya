package com.example.ecommerce;

public class Sale {
    private int id;
    private String customerName;
    private String product;
    private double payment;

    // Constructor
    public Sale(int id, String customerName, String product, double payment) {
        this.id = id;
        this.customerName = customerName;
        this.product = product;
        this.payment = payment;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProduct() {
        return product;
    }

    public double getPayment() {
        return payment;
    }

    // Override toString() method to print sale details
    @Override
    public String toString() {
        return "Sale [id=" + id + ", customerName=" + customerName + ", product=" + product + ", payment=" + payment + "]";
    }
}
