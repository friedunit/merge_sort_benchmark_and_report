/*
 * Filename: BenchmarkMergeSort.java
 * Author: John Kaiser
 * Date: 4/4/2020
 * Purpose: CMSC 451 Design & Analysis of Computer Algorithms Project 1
 *
 * You must identify some critical operation to count that reflects the overall performance and
modify each version so that it counts that operation. In addition to counting critical operations
you must measure the actual run time in nanoseconds. 
 * 
 * In addition, you should examine the result of each call to verify that the data has been properly
sorted to verify the correctness of the algorithm. If the array is not sorted, an exception should be
thrown.
 * 
 * It should also randomly generate data to pass to the sorting methods. It should produce 50 data
sets for each value of n, the size of the data set and average the result of those 50 runs. The exact
same data must be used for the iterative and the recursive algorithms. It should also create 10
different sizes of data sets.
 *
 *The output files should contain 10 lines that correspond to the 10 data set sizes. The first value
on each line should be the data set size followed by 50 pairs of values. Each pair represents the
critical element count and the time in nanoseconds for each of the 50 runs of that data set size
 *
 */
package benchmarkmergesort;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * Both the recursive and iterative MergeSort algorithms below are modified versions
 * retrieved from: https://www.geeksforgeeks.org/iterative-merge-sort/
 * The website article is contributed by Shivam Agrawal
 */

public class BenchmarkMergeSort implements MergeSortInterface {
    private long startTime;
    private long endTime;
    private int counter = 0;

    @Override
    public int[] recursiveSort(int[] list) {
        //Start recording time for measuring sort
        this.startTime = System.nanoTime();
        return recursiveMerge(list);
    }
    
    public int[] recursiveMerge(int[] list) {
        if(list.length<=1) { 
            return list; 
        } else { 
            int mid = list.length / 2; 
  
            // Split left part 
            int[] left = new int[mid]; 
            System.arraycopy(list, 0, left, 0, mid); 
              
            // Split right part 
            int[] right = new int[list.length - mid]; 
            for(int i = mid; i < list.length; i++) { 
                right[i - mid] = list[i]; 
            } 
            //Continue recursively for both new sides
            recursiveMerge(left); 
            recursiveMerge(right); 
  
            int i = 0, j = 0, k = 0;
            // Merge left and right arrays 
            while(i < left.length && j < right.length) { 
                if(left[i] < right[j]) { 
                    list[k] = left[i]; 
                    //Count critical swap and comparison operations
                    counter++;
                    i++; 
                } 
                else { 
                    list[k] = right[j]; 
                    counter++;
                    j++; 
                } 
                k++; 
            } 
            // Collect remaining elements 
            while(i < left.length) { 
                list[k] = left[i]; 
                counter++;
                i++; 
                k++; 
            } 
            while(j < right.length) { 
                list[k] = right[j]; 
                counter++;
                j++; 
                k++; 
            } 
        } 
        //End time for sort
        this.endTime = System.nanoTime();
        //Check if array is sorted
        try {
            checkSorted(list);
        } catch (UnsortedException ex) {
            System.out.println("Unsorted Exception caught. Array is not sorted");
        }
        return list;
    }
    
    @Override
    public int[] iterativeSort(int[] list) {
        this.startTime = System.nanoTime();
        int n = list.length;
        //For current size of subarrays to be merged curr_size varies from 1 to n/2 
        int curr_size;  
                      
        //For picking starting index of left subarray to be merged 
        int left_start;
        
        //Merge subarrays in bottom up manner. First merge subarrays of size 1 to create sorted  
        //subarrays of size 2, then merge subarrays of size 2 to create sorted subarrays of 
        //size 4, and so on. 
        for (curr_size = 1; curr_size <= n-1; curr_size = 2*curr_size) { 
            //Pick starting point of differentsubarrays of current size 
            for (left_start = 0; left_start < n-1; left_start += 2*curr_size) { 
                //Find ending point of left subarray. mid+1 is starting point of right 
                int mid = Math.min(left_start + curr_size - 1, n-1); 
          
                int right_end = Math.min(left_start + 2*curr_size - 1, n-1); 
          
                //Merge Subarrays arr[left_start...mid] & arr[mid+1...right_end] 
                merge(list, left_start, mid, right_end); 
            } 
        } 
        this.endTime = System.nanoTime();
        //Check to see if array is sorted
        try {
            checkSorted(list);
        } catch (UnsortedException ex) {
            System.out.println("Unsorted Exception caught. Array is not sorted");
        }
        return list;
    } 
      
