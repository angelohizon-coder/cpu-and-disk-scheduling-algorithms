// References
// https://computinglearner.com/how-to-create-a-java-console-menu-application/
// https://www.thejavaprogrammer.com/java-program-first-come-first-serve-fcfs-scheduling-algorithm/
// https://www.thejavaprogrammer.com/java-program-shortest-job-first-sjf-scheduling/
// https://shivammitra.com/operating%20system/preemptive-priority-program/


//package com.RP;

import static java.lang.System.exit;
import java.util.*;

public class CPUScheduling {
    public static void main(String[] args) {
        // Variable Declarations
        int size;
		int[][] array;
		
        size = input();
        array = inputArray(size);
	
        choice(size, array);
	}

// Process
	public static int[][] fcfsProcess(int[][] array, int size) {
		// initialize values
		int[] pid = array[0];
		int[] at = array[1];
		int[] bt = array[2];
		int[] ct = new int[size];
		int[] tat = new int[size];
		int[] wt = new int[size];

		// finding completion times
		for (int i = 0 ; i < size; i++) {
			if (i == 0) {
				ct[i] = at[i] + bt[i];
			}
			else {
				if (at[i] > ct[i-1]) {
					ct[i] = at[i] + bt[i];
				}
				else
					ct[i] = ct[i-1] + bt[i];
			}
			tat[i] = ct[i] - at[i];
			wt[i] = tat[i] - bt[i];
		}

		// update values
		array[0] = pid;
		array[1] = at;
		array[2] = bt;
		array[4] = ct;
		array[5] = tat;
		array[6] = wt;

		return array;
	}
	public static int[][] sjfNPProcess(int[][] array, int size) {
		// initialize values
		int[] pid = array[0];
		int[] at = array[1];
		int[] bt = array[2];
		int[] ct = new int[size];
		int[] tat = new int[size];
		int[] wt = new int[size];

		int[] f = new int[size];
		int st=0, tot=0;
		
		// finding completion times
		while(true){
			int c=size, min=9999;
			if (tot == size) // total no of process = completed process loop will be terminated
				break;
			for (int i=0; i<size; i++){
				/*
				* If i'th process arrival time <= system time and its flag=0 and burst<min
				* That process will be executed first
				*/
				if ((at[i] <= st) && (f[i] == 0) && (bt[i] < min)){
					min=bt[i];
					c=i;
				}
			}
			/* If c==n means c value can not be updated because no process arrival time < system time so we increase the system time */
			if (c==size)
				st++;
			else{
				ct[c] = st + bt[c];
				st += bt[c];
				tat[c] = ct[c] - at[c];
				wt[c] = tat[c] - bt[c];
				f[c] = 1;
				tot++;
			}
		}

		// update values
		array[0] = pid;
		array[1] = at;
		array[2] = bt;
		array[4] = ct;
		array[5] = tat;
		array[6] = wt;

		return array;
	}
	public static int[][] sjfPProcess(int[][] array, int size) {
		// initialize values
		int[] pid = array[0];
		int[] at = array[1];
		int[] bt = array[2];
		int[] ct = new int[size];
		int[] tat = new int[size];
		int[] wt = new int[size];

		int[] f = new int[size];
		Arrays.fill(f, 0);
		int[] k = new int[size];
		System.arraycopy(bt, 0, k, 0, bt.length);
    	int i, st=0, tot=0;

    	// finding completion times
		while(true) {
       		int min=9999,c=size;
        	if (tot==size)
            	break;
        	for (i=0;i<size;i++) {
            	if ((at[i]<=st) && (f[i]==0) && (bt[i]<min)) {
                	min=bt[i];
                	c=i;
            	}
        	}
        	if (c==size)
            	st++;
        	else {
            	bt[c]--;
            	st++;
            	if (bt[c]==0) {
            	    ct[c]= st;
            	    f[c]=1;
            	    tot++;
            	}
        	}
    	}
		for (i=0; i<size; i++) {
			tat[i] = ct[i] - at[i];
			wt[i] = tat[i] - k[i];
		}

		// update values
		array[0] = pid;
		array[1] = at;
		array[2] = k;
		array[4] = ct;
		array[5] = tat;
		array[6] = wt;

		return array;
	}
	public static int[][] prioPProcess(int[][] array, int size) {
		// initialize values
		int[] pid = array[0];
		int[] at = array[1];
		int[] bt = array[2];
		int[] prio = array[3];
		int[] ct = new int[size];
		int[] tat = new int[size];
		int[] wt = new int[size];

		int[] is_completed = new int[size];
		Arrays.fill(is_completed, 0);
		int[] burst_remaining = new int[size];
		System.arraycopy(bt, 0, burst_remaining, 0, bt.length);

		int current_time = 0;
    	int completed = 0;

		// finding completion times
		while (completed != size) {
        	int idx = -1;
        	int min = 9999;

        	for (int i = 0; i < size; i++) {
            	if ((at[i] <= current_time) && (is_completed[i] == 0)) {
                	if (prio[i] < min) {
                    	min = prio[i];
                    	idx = i;
                	}
                	if (prio[i] == min) {
                    	if (at[i] < at[idx]) {
                       		min = prio[i];
                        	idx = i;
                    	}
                	}
            	}
        	}
        	if (idx != -1) {
            	burst_remaining[idx] -= 1;
            	current_time++;
            
            	if (burst_remaining[idx] == 0) {
                	ct[idx] = current_time;
                	tat[idx] = ct[idx] - at[idx];
                	wt[idx] = tat[idx] - bt[idx];

                	is_completed[idx] = 1;
                	completed++;
            	}
        	}
        	else {
            	current_time++;
        	}  
    	}

		// update values
		array[0] = pid;
		array[1] = at;
		array[2] = bt;
		array[3] = prio;
		array[4] = ct;
		array[5] = tat;
		array[6] = wt;

		return array;
	}

// Options
	/* array contents
		array 0 = PID
		array 1 = Arrival Time
		array 2 = Burst Time
		array 3 = Priority
		array 4 = Finish Time
		array 5 = TurnAround Time
		array 6 = Waiting Time */
	public static void fcfs(int size, int[][] array) {
		int[][] tempArray = array;

		// initialize values and sort according to arrival times
		sortWithIndex(array, array[1]);

		// actual process
		array = fcfsProcess(array, size);

		// display processes along with all details
		System.out.println("\nFirst Come First Serve (FCFS)");
		sortWithIndex(tempArray, tempArray[0]);
		display(array, size);
		result(array[5], array[6], size);

		char choice = repeatAlgorithm();

        if(choice == 'y' || choice == 'Y') {
            fcfs(size, tempArray);
        } else {
            repeat(size, tempArray);
        }
	}
	public static void sjfNP(int size, int[][] array){
		int[][] tempArray = array;

		// actual process
		array = sjfNPProcess(array, size);

		// Display processes along with all details
		System.out.println("\nShortest Job First (Non-Preemptive) (SJF)");
		display(array, size);
		result(array[5], array[6], size);

		char choice = repeatAlgorithm();

        if(choice == 'y' || choice == 'Y') {
            sjfNP(size, tempArray);
        } else {
            repeat(size, tempArray);
        }
	}
	public static void sjfP(int size, int[][] array) {
		int[][] tempArray = array;

		// actual process
		array = sjfPProcess(array, size);

		// Display processes along with all details
		System.out.println("\nShortest Job First (Preemtive) (SJF)");
		display(array, size);
		result(array[5], array[6], size);

		char choice = repeatAlgorithm();

        if(choice == 'y' || choice == 'Y') {
            sjfP(size, tempArray);
        } else {
            repeat(size, tempArray);
        }
	}
	public static void prioP(int size, int[][] array) {
		int[][] tempArray = array;

		// input priority if array is empty
		if (array[3][0] == 0) {
			array[3] = inputPrio(size);
		}

		// actual process
		array = prioPProcess(array, size);

		// Display processes along with all details
		System.out.println("\nPriority (Preemtive) (Prio)");
		displayPrio(array, size);
		result(array[5], array[6], size);

		char choice = repeatAlgorithm();

        if(choice == 'y' || choice == 'Y') {
            prioP(size, tempArray);
        } else {
            repeat(size, tempArray);
        }
	}

// Misc Functions
    public static void choice(int size, int[][] array) {
        char option = ' ';
        Scanner scan = new Scanner(System.in);
        String[] options = {"[A] First Come First Serve (FCFS)",
                            "[B] Shortest Job First (Non-Preemptive) (SJF)",
                            "[C] Shortest Job First (Preemtive) (SJF)",
                            "[D] Priority (Preemtive) (Prio)",
                            "[E] Exit"
        };

        printMenu(options);
        option = scan.next().charAt(0);

        while(option != 'e' || option != 'E') {
            try {
                switch (option) {
                    case 'a':;
                    case 'A': 
						fcfs(size, array);
						break;
                    case 'b':;    
                    case 'B':
						sjfNP(size, array);
						break;
                    case 'c':;
                    case 'C':
						sjfP(size, array);
						break;
					case 'd':;
                    case 'D':
                    	prioP(size, array);
						break;
                    case 'e':;
                    case 'E':
                    	System.out.println("Thank you for using our program.");
                    	exit(0); break;
                    default: throw new Exception();
                }
            }
            catch (Exception ex){
                System.out.print("========================================\n");
                System.out.println("Invalid Input: Character input must range from A to E");
                System.out.print("========================================\n\n");
				choice(size, array);
            }
        }
    }
    public static int input() {
        int temp = 0;
        Scanner scan = new Scanner(System.in);

        while(true) {
            try {
                System.out.print("Input Number of Processes [2-9]: ");  
                temp = scan.nextInt();

                if(temp >= 2 && temp <= 9) {
                    break;
                } else {
                    System.out.print("========================================\n");
                    System.out.println("Number of Processes >= 2 AND <= 9");
                    System.out.print("========================================\n");
                }
            } catch(Exception ex) {
                System.out.print("========================================\n");
                System.out.println("Invalid Input"); 
                System.out.print("========================================\n");
                main(null);
            }
        }

        return temp;
    }
    public static int[][] inputArray(int size) {
        Scanner scan = new Scanner(System.in);
        int[][] array = new int[7][size];
        array[0] = pidList(size);

		int temp;
		String col = "";
		
		for (int i=1; i<3; i++) {
			switch (i) {
				case 1: col = "AT";
					System.out.println("\nInput individual arrival time:"); break;	
				case 2: col = "BT";
					System.out.println("\nInput individual burst time:");
			}
			for (int j=0; j<size; j++) {  
				while (true) {
					try {
						System.out.print(col + (j+1) + ": ");
						array[i][j] = scan.nextInt();
						temp = array[i][j];
						
						if(temp >= 0 && temp <= 999) {
							break;
						} else {
							System.out.print("========================================\n");
							System.out.println("Input " + col + " >= 0 AND <= 999");
							System.out.print("========================================\n");
						}
					} catch(Exception ex) {
						System.out.print("========================================\n");
						System.out.println("Invalid Input"); 
						System.out.print("========================================\n");
						main(null);
					}
				}
			}
		}
		
        return array;
    }
    public static int[] inputPrio(int size) {
    	Scanner scan = new Scanner(System.in);
    	int[] array = new int[size];
    	int temp;

    	System.out.println("\nInput individual priority (Lower # = higher priority):");
    	for (int i=0; i<size; i++) {  
			while (true) {
				try {
					System.out.print("Prio" + (i+1) + ": ");
					array[i] = scan.nextInt();
					temp = array[i];
					
					if(temp >= 1 && temp <= 99) {
						break;
					} else {
						System.out.print("========================================\n");
						System.out.println("Input priority >= 1 AND <= 99");
						System.out.print("========================================\n");
					}
				} catch(Exception ex) {
					System.out.print("========================================\n");
					System.out.println("Invalid Input"); 
					System.out.print("========================================\n");
					main(null);
				}
			}
		}

		return array;
    }
    public static void printMenu(String[] options) {
        System.out.print("========================================\n");
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }
    public static void display(int[][] array, int size) {
		System.out.println("\nPID  Arrival | Burst | Complete | TurnAround | Waiting");
		
		for(int i=0; i<size; i++){
			System.out.println(array[0][i]+"\t"+array[1][i]+"\t "+array[2][i]+"\t "+array[4][i]+"\t\t"+array[5][i]+"\t "+array[6][i]);
		}
	}
	public static void displayPrio(int[][] array, int size) {
		System.out.println("\nPID  Arrival | Burst | Prio | Complete | TurnAround | Waiting");
		
		for(int i=0; i<size; i++){
			System.out.println(array[0][i]+"\t"+array[1][i]+"\t "+array[2][i]+"\t "+array[3][i]+"\t "+array[4][i]+"\t\t"+array[5][i]+"\t "+array[6][i]);
		}
	}
	public static void result(int[] tat, int[] wt, int size) {
        float avg_tat = (float)sum(tat)/(float)size;
		float avg_wt = (float)sum(wt)/(float)size;
		
		System.out.printf("\nAverage Turnaround Time is = %.2f", avg_tat);
		System.out.printf("\nAverage Waiting Time is = %.2f", avg_wt);
		System.out.println("\n");
    }
    public static void repeat(int size, int[][] tempArray) {
        char choice = repeatInput();

        try {
            switch(choice) {
                case 'a':;
                case 'A': 
                    choice(size, tempArray); break;
                case 'b':;    
                case 'B': 
                    main(null); break;
                case 'c':;
                case 'C': 
                	System.out.println("Thank you for using our program.");
                    exit(0); break;
                default: throw new Exception();
            }
        } catch (Exception ex) {
            System.out.print("========================================\n");
            System.out.println("Invalid Input: Character input must range from A to C");
            System.out.print("========================================\n");
            repeat(size, tempArray);
        }
    }
    public static char repeatAlgorithm() {
        Scanner scan = new Scanner(System.in);

        System.out.print("========================================\n");
        System.out.println("Input again (Y/N)?");
        System.out.println("\tY = Repeat algorithm");
        System.out.println("\tN = Exit algorithm");
        System.out.print("Option: ");
        char choice = scan.next().charAt(0);

        return choice;
    }
    public static char repeatInput() {
        Scanner scan = new Scanner(System.in);

        System.out.print("========================================\n");
        System.out.println("Input again (A/B/C)?");
        System.out.println("\tA = Repeat input and choose another algorithm");
        System.out.println("\tB = New input and choose another algorithm");
        System.out.println("\tC = Exit the program");
        System.out.print("Option: ");
        char choice = scan.next().charAt(0);

        return choice;
    }
	public static int[] pidList(int size) {
		int[] pid = new int[size];
		
		for(int i=0;i<size;i++) {
			pid[i] = i;
		}
		
		return pid;
	}
    public static void sortWithIndex(int[][] arr, int[] index) {
		if (!isSorted(index)) {
			for (int i=0; i<index.length; i++) {
				for (int j=i+1; j<index.length; j++) {
					int temp = 0;
					if (index[i] > index[j]) {
						for (int k=0; k<7; k++) {
							temp = arr[k][i];
							arr[k][i] = arr[k][j];
							arr[k][j] = temp;
						}
					}
				}
			}
		}
    }
	public static boolean isSorted(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i + 1] < arr[i]) {
				return false;
			};
		}
		return true;
	}
	public static int sum(int[] arr) {
        int sum = 0;
		
        for (int i=0; i<arr.length; i++)
            sum += arr[i];
  
        return sum;
    }
	
}