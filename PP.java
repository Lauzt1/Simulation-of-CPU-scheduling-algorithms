import java.util.*;

public class PP {

    int numberOfProcess;

    LinkedList<String> processName;
    LinkedList<Integer> arrivalTime;
    LinkedList<Integer> burstTime;
    LinkedList<Integer> priority;

    int totalBurstTime;
    int typeOfAlgorithm;

    LinkedList<Integer> ganttChartTime;
    LinkedList<String> ganttChartProcess;
    LinkedList<Integer> finishTime;

    public PP(ProcessSchedulingApp temp) {
        // Copy values from ProcessSchedulingApp so that PP works with the same data
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
        int n = numberOfProcess;
        int[] remainingBurst = new int[n];  // remaining burst times for each process

        // Initialize the remaining burst times from the burstTime list
        for (int i = 0; i < n; i++) {
            remainingBurst[i] = burstTime.get(i);
        }

        // This list records the execution at each time unit.
        ArrayList<String> executionSequence = new ArrayList<>();

        int completedProcesses = 0;
        int currentTime = 0;

        // Run the simulation until all processes are completed
        while (completedProcesses < n) {
            int selectedProcess = -1;
            int minPriority = Integer.MAX_VALUE;
            
            // Check for processes that have arrived and are not finished
            for (int i = 0; i < n; i++) {
                if (arrivalTime.get(i) <= currentTime && remainingBurst[i] > 0) {
                    int procPriority = priority.get(i);
                    if (procPriority < minPriority) {
                        minPriority = procPriority;
                        selectedProcess = i;
                    } else if (procPriority == minPriority) {
                        // Tie-breaker: choose the process with the earlier arrival time.
                        if (arrivalTime.get(i) < arrivalTime.get(selectedProcess)) {
                            selectedProcess = i;
                        }
                    }
                }
            }

            if (selectedProcess == -1) {
                // No process is available at this time; record an idle time unit.
                executionSequence.add("i ");
                currentTime++;
            } else {
                // Run the selected process for one time unit.
                executionSequence.add("P" + selectedProcess);
                remainingBurst[selectedProcess]--;
                currentTime++;

                // If the process has finished executing, record its finish time.
                if (remainingBurst[selectedProcess] == 0) {
                    finishTime.set(selectedProcess, currentTime);
                    completedProcesses++;
                }
            }
        }

        // Build the Gantt chart by compressing consecutive identical entries.
        ganttChartTime.clear();
        ganttChartProcess.clear();
        ganttChartTime.add(0);

        String currentSegment = executionSequence.get(0);
        ganttChartProcess.add(currentSegment);

        for (int i = 1; i < executionSequence.size(); i++) {
            String proc = executionSequence.get(i);
            if (!proc.equals(currentSegment)) {
                ganttChartTime.add(i);
                ganttChartProcess.add(proc);
                currentSegment = proc;
            }
        }
        ganttChartTime.add(executionSequence.size());
    }
}
