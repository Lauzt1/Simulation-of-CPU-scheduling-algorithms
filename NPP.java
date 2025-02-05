import java.util.*;

public class NPP {

    int numberOfProcess;

    // Process information lists (from ProcessSchedulingApp)
    LinkedList<String> processName;
    LinkedList<Integer> arrivalTime;
    LinkedList<Integer> burstTime;
    LinkedList<Integer> priority;

    int totalBurstTime;
    int typeOfAlgorithm;

    // Gantt chart and finish time lists (shared with ProcessSchedulingApp)
    LinkedList<Integer> ganttChartTime;
    LinkedList<String> ganttChartProcess;
    LinkedList<Integer> finishTime;

    public NPP(ProcessSchedulingApp temp) {
        // Copy values from ProcessSchedulingApp so that NPP works with the same data
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

        // Initialize finishTime with placeholder values for proper indexing
        for (int i = 0; i < numberOfProcess; i++) {
            finishTime.add(0);
        }
    }

    public void startProcess() {
        int currentTime = 0;    // simulation clock
        int completed = 0;      // number of processes finished

        // Clear and initialize the Gantt chart lists
        ganttChartTime.clear();
        ganttChartProcess.clear();
        ganttChartTime.add(0);

        // Array to mark if a process has been completed
        boolean[] isCompleted = new boolean[numberOfProcess];

        // Run the simulation until all processes are scheduled
        while (completed < numberOfProcess) {
            // Build a list of process indices that have arrived and are not yet completed
            ArrayList<Integer> available = new ArrayList<>();
            for (int i = 0; i < numberOfProcess; i++) {
                if (!isCompleted[i] && arrivalTime.get(i) <= currentTime) {
                    available.add(i);
                }
            }

            // If no process is available, add an idle period.
            if (available.isEmpty()) {
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < numberOfProcess; i++) {
                    if (!isCompleted[i] && arrivalTime.get(i) < nextArrival) {
                        nextArrival = arrivalTime.get(i);
                    }
                }
                if (currentTime < nextArrival) {
                    // Record the idle period in the Gantt chart (using "i " for idle)
                    ganttChartProcess.add("i ");
                    ganttChartTime.add(nextArrival);
                }
                currentTime = nextArrival;
                continue;  // re-check available processes at the new current time
            }

            // Select the process with the highest priority (lowest numerical value).
            // In case of a tie, choose the process with the earlier arrival time.
            int chosenIndex = available.get(0);
            for (int idx : available) {
                if (priority.get(idx) < priority.get(chosenIndex)) {
                    chosenIndex = idx;
                } else if (priority.get(idx).equals(priority.get(chosenIndex))) {
                    if (arrivalTime.get(idx) < arrivalTime.get(chosenIndex)) {
                        chosenIndex = idx;
                    }
                }
            }

            // "Run" the chosen process to completion (non-preemptive)
            currentTime += burstTime.get(chosenIndex);
            finishTime.set(chosenIndex, currentTime);

            // Record this process execution in the Gantt chart
            ganttChartProcess.add(processName.get(chosenIndex));
            ganttChartTime.add(currentTime);

            isCompleted[chosenIndex] = true;
            completed++;
        }
    }
}
