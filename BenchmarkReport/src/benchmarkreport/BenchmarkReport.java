/*
 * Filename: BenchmarkReport.java
 * Author: John Kaiser
 * Date: 4/4/2020
 * Purpose: CMSC 451 Project 1
 * Produce reports created by BenchmarkMergeSort.java
The second program should produce the report. It should allow the user to select the input file
using JFileChooser. The report should contain one line for each data set size and five columns
and should be displayed using a JTable. The first column should contain the data set size the
second the average of the critical counts for the 50 runs and the third the coefficient of variance
of those 50 values expressed as a percentage. The fourth and fifth column should contain similar
data for the times. The coefficient of variance of the critical operation counts and time
measurement for the 50 runs of each data set size provide a way to gauge the data sensitivity of
the algorithm.
 */
package benchmarkreport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.*;
import java.util.Scanner;

public class BenchmarkReport extends JPanel implements ActionListener {
    JFileChooser fileChooser;
    JLabel welcome, reportName;
    JButton open;
    Scanner scan, lineScan;
    JScrollPane reportPane;
    
    int size, count = 0, timeCount = 0, value = 0;
    double avgCount, avgTime, timeSum = 0, countSum = 0;
    double countDeviation = 0, timeDeviation = 0;
    double countVariance = 0, timeVariance = 0, countVarianceSum = 0, timeVarianceSum = 0;
    long timeValue = 0;
    
    String countCoef, timeCoef, line;
    //2D array to hold data for report table
    Object[][] data = {
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef},
        {size, avgCount, countCoef, avgTime, timeCoef}
            
    };
    
    String[] columnNames = {"Size", "Avg Count", "Coef Count", "Avg Time", "Coef Time"};
    JTable report = new JTable(data, columnNames);
    
    public BenchmarkReport() {
        super(new BorderLayout(0, 10));
        //Set directory for FileChooser, Reports folder one level up
        fileChooser = new JFileChooser("../Reports");
        open = new JButton("Open Report");
        //Add action listener for open button
        open.addActionListener(this);
        report.setPreferredScrollableViewportSize(new Dimension(400, 70));
        report.setFillsViewportHeight(true);
        //show report table grid lines, make them black
        report.setShowGrid(true);
        report.setGridColor(Color.BLACK);
        //Welcome message to click open button
        welcome = new JLabel("Click the open button to select a Benchmark Report");
        //Report name will be determined by selected filename
        reportName = new JLabel();
        //Create the scroll pane and add the table to it
        reportPane = new JScrollPane(report);
        reportPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        reportName.setHorizontalAlignment(JLabel.CENTER);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(welcome, BorderLayout.WEST);
        panel.add(open, BorderLayout.EAST);
        panel.add(reportName, BorderLayout.SOUTH);
        add(panel, BorderLayout.PAGE_START);
        add(reportPane, BorderLayout.CENTER);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(BenchmarkReport.this);
        int i = 0;
        
        if(returnVal == fileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            //Set reportName JLabel according to selected file
            if(file.getName().equals("recursive.txt"))
                reportName.setText("Recursive MergeSort Benchmark Report");
            else
                reportName.setText("Iterative MergeSort Benchmark Report");
            try {
                //Use scanner and filereader to open and scan file
                scan = new Scanner(new FileReader(file));
                
                while(scan.hasNextLine()) {
                    //First integer is data set size
                    data[i][0] = scan.nextInt();
                    //assign next line to String line
                    line = scan.nextLine();
                    lineScan = new Scanner(line);
                    //First while loop gets values of count integers and time longs
                    //Adds them to countSum or timeSum for determining averages, keeps count
                    while(lineScan.hasNext()) {
                        if(lineScan.hasNextInt()) {
                            value = lineScan.nextInt();
                            countSum += value;
                            count++;
                        }
                        if(lineScan.hasNextLong()) {
                            timeValue = lineScan.nextLong();
                            timeSum += timeValue;
                            timeCount++;
                        }
                    } 
                    //Calculate and assign count and time averages
                    avgCount = countSum/count;
                    data[i][1] = avgCount;
                    avgTime = timeSum/timeCount;
                    data[i][3] = avgTime;
                    lineScan = new Scanner(line);
                    //Run the while loop again on the line to get coefficient of variation
                    while(lineScan.hasNext()) {
                        if(lineScan.hasNextInt()) {
                            //Variance is calculated by summing up each (value-mean)^2 then divide by total 
                            //number of values
                            countVariance = Math.pow((lineScan.nextInt()-avgCount), 2);
                            countVarianceSum += countVariance;
                        }
                        if(lineScan.hasNextLong()) {
                            timeVariance = Math.pow((lineScan.nextLong()-avgCount), 2);
                            timeVarianceSum += timeVariance;
                        }
                    } 
                    countVariance = countVarianceSum/count;
                    //Standard deviation is square root of variance
                    countDeviation = Math.sqrt(countVariance);
                    //Coefficient of variation = standard deviataion/average
                    countCoef = String.format("%.2f", countDeviation/avgCount);
                    //assign to correct cell in table
                    data[i][2] = countCoef + "%";
                    timeVariance = timeVarianceSum/timeCount;
                    timeDeviation = Math.sqrt(timeVariance);
                    timeCoef = String.format("%.2f", timeDeviation/avgTime);
                    data[i][4] = timeCoef + "%";
                    //set all counts back to 0
                    count = 0;
                    timeCount = 0;
                    countSum = 0;
                    timeSum = 0;
                    timeVarianceSum = 0;
                    countVarianceSum = 0;
                    //increment i for table assignments
                    i++;
                }
                
                //CLose scanners
                scan.close();
                lineScan.close();
                
            } catch (FileNotFoundException ex) {
                System.out.println("File not found");
            }
        }
    }
    
    public static void showGUI() {
        //Create and show frame
        JFrame frame = new JFrame("Benchmark Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(new BenchmarkReport());
        frame.pack();
        frame.setSize(500, 300);
        frame.setVisible(true);
        
    }


    public static void main(String[] args) {
        //BenchmarkReport test = new BenchmarkReport();
        //test.showGUI();
        showGUI();
    }   
    
}
