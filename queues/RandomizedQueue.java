/* *****************************************************************************
 *  Name: Charlie Cruzan
 *  Date: 1/16/19
 *  Description: This program creates a queue, except that the item dequeued is
 *  chosen uniformly at random from all items in the data structure. the iterator
 *  will iterate through all of the items in a uniformly random order, which is
 *  achieved through Knuth's shuffle, StdRandom.shuffle()
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] randQueue;         // array of items
    private int n;                    // number of elements in queue
    private int first;                // index of first queue element
    private int last;                 // next available index on queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        randQueue = (Item[]) new Object[2];
        n = 0;
        first = 0;
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = randQueue[(first + i) % randQueue.length];
        }
        randQueue = temp;
        first = 0;
        last = n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue null item.");
        }
        randQueue[last] = item;
        last++;
        n++;
        if (n == randQueue.length) {
            resize(2 * randQueue.length);
        }
        // wrap-around and re orient the array so it begins on index 0
        if (last == randQueue.length) {
            resize(randQueue.length);
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        int randIndex = first + StdRandom.uniform(n);
        Item randItem = randQueue[randIndex];
        randQueue[randIndex] = randQueue[first]; // replace chosen item with last item in array
        randQueue[first] = null;        // avoid loitering
        n--;
        first++;

        // shrink size of array if necessary
        if (n > 0 && n == (randQueue.length / 4)) {
            resize(randQueue.length / 2);
        }
        return randItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty.");
        Item item = randQueue[first + StdRandom.uniform(n)];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        private Item[] shuffledQueue = (Item[]) new Object[n];

        public ArrayIterator() {
            for (int j = 0; j < n; j++) {
                shuffledQueue[j] = randQueue[(first + j) % randQueue.length];
            }
            StdRandom.shuffle(shuffledQueue);
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = shuffledQueue[i];
            i++;
            return item;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        for (int i = 1; i < 500; i++) {
            if (StdRandom.uniform(2) > 0) {
                StdOut.println("queueing item #" + i);
                queue.enqueue("item #" + i);
            }
            else {
                if (!queue.isEmpty()) {
                    StdOut.println(queue.dequeue());
                }
            }

        }
        /*
        for (String s : queue)
            StdOut.println(s);
        for (int i = 1; i < 11; i++) {
            StdOut.println(queue.dequeue());
        }*/
    }
}
