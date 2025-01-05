import java.util.*;

public class Main {
    public static void main(String[] args) {
        new Main();
    }

//-------------------------------------------------------------------------------------------------------------------

    Scanner input = new Scanner(System.in);

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

    LinkedList<Integer> turnaroundTime = new LinkedList<>();
    LinkedList<Integer> waitingTime = new LinkedList<>();

    int totalTurnaroundTime, totalWaitingTime, averageTurnaroundTime, averageWaitingTime;

//-------------------------------------------------------------------------------------------------------------------

    public Main() {
        numberOfProcess = getNumberOfProcess();

        arrivalTime = getValues("arrival time");
        burstTime = getValues("burst time");
        priority = getValues("priority");

        totalBurstTime = 0;
        for (int x : burstTime)
            totalBurstTime += x;

        do {
            typeOfAlgorithm = getTypeOfAlgorithm();

            if (typeOfAlgorithm == 1)
                RR();
            else if (typeOfAlgorithm == 2)
                SJN();
            else if (typeOfAlgorithm == 3)
                PP();
            else if (typeOfAlgorithm == 4)
                NPP();

            System.out.println("\nDo you want check the result for other type of algorithm?");
            System.out.println("[1] Yes");
            System.out.println("[2] No");
            System.out.println("Enter your selection: ");
            if (input.nextInt() == 2)
                break;
        } while (true);
    }

//-------------------------------------------------------------------------------------------------------------------

    private int getNumberOfProcess() {
        System.out.println("---------------------------------------");
        System.out.println("Simulation of CPU Scheduling Algorithms");
        System.out.println("---------------------------------------");

        System.out.println("\nEnter the number of process:");
        int value = input.nextInt();
        for (int i = 0; i < value; i++) // create process name based on the number of process
            processName.add("P" + i);

        return value;
    }

    private LinkedList<Integer> getValues(String type) {
        LinkedList<Integer> values = new LinkedList<>();

        System.out.println("\nEnter the " + type + " of each process (e.g., \"1 2 3 4 5 6\"): ");

        for (int i = 0; i < numberOfProcess; i++)
            values.add(input.nextInt());

        return values;
    }

    private int getTypeOfAlgorithm() {
        System.out.println("\n-----------------------------------------------");
        System.out.println("\nType of Algorithm: ");
        System.out.println("[1] Round Robin");
        System.out.println("[2] Shortest Job Next");
        System.out.println("[3] Preemptive Priority");
        System.out.println("[4] Non-Preemptive Priority");
        System.out.println("Enter your selection:");
        int value = input.nextInt();

        return value;
    }

//-------------------------------------------------------------------------------------------------------------------

    private void RR() {
        clearData();

        // algorithm that will update the LinkedList of ganttChartTime, ganttChartProcess, and finishTime
        // Just an example code that will update the ganttChartTime, ganttChartProcess, and finishTime
        ganttChartTime.add(0);
        for (int i = 0; i < numberOfProcess; i++) {
            ganttChartTime.add(i + 29);
            ganttChartProcess.add("P" + i);
            finishTime.add(i + 29);
        }

        displayResult();
    }

    private void SJN() {
        clearData();

        // algorithm that will update the LinkedList of ganttChartTime, ganttChartProcess, and finishTime

        displayResult();
    }

    private void PP() {
        clearData();

        // algorithm that will update the LinkedList of ganttChartTime, ganttChartProcess, and finishTime

        displayResult();
    }

    private void NPP() {
        clearData();

        // algorithm that will update the LinkedList of ganttChartTime, ganttChartProcess, and finishTime

        displayResult();
    }

//-------------------------------------------------------------------------------------------------------------------

    private void clearData() {
        ganttChartTime.clear();
        ganttChartProcess.clear();
        finishTime.clear();
        turnaroundTime.clear();
        waitingTime.clear();
    }

//-------------------------------------------------------------------------------------------------------------------

    private void displayResult() {
        // Obtain the list of Turnaround Time and Waiting Time
        for (int i = 0; i < numberOfProcess; i++) {
            turnaroundTime.add(finishTime.get(i) - arrivalTime.get(i));
            waitingTime.add(turnaroundTime.get(i) - burstTime.get(i));
        }

        // Display the Gantt Chart and Table
        System.out.println("\nGantt Chart Time:\n" + ganttChartTime);
        System.out.println(ganttChartTime.size());
        System.out.println("\nGantt Chart Process:\n" + ganttChartProcess);
        System.out.println(ganttChartProcess.size());
        System.out.println("\nFinish Time:\n" + finishTime);
        System.out.println("\nTurnaround Time:\n" + turnaroundTime);
        System.out.println("\nWaiting Time:\n" + waitingTime);

        // Obtain and display the Average Turnaround Time and Average Waiting Time
        totalTurnaroundTime = 0;
        totalWaitingTime = 0;
        for (int i = 0; i < numberOfProcess; i++) {
            totalTurnaroundTime += turnaroundTime.get(i);
            totalWaitingTime += waitingTime.get(i);
        }
        averageTurnaroundTime = totalTurnaroundTime / numberOfProcess;
        averageWaitingTime = totalWaitingTime / numberOfProcess;
        System.out.println("\nAverage Turnaround Time: " + averageTurnaroundTime);
        System.out.println("\nAverage Waiting Time: " + averageWaitingTime);
    }
}
