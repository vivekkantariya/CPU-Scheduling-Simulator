import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class Scheduling {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        Random r = new Random();

        System.out.println("\n=== Welcome to CPU Schduling Algo. ===");
        ArrayList<Process> processes = new ArrayList<>();

        System.out.print("How many Process. ");
        int processcount = scan.nextInt();

        for (int i = 0; i < processcount; i++) {
            int pid = r.nextInt(100, 1000);
            int arr_time = r.nextInt(0, 10);
            int burst_time = r.nextInt(1, 10);

            processes.add(new Process(pid, arr_time, burst_time));
        }

        System.out.println("\n=== Generated Processes ===");
        for (Process p : processes) {
            System.out.println(p);
        }

        System.out.println("\n========================");
        System.out.println("Choose 1 for FCFS");
        System.out.println("Choose 2 for SJF");
        System.out.print("\n Enter the choice : ");
        int choice = scan.nextInt();

        switch (choice) {
            case 1:
                firstcomefirstserve(processes);
                break;
            case 2:
                shortestjobfirst(processes);
                break;
            default:
                System.out.println("Please Enter Valid Choice. ");
        }

        scan.close();
    }

    static void shortestjobfirst(ArrayList<Process> processes) {

        // At this exact CPU time, which jobs are available and shortest?

        int n = processes.size();
        int CPU_start = 0;
        int completed = 0;
        boolean[] isCompleted = new boolean[n];      // to mark which process finised
        int totalWT = 0;
        int totalTAT = 0;

        while (completed < n) {
            int idx = -1;       // index of selectd process 
            int minburst = Integer.MAX_VALUE;

            /*
             * From ALL processes,
             * pick the one which:
             * 1) has already arrived
             * 2) is NOT completed
             * 3) has the smallest burst time
             */
            for (int i = 0; i < n; i++) {
                Process p = processes.get(i);
                if (p.arr_time <= CPU_start && !isCompleted[i]) {
                    if (p.burst_time < minburst) {
                        minburst = p.burst_time;
                        idx = i;
                    }
                }
            }
            /*
             * If no process has arrived yet,
             * CPU idle so increase time
             */
            if (idx == -1) {
                CPU_start++;
                continue;
            }

            Process p = processes.get(idx);
            CPU_start += p.burst_time; // add burst time
            p.completion_time = CPU_start; // completion time end of start time
            p.turnaround_time = p.completion_time - p.arr_time; // formula
            p.wait_time = p.turnaround_time - p.burst_time; // formual

            totalWT += p.wait_time;
            totalTAT += p.turnaround_time;

            isCompleted[idx] = true; // process done
            completed++; // increment count
        }

        System.out.println("\n=== SJF Scheduling Result ===");
        System.out.println("PID\tAT\tBT\tCT\tTAT\tWT");

        for (Process p : processes) {
            System.out.println(
                    p.pid + "\t" +
                            p.arr_time + "\t" +
                            p.burst_time + "\t" +
                            p.completion_time + "\t" +
                            p.turnaround_time + "\t" +
                            p.wait_time);
        }

        System.out.println("\nAverage Waiting Time: " + (float) totalWT / n);
        System.out.println("Average Turnaround Time: " + (float) totalTAT / n);
    }

    static void firstcomefirstserve(ArrayList<Process> processes) {
        // The Comparator interface allows you to create a class with a
        // compare() method that compares two objects to decide which one should go
        // first in a list. from W3schlls
        Collections.sort(processes, new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.arr_time, p2.arr_time);
            }
        });
        int CPU_start = 0;
        int totalWT = 0;
        int totalTAT = 0;

        for (Process p : processes) {
            if (CPU_start < p.arr_time) {
                CPU_start = p.arr_time; // CPU is idle, allocate process arrives
            }

            // Process execution
            CPU_start += p.burst_time; // add burst time
            p.completion_time = CPU_start; // completion time end of start time
            p.turnaround_time = p.completion_time - p.arr_time; // formula
            p.wait_time = p.turnaround_time - p.burst_time; // formual

            totalWT += p.wait_time;
            totalTAT += p.turnaround_time;

        }

        System.out.println("\n=== FCFS Scheduling Result ===");
        System.out.println("PID\tAT\tBT\tCT\tTAT\tWT");

        for (Process p : processes) {
            System.out.println(
                    p.pid + "\t" +
                            p.arr_time + "\t" +
                            p.burst_time + "\t" +
                            p.completion_time + "\t" +
                            p.turnaround_time + "\t" +
                            p.wait_time);
        }

        System.out.printf(
                "\nAverage Waiting Time: %.2f",
                (double) totalWT / processes.size());

        System.out.printf(
                "\nAverage Turnaround Time: %.2f\n",
                (double) totalTAT / processes.size());
    }
}

class Process {

    int pid;
    int arr_time;
    int burst_time;
    int completion_time;
    int remaining_time;
    int wait_time;
    int turnaround_time;

    public Process(int pid, int arr_time, int burst_time) {
        this.pid = pid;
        this.arr_time = arr_time;
        this.burst_time = burst_time;
    }

    @Override
    public String toString() {
        return String.format(
                "PID: %4d | Arrival: %2d | Burst: %2d",
                pid, arr_time, burst_time);
    }
}
