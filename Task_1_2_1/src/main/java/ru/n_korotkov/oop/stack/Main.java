package ru.n_korotkov.oop.stack;

class Main {

    public static void main(String[] args) {
        Stack<Integer> stack1 = new Stack<>(Integer.class), stack2 = new Stack<>(Integer.class);
        stack2.push(4);
        stack2.push(8);

        stack1.push(2);
        System.out.println(stack1);

        stack1.push(7);
        System.out.println(stack1);

        stack1.pushStack(stack2);
        System.out.println(stack1);

        stack1.pop();
        System.out.println(stack1);

        stack1.popStack(2);
        System.out.println(stack1);

        System.out.printf("stack1.size() == %d%n", stack1.size());

    }

}