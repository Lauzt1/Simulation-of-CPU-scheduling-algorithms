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
        // This implementation of Preemptive Priority scheduling simulates the CPU cycle one time unit at a time.
        // At each time unit, it selects from the processes that have already arrived (arrivalTime <= currentTime)
        // the one with the highest priority (lowest priority number). If no process is available, the CPU remains idle.

        int n = numberOfProcess;
        int[] remainingBurst = new int[n];  // remaining burst times for each process

        // Initialize the remaining burst times from the burstTime list
        for (int i = 0; i < n; i++) {
            remainingBurst[i] = burstTime.get(i);
        }

        // We will record which process (or idle) is executed at each time unit.
        // Later, we will “compress” these time units into segments for the Gantt chart.
        ArrayList<String> executionSequence = new ArrayList<>();

        int completedProcesses = 0; // count of processes that have finished
        int currentTime = 0;        // simulation clock

        // Run the simulation until all processes are completed
        while (completedProcesses < n) {
            int selectedProcess = -1; 
            int minPriority = Integer.MAX_VALUE;
            
            // Look for processes that have arrived and are not finished
            for (int i = 0; i < n; i++) {
                if (arrivalTime.get(i) <= currentTime && remainingBurst[i] > 0) {
                    int procPriority = priority.get(i);
                    // Select process with a lower (i.e. higher) priority value.
                    if (procPriority < minPriority) {
                        minPriority = procPriority;
                        selectedProcess = i;
                    } else if (procPriority == minPriority) {
                        // Tie-breaker: if two processes have the same priority, choose the one with the earlier arrival time.
                        if (arrivalTime.get(i) < arrivalTime.get(selectedProcess)) {
                            selectedProcess = i;
                        }
                    }
                }
            }

            if (selectedProcess == -1) {
                // No process is available at this time (all have not arrived yet); CPU is idle.
                executionSequence.add("idle");
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

        // Build the Gantt chart from the executionSequence.
        // The idea is to compress consecutive identical entries (e.g., several "P0" in a row) into one segment.
        ganttChartTime.clear();
        ganttChartProcess.clear();
        ganttChartTime.add(0);  // the chart starts at time 0

        // Start with the first executed process (or idle)
        String currentSegment = executionSequence.get(0);
        ganttChartProcess.add(currentSegment);

        // Loop through the execution sequence and note the time when the process changes.
        for (int i = 1; i < executionSequence.size(); i++) {
            String proc = executionSequence.get(i);
            if (!proc.equals(currentSegment)) {
                // Process change detected; record the finishing time of the previous segment.
                ganttChartTime.add(i);
                ganttChartProcess.add(proc);
                currentSegment = proc;
            }
        }
        // Record the final finish time (which is the length of the execution sequence).
        ganttChartTime.add(executionSequence.size());
    }
}
