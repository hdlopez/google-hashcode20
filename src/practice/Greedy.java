package practice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Greedy solution
 */
public class Greedy {
    public static ArrayList<String> greedySolution(int maxSlices, int pizzasQty, int[] pizzas) {
        ArrayList<String> finalOrder = new ArrayList<>();
        if (maxSlices < pizzas[0]) return finalOrder;

        int previousOrderQty = 0;

        boolean optimal = false;
        int j = pizzasQty - 1;

        while (!optimal && j != 0) {
            // greedy loop

            ArrayList<String> order = new ArrayList<>();
            int orderQty = 0;
            for (int i = j; i >= 0; i--) {
                if (orderQty + pizzas[i] <= maxSlices) {
                    orderQty += pizzas[i];
                    order.add(0, String.valueOf(i));
                }

                if (orderQty == maxSlices) {
                    optimal = true;
                    break;
                }
            }
            if (order.size() == pizzasQty) {
                // if I am planning to order all the pizzas
                optimal = true;
            }
            if (orderQty > previousOrderQty) {
                // if my new solution is better than the previous one
                previousOrderQty = orderQty;
                finalOrder = order;
            }
            j--;
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
        Arrays.sort(pizzas);

        ArrayList<String> result = greedySolution(maxSlices, pizzasQty, pizzas);

        bufferedWriter.write(String.valueOf(result.size()));
        bufferedWriter.newLine();
        bufferedWriter.write(String.join(" ", result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }
}
