import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SplayTree<E> extends AbstractSet<E> implements Set<E> {
    private int size = 0;
    public Node<E> root = null;

    private class Node<T> {
        T element;
        Node<T> parent, left, right;

        Node(T element) {
            this.element = element;
            this.parent = this.left = this.right = null;
        }

        Node(T element, Node<T> parent, Node<T> left, Node<T> right) {
            this.element = element;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            Node<E> result = search(root, null);
            if (result == null) return false;
            else return result.element == null;
        }
        else try {
            return o.equals(search(root, (E) o));
        }
        catch (ClassCastException useless) {
            return false;
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            Stack<Node<E>> trace = new Stack<>();
            Node<E> returnedNext = null;
            {
                if (root == null) throw new NoSuchElementException();
                trace.push(root);
            }

            @Override
            public boolean hasNext() {
                return !trace.isEmpty();
            }

            @Override
            public E next() {
                while (trace.peek().left != null) trace.push(trace.peek().left);
                returnedNext = trace.pop();
                if (returnedNext.right != null) trace.push(returnedNext.right);
                return returnedNext.element;
            }

            @Override
            public void remove() {
                SplayTree.this.remove(returnedNext);
            }
        };
    }

    @Override
    public boolean add(E e) {
        Node<E> inputPlace = search(root, e);
        if (inputPlace == null) {
            root = new Node<>(e);
            size = size == Integer.MAX_VALUE ? Integer.MAX_VALUE : size + 1;
            return true;
        }
        else if (inputPlace.element == e) {
            return false;
        }
        Node<E> newNode = new Node<>(e);
        size = size == Integer.MAX_VALUE ? Integer.MAX_VALUE : size + 1;
        /*This unchecked cast exists in TreeSet. I will not fix it. */
        int cmp = ((Comparable<? super E>)inputPlace.element).compareTo(e);
        if (cmp < 0) {
            newNode.parent = inputPlace.parent;
            newNode.left = inputPlace;
            newNode.right = inputPlace.right;
            if (inputPlace.right == null) inputPlace.parent = newNode;
            else inputPlace.right.parent = newNode;
            inputPlace.right = null;
            return true;
        }
        else {
            newNode.parent = inputPlace.parent;
            newNode.left = inputPlace.left;
            newNode.right = inputPlace;
            if (inputPlace.left == null) inputPlace.parent = newNode;
            else inputPlace.left.parent = newNode;
            inputPlace.left = null;
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (root == null) return false;
        E element = (E) o;
        if (o.equals(search(root, element))) {
            size = size == Integer.MAX_VALUE ? Integer.MAX_VALUE : size - 1;
            if (root.left == null) {
                root.right.parent = null;
                root = root.right;
                return true;
            }
            if (root.right == null) {
                root.left.parent = null;
                root = root.left;
                return true;
            }
            Node<E> left = root.left;
            Node<E> right = root.right;
            root = null;
            left.parent = null;
            left = search(left, right.element);
            assert left != null;
            left.right = right;
            right.parent = left;
            return true;
        }
        else return false;
    }

    private Node<E> splay(Node<E> node) {
        if (node.parent == null) return node;

        if (node.parent.parent == null) {
            rotate(node, node.parent);
            return node;
        }
        else {
            if ((node.parent.parent.right == node.parent) == (node.parent.right == node)) {
                rotate(node.parent, node.parent.parent);
                rotate(node, node.parent);
            }
            else {
                rotate(node, node.parent);
                rotate(node, node.parent.parent);

            }
            return splay(node);
        }
    }

    private void rotate(Node<E> child, Node<E> parent) {
        if (parent.parent != null) {
            if (parent.parent.right == parent) parent.parent.right = child;
            else parent.parent.left = child;
        }

        child.parent = parent.parent;
        parent.parent = child;
        if (parent.right == child) {
            parent.right = child.left;
            if (child.left != null) child.left.parent = parent;
            child.left = parent;
        }
        else {
            parent.left = child.right;
            if (child.right != null) child.right.parent = parent;
            child.right = parent;
        }
    }

    private Node<E> search(Node<E> node, E element) {
        while (true) {
            if (node == null) return null;
            if (node.element == element) return node;
            int cmp = ((Comparable<? super E>) node.element).compareTo(element);
            if ((cmp < 0) && (node.left != null)) {
                node = node.left;
                continue;
            }
            if (cmp > 0 && node.right != null) {
                node = node.right;
                continue;
            }
            return splay(node);
        }
    }


}
