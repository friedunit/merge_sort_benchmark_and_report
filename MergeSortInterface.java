/*
 * Filename: MergeSortInterface.java
 * Author: John Kaiser
 * Date: 4/4/2020
 * Purpose: Create interace to be use for MergeSort benchmark
 */
package benchmarkmergesort;

public interface MergeSortInterface {
    public int[] recursiveSort(int[] list);
    public int[] iterativeSort(int[] list);
    public int getCount();
    public long getTime();
}
