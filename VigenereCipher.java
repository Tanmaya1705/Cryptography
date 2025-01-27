import java.util.*;

public class VigenereCipher {

    public static List<String> generateKeys(String key, char missingChar, int length) {
        int charLen = (int) key.chars().filter(c -> c == missingChar).count();
        String keyPiece = key.substring(0, length - charLen);

        List<String> generatedKeys = new ArrayList<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        
        generateCombinations(alphabet, "", charLen, keyPiece, generatedKeys);

        return generatedKeys;
    }

    private static void generateCombinations(char[] alphabet, String prefix, int remaining, String keyPiece, List<String> generatedKeys) {
        if (remaining == 0) {
            generatedKeys.add(keyPiece + prefix);
            return;
        }
        for (char c : alphabet) {
            generateCombinations(alphabet, prefix + c, remaining - 1, keyPiece, generatedKeys);
        }
    }

    public static String vigenere(String text, String key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        char[] code = text.toUpperCase().toCharArray();
        char[] keyArray = key.toUpperCase().toCharArray();
        int j = 0;

        for (int i = 0; i < code.length; i++) {
            char currentChar = code[i];
            if (Character.isAlphabetic(currentChar)) {
                char keyChar = keyArray[(i + j) % keyArray.length];
                int offset = encrypt ? (currentChar + keyChar - 2 * 'A') % 26 : (currentChar - keyChar + 26) % 26;
                result.append((char) ('A' + offset));
            } else {
                result.append(currentChar);
                j--;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Vigenère cipher");

        System.out.print("Encrypt or Decrypt (e/d): ");
        String mode = scanner.nextLine().toLowerCase();
        boolean encrypt = mode.equals("e");

        if (encrypt) {
            System.out.print("Enter the text: ");
            String text = scanner.nextLine().toUpperCase();
            System.out.print("Enter the key: ");
            String key = scanner.nextLine().toUpperCase();

            System.out.println("Encrypted Text: " + vigenere(text, key, true));

        } else {
            System.out.print("Enter the text: ");
            String text = scanner.nextLine().toUpperCase();

            System.out.print("Do you have the key (y/n)? ");
            String hasKey = scanner.nextLine().toLowerCase();

            if (hasKey.equals("y")) {
                System.out.print("Enter the key: ");
                String key = scanner.nextLine().toUpperCase();

                System.out.println("Decrypted Text: " + vigenere(text, key, false));

            } else {
                System.out.print("Enter a part of the key or length (1 for partial key, 2 for length, or nothing): ");
                String option = scanner.nextLine();

                if (option.equals("1")) {
                    System.out.print("Enter the partial key (use '?' for missing letters): ");
                    String partialKey = scanner.nextLine().toUpperCase();
                    List<String> keys = generateKeys(partialKey, '?', partialKey.length());

                    for (String k : keys) {
                        System.out.println("For key " + k + " ==> " + vigenere(text, k, false));
                    }

                } else if (option.equals("2")) {
                    System.out.print("Enter the length: ");
                    int length = scanner.nextInt();
                    scanner.nextLine(); 

                    char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
                    Random random = new Random();

                    while (true) {
                        StringBuilder generatedKey = new StringBuilder();
                        for (int i = 0; i < length; i++) {
                            generatedKey.append(alphabet[random.nextInt(alphabet.length)]);
                        }

                        System.out.println("For generated key " + generatedKey + " = " + vigenere(text, generatedKey.toString(), false));

                        System.out.print("Continue (y/n)? ");
                        if (scanner.nextLine().equalsIgnoreCase("n")) {
                            break;
                        }
                    }

                } else {
                    System.out.println("Sorry, this script cannot find your encrypted text without sufficient input.");
                }
            }
        }

        scanner.close();
    }
}

