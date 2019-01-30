/* *****************************************************************************
 *  Name: Charlie Cruzan
 *  Date: 1/16/19
 *  Description: This program creates a doubly linked list to emulate a stack and
 *  queue rolled into one, meaning this ADT (abstract data type) supports
 *  adding and removing items from either the front or the back of the data
 *  structure.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;    // beginning
    private Node<Item> last;     // end
    private int n;               // number of elements

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }
        Node<Item> oldfirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = oldfirst;
        first.previous = null;
        if (n == 0) {
            last = first;
        }
        else {
            oldfirst.previous = first;
        }
        n++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }
        Node<Item> oldlast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (isEmpty()) first = last;
        else oldlast.next = last;
        n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        Item item = first.item;
        if (n == 1) { // removing last item
            last = null;
            first = null;
        }
        else {
            first = first.next;
            first.previous = null;
        }
        n--;
        return item;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        Item item = last.item;
        if (n == 1) { // removing last item
            last = null;
            first = null;
        }
        else {
            last = last.previous;
            last.next = null;
        }
        n--;
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> queue = new Deque<String>();
        for (int i = 1; i < 11; i++) {
            queue.addLast("item #" + i);
        }
        for (String s : queue)
            StdOut.println(s);
        /* for (int i = 1; i < 11; i++) {
            queue.addFirst("item #" + i);
        }
        for (int i = 1; i < 11; i++) {
            StdOut.println(queue.removeLast());
        }
        for (int i = 1; i < 11; i++) {
            queue.addFirst("item #" + i);
        }
        for (int i = 1; i < 11; i++) {
            StdOut.println(queue.removeFirst());
        } */
        StdOut.println("(" + queue.size() + " left on queue)");
    }
}
