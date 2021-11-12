package ru.n_korotkov.oop.calculator;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A class for evaluating mathematical expressions in prefix form.
 */
class Calculator {

    /**
     * A class representing a mathematical operation supported by a <code>Calculator</code>.
     */
    class Op {

        private int arity;
        private Function<double[], Double> func;

        /**
         * Constructs an operation which is calculated using the given function.
         * @param arity the number of arguments of the operation
         * @param func the function calculating the value of the operation for given parameters.
         * The parameters are passed as a <code>double[]</code>.
         */
        public Op(int arity, Function<double[], Double> func) {
            this.arity = arity;
            this.func = func;
        }

        /**
         * Calculates the value of the operation for given parameters.
         * @param args the list of arguments
         * @return the value of the operation
         */
        public double calculate(double[] args) {
            if (args.length != arity) {
                throw new IllegalArgumentException("Incorrect number of arguments");
            }
            return func.apply(args);
        }

    }

    private Map<String, Op> operations = new HashMap<>();

    /**
     * Constructs a <code>Calculator</code> with a set of default operations
     * (+, -, *, /, log, pow, sqrt, sin, cos).
     */
    public Calculator() {
        // addOp(0, "e",    args -> Math.E);
        // addOp(0, "pi",   args -> Math.PI);

        addOp("cos",  1, args -> Math.cos(args[0]));
        addOp("log",  1, args -> Math.log(args[0]));
        addOp("sin",  1, args -> Math.sin(args[0]));
        addOp("sqrt", 1, args -> Math.sqrt(args[0]));

        addOp("-",    2, args -> args[0] - args[1]);
        addOp("*",    2, args -> args[0] * args[1]);
        addOp("/",    2, args -> args[0] / args[1]);
        addOp("+",    2, args -> args[0] + args[1]);
        addOp("pow",  2, args -> Math.pow(args[0], args[1]));
    }

    /**
     * Adds a new operation for this <code>Calculator</code> to recognize.
     * @param opName the name of the operation
     * @param arity the number of arguments of the operation
     * @param func the function calculating the value of the operation for given parameters.
     * The parameters are passed as a <code>double[]</code>.
     */
    public void addOp(String opName, int arity, Function<double[], Double> func) {
        operations.put(opName, new Op(arity, func));
    }

    /**
     * Evaluates the given expression andreturns its value.
     * @param expr the expression to evaluate
     * @return the value of <code>expr</code>
     */
    public double evaluate(String expr) {
        List<String> tokens = Arrays.asList(expr.trim().split(" +"));
        Deque<Double> valueStack = new ArrayDeque<>();

        Collections.reverse(tokens);
        for (String token : tokens) {
            try {
                double value = Double.valueOf(token);
                valueStack.push(value);
            } catch (NumberFormatException e) {
                Op operation = operations.get(token);
                if (operation != null && operation.arity <= valueStack.size()) {
                    double[] args = new double[operation.arity];
                    for (int i = 0; i < operation.arity; i++) {
                        args[i] = valueStack.pop();
                    }
                    valueStack.push(operation.calculate(args));
                } else {
                    throw new IllegalArgumentException("Invalid expression");
                }
            }
        }
        if (valueStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return valueStack.pop();
    }

}
