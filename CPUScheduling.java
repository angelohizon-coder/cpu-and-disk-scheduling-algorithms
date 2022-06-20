// References
// https://computinglearner.com/how-to-create-a-java-console-menu-application/
// https://www.thejavaprogrammer.com/java-program-first-come-first-serve-fcfs-scheduling-algorithm/
// https://www.thejavaprogrammer.com/java-program-shortest-job-first-sjf-scheduling/
// https://shivammitra.com/operating%20system/preemptive-priority-program/
// https://educativesite.com/shortest-job-first-sjf-scheduling-algorithm-in-c-and-c-with-gantt-chart/

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

		// create array list for gantt chart
    	ArrayList<Integer> gantt = new ArrayList<Integer>();
    	gantt.add(min(at));

		// finding completion times
		for (int i = 0 ; i < size; i++) {
			if (i == 0) {
				ct[i] = at[i] + bt[i];
			}
			else {
				// Checks if there is idle time
				if (at[i] - ct[i - 1] > 0) {
					// Adds the Idle State
					gantt.add(0);

					// Adds the idle gap value.
					gantt.add(at[i]);
				}
				if (at[i] > ct[i-1]) {
					ct[i] = at[i] + bt[i];
				} else {
					ct[i] = ct[i-1] + bt[i];
				}
			}
			// Turn-around time
			tat[i] = ct[i] - at[i];

			// Waiting time
			wt[i] = tat[i] - bt[i];

			// gantt chart
			gantt.add(pid[i]);
			gantt.add(ct[i]);
		}

		// generate gantt chart
    	ganttChart(gantt);
    	ganttChart2(gantt);

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

		// create array list for gantt chart
		int all_completed = 0;
    	ArrayList<Integer> gantt = new ArrayList<Integer>();
		
    	// for checking idle time
    	int old_bt = 0;
    	int old_time = min(at);

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
			if (c==size) {
				st++;
			}
			else{
				ct[c] = st + bt[c];				
				tat[c] = ct[c] - at[c];
				wt[c] = tat[c] - bt[c];

				// check if there is idle time
				if (at[c] > (old_time + old_bt)) {
					// add idle state and gap value
					gantt.add(old_time + old_bt);
					gantt.add(0);
				}
				old_time = st;
				old_bt = bt[c];

				// add system time and pid to gantt chart
				gantt.add(st);
				gantt.add(pid[c]);
				
				st += bt[c];
				f[c] = 1;
				tot++;

				// gantt chart
                all_completed = st;	
			}
		}

		// generate gantt chart
		gantt.add(all_completed);
    	ganttChart(gantt);
    	ganttChart2(gantt);

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

    	// create array list for gantt chart
    	int old_pid = 0;
    	int new_pid = 0;
    	int all_completed = 0;
    	ArrayList<Integer> gantt = new ArrayList<Integer>();

    	// for checking idle time
    	int old_time = min(at);

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
        		// check if there is idle time
				if ((old_time+1) < st) {
					// add idle state and gap value
					gantt.add(old_time+1);
					gantt.add(0);
				}
				old_time = st;

            	// add new pid to gantt chart; check if pid changes; this means old process is replaced with new
            	new_pid = pid[c];
            	if (old_pid != new_pid) {
            		gantt.add(st);
            		gantt.add(new_pid);
            		old_pid = new_pid;
				}

				bt[c]--;
				st++;

            	if (bt[c]==0) {
            	    ct[c] = st;
            	    f[c] = 1;
            	    tot++;
            	    
            	    // gantt chart
                	all_completed = st;	
            	}
        	}
    	}
		for (i=0; i<size; i++) {
			tat[i] = ct[i] - at[i];
			wt[i] = tat[i] - k[i];
		}

		// generate gantt chart
		gantt.add(all_completed);
    	ganttChart(gantt);
    	ganttChart2(gantt);

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

    	// create array list for gantt chart
    	int old_pid = 0;
    	int new_pid = 0;
    	int all_completed = 0;
    	ArrayList<Integer> gantt = new ArrayList<Integer>();

    	// for checking idle time
    	int old_time = min(at);

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
        		// check if there is idle time
				if ((old_time+1) < current_time) {
					// add idle state and gap value
					gantt.add(old_time+1);
					gantt.add(0);
				}
				old_time = current_time;

            	// add new pid to gantt chart; check if pid changes; this means old process is replaced with new
            	new_pid = pid[idx];
            	if (old_pid != new_pid) {
            		gantt.add(current_time);
            		gantt.add(new_pid);
            		old_pid = new_pid;
				}

				burst_remaining[idx] -= 1;  
				current_time++;

            	if (burst_remaining[idx] == 0) {
                	ct[idx] = current_time;
                	tat[idx] = ct[idx] - at[idx];
                	wt[idx] = tat[idx] - bt[idx];

					is_completed[idx] = 1;
                	completed++;

                	// gantt chart
                	all_completed = current_time;	
            	}
        	}
        	else {
            	current_time++;
        	}  
    	}

    	// generate gantt chart
    	gantt.add(all_completed);
    	ganttChart(gantt);
    	ganttChart2(gantt);

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
	public static void algorithm(int size, int[][] array, int algo) {
		int[][] tempArray = array;

		switch (algo) {
			case 1:
				System.out.println("\nFirst Come First Serve (FCFS)");
				sortWithIndex(array, array[1]);
				array = fcfsProcess(array, size); break;

			case 2:
				System.out.println("\nShortest Job First (Non-Preemptive) (SJF)");
				array = sjfNPProcess(array, size); break;

			case 3:
				System.out.println("\nShortest Job First (Preemtive) (SJF)");
				array = sjfPProcess(array, size); break;

			case 4:
				// input priority if array is empty
				if (array[3][0] == 0) {
					array[3] = inputPrio(size);
				}
				System.out.println("\nPriority (Preemtive) (Prio)");
				array = prioPProcess(array, size);
		}

		// display processes along with all details
		System.out.println("\nGantt Table");

		// display results
		if (algo == 1) {
			sortWithIndex(tempArray, tempArray[0]);
			display(array, size);
		}
		else if (algo == 4) {
			displayPrio(array, size);
		}
		else {
			display(array, size);
		}
		result(array[5], array[6], size);

		char choice = repeatAlgorithm();

        if (choice == 'y' || choice == 'Y') {
            algorithm(size, tempArray, algo);
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
                            "[C] Shortest Job First (Preemptive) (SJF)",
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
						algorithm(size, array, 1);
						break;
                    case 'b':;    
                    case 'B':
						algorithm(size, array, 2);
						break;
                    case 'c':;
                    case 'C':
						algorithm(size, array, 3);
						break;
					case 'd':;
                    case 'D':
                    	algorithm(size, array, 4);
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
				// System.out.println(ex);
                // ex.printStackTrace(System.out);
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

		int[] at = new int[size];
		int[] bt = new int[size];
		int temp;
		
		try {
			System.out.println("\nInput individual arrival time:");
			for (int i = 0; i<size; i++) {
				while (true) {
					System.out.print("AT" + (i+1) + ": ");
					at[i] = scan.nextInt();
					temp = at[i];
					
					if (temp >= 0 && temp <= 99) {
						break;
					} else {
						System.out.print("========================================\n");
						System.out.println("Input arrival time >= 0 AND <= 99");
						System.out.print("========================================\n");
					}
				}
			}
			System.out.println("\nInput individual burst time:");
			for (int i = 0; i<size; i++) {
				while (true) {
					System.out.print("BT" + (i+1) + ": ");
					bt[i] = scan.nextInt();
					temp = bt[i];
					
					if (temp >= 1 && temp <= 99) {
						break;
					} else {
						System.out.print("========================================\n");
						System.out.println("Input burst time >= 1 AND <= 99");
						System.out.print("========================================\n");
					}
				}
			}
		} catch(Exception ex) {
			System.out.print("========================================\n");
			System.out.println("Invalid Input"); 
			System.out.print("========================================\n");
			main(null);
		}

		// update values
		array[1] = at;
		array[2] = bt;

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
        System.out.print("Choose your option: ");
    }
    public static void display(int[][] array, int size) {
		System.out.println("PID  Arrival | Burst | Complete | TurnAround | Waiting");
		
		for(int i=0; i<size; i++){
			System.out.println(array[0][i]+"\t"+array[1][i]+"\t "+array[2][i]+"\t "+array[4][i]+"\t\t"+array[5][i]+"\t "+array[6][i]);
		}
	}
	public static void displayPrio(int[][] array, int size) {
		System.out.println("PID  Arrival | Burst | Complete | TurnAround | Waiting | Prio");
		
		for(int i=0; i<size; i++){
			System.out.println(array[0][i]+"\t"+array[1][i]+"\t "+array[2][i]+"\t "+array[4][i]+"\t\t"+array[5][i]+"\t "+array[6][i]+"\t " +array[3][i]);
		}
	}
	public static void result(int[] tat, int[] wt, int size) {
        float avg_tat = (float)sum(tat)/(float)size;
		float avg_wt = (float)sum(wt)/(float)size;
		
		System.out.printf("\nAverage Turnaround Time is = %.2f", avg_tat);
		System.out.print(" ms");
		System.out.printf("\nAverage Waiting Time is = %.2f", avg_wt);
		System.out.print(" ms");
		System.out.println("\n");
    }
    // if array contains 0 time, it must be idle time
    public static void ganttChart(ArrayList<Integer> arr) {
    	String temp = "";
    	for (int i=0; i<arr.size(); i++) {
    		if (i%2 == 1) {
    			if (arr.get(i) == 0)
    				temp += "| IS |"; // pid
    			else
      				temp += "| P" + arr.get(i) + " |"; // pid
    		}
      		else
      			temp += " " + arr.get(i) + " "; // time
    	}
    	System.out.println("\nGantt Chart (Simple)");
    	System.out.println(temp);
    }
    // if array contains 0 time, it must be idle time
    public static void ganttChart2(ArrayList<Integer> arr) {
    	// print title
		System.out.println("\nGantt Chart (Visualized)");

    	// initialization
      	int all_completed = arr.get(arr.size()-1);
      	int arrival = arr.get(0);

      	if ((all_completed - arrival) > 45) {
      		System.out.println("Too big to visualize....");
      		return;
      	}

    	ArrayList<Integer> pidl = new ArrayList<Integer>();
    	ArrayList<Integer> timel = new ArrayList<Integer>();

    	// separate array list to two arrays
    	for (int i=0; i<arr.size(); i++) {
    		if (i%2 == 1)
      			pidl.add(arr.get(i));
      		else {
      			timel.add(arr.get(i));
      		}
    	}

    	// array list to array
    	Integer[] pid = new Integer[pidl.size()];
    	Integer[] time = new Integer[timel.size()];
    	for (int i = 0; i < pidl.size(); i++)
            pid[i] = pidl.get(i);
        for (int i = 0; i < timel.size(); i++)
            time[i] = timel.get(i);

	    // print top bar
	    System.out.print(" ");
        for (int i=arrival, j=0; i<all_completed; i++) {
			if (i == time[j]) {
        		System.out.print(" ");
        		j++;
			}
			System.out.print("--");				
        } 
    	System.out.print("\n");
 
	    // printing process id in the middle
	    System.out.print(" ");
        for (int i=arrival, j=0; i<all_completed; i++) { 
	        if (i == time[j]) {
				System.out.print("|");
				if (pid[j] == 0)
					System.out.print("IS");
				else
					System.out.printf("P%d", pid[j]);
				j++;
			}
			else
				System.out.print("  ");	
        }
    	System.out.print("|\n");

	    // printing bottom bar
	    System.out.print(" ");
        for (int i=arrival, j=0; i<all_completed; i++) {
			if (i == time[j]) {
        		System.out.print(" ");
        		j++;
        	}
        	System.out.print("--");
        }
        System.out.print("\n");
 
	    // printing the time line
	    for (int i=arrival, j=0; i<all_completed; i++) {
	    	System.out.print("  ");
		    if (i == time[j]) {
		    	if (time[j] > 9)
		    		System.out.print("\b\b ");
		        System.out.printf("%d", time[j]);
		        j++;
		    }
			
	    }
	    System.out.printf(" %d", all_completed);
	    System.out.print("\n");
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
			pid[i] = i+1;
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
	public static int min(int[] arr){  
		int min = 9999;  
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] < min) {
				min = arr[i];
			}
	    }  
	    return min;  
	}
	public static int max(int[] arr){  
		int max = -1;  
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
	    }  
	    return max;  
	}    
	public static int sum(int[] arr) {
        int sum = 0;
		
        for (int i=0; i<arr.length; i++)
            sum += arr[i];
  
        return sum;
    }
	
}
