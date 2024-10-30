package pp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShamirSecretSharing {

    public static void main(String[] args) {
        try {
            // Read JSON input from file
            String content = new String(Files.readAllBytes(Paths.get("C:\\Users\\Prasanna Kumari\\Desktop\\assignment\\input.json")));
            processJson(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processJson(String json) {
        // Regex to find all keys and their corresponding values
        Pattern keyPattern = Pattern.compile("\"(\\d+)\":\\s*\\{([^}]*)\\}");
        Matcher keyMatcher = keyPattern.matcher(json);

        // Extract the value of k from the JSON
        int k = extractK(json);

        while (keyMatcher.find()) {
            String key = keyMatcher.group(1);
            String values = keyMatcher.group(2);
            List<int[]> roots = decodeRoots(values);
            int constantTerm = calculateConstantTerm(roots);
            System.out.println("The constant term for case " + key + " is: " + constantTerm);
        }
    }

    private static int extractK(String json) {
        // Regex to find the value of k
        Pattern kPattern = Pattern.compile("\"k\":\\s*(\\d+)");
        Matcher kMatcher = kPattern.matcher(json);
        if (kMatcher.find()) {
            return Integer.parseInt(kMatcher.group(1));
        }
        return 0; // Default return if not found
    }

    private static List<int[]> decodeRoots(String values) {
        List<int[]> roots = new ArrayList<>();
        Pattern rootPattern = Pattern.compile("\"(\\d+)\":\\s*\\{\\s*\"base\":\\s*(\\d+),\\s*\"value\":\\s*\"([^\"]+)\"\\s*\\}");
        Matcher rootMatcher = rootPattern.matcher(values);

        while (rootMatcher.find()) {
            int base = Integer.parseInt(rootMatcher.group(2));
            String value = rootMatcher.group(3);
            int decodedValue = decodeValue(value, base);
            roots.add(new int[]{Integer.parseInt(rootMatcher.group(1)), decodedValue});
        }
        return roots;
    }

    private static int decodeValue(String value, int base) {
        int decodedValue = 0;
        for (int i = 0; i < value.length(); i++) {
            char digit = value.charAt(value.length() - 1 - i);
            int digitValue = Character.digit(digit, base);
            decodedValue += digitValue * Math.pow(base, i);
        }
        return decodedValue;
    }

    private static int calculateConstantTerm(List<int[]> roots) {
        int constantTerm = 1;
        for (int[] root : roots) {
            constantTerm *= root[1]; // Take the y-values
        }
        return constantTerm; // As it's a product, we don't need (-1)^k
    }
}