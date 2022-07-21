package com.naviware;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static int numberOfProcesses;   // total number of processes
    static int temp, aggregate;       //variable for holding temporal values

    static ArrayList<Integer> processes;
    static ArrayList<Integer> arrivalTime;       // the arrival time of processes
    static ArrayList<Integer> burstTime;         // the burst or execution time of processes
    static ArrayList<Integer> completionTime;    // the time at which processes complete execution
    static ArrayList<Integer> turnAroundTime;    // the difference b/n completion time and arrival time
    static ArrayList<Integer> waitingTime;       // the difference between turn around time and burst time

    static float totalWaitTime = 0; // the total time it takes to wait
    static float totalTurnAroundTime = 0;   // the total turn around time

    public static void main(String[] args) {
        jobsFactory();

        //sorting according to arrival times
        for (int i = 0; i < processes.size(); i++) {
            for (int j = 0; j < processes.size() - (i + 1); j++) {
                if (arrivalTime.get(j) > arrivalTime.get(j + 1)) {
                    //set the arrival time in order
                    temp = arrivalTime.get(j);
                    arrivalTime.set(j, arrivalTime.get(j + 1));
                    arrivalTime.set(j + 1, temp);

                    //set the burst time in order
                    temp = burstTime.get(j);
                    burstTime.set(j, burstTime.get(j + 1));
                    burstTime.set(j + 1, temp);

                    //set the processIDs in order
                    temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }

        //calculate completion time
        for (int i = 0; i < processes.size(); i++) {
            //completion time of current process = arrivalTime + burstTime
            if (i == 0) {
                completionTime.add(i, arrivalTime.get(i) + burstTime.get(i));
            } else {
                if (arrivalTime.get(i) > completionTime.get(i - 1)) {
                    completionTime.add(i, arrivalTime.get(i) + burstTime.get(i));
                } else {
                    completionTime.add(i, completionTime.get(i - 1) + burstTime.get(i));
                }
            }

            //turnaround time = completion time - arrival time
            turnAroundTime.add(i, completionTime.get(i) - arrivalTime.get(i));

            //waiting time = completion time - burst time
            waitingTime.add(i, turnAroundTime.get(i) - burstTime.get(i));
        }

        //Print out results
        System.out.println("\nProcessID\tArrival Time\tBurst Time\tCompletion Time\tTurn Around\tWaiting Time");
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("\t" + processes.get(i) + "\t\t\t" + arrivalTime.get(i) + "\t\t\t\t" + burstTime.get(i)
                    + "\t\t\t\t" + completionTime.get(i) + "\t\t\t" + turnAroundTime.get(i) + "\t\t\t" + waitingTime.get(i));
        }

        //call the RR algorithm
        roundRobin(processes, arrivalTime, burstTime, completionTime, turnAroundTime, waitingTime);
    }

    public static void roundRobin(ArrayList<Integer> processes, ArrayList<Integer> arrivalTime, ArrayList<Integer> burstTime,
                                  ArrayList<Integer> completionTime, ArrayList<Integer> turnAroundTime, ArrayList<Integer> waitingTime) {
        System.out.println("\n\nAPPLYING ROUND ROBIN");

        //Find the mean and use it as the dynamic mean
        int timeQuantum = aggregate / numberOfProcesses;
        System.out.println("Time Quantum: " + timeQuantum);

        //go through the queue and process jobs
        for (int i = 0; i < processes.size(); i++) {
            if (burstTime.get(i) > timeQuantum) {
                //process and update the burst time
                burstTime.set(i, burstTime.get(i) - timeQuantum);

                completionTime.set(i, arrivalTime.get(i) + burstTime.get(i));

                //turnaround time = completion time - arrival time
                turnAroundTime.set(i, completionTime.get(i) - arrivalTime.get(i));

                //waiting time = completion time - burst time
                waitingTime.set(i, turnAroundTime.get(i) - burstTime.get(i));
            } else {
                burstTime.set(i, 0);
                processes.set(i, 0);
                arrivalTime.set(i, 0);
                waitingTime.set(i, 0);
                turnAroundTime.set(i, 0);
                completionTime.set(i, 0);
            }

            //Calculate the totals and throughput
            //totalTurnAroundTime += turnAroundTime.get(i);
//            System.out.println("Total TAT: " + totalTurnAroundTime);
        }

        //Remove all elements that are 0s
        processes.removeIf(val -> (val < 1));
        burstTime.removeIf(val -> (val < 1));
        arrivalTime.removeIf(val -> (val < 1));
        waitingTime.removeIf(val -> (val < 1));
        turnAroundTime.removeIf(val -> (val < 1));
        completionTime.removeIf(val -> (val < 1));

        //Trim the ArrayList down to size
        processes.trimToSize();
        burstTime.trimToSize();
        arrivalTime.trimToSize();
        waitingTime.trimToSize();
        turnAroundTime.trimToSize();
        completionTime.trimToSize();

        //Display remaining processes and their burst times after performing Round Robin
        System.out.println("RESULT AFTER APPLYING ROUND ROBIN");
        System.out.println("\nProcessID\tArrival Time\tBurst Time\tCompletion Time\tTurn Around"); //\tWaiting Time");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println("\t" + processes.get(i) + "\t\t\t" + arrivalTime.get(i) + "\t\t\t\t" + burstTime.get(i)
                    + "\t\t\t\t" + completionTime.get(i) + "\t\t\t" + turnAroundTime.get(i)); // + "\t\t\t" + waitingTime.get(i));
        }

        //System.out.println("\nThe Average Turn Around Time is : " + (totalTurnAroundTime / numberOfProcesses));    // printing average turnaround time.

        //continue with the SRTF algorithm
        shortestRemainingTimeFirst(processes, arrivalTime, burstTime, completionTime, turnAroundTime);
    }

    public static void shortestRemainingTimeFirst(ArrayList<Integer> processes, ArrayList<Integer> arrivalTime, ArrayList<Integer> burstTime,
                                                  ArrayList<Integer> completionTime, ArrayList<Integer> turnAroundTime) {
        System.out.println("\nAPPLYING SHORTEST REMAINING TIME FIRST");

        while (processes.size() != 0) {
            //starting from 0 and picking the first element
            temp = burstTime.get(0);
            int index = 0;

            //finding the smallest number
            for (int i = 0; i < processes.size(); i++) {
                if (burstTime.get(i) < temp) {
                    temp = burstTime.get(i);
                    index = i;
                }
            }

            System.out.println("Smallest burst time: " + temp);

            //executing and terminating process
            burstTime.remove(index);
            processes.remove(index);
            arrivalTime.remove(index);
            turnAroundTime.remove(index);
            completionTime.remove(index);

            processes.trimToSize();
            burstTime.trimToSize();
            arrivalTime.trimToSize();
            turnAroundTime.trimToSize();
            completionTime.trimToSize();

            System.out.println(burstTime);
        }
    }

    //randomizer to generate jobs
    public static void jobsFactory() {
        Random random = new Random();
        numberOfProcesses = random.nextInt(1, 100);
        System.out.println("Number of processes: " + numberOfProcesses);

        //assign proportionate values to all components of the algorithm
        processes = new ArrayList<>(numberOfProcesses);
        arrivalTime = new ArrayList<>(numberOfProcesses);
        burstTime = new ArrayList<>(numberOfProcesses);
        completionTime = new ArrayList<>(numberOfProcesses);
        turnAroundTime = new ArrayList<>(numberOfProcesses);
        waitingTime = new ArrayList<>(numberOfProcesses);

        //Receive the arrival time and burst time for each process
        for (int i = 0; i < numberOfProcesses; i++) {
            arrivalTime.add(i, random.nextInt(1, 100));

            burstTime.add(i, random.nextInt(1, 100));
            aggregate += burstTime.get(i);

            processes.add(i, i + 1);
        }
    }
}
