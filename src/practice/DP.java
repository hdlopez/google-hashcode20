package practice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Dynamic programming solution. Not finished yet.
 *
 * Have to improve the matrix storage https://stackoverflow.com/questions/390181/sparse-matrices-arrays-in-java
 */
public class DP {

    static void print(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static ArrayList<String> dp1(int maxSlices, int pizzasQty, int[] pizzas) {
        ArrayList<String> finalOrder = new ArrayList<>();

        int n = pizzasQty + 1;
        int W = maxSlices + 1;
        int[][] V = new int[n][W];

        for (int i = 1; i <= pizzasQty; i++) {
            for (int j = 1; j <= maxSlices; j++) {
                int wi = pizzas[i - 1];
                int v1; // not adding object i to the solution
                int v2; // adding object i to the solution

                v1 = V[i - 1][j];

                if (j - wi < 0) {
                    v2 = Integer.MIN_VALUE;
                } else {
                    v2 = V[i - 1][j - wi] + wi;
                }
                V[i][j] = Math.max(v1, v2);
            }
        }

        //print(V);

        int i = pizzasQty;
        int j = maxSlices;
        while (i > 0) {
            int wi = pizzas[i - 1];
            int v = V[i][j];
            if (i - 1 < 0) break;

            int v1 = V[i - 1][j];

            int v2;
            if (j - wi < 0) {
                v2 = Integer.MIN_VALUE;
            } else {
                v2 = V[i - 1][j - wi] + wi;
            }
            if (v == v2) {
                // solution includes pizza type i
                finalOrder.add(0, String.valueOf(i - 1));
                j = j - wi;
            }
            i = i - 1;
        }


        return finalOrder;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int maxSlices = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int pizzasQty = scanner.nextInt();
        int[] pizzas = new int[pizzasQty];
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        String[] pizzaLine = scanner.nextLine().split(" ");
        for (int i = 0; i < pizzaLine.length; i++) {
            pizzas[i] = Integer.parseInt(pizzaLine[i]);
        }

        ArrayList<String> result = dp1(maxSlices, pizzasQty, pizzas);

        bufferedWriter.write(String.valueOf(result.size()));
        bufferedWriter.newLine();
        bufferedWriter.write(String.join(" ", result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
