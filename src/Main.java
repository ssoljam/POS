import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static final int MAX_ITEMS_COUNT = 100;
    private static final int MAX_NAME_LENGTH = 20;
    public static double discountValue = 1;
    private static final String NO_DISCOUNT_MESSAGE = "N/A";
    private static final Scanner scanner = new Scanner(System.in);

    static class Item {
        String name;
        double value;
        double quantity;

        public Item(String name, double value, double quantity) {
            this.name = name.length() > MAX_NAME_LENGTH ? name.substring(0, MAX_NAME_LENGTH) : name;
            this.value = value;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        boolean done = false;

        while (!done) {
            ArrayList<Item> items = new ArrayList<>();
            boolean itemsEntered = false;

            System.out.println("Point of Sale System v1.0");

            while (true) {
                System.out.print("Enter item name (type 'DONE' to finish entering items): ");
                String itemName = getStringInput().toUpperCase();

                if (itemName.equals("DONE")) {
                    if (!itemsEntered) {
                        System.out.print("No items entered. Do you want to exit? (Y/N): ");
                        char exitConfirmation = checkInput();
                        if (exitConfirmation == 'Y') {
                            System.out.println("Exiting the program.");
                            return;
                        }
                        continue;
                    }
                    break;
                }

                System.out.print("Enter item value for " + itemName + ": ");
                double itemValue = checkDouble();

                System.out.print("Enter item quantity for " + itemName + ": ");
                double itemQuantity = checkDouble();

                items.add(new Item(itemName, itemValue, itemQuantity));
                itemsEntered = true;

                if (items.size() >= MAX_ITEMS_COUNT) {
                    System.out.println("Maximum number of items reached. Exiting input.");
                    break;
                }
            }

            double subTotal = calculateSubTotal(items);
            System.out.print("Is there a discount? (Y/N): ");
            char discounted = checkInput();

            if (discounted == 'Y') {
                System.out.print("How much percentage is the discount?: ");
                discountValue = checkDouble() * 0.01;
            }

            double grandTotal = discounted == 'Y' ? subTotal - (subTotal * discountValue) : subTotal;
            System.out.println("Total amount " + (discounted == 'Y' ? "after discount is: " : "is: ") + grandTotal);

            double cashGiven;
            while (true) {
                System.out.print("Enter cash amount: ");
                cashGiven = checkDouble();
                if (cashGiven >= grandTotal) break;
                System.out.println("Cash given is lower than total. Please try again.");
            }

            double changeToBeGiven = cashGiven - grandTotal;
            System.out.println("Change is: " + changeToBeGiven);

            printReceipt(items, subTotal, grandTotal, cashGiven, changeToBeGiven, discounted == 'Y');

            System.out.print("Do you want to enter a new transaction? (Y/N): ");
            char newTransaction = checkInput();
            if (newTransaction == 'N') {
                System.out.println("Thank you for using Point of Sale System v1.0!");
                done = true;
            }
        }
    }

    private static double calculateSubTotal(ArrayList<Item> items) {
        double total = 0;
        for (Item item : items) {
            total += item.value * item.quantity;
        }
        return total;
    }

    private static char checkInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim().toUpperCase();
                if (input.length() == 1 && (input.charAt(0) == 'Y' || input.charAt(0) == 'N')) {
                    return input.charAt(0);
                }
                System.out.print("Invalid input. Please enter Y/N: ");
            } catch (Exception e) {
                System.out.print("Invalid input. Please enter Y/N: ");
                scanner.nextLine();
            }
        }
    }

    private static double checkDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= 0) return value;
                System.out.print("Invalid input. Please enter a valid positive number: ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid positive number: ");
            }
        }
    }

    private static String getStringInput() {
        while (true) {
            try {
                return scanner.nextLine().trim();
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
            }
        }
    }

    private static void printReceipt(ArrayList<Item> items, double subTotal, double grandTotal,
                                     double cashGiven, double change, boolean discounted) {
        System.out.println("--------------------------------------------");
        System.out.printf("%-20s%-7s%-10s%-10s%n", "Item", "Qty", "Price", "Total");
        System.out.println("--------------------------------------------");

        for (Item item : items) {
            double total = item.value * item.quantity;
            System.out.printf("%-20s%-7.2f%-10.2f%-10.2f%n",
                    item.name, item.quantity, item.value, total);
        }

        System.out.println("--------------------------------------------");
        System.out.printf("%-37s%-10.2f%n", "Subtotal", subTotal);
        System.out.printf("%-37s%-10s%n", "Discount", discounted ? discountValue * 100 : NO_DISCOUNT_MESSAGE);
        System.out.println("--------------------------------------------");
        System.out.printf("%-37s%-10.2f%n", "Grand Total", grandTotal);
        System.out.println("--------------------------------------------");
        System.out.printf("%-37s%-10.2f%n", "Cash Given", cashGiven);
        System.out.printf("%-37s%-10.2f%n", "Change", change);
        System.out.println("--------------------------------------------");
    }
}