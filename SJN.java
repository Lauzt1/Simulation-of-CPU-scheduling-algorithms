import java.util.*;
    
public class SJN {

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

    public SJN(ProcessSchedulingApp temp) {
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
        
        // Initialize finishTime with placeholder values
        for (int i = 0; i < numberOfProcess; i++) {
            finishTime.add(0);
        }
    }

    public void startProcess() {
        LinkedList<String> tempProcessName = new LinkedList<>(processName);
        LinkedList<Integer> tempArrivalTime = new LinkedList<>(arrivalTime);
        LinkedList<Integer> tempBurstTime = new LinkedList<>(burstTime);

        ganttChartTime.clear();
        ganttChartProcess.clear();

        // If there is an idle period before the first process arrives, record it.
        int firstArrival = Collections.min(tempArrivalTime);
        if (firstArrival > 0) {
            ganttChartProcess.add("i ");
            ganttChartTime.add(firstArrival);
        } else {
            ganttChartTime.add(0);
        }

        // Continue scheduling until all processes are handled.
        while (!tempProcessName.isEmpty()) {
            // Build a list of indices for processes that have arrived by the current time.
            ArrayList<Integer> availableIndices = new ArrayList<>();
            for (int i = 0; i < tempProcessName.size(); i++) {
                if (tempArrivalTime.get(i) <= ganttChartTime.getLast()) {
                    availableIndices.add(i);
                }
            }
            if (availableIndices.isEmpty()) {
                // No process available: add an idle period until the next arrival.
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < tempArrivalTime.size(); i++) {
                    nextArrival = Math.min(nextArrival, tempArrivalTime.get(i));
                }
                ganttChartProcess.add("i ");
                ganttChartTime.add(nextArrival);
                continue;
            }
            // Choose the process with the shortest burst time among those available.
            int chosenIndex = availableIndices.get(0);
            for (int idx : availableIndices) {
                if (tempBurstTime.get(idx) < tempBurstTime.get(chosenIndex)) {
                    chosenIndex = idx;
                }
            }
            int finish = ganttChartTime.getLast() + tempBurstTime.get(chosenIndex);
            ganttChartProcess.add(tempProcessName.get(chosenIndex));
            ganttChartTime.add(finish);

            // Update finish time in the original list.
            int originalIndex = processName.indexOf(tempProcessName.get(chosenIndex));
            finishTime.set(originalIndex, finish);

            // Remove the chosen process from the temporary lists.
            tempProcessName.remove(chosenIndex);
            tempArrivalTime.remove(chosenIndex);
            tempBurstTime.remove(chosenIndex);
        }
    }
}
