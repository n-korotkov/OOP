package ru.n_korotkov.oop.stack;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A class implementing a LIFO (last-in-first-out) collection.
 */
class Stack<T> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;

    private int size = 0;
    private T[] data;

    private final Class<?> elementType;

    /**
     * Constructs a stack of given capacity.
     * @param type the component type of the new stack
     * @param capacity the capacity of the new stack
     */
    @SuppressWarnings("unchecked")
    public Stack(Class<?> type, int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Given capacity is negative");

        this.elementType = type;
        data = (T[]) Array.newInstance(this.elementType, capacity);
    }

    /**
     * Constructs a stack of default capacity.
     * @param type the component type of the new stack
     */
    public Stack(Class<?> type) {
        this(type, DEFAULT_CAPACITY);
    }

    /**
     * Constructs a stack containing all the elements of <code>sourceCollection</code>.
     * The elements are pushed in the same order as they appear in <code>sourceCollection</code>.
     * @param type the component type of the new stack
     * @param sourceCollection the collection to construct a stack from
     */
    public Stack(Class<?> type, Collection<? extends T> sourceCollection) {
        this(type, sourceCollection.size());
        sourceCollection.toArray(data);
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
    public int size() {
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
        while (newCapacity < size + other.size()) {
            newCapacity = newCapacity * GROW_FACTOR + 1;
        }
        grow(newCapacity);
        System.arraycopy(other.data, 0, data, size, other.size());
        size += other.size();
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

        Stack<T> stack = new Stack<>(elementType, 0);
        stack.data = Arrays.copyOfRange(data, size, size + length);
        stack.size = length;
        return stack;
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(data, size));
    }

}
