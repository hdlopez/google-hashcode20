package com.withgoogle.alpacas.practice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Recursive solution converted into iteration. It also use a solution "cache" (kind of DM).
 *
 * It works for big solutions but it's still pretty slow
 */
public class Recursive2 {
    static SolutionCache cache;

    static class Solution {
        int slicesQty;
        ArrayList<String> order;

        public Solution() {
            slicesQty = 0;
            order = new ArrayList<>();
        }

        void add(int index, int pizzaSlices) {
            slicesQty += pizzaSlices;
            order.add(String.valueOf(index));
        }

        Solution cloneAdd(int index, int pizzaSlices) {
            Solution s = new Solution();
            s.slicesQty += slicesQty + pizzaSlices;
            s.order.addAll(order);
            s.order.add(String.valueOf(index));
            return s;
        }

        Solution appendSolution(Solution toAppend) {
            slicesQty += toAppend.slicesQty;
            order.addAll(toAppend.order);
            return this;
        }
    }

    static class SolutionCall {
        int maxSlices;
        int index;
        boolean mustAdd;
        Solution solution;

        SolutionCall(int maxSlices, int index, boolean mustAdd) {
            this.maxSlices = maxSlices;
            this.index = index;
            this.mustAdd = mustAdd;
        }

        SolutionCall(int maxSlices, int index, Solution solution) {
            this.maxSlices = maxSlices;
            this.index = index;
            this.solution = solution;
        }
    }

    public static Solution iterativeRecursionSolution(int m, int i, int[] pizzas) {
        Stack<SolutionCall> stack = new Stack<>();

        int index = i;
        int maxSlices = m;
        while (index >= 0) {
            int selectedPizza = pizzas[index];

            Optional<Solution> cachedSolution = cache.getSolution(index, maxSlices);
            if (cachedSolution.isPresent()) {
                stack.push(new SolutionCall(maxSlices, index, cachedSolution.get()));
                break;
            }

            boolean mustAdd = false;
            if (maxSlices - selectedPizza >= 0) {
                maxSlices = maxSlices - selectedPizza;
                mustAdd = true;
            }
            index--;
            stack.push(new SolutionCall(maxSlices, index, mustAdd));
        }

        Solution solution = new Solution();
        while (!stack.empty()) {
            SolutionCall call = stack.pop();
            index = call.index + 1;
            int selectedPizza = pizzas[index];

            if (call.solution != null) {
                solution.appendSolution(call.solution);
                break;
            }

            if (call.mustAdd) {
                solution.add(index, selectedPizza);
            }

            cache.saveSolution(index, call.maxSlices + selectedPizza, solution.cloneAdd(0, 0));
        }

        return solution;
    }

    public static ArrayList<String> solution(int maxSlices, int pizzasQty, int[] pizzas) {
        ArrayList<String> finalOrder = new ArrayList<>();
        if (maxSlices < pizzas[0]) return finalOrder;

        Solution finalSolution = new Solution();

        boolean optimal = false;
        int j = pizzasQty - 1;

        while (!optimal && j != 0) {

            Solution sol = iterativeRecursionSolution(maxSlices, j, pizzas);

            if (sol.order.size() == pizzasQty) {
                // if I am planning to order all the pizzas
                optimal = true;
            }
            if (sol.slicesQty > finalSolution.slicesQty) {
                // if my new solution is better than the previous one
                finalSolution = sol;
            }
            j--;
        }

        return finalSolution.order;
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

        cache = new SolutionCache(pizzasQty);
        Arrays.sort(pizzas);

        ArrayList<String> result = solution(maxSlices, pizzasQty, pizzas);

        bufferedWriter.write(String.valueOf(result.size()));
        bufferedWriter.newLine();
        bufferedWriter.write(String.join(" ", result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }

    static class SolutionCache {
        // one item by pizza type
        SolutionCacheItem[] items;

        SolutionCache(int n) {
            items = new SolutionCacheItem[n];
            for (int i = 0; i < n; i++) {
                items[i] = new SolutionCacheItem();
            }
        }

        void saveSolution(int index, int slices, Solution sol) {
            String sliceStr = String.valueOf(slices);
            if (!items[index].cacheByWeight.containsKey(sliceStr)) {
                items[index].cacheByWeight.put(sliceStr, sol);
            }
        }

        Optional<Solution> getSolution(int index, int slices) {
            String sliceStr = String.valueOf(slices);
            if (items[index].cacheByWeight.containsKey(sliceStr)) {
                return Optional.of(items[index].cacheByWeight.get(sliceStr));
            }

            return Optional.empty();
        }
    }

    static class SolutionCacheItem {
        private String index;
        private HashMap<String, Solution> cacheByWeight;

        SolutionCacheItem() {
            cacheByWeight = new HashMap<>();
        }
    }
}



