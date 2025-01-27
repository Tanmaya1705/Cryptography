import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ExtendedEuclideanStep {
    int a, b, remainder, quotient, x, y;

    public ExtendedEuclideanStep(int a, int b, int remainder, int quotient, int x, int y) {
        this.a = a;
        this.b = b;
        this.remainder = remainder;
        this.quotient = quotient;
        this.x = x;
        this.y = y;
    }
}

public class ExtendedEuclidean {
    public static List<ExtendedEuclideanStep> extendedGCD(int a, int b) {
        List<ExtendedEuclideanStep> steps = new ArrayList<>();
        int x0 = 1, x1 = 0, y0 = 0, y1 = 1;

        while (b != 0) {
            int quotient = a / b;
            int remainder = a % b;

            steps.add(new ExtendedEuclideanStep(a, b, remainder, quotient, x1, y1));

            
            a = b;
            b = remainder;

            int tempX = x1;
            x1 = x0 - quotient * x1;
            x0 = tempX;

            int tempY = y1;
            y1 = y0 - quotient * y1;
            y0 = tempY;
        }

        steps.add(new ExtendedEuclideanStep(a, b, a, 0, x0, y0));

        return steps;
    }

    public static void printSteps(List<ExtendedEuclideanStep> steps) {
        System.out.printf("%-6s%-6s%-6s%-10s%-10s%-6s%-6s%n", "Step", "a", "b", "a % b", "Quotient", "x", "y");

        for (int i = 0; i < steps.size(); i++) {
            ExtendedEuclideanStep step = steps.get(i);
            String quotient = step.quotient != 0 ? String.valueOf(step.quotient) : "N/A";
            System.out.printf("%-6d%-6d%-6d%-10d%-10s%-6d%-6d%n", i + 1, step.a, step.b, step.remainder, quotient, step.x, step.y);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a: ");
        int a = scanner.nextInt();

        System.out.print("Enter b: ");
        int b = scanner.nextInt();

        List<ExtendedEuclideanStep> steps = extendedGCD(a, b);

        printSteps(steps);

        int gcd = steps.get(steps.size() - 1).a;
        int x = steps.get(steps.size() - 1).x;
        int y = steps.get(steps.size() - 1).y;

        System.out.println("\nGCD(" + a + ", " + b + ") = " + gcd);
        System.out.println("Coefficients: x = " + x + ", y = " + y);

        scanner.close();
    }
}

