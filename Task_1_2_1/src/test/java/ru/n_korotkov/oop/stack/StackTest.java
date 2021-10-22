package ru.n_korotkov.oop.stack;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StackTest {

    static Stream<Arguments> singleStackParameterProvider() {
        return Stream.of(
            new Integer[]{ 0 },
            new Integer[]{ Integer.MAX_VALUE, Integer.MIN_VALUE },
            new Integer[]{ 1, 4, 9, 16, 25, 36, 49, 64, 81, 100 },
            IntStream.range(0, 128).boxed().toArray(Integer[]::new)

        ).map(arr -> {
            List<Integer> l = new ArrayList<>(Arrays.asList(arr));
            return arguments(l, new Stack<Integer>(Integer.class, l));
        });
    }

    static Stream<Arguments> stackPairParameterProvider() {
        return Stream.of(
            new Integer[][]{
                new Integer[0],
                new Integer[0]
            },
            new Integer[][]{
                new Integer[]{ 1, 2, 3 },
                new Integer[0]
            },
            new Integer[][]{
                new Integer[0],
                new Integer[]{ 9, 8, 7 }
            },
            new Integer[][]{
                IntStream.range(0, 128).boxed().toArray(Integer[]::new),
                new Integer[]{ Integer.MAX_VALUE, Integer.MIN_VALUE }
            }

        ).map(pair -> {
            List<Integer> l1 = new ArrayList<>(Arrays.asList(pair[0]));
            List<Integer> l2 = new ArrayList<>(Arrays.asList(pair[1]));
            return arguments(l1, new Stack<>(Integer.class, l1), l2, new Stack<>(Integer.class, l2));
        });

    }

    @ParameterizedTest
    @MethodSource("singleStackParameterProvider")
    void sizeTest(List<Integer> elements, Stack<Integer> stack) {
        assertEquals(elements.size(), stack.size());
    }

    @ParameterizedTest
    @MethodSource("singleStackParameterProvider")
    void toArrayTest(List<Integer> elements, Stack<Integer> stack) {
        assertIterableEquals(elements, stack.toList());
    }

    @ParameterizedTest
    @MethodSource("singleStackParameterProvider")
    void popTest(List<Integer> elements, Stack<Integer> stack) {
        int lastIndex = elements.size() - 1;
        int lastElement = elements.get(lastIndex);
        int stackLastElement = stack.pop();
        elements.remove(lastIndex);
        
        assertEquals(lastElement, stackLastElement);
        assertEquals(elements.size(), stack.size());
        assertIterableEquals(elements, stack.toList());
    }

    @ParameterizedTest
    @MethodSource("singleStackParameterProvider")
    void pushTest(List<Integer> elements, Stack<Integer> stack) {
        int elem = new Random().nextInt();
        stack.push(elem);
        elements.add(elem);
        
        assertEquals(elements.size(), stack.size());
        assertIterableEquals(elements, stack.toList());
    }

    @ParameterizedTest
    @MethodSource("singleStackParameterProvider")
    void stackPopTest(List<Integer> elements, Stack<Integer> stack) {
        int popSize = elements.size() / 2;
        Stack<Integer> poppedStack = stack.popStack(popSize);
        assertEquals(elements.size() - popSize, stack.size());
        assertEquals(popSize, poppedStack.size());

        List<Integer> remainingElements = elements.subList(0, elements.size() - popSize);
        List<Integer> poppedElements = elements.subList(elements.size() - popSize, elements.size());
        assertIterableEquals(remainingElements, stack.toList());
        assertIterableEquals(poppedElements, poppedStack.toList());
    }

    @ParameterizedTest
    @MethodSource("stackPairParameterProvider")
    void stackPushTest(List<Integer> elements, Stack<Integer> stack, List<Integer> pushedElements, Stack<Integer> pushedStack) {
        stack.pushStack(pushedStack);
        assertEquals(elements.size() + pushedElements.size(), stack.size());

        elements.addAll(pushedElements);
        assertIterableEquals(elements, stack.toList());
    }

    @ParameterizedTest
    @MethodSource("singleStackParameterProvider")
    void stackIteratorTest(List<Integer> elements, Stack<Integer> stack) {
        Iterator<Integer> stackIterator = stack.iterator();
        for (int i = elements.size() - 1; i >= 0; i--) {
            assertTrue(stackIterator.hasNext());
            assertEquals(elements.get(i), stackIterator.next());
        }
        assertFalse(stackIterator.hasNext());
    }

    @Test
    void growTest() {
        int bound = 1000;
        Stack<Integer> stack = new Stack<>(Integer.class, 0);
        for (int i = 0; i < bound; i++) {
            stack.push(i);
        }
        assertEquals(bound, stack.size());
        assertIterableEquals(IntStream.range(0, bound).boxed().collect(Collectors.toList()), stack.toList());
    }

    @Test
    void negativeCapacityStackThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Stack<Object>(Object.class, -1));
    }

    @Test
    void popFromEmptyStackThrows() {
        Stack<Object> stack = new Stack<>(Object.class);
        assertEquals(0, stack.size());
        assertThrows(IllegalStateException.class, stack::pop);
    }

    @Test
    void popStackFromShortStackThrows() {
        Stack<Integer> stack = new Stack<>(Integer.class, Arrays.asList(1, 2, 3));
        assertThrows(IllegalStateException.class, () -> stack.popStack(5));
    }

}
