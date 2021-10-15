package ru.n_korotkov.oop.stack;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A class implementing a LIFO (last-in-first-out) collection.
 */
class Stack<T> {

    private final int DEFAULT_CAPACITY = 16;
    private final int GROW_FACTOR = 2;

    private int size = 0;
    private T[] data;

    /**
     * Constructs a stack of default capacity.
     */
    public Stack() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
    }

    /**
     * Constructs a stack of given capacity.
     * @param capacity the capacity of the new stack
     */
    public Stack(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Given capacity is negative");

        data = (T[]) new Object[capacity];
    }

    /**
     * Constructs a stack containing all the elements of <code>sourceCollection</code>.
     * The elements are pushed in the same order as they appear in <code>sourceCollection</code>.
     * @param sourceArray the array to construct a stack from
     */
    public Stack(Collection<? extends T> sourceCollection) {
        data = (T[]) sourceCollection.toArray();
        size = sourceCollection.size();
    }

    /**
     * Returns a list containing all the elements of this stack.
     * @return the list of elements of this stack
     */
    public List<T> toList() {
        return Arrays.asList(Arrays.copyOf(data, size));
    }

    /**
     * Increases the capacity of this stack to <code>newCapacity</code>.
     * If <code>newCapacity</code> is less than current capacity, this stack is unaltered.
     * @param newCapacity the new stack capacity
     */
    private void grow(int newCapacity) {
        if (newCapacity <= data.length)
            return;

        data = Arrays.copyOf(data, newCapacity);
    }

    /**
     * Returns the number of elements in this stack.
     * @return the number of elements in this stack
     */
    public int count() {
        return size;
    }

    /**
     * Pushes a new element onto this stack.
     * @param value the element to push
     */
    public void push(T value) {
        if (size == data.length) {
            grow(data.length * GROW_FACTOR + 1);
        }
        data[size] = value;
        size++;
    }

    /**
     * Pushes the elements of <code>other</code> onto this stack. The elements are pushed in the
     * same order as they appear in <code>other</code>, bottom to top.
     * @param other the stack to push the elements of
     */
    public void pushStack(Stack<T> other) {
        int newCapacity = data.length;
        while (newCapacity < size + other.count()) {
            newCapacity = newCapacity * GROW_FACTOR + 1;
        }
        grow(newCapacity);
        System.arraycopy(other.data, 0, data, size, other.count());
        size += other.count();
    }

    /**
     * Removes the top element from this stack and returns that element.
     * @return the removed element
     */
    public T pop() {
        if (size == 0)
            throw new IllegalStateException("This stack is empty");

        size--;
        return data[size];
    }

    /**
     * Removes the top <code>length</code> elements from this stack and returns a new stack
     * containing those elements. The order of elements in the returned stack is the same as
     * it was in the original stack.
     * @param length the number of elements to remove
     * @return the stack containing the removed elements
     */
    public Stack<T> popStack(int length) {
        if (size < length)
            throw new IllegalStateException("This stack does not contain enough elements");

        size -= length;

        Stack<T> stack = new Stack<>(0);
        stack.data = Arrays.copyOfRange(data, size, size + length);
        stack.size = length;
        return stack;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(data, size));
    }

}