    //Method to merge the two halves arr[l..m] and arr[m+1..r] of array arr[] 
    public void merge(int[] list, int left, int mid, int right) { 
        int i, j, k; 
        int n1 = mid - left + 1; 
        int n2 = right - mid; 
      
        //Create temp arrays
        int L[] = new int[n1]; 
        int R[] = new int[n2]; 
      
        //Copy data to temp arrays L[] and R[] 
        for (i = 0; i < n1; i++) 
            L[i] = list[left + i]; 
        for (j = 0; j < n2; j++) 
            R[j] = list[mid + 1+ j]; 
      
        //Merge the temp arrays back into arr[l..r]
        i = 0; 
        j = 0; 
        k = left; 
        while (i < n1 && j < n2) { 
            if (L[i] <= R[j]) { 
                list[k] = L[i]; 
                counter++;
                i++; 
            } 
            else { 
                list[k] = R[j]; 
                counter++;
                j++; 
            } 
            k++; 
        } 
      
        //Copy the remaining elements of L[], if there are any 
        while (i < n1) { 
            list[k] = L[i]; 
            counter++;
            i++; 
            k++; 
        } 
      
        //Copy the remaining elements of R[], if there are any 
        while (j < n2) { 
            list[k] = R[j]; 
            counter++;
            j++; 
            k++; 
        }
    }
    
    @Override
    public int getCount() {
        return this.counter;
    }
    
    
    @Override
    public long getTime() {
        return endTime - startTime;
    }
    
    //Method to check if output array is sorted
    public void checkSorted(int[] array) throws UnsortedException {
        boolean sorted;
        for(int i=0;i<array.length-1;i++) {
            //If each i+1 > i, then sorted = true
            sorted = (array[i+1]>=array[i]);
            //else, array is unsorted, throw exception
            if(!sorted) {
                throw new UnsortedException();
            }
        }
        
    }
    
    //Method to warm up JVM
    public static void warmUp() {
        //Create new int array to be sorted
        int[] array = new int[20];
        //Run loop 100,000 times to warm up JVM
        for(int i=0;i<100000;i++) {
            //Create new recursive and iterative objects
            BenchmarkMergeSort r = new BenchmarkMergeSort();
            BenchmarkMergeSort it = new BenchmarkMergeSort();
            //Load up array with random numbers to sort
            for(int k = 0; k < array.length; k++) 
                array[k] = (int) (Math.random() * 100 + 1);
            //Call recursive and iterative sort on array
            r.recursiveSort(array);
            it.iterativeSort(array);
            
        }
    }
    
    //Method to generate output reports
    public static void createReports() throws IOException {
        //Create new recursive and iterative objects
        BenchmarkMergeSort recursive = new BenchmarkMergeSort();
        BenchmarkMergeSort iterative = new BenchmarkMergeSort();

        //Create a new folder called "Reports" one level up
        File reports = new File("../Reports");
        reports.mkdir();
        //Use printwriter and filereader to write to 2 files
        PrintWriter recurse = new PrintWriter(new FileWriter("../Reports/recursive.txt"));
        PrintWriter iterate = new PrintWriter(new FileWriter("../Reports/iterative.txt"));

        //Loop to start at 100, increment by 100 until 1000 data set size
        for(int i = 100; i <= 1000; i+=100) {
            //First print data set size i
            recurse.print(i + " ");
            iterate.print(i + " ");
            //Create new int array
            int[] array = new int[i];
            //Each data set will run 50 times
            for(int j = 0; j < 50; j++) {
                //Loop to fill array with random numbers between 1 and 1000
                for(int k = 0; k < array.length; k++) {
                    array[k] = (int) (Math.random() * 1000 + 1);
                }
                //Call sort methods
                recursive.recursiveSort(array);
                //Print output count and time
                recurse.print(recursive.getCount() + " " + recursive.getTime() + " ");
                iterative.iterativeSort(array);
                iterate.print(iterative.getCount() + " " + iterative.getTime() + " ");
                //Reset counters
                recursive.counter = 0;
                iterative.counter = 0;
            }
            //Write newline
            recurse.printf("\n");
            iterate.printf("\n");
        }
        //Close printwriters
        recurse.close();
        iterate.close();
    }
    
    public static void main(String[] args) throws UnsortedException, IOException {
        warmUp();
        createReports();
    }
    
}
