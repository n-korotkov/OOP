package ru.n_korotkov.oop.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TreeTest {

    static Tree<String> tree;
    static Tree<String>.Node nodeA, nodeB;

    void assertStreamContent(String elems, Stream<String> stream) {
        assertEquals(elems, stream.collect(Collectors.joining(" ")));
    }

    @BeforeEach
    void initTree() {
        tree = new Tree<>();
        tree.add("R");
        nodeA = tree.add("A"); //      R
        nodeB = tree.add("B"); //     / \
        tree.add(nodeA, "AA"); //    /   \
        tree.add(nodeA, "AB"); //   A     B
        tree.add(nodeB, "BA"); //  / \   / \
        tree.add(nodeB, "BB"); // AA AB BA BB
    }

    @Test
    void sizeTest() {
        assertEquals(7, tree.size());
    }

    @Test
    void clearTest() {
        tree.clear();
        assertEquals(0, tree.size());
    }

    @Test
    void isEmptyTest() {
        assertFalse(tree.isEmpty());
        tree.clear();
        assertTrue(tree.isEmpty());
    }

    @Test
    void containsTest() {
        assertTrue(tree.contains("R"));
        assertTrue(tree.contains("A"));
        assertTrue(tree.contains("AB"));
        assertFalse(tree.contains("AC"));
    }

    @Test
    void leafStreamTest() {
        assertStreamContent("AB BA BB", tree.leafStream().filter((str) -> str.contains("B")));
    }

    @Test
    void dfsStreamTest() {
        assertStreamContent("R A AA AB B BA BB", tree.stream());
    }

    @Test
    void bfsStreamTest() {
        assertStreamContent("R A B AA AB BA BB", tree.bfsStream());
    }

    @Test
    void addTest() {
        tree.add("C");
        assertStreamContent("R A AA AB B BA BB C", tree.stream());
        assertEquals(8, tree.size());

        tree.add(nodeA, "D");
        assertStreamContent("R A AA AB D B BA BB C", tree.stream());
        assertEquals(9, tree.size());

        tree.add(nodeB, "E");
        assertStreamContent("R A AA AB D B BA BB E C", tree.stream());
        assertEquals(10, tree.size());
        /*
                  _________R_________
                 /         |         \
              __A__      __B__        C
             /  |  \    /  |  \
            AA  AB  D  BA  BB  E
        */
    }

    @Test
    void removeTest() {
        assertTrue(tree.remove("AB"));
        
        assertStreamContent("R A AA B BA BB", tree.stream());
        assertEquals(6, tree.size());

        assertTrue(tree.remove("B"));
        assertStreamContent("R A AA", tree.stream());
        assertEquals(3, tree.size());

        assertFalse(tree.remove("C"));
        assertStreamContent("R A AA", tree.stream());
        assertEquals(3, tree.size());
        /*
            R
            |
            A
            |
            AA
        */
    }

    @Test
    void concurrentModificationExceptionTest() {
        assertThrows(ConcurrentModificationException.class, () -> {
            String cat = "";
            for (String s : tree) {
                tree.add("S");
                cat += s;
            }
            System.out.println(cat);
        });
    }

}
