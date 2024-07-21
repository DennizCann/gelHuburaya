package com.example.ecommerce;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class SaleManager {
    private Connection connect() {
        String url = "jdbc:sqlite:sales.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS sales (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    customerName TEXT NOT NULL,\n"
                + "    product TEXT NOT NULL,\n"
                + "    payment REAL\n"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSale(String customerName, String product, double payment) {
        String sql = "INSERT INTO sales(customerName, product, payment) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, product);
            pstmt.setDouble(3, payment);
            pstmt.executeUpdate();
            System.out.println("Sale added.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteSale(int id) {
        String sql = "DELETE FROM sales WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Sale deleted.");
                updateSaleIds(); // Update IDs after deletion
            } else {
                System.out.println("Sale with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateSaleIds() {
        String selectSql = "SELECT id, customerName, product, payment FROM sales ORDER BY id";
        String deleteSql = "DELETE FROM sales";
        String insertSql = "INSERT INTO sales(id, customerName, product, payment) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {

            conn.setAutoCommit(false); // Disable auto-commit mode

            // Collect sales data
            List<Sale> sales = new ArrayList<>();
            while (rs.next()) {
                sales.add(new Sale(rs.getString("customerName"), rs.getString("product"), rs.getDouble("payment")));
            }

            // Delete all rows
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.executeUpdate();
            }

            // Insert rows with new IDs
            int newId = 1;
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                for (Sale sale : sales) {
                    insertStmt.setInt(1, newId);
                    insertStmt.setString(2, sale.getCustomerName());
                    insertStmt.setString(3, sale.getProduct());
                    insertStmt.setDouble(4, sale.getPayment());
                    insertStmt.executeUpdate();
                    newId++;
                }
            }

            conn.commit(); // Commit the transaction
            conn.setAutoCommit(true); // Enable auto-commit mode

            System.out.println("Sale IDs updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showSales() {
        String sql = "SELECT id, customerName, product, payment FROM sales";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(
                        "ID: " + rs.getInt("id") +
                                ", Customer: " + rs.getString("customerName") +
                                ", Product: " + rs.getString("product") +
                                ", Payment: " + rs.getDouble("payment")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static class Sale {
        private String customerName;
        private String product;
        private double payment;

        public Sale(String customerName, String product, double payment) {
            this.customerName = customerName;
            this.product = product;
            this.payment = payment;
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
    }
}
