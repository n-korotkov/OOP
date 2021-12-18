package ru.n_korotkov.oop.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.assertj.core.util.Streams;

public class Tree<T> implements Iterable<T> {

    public class Node {

        private T value;
        private List<Node> children = new ArrayList<>();

        /**
         * Constructs a node containing the specified value.
         * @param val
         */
        private Node(T val) {
            value = val;
        }

        /**
         * Adds the specified value to the list of this node's children.
         * @param e the value to add
         * @return the added child node
         */
        private Node addChild(T e) {
            Node newNode = new Node(e);
            children.add(newNode);
            return newNode;
        }

        /**
         * Removes the first occurence of the specified value from the sub-tree
         * of this node, if it is present. All its children are also removed.
         * @param e the value to remove
         * @return the number of nodes removed
         */
        private int removeChild(T e) {
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                if (Objects.equals(child.value, e)) {
                    int sizeRemoved = child.size();
                    children.remove(i);
                    return sizeRemoved;
                }
                int sizeRemoved = child.removeChild(e);
                if (sizeRemoved > 0) {
                    return sizeRemoved;
                }
            }
            return 0;
        }

        /**
         * Returns the number of elements in the sub-tree of this node.
         * @return the size of this node's subtree
         */
        private int size() {
            return children.stream().mapToInt((c) -> c.size()).sum() + 1;
        }

    }

    private Node root = null;
    private int size = 0;
    private int modCount = 0;

    /**
     * Adds the specified element into this tree. If the tree is empty, then
     * the added element is the new root node. Otherwise, it is a child node of
     * the root.
     * @param e the element to add
     * @return the added node of this tree
     */
    public Node add(T e) {
        modCount++;
        size++;
        if (size == 1) {
            root = new Node(e);
            return root;
        } else {
            return root.addChild(e);
        }
    }

    /**
     * Adds the specified element into this tree as a child of the given node.
     * @param node the node to add a child element to
     * @param e the element to add
     * @return the added node of this tree
     */
    public Node add(Node node, T e) {
        modCount++;
        size++;
        return node.addChild(e);
    }

    /**
     * Removes the first (according to the tree's iterator) occurence of the
     * specified element from this tree, if it is present. All its children
     * nodes are also removed.
     * @param e the element to add
     * @return <code>true</code> if this tree was modified
     */
    public boolean remove(T e) {
        modCount++;
        if (Objects.equals(root.value, e)) {
            root = null;
            size = 0;
            return true;
        } else {
            int sizeRemoved = root.removeChild(e);
            size -= sizeRemoved;
            return sizeRemoved > 0;
        }
    }

    /**
     * Removes all elements from this tree.
     */
    public void clear() {
        modCount++;
        root = null;
        size = 0;
    }

    /**
     * Checks if this tree contains the specified element.
     * @param e the element to search for
     * @return <code>true</code> if this tree contains <code>e</code>
     */
    public boolean contains(T e) {
        return stream().anyMatch((x) -> Objects.equals(x, e));
    }

    /**
     * Checks if this tree is empty.
     * @return <code>true</code> if this tree is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in this tree.
     * @return the size of this tree
     */
    public int size() {
        return size;
    }

    private abstract class TreeIterator<S> implements Iterator<S> {
        Deque<Node> deque;
        int expectedModCount = modCount;

        public TreeIterator() {
            deque = new ArrayDeque<>();
            if (root != null) {
                deque.add(root);
            }
        }

        void checkModCount() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            return !deque.isEmpty();
        }
    }

    /**
     * Returns an iterator over the elements of this tree, traversing the tree
     * in depth-first order.
     * @return the DFS iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new TreeIterator<T>() {

            @Override
            public T next() {
                checkModCount();
                Node topNode = deque.removeFirst();
                var it = topNode.children.listIterator(topNode.children.size());
                while (it.hasPrevious()) {
                    deque.addFirst(it.previous());
                }
                return topNode.value;
            }
            
        };
    }

    /**
     * Returns an iterator over the elements of this tree, traversing the tree
     * in breadth-first order.
     * @return the BFS iterator
     */
    public Iterator<T> bfsIterator() {
        return new TreeIterator<T>() {

            @Override
            public T next() {
                checkModCount();
                Node topNode = deque.removeFirst();
                deque.addAll(topNode.children);
                return topNode.value;
            }
            
        };
    }

    /**
     * Returns an iterator over the leaf nodes of this tree.
     * @return the leaf node iterator
     */
    public Iterator<T> leafIterator() {
        return new TreeIterator<T>() {

            @Override
            public T next() {
                checkModCount();
                Node topNode;
                do {
                    topNode = deque.removeFirst();
                    var it = topNode.children.listIterator(topNode.children.size());
                    while (it.hasPrevious()) {
                        deque.addFirst(it.previous());
                    }
                } while (!topNode.children.isEmpty());
                return topNode.value;
            }
            
        };
    }

    /**
     * Returns a stream containing the elements of this tree in depth-first
     * order.
     * @return the DFS stream
     */
    public Stream<T> stream() {
        return Streams.stream(iterator());
    }

    /**
     * Returns a stream containing the elements of this tree in breadth-first
     * order.
     * @return the BFS stream
     */
    public Stream<T> bfsStream() {
        return Streams.stream(bfsIterator());
    }

    /**
     * Returns a stream containing the leaf elements of this tree.
     * @return the leaf stream
     */
    public Stream<T> leafStream() {
        return Streams.stream(leafIterator());
    }

}
