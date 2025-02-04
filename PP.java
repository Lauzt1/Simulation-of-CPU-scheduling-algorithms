import java.util.*;

public class PP {

    int numberOfProcess;

    LinkedList<String> processName = new LinkedList<>();
    LinkedList<Integer> arrivalTime = new LinkedList<>();
    LinkedList<Integer> burstTime = new LinkedList<>();
    LinkedList<Integer> priority = new LinkedList<>();

    int totalBurstTime;

    int typeOfAlgorithm;

    LinkedList<Integer> ganttChartTime = new LinkedList<>();
    LinkedList<String> ganttChartProcess = new LinkedList<>();
    LinkedList<Integer> finishTime = new LinkedList<>();

    public PP(ProcessSchedulingApp temp) {
        this.numberOfProcess = temp.numberOfProcess;
        this.processName = temp.processName;
        this.arrivalTime = temp.arrivalTime;
        this.burstTime = temp.burstTime;
        this.priority = temp.priority;
        this.totalBurstTime = temp.totalBurstTime;
        this.typeOfAlgorithm = temp.typeOfAlgorithm;
        this.ganttChartTime = temp.ganttChartTime;
        this.ganttChartProcess = temp.ganttChartProcess;
        this.finishTime = temp.finishTime;
    }

    public void startProcess() {
        LinkedList<Integer> remainingBurstTime = new LinkedList<>(burstTime);
        LinkedList<Integer> tempPriority = new LinkedList<>(priority);
        LinkedList<Integer> tempArrivalTime = new LinkedList<>(arrivalTime);
    
        ganttChartTime.add(0);
        int currentTime = 0;
        int completedProcesses = 0;
    
        while (completedProcesses < numberOfProcess) {
            int highestPriorityIndex = -1;
            int highestPriorityValue = Integer.MAX_VALUE;
    
            // Find the process with the highest priority that has arrived and is not yet completed
            for (int i = 0; i < numberOfProcess; i++) {
                if (remainingBurstTime.get(i) > 0 && tempArrivalTime.get(i) <= currentTime) {
                    if (tempPriority.get(i) < highestPriorityValue) {
                        highestPriorityValue = tempPriority.get(i);
                        highestPriorityIndex = i;
                    }
                }
            }
    
            if (highestPriorityIndex != -1) {
                int processIndex = highestPriorityIndex;
                int processBurstTime = remainingBurstTime.get(processIndex);
    
                // Execute the process for its full remaining burst time
                remainingBurstTime.set(processIndex, 0);
                ganttChartTime.add(ganttChartTime.getLast() + processBurstTime);
                ganttChartProcess.add(processName.get(processIndex));
    
                finishTime.add(ganttChartTime.getLast());
                completedProcesses++;
                currentTime += processBurstTime;
            } else {
                // If no process is ready to execute, increment time
                currentTime++;
            }
        }
    
        // Correctly calculate waiting and turnaround times
        for (int i = 0; i < numberOfProcess; i++) {
            int finish = finishTime.get(i);
            int arrival = arrivalTime.get(i);
            int burst = burstTime.get(i);
    
            int turnaroundTime = finish - arrival;
            int waitingTime = turnaroundTime - burst;
    
            //System.out.println("| " + processName.get(i) + " | " + arrivalTime.get(i) + " | " + burstTime.get(i) + " | " + priority.get(i) + " | " + finish + " | " + turnaroundTime + " | " + waitingTime + " |");
        }
    
        // Calculate average times
        double avgTurnaroundTime = 0;
        double avgWaitingTime = 0;
        for (int i = 0; i < numberOfProcess; i++) {
            int turnaroundTime = finishTime.get(i) - arrivalTime.get(i);
            int waitingTime = turnaroundTime - burstTime.get(i);
            avgTurnaroundTime += turnaroundTime;
            avgWaitingTime += waitingTime;
        }
    
        avgTurnaroundTime /= numberOfProcess;
        avgWaitingTime /= numberOfProcess;
    
        //System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        //System.out.println("Average Waiting Time: " + avgWaitingTime);
    }
}