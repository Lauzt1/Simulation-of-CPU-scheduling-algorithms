import java.util.*;

public class PP {
    private ProcessSchedulingApp app;
    private PriorityQueue<Process> queue;
    private List<Process> processList;

    public PP(ProcessSchedulingApp app) {
        this.app = app;
        this.queue = new PriorityQueue<>((a, b) -> a.priority == b.priority ? a.arrivalTime - b.arrivalTime : a.priority - b.priority);
        this.processList = new ArrayList<>();
        for (int i = 0; i < app.numberOfProcess; i++) {
            processList.add(new Process(
                app.processName.get(i),
                app.arrivalTime.get(i),
                app.burstTime.get(i),
                app.priority.get(i)
            ));
        }
    }

    public void startProcess() {
        int currentTime = 0;
        int completed = 0;
        Process currentProcess = null;
        int processStartTime = 0;
    
        List<Process> processCopy = new ArrayList<>();
        for (Process p : processList) {
            p.priority = 1 / p.burstTime; // Initial priority (inverse of burst time)
            processCopy.add(new Process(p));
        }
    
        // Add 0 to the Gantt chart at the start
        app.ganttChartTime.add(0);
    
        while (completed < processList.size()) {
            for (Process p : processCopy) {
                if (p.arrivalTime == currentTime && !p.completed) {
                    queue.add(p);
                }
            }
    
            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }
    
            // Update priority dynamically: waitingTime / remainingBurstTime
            for (Process p : queue) {
                p.priority = (currentTime - p.arrivalTime) / (p.remainingBurst == 0 ? 1 : p.remainingBurst);
            }
    
            // Sort queue based on updated priority
            List<Process> tempQueue = new ArrayList<>(queue);
            tempQueue.sort((a, b) -> a.priority == b.priority ? a.arrivalTime - b.arrivalTime : a.priority - b.priority);
            queue.clear();
            queue.addAll(tempQueue);
    
            Process highestPriorityProcess = queue.poll();
    
            // If a new process takes over, log previous process in Gantt Chart
            if (currentProcess != highestPriorityProcess) {
                if (currentProcess != null) {
                    app.ganttChartProcess.add(currentProcess.name);
                    app.ganttChartTime.add(currentTime); // Log the finish time of the previous process
                }
                currentProcess = highestPriorityProcess;
                processStartTime = currentTime;
            }
    
            currentProcess.remainingBurst--;
            currentTime++;
    
            if (currentProcess.remainingBurst == 0) {
                currentProcess.completed = true;
                currentProcess.finishTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.finishTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
    
                app.finishTime.add(currentProcess.finishTime);
                app.turnaroundTime.add(currentProcess.turnaroundTime);
                app.waitingTime.add(currentProcess.waitingTime);
    
                completed++;
            } else {
                queue.add(currentProcess); // Put back if not completed
            }
        }
    
        // Add the final time to the Gantt chart
        app.ganttChartProcess.add(currentProcess.name);
        app.ganttChartTime.add(currentTime);
    }
}

class Process {
    String name;
    int arrivalTime, burstTime, priority, finishTime, turnaroundTime, waitingTime, remainingBurst;
    boolean completed;

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingBurst = burstTime;
        this.completed = false;
    }

    public Process(Process p) {
        this.name = p.name;
        this.arrivalTime = p.arrivalTime;
        this.burstTime = p.burstTime;
        this.priority = p.priority;
        this.remainingBurst = p.remainingBurst;
        this.completed = p.completed;
    }
}
