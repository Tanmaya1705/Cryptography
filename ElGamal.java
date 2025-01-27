import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ElGamal {
    static SecureRandom random = new SecureRandom();

    // Generate a large random prime number
    public static BigInteger generatePrime(int bits) {
        return BigInteger.probablePrime(bits, random);
    }

    // Generate public and private keys
    public static KeyPair generateKeyPair(int bits) {
        BigInteger p = generatePrime(bits); // Large prime number
        BigInteger g = new BigInteger(bits - 1, random).mod(p.subtract(BigInteger.ONE)).add(BigInteger.TWO); // Random g
        BigInteger x = new BigInteger(bits - 1, random).mod(p.subtract(BigInteger.TWO)).add(BigInteger.TWO); // Private key x
        BigInteger h = g.modPow(x, p); // Public key h
        return new KeyPair(new PublicKey(p, g, h), new PrivateKey(p, g, x));
    }

    // Encrypt a message
    public static List<BigInteger[]> encrypt(PublicKey publicKey, String plaintext) {
        BigInteger p = publicKey.p;
        BigInteger g = publicKey.g;
        BigInteger h = publicKey.h;

        List<BigInteger[]> ciphertext = new ArrayList<>();
        for (char c : plaintext.toCharArray()) {
            BigInteger m = BigInteger.valueOf((int) c); // Convert character to ASCII value
            BigInteger y = new BigInteger(p.bitLength() - 1, random).mod(p.subtract(BigInteger.TWO)).add(BigInteger.TWO); // Random y
            BigInteger c1 = g.modPow(y, p); // c1 = g^y mod p
            BigInteger c2 = m.multiply(h.modPow(y, p)).mod(p); // c2 = m * h^y mod p
            ciphertext.add(new BigInteger[]{c1, c2});
        }
        return ciphertext;
    }

    // Decrypt a message
    public static String decrypt(PrivateKey privateKey, List<BigInteger[]> ciphertext) {
        BigInteger p = privateKey.p;
        BigInteger x = privateKey.x;

        StringBuilder plaintext = new StringBuilder();
        for (BigInteger[] pair : ciphertext) {
            BigInteger c1 = pair[0];
            BigInteger c2 = pair[1];
            BigInteger m = c2.multiply(c1.modPow(p.subtract(BigInteger.ONE).subtract(x), p)).mod(p); // m = c2 * (c1^(p-1-x)) mod p
            plaintext.append((char) m.intValue());
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Generating ElGamal keys...");
        KeyPair keyPair = generateKeyPair(512);

        PublicKey publicKey = keyPair.publicKey;
        PrivateKey privateKey = keyPair.privateKey;

        System.out.println("Public Key: " + publicKey);
        System.out.println("Private Key: " + privateKey);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Encrypt a message");
            System.out.println("2. Decrypt a message");
            System.out.println("3. Exit");

            String choice = scanner.nextLine();

            if ("1".equals(choice)) {
                System.out.print("\nEnter a message to encrypt: ");
                String message = scanner.nextLine();
                List<BigInteger[]> encryptedMessage = encrypt(publicKey, message);
                System.out.println("\nEncrypted Message: " + encryptedMessage);

            } else if ("2".equals(choice)) {
                System.out.print("\nEnter the encrypted message (format: (c1,c2) (c1,c2) ...): ");
                String encryptedInput = scanner.nextLine();
                encryptedInput = encryptedInput.replaceAll("\\) \\(", ")_("); // Format the input for easier processing
                encryptedInput = encryptedInput.replaceAll("[()]", ""); // Remove parentheses

                String[] pairs = encryptedInput.split("_");
                List<BigInteger[]> encryptedMessage = new ArrayList<>();
                for (String pair : pairs) {
                    String[] values = pair.split(",");
                    BigInteger c1 = new BigInteger(values[0].trim());
                    BigInteger c2 = new BigInteger(values[1].trim());
                    encryptedMessage.add(new BigInteger[]{c1, c2});
                }

                String decryptedMessage = decrypt(privateKey, encryptedMessage);
                System.out.println("\nDecrypted Message: " + decryptedMessage);

            } else if ("3".equals(choice)) {
                System.out.println("\nExiting the program...");
                break;

            } else {
                System.out.println("\nInvalid choice, please try again.");
            }
        }

        scanner.close();
    }
}

// Public key class
class PublicKey {
    BigInteger p, g, h;

    public PublicKey(BigInteger p, BigInteger g, BigInteger h) {
        this.p = p;
        this.g = g;
        this.h = h;
    }

    @Override
    public String toString() {
        return "(p=" + p + ", g=" + g + ", h=" + h + ")";
    }
}

// Private key class
class PrivateKey {
    BigInteger p, g, x;

    public PrivateKey(BigInteger p, BigInteger g, BigInteger x) {
        this.p = p;
        this.g = g;
        this.x = x;
    }

    @Override
    public String toString() {
        return "(p=" + p + ", g=" + g + ", x=" + x + ")";
    }
}

// Key pair class
class KeyPair {
    PublicKey publicKey;
    PrivateKey privateKey;

    public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
