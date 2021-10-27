package ru.n_korotkov.oop.calculator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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
        private Function<List<Double>, Double> func;

        /**
         * Constructs an operation which is calculated using the given function.
         * @param arity the number of arguments of the operation
         * @param func the function calculating the value of the operation for given parameters.
         * The parameters are passed as a <code>List&lt;Double&gt;</code>.
         */
        public Op(int arity, Function<List<Double>, Double> func) {
            this.arity = arity;
            this.func = func;
        }

        /**
         * Calculates the value of the operation for given parameters.
         * @param args the list of arguments
         * @return the value of the operation
         */
        public double calculate(List<Double> args) {
            if (args.size() != arity) {
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

        addOp(1, "cos",  args -> Math.cos(args.get(0)));
        addOp(1, "log",  args -> Math.log(args.get(0)));
        addOp(1, "sin",  args -> Math.sin(args.get(0)));
        addOp(1, "sqrt", args -> Math.sqrt(args.get(0)));

        addOp(2, "-",    args -> args.get(0) - args.get(1));
        addOp(2, "*",    args -> args.get(0) * args.get(1));
        addOp(2, "/",    args -> args.get(0) / args.get(1));
        addOp(2, "+",    args -> args.get(0) + args.get(1));
        addOp(2, "pow",  args -> Math.pow(args.get(0), args.get(1)));
    }

    /**
     * Adds a new operation for this <code>Calculator</code> to recognize.
     * @param arity the number of arguments of the operation
     * @param opName the name of the operation
     * @param func the function calculating the value of the operation for given parameters.
     * The parameters are passed as a <code>List&lt;Double&gt;</code>.
     */
    public void addOp(int arity, String opName, Function<List<Double>, Double> func) {
        operations.put(opName, new Op(arity, func));
    }

    /**
     * Evaluates the given expression andreturns its value.
     * @param expr the expression to evaluate
     * @return the value of <code>expr</code>
     */
    public double evaluate(String expr) {
        List<String> tokens = Arrays.asList(expr.trim().split(" +"));
        Deque<Op> opStack = new ArrayDeque<>();
        Deque<List<Double>> opArgsStack = new ArrayDeque<>();

        // A 'sentinel' operation: when the expression is evaluated, its value is stored as a
        // sole argument of the 'sentinel'. If there is a different number of arguments, then
        // 'expr' is not a valid expression. 'expr' is also invalid if in 'opStack' there remain
        // other operations besides the 'sentinel'.
        opStack.push(new Op(2, args -> 0.0));
        opArgsStack.push(new ArrayList<>());

        for (String token : tokens) {
            try {
                double value = Double.valueOf(token);
                opArgsStack.getFirst().add(value);
            } catch (NumberFormatException e) {
                if (operations.containsKey(token)) {
                    opStack.push(operations.get(token));
                    opArgsStack.push(new ArrayList<>());
                } else {
                    throw new IllegalArgumentException("Invalid expression");
                }
            }
            while (opStack.getFirst().arity == opArgsStack.getFirst().size()) {
                Op operation = opStack.getFirst();
                List<Double> args = opArgsStack.getFirst();
                double result = operation.calculate(args);
                opStack.pop();
                opArgsStack.pop();
                if (opStack.size() == 0) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                opArgsStack.getFirst().add(result);
            }
        }
        if (opArgsStack.getLast().size() != 1 || opStack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return opArgsStack.getFirst().get(0);
    }

}
