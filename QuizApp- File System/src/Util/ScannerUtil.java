package Util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ScannerUtil {

    public static int nextInt(Scanner sc, String msg) {
        while (true) {
            try {
                System.out.print(msg);
                String input = sc.nextLine().trim();
                int num = Integer.parseInt(input);
                return num;
            } catch ( InputMismatchException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    public static String nextLine(Scanner sc, String msg) {
        while (true) {
            try {
                System.out.print(msg);
                String input = sc.nextLine();
                if (input.trim().isEmpty()) {
                    System.out.println("Empty input. Please enter something.");
                    continue;
                }
                return input;
            } catch (Exception e) {
                System.out.println("Error reading input. Try again.");
            }
        }
    }
}
