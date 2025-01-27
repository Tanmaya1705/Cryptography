import java.util.Scanner;

public class AffineCipher {
    static final int M = 20; // Modulus for English alphabet

    // Method to find the modular multiplicative inverse of alpha under modulo m
    public static int modularInverse(int alpha, int m) {
        for (int a_inv = 0; a_inv < m; a_inv++) {
            if ((alpha * a_inv) % m == 1) {
                return a_inv;
            }
        }
        return -1; // Inverse doesn't exist
    }

    // Encrypt the plaintext using the Affine Cipher
    public static String encryptAffine(String plaintext, int alpha, int beta) {
        plaintext = plaintext.replaceAll("\\s", "").toUpperCase(); // Remove spaces and convert to uppercase

        StringBuilder ciphertext = new StringBuilder();
        if (plaintext.matches("[A-Z]+") && plaintext.length() >= 1) {
            for (char c : plaintext.toCharArray()) {
                int idx = c - 'A';
                int x = (alpha * idx + beta) % M;
                ciphertext.append((char) ('A' + x));
            }
            return ciphertext.toString();
        } else {
            return "An Error has occurred, please double-check input.";
        }
    }

    // Decrypt the ciphertext using the Affine Cipher
    public static String decryptAffine(String ciphertext, int alpha, int beta) {
        ciphertext = ciphertext.replaceAll("\\s", "").toUpperCase(); // Remove spaces and convert to uppercase

        StringBuilder plaintext = new StringBuilder();
        if (ciphertext.matches("[A-Z]+") && ciphertext.length() >= 1) {
            int a_inv = modularInverse(alpha, M); // Get modular inverse of alpha
            if (a_inv == -1) {
                return "Error: Modular inverse does not exist for the given alpha.";
            }

            for (char c : ciphertext.toCharArray()) {
                int idx = c - 'A';
                int x = (a_inv * (idx - beta + M)) % M; // Adjust for negative values with +M
                plaintext.append((char) ('A' + x));
            }
            return plaintext.toString();
        } else {
            return "An Error has occurred, please double-check input.";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Would you like to perform encryption or decryption?\nPlease Enter 'e' or 'd': ");
        String userChoice = scanner.nextLine().toLowerCase();

        if (!userChoice.equals("e") && !userChoice.equals("d")) {
            System.out.println("Invalid input, please enter 'e' for encryption or 'd' for decryption.");
            scanner.close();
            return;
        }

        try {
            System.out.print("Enter an alpha value between 1 and " + (M - 1) + ": ");
            int alpha = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter a beta value between 0 and " + (M - 1) + ": ");
            int beta = Integer.parseInt(scanner.nextLine());

            if (alpha < 1 || alpha > M - 1 || beta < 0 || beta >= M) {
                System.out.println("Alpha and Beta values are out of valid range.");
                return;
            }

            if (gcd(alpha, M) != 1) {
                System.out.println("The GCD of alpha and " + M + " must be 1.");
                return;
            }

            if (userChoice.equals("e")) { // Perform encryption
                System.out.print("\nPlease enter the plaintext to encrypt: ");
                String plaintext = scanner.nextLine().strip();
                String ciphertext = encryptAffine(plaintext, alpha, beta);
                System.out.println("\nPlaintext (Original): " + plaintext);
                System.out.println("Ciphertext (Encrypted): " + ciphertext);

            } else if (userChoice.equals("d")) { // Perform decryption
                System.out.print("\nPlease enter the ciphertext to decrypt: ");
                String ciphertext = scanner.nextLine().strip();
                String plaintext = decryptAffine(ciphertext, alpha, beta);
                System.out.println("\nCiphertext (Original): " + ciphertext);
                System.out.println("Plaintext (Decrypted): " + plaintext);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input, please enter valid integers.");
        } finally {
            scanner.close();
        }
    }

    // Helper method to calculate the GCD of two numbers
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }
}
