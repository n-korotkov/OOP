package ru.n_korotkov.oop.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TreeTest {

    static Tree<String> tree;
    static Tree<String>.Node nodeA, nodeB;

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
        assertEquals(
            "AB BA BB",
            tree.leafStream()
                .filter((str) -> str.contains("B"))
                .collect(Collectors.joining(" "))
        );
    }

    @Test
    void dfsStreamTest() {
        assertEquals("R A AA AB B BA BB", tree.stream().collect(Collectors.joining(" ")));
    }

    @Test
    void bfsStreamTest() {
        assertEquals("R A B AA AB BA BB", tree.bfsStream().collect(Collectors.joining(" ")));
    }

    @Test
    void addTest() {
        tree.add("C");
        assertEquals("R A AA AB B BA BB C", tree.stream().collect(Collectors.joining(" ")));
        assertEquals(8, tree.size());

        tree.add(nodeA, "D");
        assertEquals("R A AA AB D B BA BB C", tree.stream().collect(Collectors.joining(" ")));
        assertEquals(9, tree.size());

        tree.add(nodeB, "E");
        assertEquals("R A AA AB D B BA BB E C", tree.stream().collect(Collectors.joining(" ")));
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
        assertEquals("R A AA B BA BB", tree.stream().collect(Collectors.joining(" ")));
        assertEquals(6, tree.size());

        assertTrue(tree.remove("B"));
        assertEquals("R A AA", tree.stream().collect(Collectors.joining(" ")));
        assertEquals(3, tree.size());

        assertFalse(tree.remove("C"));
        assertEquals("R A AA", tree.stream().collect(Collectors.joining(" ")));
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
