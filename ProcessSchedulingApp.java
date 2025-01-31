import java.util.*;

public class ProcessSchedulingApp {
            
        Scanner input = new Scanner(System.in);
    
        public int numberOfProcess;
    
        public LinkedList<String> processName = new LinkedList<>();
        public LinkedList<Integer> arrivalTime = new LinkedList<>();
        public LinkedList<Integer> burstTime = new LinkedList<>();
        public LinkedList<Integer> priority = new LinkedList<>();
    
        public int totalBurstTime;
    
        public int typeOfAlgorithm;
    
        public LinkedList<Integer> ganttChartTime = new LinkedList<>();
        public LinkedList<String> ganttChartProcess = new LinkedList<>();
        public LinkedList<Integer> finishTime = new LinkedList<>();
    
        LinkedList<Integer> turnaroundTime = new LinkedList<>();
        LinkedList<Integer> waitingTime = new LinkedList<>();
    
        int totalTurnaroundTime, totalWaitingTime;
        double averageTurnaroundTime, averageWaitingTime;
    
        //-------------------------------------------------------------------------------------------------------------------
        
        public ProcessSchedulingApp() {
            numberOfProcess = getNumberOfProcess();
    
            arrivalTime = getValues("arrival time");
            burstTime = getValues("burst time");
            priority = getValues("priority");
    
            // Make sure there exist at least one zero in the arrival time
            do {
                if (arrivalTimeConsistOfZero())
                    break;
    
                for (int i = 0; i < numberOfProcess; i++)
                    arrivalTime.set(i, arrivalTime.get(i) - 1);
    
            } while (true);
    
            totalBurstTime = 0;
            for (int x : burstTime)
                totalBurstTime += x;
    
            System.out.println("\n-------------------------------------------------------------------------------------------------");
            do {
                typeOfAlgorithm = getTypeOfAlgorithm();
    
                clearData();
    
                if (typeOfAlgorithm == 1)
                    new RR(this).startProcess();
                else if (typeOfAlgorithm == 2)
                    new SJN(this).startProcess();
                else if (typeOfAlgorithm == 3)
                    new PP(this).startProcess();
                else if (typeOfAlgorithm == 4)
                    new NPP(this).startProcess();
    
                displayResult();
    
                System.out.println("\nDo you want to check the result for other type of algorithm?");
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
            
            int value;
            
            do {
                System.out.println("\nEnter the number of process (3 - 10):");
                value = input.nextInt();
                if (value < 3 || value > 10)
                {
                    System.out.println("The value must be between 3 and 10. Please try again.");
                }
                else
                {
                    for (int i = 0; i < value; i++) // create process name based on the number of process
                        processName.add("P" + i);
                }
            }
            while (value < 3 || value > 10);
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
            System.out.println("\nType of Algorithm: ");
            System.out.println("[1] Round Robin");
            System.out.println("[2] Shortest Job Next");
            System.out.println("[3] Preemptive Priority");
            System.out.println("[4] Non-Preemptive Priority");
            System.out.println("Enter your selection:");
            int value = input.nextInt();
    
            return value;
        }
    
        private boolean arrivalTimeConsistOfZero() {
            for (int x : arrivalTime) {
                if (x == 0)
                    return true;
            }
            return false;
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
        
        private void printGanttChart() {
            System.out.println();
    
            System.out.println("Gantt Chart:");
            System.out.print("-");
            for (int i = 0; i < ganttChartProcess.size(); i++) {
                System.out.print("-------");
            }
    
            System.out.print("\n|");
            for (int i = 0; i < ganttChartProcess.size(); i++) {
                System.out.print("  " + ganttChartProcess.get(i) + "  |");
            }
    
            System.out.print("\n|");
            for (int i = 0; i < ganttChartProcess.size(); i++) {
                System.out.print("------|");
            }
    
            System.out.println();
            for (int i = 0; i < ganttChartTime.size(); i++) {
                System.out.print(ganttChartTime.get(i));
    
                    for (int j = 0; j < 7 - String.valueOf(ganttChartTime.get(i)).length(); j++)
                        System.out.print(" ");   
            }
        }
        
        //-------------------------------------------------------------------------------------------------------------------
        
        private void printTable() {
            System.out.println();
            System.out.println();
    
            System.out.println("Table:");
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.println("| Process | Arrival Time | Burst Time | Priority | Finish Time | Turnaround Time | Waiting Time |");
    
            for (int i = 0; i < numberOfProcess * 2; i++) {
                if (i % 2 == 0) {
                    System.out.println("|---------|--------------|------------|----------|-------------|-----------------|--------------|");
                }
                else {
                    System.out.print("| " + processName.get(i / 2) + "      ");
    
                    System.out.print("| " + arrivalTime.get(i / 2));
                    for (int j = 0; j < 13 - String.valueOf(arrivalTime.get(i / 2)).length(); j++) {
                        System.out.print(" ");
                    }
    
                    System.out.print("| " + burstTime.get(i / 2));
                    for (int j = 0; j < 11 - String.valueOf(burstTime.get(i / 2)).length(); j++) {
                        System.out.print(" ");
                    }
    
                    System.out.print("| " + priority.get(i / 2));
                    for (int j = 0; j < 9 - String.valueOf(priority.get(i / 2)).length(); j++) {
                        System.out.print(" ");
                    }
    
                    System.out.print("| " + finishTime.get(i / 2));
                    for (int j = 0; j < 12 - String.valueOf(finishTime.get(i / 2)).length(); j++) {
                        System.out.print(" ");
                    }
    
                    System.out.print("| " + turnaroundTime.get(i / 2));
                    for (int j = 0; j < 16 - String.valueOf(turnaroundTime.get(i / 2)).length(); j++) {
                        System.out.print(" ");
                    }
    
                    System.out.print("| " + waitingTime.get(i / 2));
                    for (int j = 0; j < 13 - String.valueOf(waitingTime.get(i / 2)).length(); j++) {
                        System.out.print(" ");
                    }
    
                    System.out.println("|");
                }
            }
    
            System.out.println("-------------------------------------------------------------------------------------------------");
        }
        
        //-------------------------------------------------------------------------------------------------------------------
            
        private void displayResult() {
            // Obtain the list of Turnaround Time and Waiting Time
            for (int i = 0; i < numberOfProcess; i++) {
                turnaroundTime.add(finishTime.get(i) - arrivalTime.get(i));
                waitingTime.add(turnaroundTime.get(i) - burstTime.get(i));
            }
    
            // Display the Gantt Chart and Table, will change later to visual form
            printGanttChart();
            printTable();
    
            // Obtain and display the Average Turnaround Time and Average Waiting Time
            totalTurnaroundTime = 0;
            totalWaitingTime = 0;
            for (int i = 0; i < numberOfProcess; i++) {
                totalTurnaroundTime += turnaroundTime.get(i);
                totalWaitingTime += waitingTime.get(i);
            }
            averageTurnaroundTime = (double)totalTurnaroundTime / numberOfProcess;
            averageWaitingTime = (double)totalWaitingTime / numberOfProcess;
    
            String formattedAverageTurnaroundTime = String.format("%.2f", averageTurnaroundTime);
            String formattedAverageWaitingTime = String.format("%.2f", averageWaitingTime);
    
            System.out.println("\nAverage Turnaround Time: " + formattedAverageTurnaroundTime);
            System.out.println("\nAverage Waiting Time: " + formattedAverageWaitingTime);
            System.out.println("\n-------------------------------------------------------------------------------------------------");
        }
}
