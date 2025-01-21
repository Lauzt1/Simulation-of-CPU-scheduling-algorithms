import java.util.*;

public class NPP {

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

    public NPP(ProcessSchedulingApp temp) {
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
        // create temporary lists so we can remove processes as we schedule them
        LinkedList<String> tempProcessName = new LinkedList<>(processName);
        LinkedList<Integer> tempArrivalTime = new LinkedList<>(arrivalTime);
        LinkedList<Integer> tempBurstTime = new LinkedList<>(burstTime);
        LinkedList<Integer> tempPriority = new LinkedList<>(priority);

        // queues to hold waiting processes that arrived but not yet executed
        LinkedList<String> waitingQueueProcess = new LinkedList<>();
        LinkedList<Integer> waitingQueueArrival = new LinkedList<>();
        LinkedList<Integer> waitingQueueBurst = new LinkedList<>();
        LinkedList<Integer> waitingQueuePriority = new LinkedList<>();

        // gantt chart time at 0
        ganttChartTime.add(0);

        
        for (int i = 0; i < tempProcessName.size(); i++) {  // find a process that arrives at time 0 (if any) and schedule it first
            if (tempArrivalTime.get(i) == 0) {
                ganttChartTime.add(tempBurstTime.get(i));  // ending time for this process
                ganttChartProcess.add(tempProcessName.get(i));
                finishTime.add(tempBurstTime.get(i));      // finishing time of the first process

                // remove the process from the temporary lists after finishing
                tempProcessName.remove(i);
                tempArrivalTime.remove(i);
                tempBurstTime.remove(i);
                tempPriority.remove(i);
                break;
            }
        }

        
        while (!tempProcessName.isEmpty()) {    // continue until we schedule all processes

            // collect all processes that have arrived by the current Gantt time
            for (int i = 0; i < tempProcessName.size(); i++) {
                if (tempArrivalTime.get(i) <= ganttChartTime.getLast()) {
                    waitingQueueProcess.add(tempProcessName.get(i));
                    waitingQueueArrival.add(tempArrivalTime.get(i));
                    waitingQueueBurst.add(tempBurstTime.get(i));
                    waitingQueuePriority.add(tempPriority.get(i));
                }
            }

            // pick next earliest arrival
            if (waitingQueueProcess.isEmpty()) {
                int earliestIndex = 0;
                int earliestTime = tempArrivalTime.get(0);
                for (int i = 1; i < tempArrivalTime.size(); i++) {
                    if (tempArrivalTime.get(i) < earliestTime) {
                        earliestTime = tempArrivalTime.get(i);
                        earliestIndex = i;
                    }
                }
                // move Gantt time to that arrival, schedule it
                ganttChartTime.add(earliestTime);
                ganttChartProcess.add(tempProcessName.get(earliestIndex));
                finishTime.add(earliestTime);

                // remove it from the temporary lists
                tempProcessName.remove(earliestIndex);
                tempArrivalTime.remove(earliestIndex);
                tempBurstTime.remove(earliestIndex);
                tempPriority.remove(earliestIndex);

                continue; 
            }

            // choose the one waiting process with HIGHEST priority (lower number means higher priority)
            int highestPriorityIndex = 0;
            int minPriorityValue = waitingQueuePriority.get(0);

            for (int i = 1; i < waitingQueuePriority.size(); i++) {
                if (waitingQueuePriority.get(i) < minPriorityValue) {
                    minPriorityValue = waitingQueuePriority.get(i);
                    highestPriorityIndex = i;
                }
            }

            // schedule the highest-priority process
            String chosenProcess = waitingQueueProcess.get(highestPriorityIndex);
            int chosenBurst    = waitingQueueBurst.get(highestPriorityIndex);

            // update Gantt Chart
            ganttChartTime.add(ganttChartTime.getLast() + chosenBurst);
            ganttChartProcess.add(chosenProcess);
            finishTime.add(finishTime.getLast() + chosenBurst);

            // remove it from temp lists
            for (int i = 0; i < tempProcessName.size(); i++) {
                if (tempProcessName.get(i).equals(chosenProcess)) {
                    tempProcessName.remove(i);
                    tempArrivalTime.remove(i);
                    tempBurstTime.remove(i);
                    tempPriority.remove(i);
                    break;
                }
            }

            // clear the waiting queue for the next iteration
            waitingQueueProcess.clear();
            waitingQueueArrival.clear();
            waitingQueueBurst.clear();
            waitingQueuePriority.clear();
        }
    }
}
