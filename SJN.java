import java.util.*;

public class SJN {

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
    }

    public void startProcess() {
        LinkedList<String> tempProcessName = new LinkedList<>(processName);
        LinkedList<Integer> tempArrivalTime = new LinkedList<>(arrivalTime);
        LinkedList<Integer> tempBurstTime = new LinkedList<>(burstTime);
        LinkedList<String> waitingQueueProcess = new LinkedList<>();
        LinkedList<Integer> waitingQueueBurstTime = new LinkedList<>();
        LinkedList<Integer> waitingQueueArrivalTime = new LinkedList<>();
        ganttChartTime.add(0);
        String firstProcess = null;

        for (int i = 0; i < numberOfProcess; i++) {
            finishTime.add(0);
        }

        // Adding the first element into Gantt chart
        for (int i = 0; i < numberOfProcess; i++) {
            if (tempArrivalTime.get(i) == 0) {
                firstProcess = tempProcessName.get(i);
                ganttChartTime.add(tempBurstTime.get(i));
                ganttChartProcess.add(firstProcess);
                finishTime.set(i, tempBurstTime.get(i));
                tempProcessName.remove(i);
                tempBurstTime.remove(i);
                tempArrivalTime.remove(i);
                break;
            }
        }

        // Adding the remaining elements into Gantt chart
        while (tempProcessName.size() > 0) {
            for (int i = 0; i < tempProcessName.size(); i++) {
                if (tempArrivalTime.get(i) <= ganttChartTime.getLast()) {
                    waitingQueueProcess.add(tempProcessName.get(i));
                    waitingQueueArrivalTime.add(tempArrivalTime.get(i));
                    waitingQueueBurstTime.add(tempBurstTime.get(i));
                }
            }

            int shortestBurstTimeIndex = 0;
            int shortestBurstTime = waitingQueueBurstTime.get(shortestBurstTimeIndex);
            for (int i = 1; i < waitingQueueProcess.size(); i++) {
                if (waitingQueueBurstTime.get(i) < shortestBurstTime) {
                    shortestBurstTime = waitingQueueBurstTime.get(i);
                    shortestBurstTimeIndex = i;
                }
            }

            String finishedProcess = waitingQueueProcess.get(shortestBurstTimeIndex);
            Integer finishedProcessIndex = processName.indexOf(finishedProcess);

            for (int i = 0; i < tempProcessName.size(); i++) {
                if (tempProcessName.get(i).equals(finishedProcess)) {
                    tempProcessName.remove(i);
                    tempBurstTime.remove(i);
                    tempArrivalTime.remove(i);
                    break;
                }
            }

            ganttChartTime.add(ganttChartTime.getLast() + shortestBurstTime);
            ganttChartProcess.add(finishedProcess);

            if (finishedProcessIndex != -1) {
                finishTime.set(finishedProcessIndex, ganttChartTime.getLast());
            }

            waitingQueueProcess.clear();
            waitingQueueArrivalTime.clear();
            waitingQueueBurstTime.clear();
        }
    }
}
