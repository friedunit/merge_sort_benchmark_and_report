/*
 * Filename: UnsortedException.java
 * Author: John Kaiser
 * Date: 4/4 2020
 * Purpose: Unsorted Exception to be thrown if output array is not sorted
 */
package benchmarkmergesort;

public class UnsortedException extends Exception {

    /**
     * Creates a new instance of <code>UnsortedException</code> without detail
     * message.
     */
    public UnsortedException() {
    }

    /**
     * Constructs an instance of <code>UnsortedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UnsortedException(String msg) {
        super(msg);
    }
}
