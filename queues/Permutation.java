/* *****************************************************************************
 *  Name: Charlie Cruzan
 *  Date: 1/16/19
 *  Description: This program will take an integer, k, as a command line argument,
 *  then reads string from the file given in the CL, and prints exactly k strings from
 *  that file, uniformly at random. It will print each string at MOST one time.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);      // input file
        // int i = 0;

        RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            randomQueue.enqueue(item);
            // i++;
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(randomQueue.dequeue());
        }
    }
}

/*
int k = Integer.parseInt(args[0]);      // input file
        int i = 0;

        RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty() && i < k) {
            String item = StdIn.readString();
            randomQueue.enqueue(item);
            i++;
        }
        for (String s : randomQueue)
            StdOut.println(s);
 */
