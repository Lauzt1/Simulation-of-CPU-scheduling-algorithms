import java.util.*;

public class NPP {

    int numberOfProcess;

    // process information
    LinkedList<String> processName;
    LinkedList<Integer> arrivalTime;
    LinkedList<Integer> burstTime;
    LinkedList<Integer> priority;

    int totalBurstTime;
    int typeOfAlgorithm;

    // for the Gantt chart and finish times
    LinkedList<Integer> ganttChartTime;
    LinkedList<String> ganttChartProcess;
    LinkedList<Integer> finishTime;

    public NPP(ProcessSchedulingApp temp) {
        // copy data from ProcessSchedulingApp
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

        // initialize finishTime with placeholder values (one for each process)
        for (int i = 0; i < numberOfProcess; i++) {
            finishTime.add(0);
        }
    }

    public void startProcess() {
        int currentTime = 0;    // simulation clock
        int completed = 0;      // count of processes that have finished
        
        // start the Gantt chart at time 0.
        ganttChartTime.clear();
        ganttChartProcess.clear();
        ganttChartTime.add(0);

        // boolean array to mark if a process is finished.
        boolean[] isCompleted = new boolean[numberOfProcess];

        // continue until all processes have been scheduled.
        while (completed < numberOfProcess) {
            // collect the indices of all processes that have arrived and are not yet completed.
            ArrayList<Integer> available = new ArrayList<>();
            for (int i = 0; i < numberOfProcess; i++) {
                if (!isCompleted[i] && arrivalTime.get(i) <= currentTime) {
                    available.add(i);
                }
            }

            if (available.isEmpty()) {
                // no process has arrived yet. Advance currentTime to the next arrival.
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < numberOfProcess; i++) {
                    if (!isCompleted[i] && arrivalTime.get(i) < nextArrival) {
                        nextArrival = arrivalTime.get(i);
                    }
                }
                currentTime = nextArrival;
                continue;  // check again for available processes.
            }

            // select the process with the highest priority (lowest priority value).
            int chosenIndex = available.get(0);
            for (int idx : available) {
                // if the process has a lower priority value, choose it.
                if (priority.get(idx) < priority.get(chosenIndex)) {
                    chosenIndex = idx;
                }
                // if both have the same priority, choose the one with the earlier arrival time.
                else if (priority.get(idx).equals(priority.get(chosenIndex))) {
                    if (arrivalTime.get(idx) < arrivalTime.get(chosenIndex)) {
                        chosenIndex = idx;
                    }
                }
            }

            // schedule the chosen process (run it to completion).
            currentTime += burstTime.get(chosenIndex);
            finishTime.set(chosenIndex, currentTime);

            // add the process to the Gantt chart.
            ganttChartProcess.add(processName.get(chosenIndex));
            ganttChartTime.add(currentTime);

            // mark the process as completed.
            isCompleted[chosenIndex] = true;
            completed++;
        }
    }
}
