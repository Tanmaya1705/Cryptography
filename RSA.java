import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA {

    private BigInteger n, d, e;
    private int bitLength = 1024;

    // Constructor to generate keys
    public RSA(int bits) {
        this.bitLength = bits;
        SecureRandom random = new SecureRandom();
        
        BigInteger p = BigInteger.probablePrime(bitLength / 2, random);
        BigInteger q = BigInteger.probablePrime(bitLength / 2, random);
        while (p.equals(q)) {
            q = BigInteger.probablePrime(bitLength / 2, random);
        }
        
        n = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        e = BigInteger.probablePrime(bitLength / 2, random);
        while (!e.gcd(phi).equals(BigInteger.ONE)) {
            e = BigInteger.probablePrime(bitLength / 2, random);
        }
        
        d = e.modInverse(phi);
    }

    // Encryption
    public BigInteger[] encrypt(String plaintext) {
        byte[] bytes = plaintext.getBytes();
        BigInteger[] encrypted = new BigInteger[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            encrypted[i] = BigInteger.valueOf(bytes[i]).modPow(e, n);
        }
        return encrypted;
    }

    // Decryption
    public String decrypt(BigInteger[] ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        for (BigInteger c : ciphertext) {
            char decryptedChar = (char) c.modPow(d, n).intValue();
            plaintext.append(decryptedChar);
        }
        return plaintext.toString();
    }

    // Getters for public and private keys
    public String getPublicKey() {
        return "(" + e + ", " + n + ")";
    }

    public String getPrivateKey() {
        return "(" + d + ", " + n + ")";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Generating RSA keys...");
        RSA rsa = new RSA(512); // Generate RSA keys with 512-bit length

        System.out.println("Public Key: " + rsa.getPublicKey());
        System.out.println("Private Key: " + rsa.getPrivateKey());

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Encrypt a message");
            System.out.println("2. Decrypt a message");
            System.out.println("3. Exit");

            System.out.print("\nEnter your choice (1/2/3): ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("\nEnter a message to encrypt: ");
                String message = scanner.nextLine();
                BigInteger[] encryptedMessage = rsa.encrypt(message);

                System.out.println("\nEncrypted Message:");
                for (BigInteger c : encryptedMessage) {
                    System.out.print(c + " ");
                }
                System.out.println();

            } else if (choice.equals("2")) {
                System.out.print("\nEnter the encrypted message (space-separated numbers): ");
                String encryptedInput = scanner.nextLine();
                String[] encryptedNumbers = encryptedInput.split(" ");
                BigInteger[] encryptedMessage = new BigInteger[encryptedNumbers.length];
                for (int i = 0; i < encryptedNumbers.length; i++) {
                    encryptedMessage[i] = new BigInteger(encryptedNumbers[i]);
                }

                String decryptedMessage = rsa.decrypt(encryptedMessage);
                System.out.println("\nDecrypted Message: " + decryptedMessage);

            } else if (choice.equals("3")) {
                System.out.println("\nExiting the program...");
                break;

            } else {
                System.out.println("\nInvalid choice, please try again.");
            }
        }

        scanner.close();
    }
}

