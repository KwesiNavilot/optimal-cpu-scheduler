package com.naviware;

import java.util.ArrayList;
import java.util.Random;

public class Main {

    static int numberOfProcesses;   // total number of processes
    static int temp, aggregate;       //variable for holding temporal values

    static ArrayList<Integer> processes;
    static ArrayList<Integer> arrivalTime;       // the arrival time of processes
    static ArrayList<Integer> burstTime;         // the burst or execution time of processes
    static ArrayList<Integer> completionTime;    // the time at which processes complete execution
    static ArrayList<Integer> turnAroundTime;    // the difference b/n completion time and arrival time
    static ArrayList<Integer> waitingTime;       // the difference between turn around time and burst time

    static float totalWaitTime; // the total time it takes to wait
    static float totalTurnAroundTime, sjfTAT;   // the total turn around time
    static float averageTurnAroundTime;   // the average turn around time
    static float averageWaitingTime;

    public static void main(String[] args) {
        //generate processes
        jobsFactory();

        sort("arrival");

        //calculate completion, waiting and turnaround time
        calculateTimings("start");

        //Print out results
        System.out.println("\nProcessID\tArrival Time\tBurst Time\tWaiting Time\tTurn Around");
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("\t" + processes.get(i) + "\t\t\t" + arrivalTime.get(i) + "\t\t\t\t" + burstTime.get(i)
                    + "\t\t\t" + waitingTime.get(i) + "\t\t\t\t" + turnAroundTime.get(i));
        }

        //call the RR algorithm
        roundRobin(processes, arrivalTime, burstTime, completionTime, turnAroundTime, waitingTime);
    }

    public static void roundRobin(ArrayList<Integer> processes, ArrayList<Integer> arrivalTime, ArrayList<Integer> burstTime,
                                  ArrayList<Integer> completionTime, ArrayList<Integer> turnAroundTime, ArrayList<Integer> waitingTime) {
        System.out.println("\n\nAPPLYING ROUND ROBIN");

        //Find the mean and use it as the dynamic mean
        int timeQuantum = aggregate / numberOfProcesses;
        System.out.println("Dynamic Time Quantum: " + timeQuantum);

        //go through the queue and process jobs
        for (int i = 0; i < processes.size(); i++) {
            //Calculate the totals and throughput
            totalTurnAroundTime += turnAroundTime.get(i);
            totalWaitTime += waitingTime.get(i);

            if (burstTime.get(i) > timeQuantum) {
                //process and update the burst time
                burstTime.set(i, burstTime.get(i) - timeQuantum);
            } else {
                burstTime.set(i, 0);
                processes.set(i, 0);
                arrivalTime.set(i, 0);
                waitingTime.set(i, 0);
                turnAroundTime.set(i, 0);
                completionTime.set(i, 0);
            }
        }

        //Remove all elements that are 0s
        processes.removeIf(val -> (val <= 0));
        burstTime.removeIf(val -> (val <= 0));
        arrivalTime.removeIf(val -> (val <= 0));
        waitingTime.removeIf(val -> (val <= 0));
        turnAroundTime.removeIf(val -> (val <= 0));
        completionTime.removeIf(val -> (val <= 0));

        //Trim the ArrayList down to size
        processes.trimToSize();
        burstTime.trimToSize();
        arrivalTime.trimToSize();
        waitingTime.trimToSize();
        turnAroundTime.trimToSize();
        completionTime.trimToSize();

        sort("burst");

        //calculate completion, waiting and turnaround time
        calculateTimings("middle");

        //Display remaining processes and their burst times after performing Round Robin
        System.out.println("\n\nRESULT AFTER APPLYING ROUND ROBIN");
        System.out.println("ProcessID\tArrival Time\tBurst Time\tWaiting Time\tTurn Around");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println("\t" + processes.get(i) + "\t\t\t" + arrivalTime.get(i) + "\t\t\t\t" + burstTime.get(i)
                    + "\t\t\t" + waitingTime.get(i) + "\t\t\t\t" + turnAroundTime.get(i));
        }

        //continue with the SRTF algorithm
        shortestRemainingTimeFirst(processes, arrivalTime, burstTime, completionTime, turnAroundTime);
    }

    public static void shortestRemainingTimeFirst(ArrayList<Integer> processes, ArrayList<Integer> arrivalTime, ArrayList<Integer> burstTime,
                                                  ArrayList<Integer> completionTime, ArrayList<Integer> turnAroundTime) {
        System.out.println("\nAPPLYING SHORTEST REMAINING TIME FIRST");

        int index = 0;
        while (processes.size() != 0) {
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

        System.out.println("\n\nSCHEDULING COMPLETE");

        //calculate average turnAroundTime
        averageTurnAroundTime = totalTurnAroundTime / numberOfProcesses;
        averageWaitingTime = totalWaitTime / numberOfProcesses;
        System.out.println("Average TAT: " + averageTurnAroundTime);
        System.out.println("Average WT: " + averageWaitingTime);
    }

    //randomizer to generate jobs
    public static void jobsFactory() {
        Random random = new Random();
        numberOfProcesses = 15; //random.nextInt(1, 50);
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
            arrivalTime.add(i, i);

            burstTime.add(i, random.nextInt(1, 100));

            //find the aggregate of burst times
            aggregate += burstTime.get(i);

            processes.add(i, i + 1);
        }
    }

    public static void calculateTimings(String stage) {
        if (stage.equals("start")) {
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

                //waiting time = completion time - burst time
                waitingTime.add(i, completionTime.get(i) - burstTime.get(i));

                //turnaround time = waiting time + burst time
                turnAroundTime.add(i, waitingTime.get(i) + burstTime.get(i));
            }
        } else {
            for (int i = 0; i < processes.size(); i++) {
                //completion time of current process = arrivalTime + burstTime
                if (i == 0) {
                    completionTime.set(i, arrivalTime.get(i) + burstTime.get(i));
                } else {
                    completionTime.set(i, completionTime.get(i - 1) + burstTime.get(i));
                }

                //waiting time = completion time - burst time
                if (i == 0) {
                    waitingTime.set(i, 0);
                } else {
                    waitingTime.set(i, completionTime.get(i) - burstTime.get(i));
                }

                //turnaround time = waiting time + burst time
                turnAroundTime.set(i, waitingTime.get(i) + burstTime.get(i));
            }
        }
    }

    public static void sort(String basis) {
        if (basis.equals("arrival"))
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
        else {
            for (int i = 0; i < processes.size(); i++) {
                for (int j = 0; j < processes.size() - (i + 1); j++) {
                    if (burstTime.get(j) > burstTime.get(j + 1)) {
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
        }
    }
}
