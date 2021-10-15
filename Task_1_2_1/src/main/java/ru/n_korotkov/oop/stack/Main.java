package ru.n_korotkov.oop.stack;

class Main {

    public static void main(String[] args) {
        Stack<Integer> steak1 = new Stack<>(), stack2 = new Stack<>();
        stack2.push(4);
        stack2.push(8);

        steak1.push(2);
        System.out.println(steak1);

        steak1.push(7);
        System.out.println(steak1);

        steak1.pushStack(stack2);
        System.out.println(steak1);

        steak1.pop();
        System.out.println(steak1);

        steak1.popStack(2);
        System.out.println(steak1);

        System.out.printf("steak1.count() == %d%n", steak1.count());

    }

}