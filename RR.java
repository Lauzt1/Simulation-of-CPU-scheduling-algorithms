import java.util.*;

public class RR {

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

    public RR(ProcessSchedulingApp temp) {
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
        // algorithm that will update the LinkedList of ganttChartTime, ganttChartProcess, and finishTime
        Scanner input = new Scanner(System.in);
        
        int quantum = 3;
        System.out.println("\nPlease enter the time quantum (Default = 3):");
        quantum = input.nextInt();

        ganttChartTime.add(0);

        LinkedList<Integer> ganttChartProcessInt = new LinkedList<>();
        int[] remainingBurstTime = new int[numberOfProcess];
        boolean[] hasExecuted = new boolean[numberOfProcess];
        
        LinkedList<Integer> readyQueue = new LinkedList<>();
        int completedProcess = 0;
        int currentTime = 0;

        // Initially, all hasExecuted is false.
        // All remaining burst time is the same as burst time
        // And put process that arrives at time = 0 into ready queue
        for (int i = 0; i < numberOfProcess; i++) {
            remainingBurstTime[i] = burstTime.get(i);
            hasExecuted[i] = false;
            finishTime.add(0);

            if (arrivalTime.get(i) == 0) {
                readyQueue.add(i);
            }
        }

        // Sort the process (arrival time = 0) based on priority. If priority is the same, order will not change.
        readyQueue.sort((p1, p2) -> {
            if (priority.get(p1) == (priority.get(p2))) {
                if (arrivalTime.get(p1) == (arrivalTime.get(p2))) {
                    if (!hasExecuted[p1] && hasExecuted[p2]) return -1;
                    if (hasExecuted[p1] && !hasExecuted[p2]) return 1;
                    return p1 - p2;
                }
                return arrivalTime.get(p1) - arrivalTime.get(p2);
            }
            return priority.get(p1) - priority.get(p2); // Lower priority value means higher priority
        });

        while (true) {
            LinkedList<Integer> tempReadyQueue = new LinkedList<>();
            for (int i = 0; i < numberOfProcess; i++) {
                if (arrivalTime.get(i) != 0 && arrivalTime.get(i) == currentTime)
                    tempReadyQueue.add(i);
            }

            if (!tempReadyQueue.isEmpty()) {
                if (tempReadyQueue.size() > 1) {
                    tempReadyQueue.sort((p1, p2) -> {
                        if (priority.get(p1) == (priority.get(p2))) {
                            if (arrivalTime.get(p1) == (arrivalTime.get(p2))) {
                                if (!hasExecuted[p1] && hasExecuted[p2]) return -1;
                                if (hasExecuted[p1] && !hasExecuted[p2]) return 1;
                                return p1 - p2;
                            }
                            return arrivalTime.get(p1) - arrivalTime.get(p2);
                        }
                        return priority.get(p1) - priority.get(p2); // Lower priority value means higher priority
                    });
                }

                readyQueue.addAll(tempReadyQueue);
            }

            if (currentTime == ganttChartTime.getLast()) {
                if (!ganttChartProcessInt.isEmpty() && remainingBurstTime[ganttChartProcessInt.getLast()] != 0) {
                    readyQueue.add(ganttChartProcessInt.getLast());
                }

                int currentProcess = readyQueue.removeFirst();

                ganttChartProcessInt.add(currentProcess);

                if (remainingBurstTime[currentProcess] <= quantum) {
                    ganttChartTime.add(ganttChartTime.getLast() + remainingBurstTime[currentProcess]);
                    finishTime.set(currentProcess, ganttChartTime.getLast());
                    remainingBurstTime[currentProcess] = 0;
                    hasExecuted[currentProcess] = true;
                    completedProcess++;
                }
                else {
                    ganttChartTime.add(ganttChartTime.getLast() + quantum);
                    remainingBurstTime[currentProcess] = remainingBurstTime[currentProcess] - quantum;
                }
            }

            currentTime++;

            if (completedProcess == numberOfProcess && currentTime == totalBurstTime)
                break;
        }

        for (int i = 0; i < ganttChartProcessInt.size(); i++) {
            String temp = "P" + ganttChartProcessInt.get(i);
            ganttChartProcess.add(temp);
        }
    }
}
