package com.example.ecommerce;

import java.util.Scanner;

public class ECommerceApp {
    public static void main(String[] args) {
        SaleManager saleManager = new SaleManager();
        saleManager.createTable(); // Ensure the table exists
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Choose an option:");
            System.out.println("1. Add sale");
            System.out.println("2. Delete sale");
            System.out.println("3. Show sales");
            System.out.println("4. Exit");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1:
                    System.out.println("Enter customer name:");
                    String customerName = getStringInput(scanner);
                    System.out.println("Enter product:");
                    String product = getStringInput(scanner);
                    System.out.println("Enter payment:");
                    double payment = getDoubleInput(scanner);
                    saleManager.addSale(customerName, product, payment);
                    break;
                case 2:
                    System.out.println("Enter sale ID to delete:");
                    int idToDelete = getIntInput(scanner);
                    saleManager.deleteSale(idToDelete);
                    break;
                case 3:
                    saleManager.showSales();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        scanner.close();
    }

    private static int getIntInput(Scanner scanner) {
        while (true) {
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                return value;
            } else {
                System.out.println("Invalid input. Please enter an integer:");
                scanner.next(); // Discard invalid input
            }
        }
    }

    private static double getDoubleInput(Scanner scanner) {
        while (true) {
            if (scanner.hasNextDouble()) {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consume the newline character
                return value;
            } else {
                System.out.println("Invalid input. Please enter a valid number:");
                scanner.next(); // Discard invalid input
            }
        }
    }

    private static String getStringInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine();
            if (input.matches("[a-zA-Z\\sğüşıöçĞÜŞİÖÇ]+")) { // Checks if the input contains only letters and spaces, including Turkish characters
                return input;
            } else {
                System.out.println("Invalid input. Please enter a valid string containing only letters:");
            }
        }
    }
}
