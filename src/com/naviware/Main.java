package com.naviware;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static int numberOfProcesses;   // total number of processes

    static ArrayList<Integer> processes;
    static ArrayList<Integer> arrivalTime;       // the arrival time of processes
    static ArrayList<Integer> burstTime;         // the burst or execution time of processes
    static ArrayList<Integer> completionTime;    // the time at which processes complete execution
    static ArrayList<Integer> turnAroundTime;    // the difference b/n completion time and arrival time
    static ArrayList<Integer> waitingTime;       // the difference between turn around time and burst time

    static float totalWaitTime = 0; // the total time it takes to wait
    static float totalTurnAroundTime = 0;   // the total turn around time

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);        //Scanner to receive user inputs
        int timeQuantum;

        //Ask the user for their number of processes
        System.out.print("Enter no of processes: ");
        numberOfProcesses = scanner.nextInt();

        //assign proportionate values to all components of the algorithm
        processes = new ArrayList<>(numberOfProcesses);
        arrivalTime = new ArrayList<>(numberOfProcesses);
        burstTime = new ArrayList<>(numberOfProcesses);
        completionTime = new ArrayList<>(numberOfProcesses);
        turnAroundTime = new ArrayList<>(numberOfProcesses);
        waitingTime = new ArrayList<>(numberOfProcesses);

        //Receive the arrival time and burst time for each process
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.print("Enter arrival time for Process " + (i + 1) + ": ");
            arrivalTime.add(i, scanner.nextInt());

            System.out.print("Enter burst time for Process " + (i + 1) + ": ");
            burstTime.add(i, scanner.nextInt());

            processes.add(i, i + 1);
        }

        System.out.print("Enter time quantum: ");
        timeQuantum = scanner.nextInt();

        System.out.println("\nProcessID\tArrival Time\tBurst Time");
        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("\t" + processes.get(i) + "\t\t\t" + arrivalTime.get(i) + "\t\t\t\t" + burstTime.get(i));
        }

        roundRobin(timeQuantum, processes, burstTime);


//        System.out.println("\nProcessID\tArrival Time\tBurst Time\tCompletion Time\tTurn Around\tWaiting Time");
//        for (int i = 0; i < numberOfProcesses; i++) {
//            System.out.println("\t" + processes.get(i) + "\t\t\t" + arrivalTime.get(i) + "\t\t\t\t" + burstTime.get(i) + "\t\t\t\t" + completionTime.get(i) + "\t\t\t" + turnAroundTime.get(i) + "\t\t\t" + waitingTime.get(i));
//        }
    }

    public static void roundRobin(int timeQuantum, ArrayList<Integer> processes, ArrayList<Integer> burstTime) {
        int temp;       //variable for holding temporal values

        for (int i = 0; i < processes.size(); i++) {
            System.out.println("Process: " + processes.get(i) + "\nBurst Time: " + burstTime.get(i) + "\nTime Quantum: " + timeQuantum);

            if (burstTime.get(i) > timeQuantum) {
                System.out.println("Greater than time quantum. Deducting....");
                temp = burstTime.get(i) - timeQuantum;
                burstTime.set(i, temp);
            } else {
                System.out.println("Lower/equal to time quantum. Removing....");
                processes.remove(i);
                burstTime.remove(i);
            }
        }

        System.out.println("\nProcessID\tBurst Time");
        for (int i = 0; i < processes.size(); i++) {
            System.out.println("\t" + processes.get(i) + "\t\t\t\t" + burstTime.get(i));
        }

    }

}
