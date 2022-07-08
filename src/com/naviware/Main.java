package com.naviware;

import java.util.Scanner;

public class Main {

    static int numberOfProcesses;   // total number of processes

    static int[] processIDs;        // the process IDs
    static int[] arrivalTime;       // the arrival time of processes
    static int[] burstTime;         // the burst or execution time of processes
    static int[] completionTime;    // the time at which processes complete execution
    static int[] turnAroundTime;    // the difference b/n completion time and arrival time
    static int[] waitingTime;       // the difference between turn around time and burst time

    static float totalWaitTime = 0; // the total time it takes to wait
    static float totalTurnAroundTime = 0;   // the total turn around time

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);        //Scanner to receive user inputs

        int temp;       //variable for sorting processes
        int timeQuantum;

        //Ask the user for their number of processes
        System.out.print("Enter no of processes: ");
        numberOfProcesses = scanner.nextInt();

        //assign proportionate values to all components of the algorithm
        processIDs = new int[numberOfProcesses];
        arrivalTime = new int[numberOfProcesses];
        burstTime = new int[numberOfProcesses];
        completionTime = new int[numberOfProcesses];
        turnAroundTime = new int[numberOfProcesses];
        waitingTime = new int[numberOfProcesses];

        //Receive the arrival time and burst time for each process
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.print("Enter arrival time for Process " + (i + 1) + ": ");
            arrivalTime[i] = scanner.nextInt();

            System.out.print("Enter burst time for Process " + (i + 1) + ": ");
            burstTime[i] = scanner.nextInt();

            processIDs[i] = i + 1;
        }

        System.out.println("Enter time quantum: ");
        timeQuantum = scanner.nextInt();

        System.out.println("\nProcessID\tArrival Time\tBurst Time\tCompletion Time\tTurn Around\tWaiting Time");
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("\t" + processIDs[i] + "\t\t\t" + arrivalTime[i] + "\t\t\t\t" + burstTime[i] + "\t\t\t\t" + completionTime[i] + "\t\t\t" + turnAroundTime[i] + "\t\t\t" + waitingTime[i]);
        }
    }

}
