// References
// https://computinglearner.com/how-to-create-a-java-console-menu-application/
// https://www.geeksforgeeks.org/fcfs-disk-scheduling-algorithms/


package com.RP;

import static java.lang.System.exit;
import java.util.*;

public class DiskScheduling {
    public static void main(String[] args) {
        // Variable Declarations
        int[] array, temp;

        temp = input();
        array = inputArray(temp[0], temp[1], temp[2]);

        choice(temp[0], temp[1], temp[2], array);
    }

// Options
    public static void fcfs(int header, int disk_size, int size, int[] array) {
        Scanner scan = new Scanner(System.in);

        int[] temp = {header,disk_size,size};
        int[] tempArray = array;
        int seek = 0;
        int distance, cur_track;

        for (int i = 0; i < size; i++)
        {
            cur_track = array[i];
     
            // calculate absolute distance
            distance = Math.abs(cur_track - header);
     
            // increase the total count
            seek += distance;
     
            // accessed track is now new header
            header = cur_track;
        }
        
        result(seek, size);
        
        System.out.print(temp[0] + ", ");
        for (int i = 0; i < size; i++)
        {
            if (i == size-1) {
                System.out.print(array[i] + "\n");
            } else {
                System.out.print(array[i] + ", ");
            }
        }

        printGraph(temp[0], disk_size, size, array);
        char choice = ' ';

        while(choice != 'y' || choice != 'N' || choice != 'n' || choice != 'N') {
            choice = repeatAlgorithm();

            if (choice == 'y' || choice == 'Y') {
                fcfs(temp[0], temp[1], temp[2], tempArray);
            } else if (choice == 'n' || choice == 'N') {
                repeat(temp, tempArray);
            }
        }
    }
    public static void sstf(int header, int disk_size, int size, int[] array) {
        int[] placeholder = new int[size];
        int[] temp = {header,disk_size,size};
        int[] tempArray = array;
        double seek = 0.0;
        double minimum = 0.0;
        if (size <= 0) {
            return;
        }
        //This are storing the information of seek sequence
        int[] skeek = new int[size + 1];
        //Create 2d array which is used to store distance and visited status
        int[][] auxiliary = new int[size][2];
        for (int i = 0; i < size; ++i) {
            // set initial distance
            auxiliary[i][0] = 0;
            // set the visiting element status
            auxiliary[i][1] = 0;
        }
        // Loop controlling variable
        int i, j = 0;
        int location = 0;
        for (i = 0; i < size; i++)
        {
            skeek[i] = header;
            // Find distance using header value
            for (j = 0; j < size; ++j)
            {
                auxiliary[j][0] = array[j] - header;
                if (auxiliary[j][0] < 0)
                {
                    auxiliary[j][0] = -auxiliary[j][0];
                }
            }
            minimum = Integer.MAX_VALUE;
            location = -1;
            //Find the minimum element location that is not visited
            for (j = 0; j < size; ++j)
            {
                if (auxiliary[j][1] == 0 && auxiliary[j][0] <= minimum)
                {
                    // Get the new minimum distance element location
                    location = j;
                    minimum = auxiliary[j][0];
                }
            }
            // Update the visited status of new get element
            auxiliary[location][1] = 1;
            // Update header data into current track value
            header = array[location];
            // Add current distance into seek
            seek += auxiliary[location][0];
        }

        if (header != 0) {
            // Add last skee info
            skeek[size] = header;
        }
        
        result((int)seek, size);
        
        for (i = 0; i <= size; i++) {
            if (i == size) {
                System.out.print(skeek[i] + "\n");
            } else {
                System.out.print(skeek[i] + ", ");
            }
        }

        // Gets the elements of the array excluding the initial element to prevent ArrayIndexOutOfBoundsException:
        for (i = 1; i <= size; i++) {
            placeholder[i-1] = skeek[i];
        }
        
        printGraph(temp[0], disk_size, size, placeholder);
        char choice = ' ';

        while(choice != 'y' || choice != 'N' || choice != 'n' || choice != 'N') {
            choice = repeatAlgorithm();

            if (choice == 'y' || choice == 'Y') {
                sstf(temp[0], temp[1], temp[2], tempArray);
            } else if (choice == 'n' || choice == 'N') {
                repeat(temp, tempArray);
            }
        }
    }
    public static void scanLook(int header, int disk_size, int size, int[] array, String direction) {
        int[] placeholder = new int[size];
        int[] temp = {header, disk_size, size};
        int[] tempArray = array;
        int seek = 0;
        int distance, cur_track;
        Vector<Integer> left = new Vector<Integer>();
        Vector<Integer> right = new Vector<Integer>();
        Vector<Integer> seek_sequence = new Vector<Integer>();
     
        // appending end values
        // which has to be visited
        // before reversing the direction
        if (direction == "left") {
            placeholder = new int[size+1];
            left.add(0);
        }
     
        for (int i = 0; i < size; i++)
        {
            if (array[i] < header)
                left.add(array[i]);
            if (array[i] > header)
                right.add(array[i]);
        }
     
        // sorting left and right vectors
        Collections.sort(left);
        Collections.sort(right);
     
        // run the while loop two times.
        // one by one scanning right
        // and left of the header
        int run = 2;
        while (run-- >0)
        {
            if (direction == "left")
            {
                for (int i = left.size() - 1; i >= 0; i--)
                {
                    cur_track = left.get(i);
     
                    // appending current track to seek sequence
                    seek_sequence.add(cur_track);
     
                    // calculate absolute distance
                    distance = Math.abs(cur_track - header);
     
                    // increase the total count
                    seek += distance;
     
                    // accessed track is now the new header
                    header = cur_track;
                }
                direction = "right";
            }
            else if (direction == "right")
            {
                for (int i = 0; i < right.size(); i++)
                {
                    cur_track = right.get(i);
                     
                    // appending current track to seek sequence
                    seek_sequence.add(cur_track);
     
                    // calculate absolute distance
                    distance = Math.abs(cur_track - header);
     
                    // increase the total count
                    seek += distance;
     
                    // accessed track is now new header
                    header = cur_track;
                }
                direction = "left";
            }
        }

        result(seek, size);
        
        System.out.print(temp[0] + ", ");
        for (int i = 0; i < seek_sequence.size(); i++) {
            if ((i == size && direction.equals("left")) || (i == size-1 && direction.equals("right"))) {
                System.out.print(seek_sequence.get(i) + "\n");
            } else {
                System.out.print(seek_sequence.get(i) + ", ");
            }
        }

        for(int i = 0;i < seek_sequence.size(); i++) {
            placeholder[i] = seek_sequence.get(i);
        }

        printGraph(temp[0], disk_size, size, placeholder);
        char choice = ' ';

        while(choice != 'y' || choice != 'N' || choice != 'n' || choice != 'N') {
            choice = repeatAlgorithm();

            if (choice == 'y' || choice == 'Y') {
                scanLook(temp[0], temp[1], temp[2], tempArray, direction);
            } else if (choice == 'n' || choice == 'N') {
                repeat(temp, tempArray);
            }
        }
    }
	public static void cirScan(int header, int disk_size, int size, int[] array) {
        int[] placeholder = new int[size+2];
		int[] temp = {header, disk_size, size};
        int[] tempArray = array;
		int seek = 0;
		int distance, cur_track;

		Vector<Integer> left = new Vector<Integer>();
		Vector<Integer> right = new Vector<Integer>();
		Vector<Integer> seek_sequence
			= new Vector<Integer>();

		// Appending end values which has
		// to be visited before reversing
		// the direction
		left.add(0);
		right.add(disk_size - 1);

		// Tracks on the left of the
		// head will be serviced when
		// once the head comes back
		// to the beggining (left end).
		for (int i = 0; i < size; i++) {
			if (array[i] < header)
				left.add(array[i]);
			if (array[i] > header)
				right.add(array[i]);
		}

		// Sorting left and right vectors
		Collections.sort(left);
		Collections.sort(right);

		// First service the requests
		// on the right side of the
		// head.
		for (int i = 0; i < right.size(); i++) {
			cur_track = right.get(i);

			// Appending current track to seek sequence
			seek_sequence.add(cur_track);

			// Calculate absolute distance
			distance = Math.abs(cur_track - header);

			// Increase the total count
			seek += distance;

			// Accessed track is now new head
			header = cur_track;
		}

		// Once reached the right end
		// jump to the beggining.
		header = 0;

		// adding seek count for head returning from 199 to
		// 0
		seek += (disk_size - 1);

		// Now service the requests again
		// which are left.
		for (int i = 0; i < left.size(); i++) {
			cur_track = left.get(i);

			// Appending current track to
			// seek sequence
			seek_sequence.add(cur_track);

			// Calculate absolute distance
			distance = Math.abs(cur_track - header);

			// Increase the total count
			seek += distance;

			// Accessed track is now the new head
			header = cur_track;
		}

        result(seek, size);

        System.out.print(temp[0] + ", ");
		for (int i = 0; i < seek_sequence.size(); i++) {
			if (i == size+1) {
                System.out.print(seek_sequence.get(i) + "\n");
            } else {
                System.out.print(seek_sequence.get(i) + ", ");
            }
		}

        for (int i = 0; i < seek_sequence.size(); i++) {
            placeholder[i] = seek_sequence.get(i);
        }
		
        printGraph(temp[0], disk_size, size, placeholder);
		char choice = ' ';

        while(choice != 'y' || choice != 'N' || choice != 'n' || choice != 'N') {
            choice = repeatAlgorithm();

            if (choice == 'y' || choice == 'Y') {
                cirScan(temp[0], temp[1], temp[2], tempArray);
            } else if (choice == 'n' || choice == 'N') {
                repeat(temp, tempArray);
            }
        }
	}
	public static void cirLook(int header, int disk_size, int size, int[] array) {
        int[] placeholder = new int[size];
		int[] temp = {header, disk_size, size};
        int[] tempArray = array;
		int seek = 0;
		int distance, cur_track;
		
		Vector<Integer> left = new Vector<Integer>();
		Vector<Integer> right = new Vector<Integer>();
		Vector<Integer> seek_sequence = new Vector<Integer>();

		// Tracks on the left of the
		// head will be serviced when
		// once the head comes back
		// to the beginning (left end)
		for (int i = 0; i < size; i++)
		{
			if (array[i] < header)
				left.add(array[i]);
			if (array[i] > header)
				right.add(array[i]);
		}

		// Sorting left and right vectors
		Collections.sort(left);
		Collections.sort(right);

		// First service the requests
		// on the right side of the head
		for (int i = 0; i < right.size(); i++)
		{
			cur_track = right.get(i);

			// Appending current track
			// to seek sequence
			seek_sequence.add(cur_track);

			// Calculate absolute distance
			distance = Math.abs(cur_track - header);

			// Increase the total count
			seek += distance;

			// Accessed track is now new head
			header = cur_track;
		}

		// Once reached the right end
		// jump to the last track that
		// is needed to be serviced in
		// left direction
		seek += Math.abs(header - left.get(0));
		header = left.get(0);

		// Now service the requests again
		// which are left
		for (int i = 0; i < left.size(); i++)
		{
			cur_track = left.get(i);

			// Appending current track to
			// seek sequence
			seek_sequence.add(cur_track);

			// Calculate absolute distance
			distance = Math.abs(cur_track - header);

			// Increase the total count
			seek += distance;

			// Accessed track is now the new head
			header = cur_track;
		}

        result(seek, size);
		
        System.out.print(temp[0] + ", ");
		for (int i = 0; i < seek_sequence.size(); i++)
		{
			if (i == size-1) {
                System.out.print(seek_sequence.get(i) + "\n");
            } else {
                System.out.print(seek_sequence.get(i) + ", ");
            }
		}

        for (int i = 0; i < seek_sequence.size(); i++) {
            placeholder[i] = seek_sequence.get(i);
        }
        
        printGraph(temp[0], disk_size, size, placeholder);
		char choice = ' ';

        while(choice != 'y' || choice != 'N' || choice != 'n' || choice != 'N') {
            choice = repeatAlgorithm();

            if (choice == 'y' || choice == 'Y') {
                cirLook(temp[0], temp[1], temp[2], tempArray);
            } else if (choice == 'n' || choice == 'N') {
                repeat(temp, tempArray);
            }
        }
	}
	
// Misc Functions
    public static void choice(int header, int disk_size, int size, int[] array) {
        char option = ' ';
        Scanner scan = new Scanner(System.in);
        String[] options = {"[A] First Come First Serve (FCFS)",
                            "[B] Shortest Seek Time First (SSTF)",
                            "[C] SCAN (Elevator Algorithm)",
                            "[D] C-SCAN (Circular Scan)",
                            "[E] LOOK",
                            "[F] C-LOOK (Circular Look)",
                            "[G] Exit"
        };

        printMenu(options);
        option = scan.next().charAt(0);

        while(option != 'g' || option != 'G') {
            try {
                switch (option) {
                    case 'a':;
                    case 'A': 
                        fcfs(header, disk_size, size, array); break;
                    case 'b':;    
                    case 'B': 
                        sstf(header, disk_size, size, array); break;
                    case 'c':;
                        case 'C': 
                        scanLook(header, disk_size, size, array, "left"); break;
                    case 'd':;
                    case 'D':
                        cirScan(header, disk_size, size, array); break;
                    case 'e':;
                    case 'E': 
                        scanLook(header, disk_size, size, array, "right"); break;
                    case 'f':;
                    case 'F':
                        cirLook(header, disk_size, size, array); break;
                    case 'g':;
                    case 'G': exit(0);
                    default: throw new InputMismatchException();
                }
            }
            catch (InputMismatchException ex){
                System.out.print("========================================\n");
                System.out.println("Invalid Input: Character input must range from A to G");
                System.out.print("========================================\n\n");
                choice(header, disk_size, size, array);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                exit(0);
            }
        }
    }
    public static int conversion(char ch){
        int n;
        n = (int)ch;
        return n;
    }
    public static int[] input() {
        int[] temp = new int[3];
        Scanner scan = new Scanner(System.in);

        try {
            // REPEATS INPUT REQUEST UNTIL INPUT IS RIGHT
            while(true) {
                System.out.print("Input Current Position: ");  
                temp[0] = scan.nextInt();

                if(temp[0] <= 0) {
                    System.out.print("========================================\n");
                    System.out.println("Invalid Input: Current Position is <= 0");
                    System.out.print("========================================\n");
                } else {
                    break;
                }
            }

            // REPEATS INPUT REQUEST UNTIL INPUT IS RIGHT
            while(true) {
                System.out.print("Input Track Size: ");  
                temp[1] = scan.nextInt();

                if(temp[1] < 100 || temp[1] > 200) {
                    System.out.print("========================================\n");
                    System.out.println("Invalid Input: Track Size is < 100 and > 200");
                    System.out.print("========================================\n");
                } else {
                    break;
                }
            }

            // REPEATS INPUT REQUEST UNTIL INPUT IS RIGHT
            while(true) {
                System.out.print("Input Number of Request: ");  
                temp[2] = scan.nextInt();

                if(temp[2] <= 1 || temp[2] > 10) {
                    System.out.print("========================================\n");
                    System.out.println("Invalid Input: Number of Request is < 1 AND > 10");
                    System.out.print("========================================\n");
                } else {
                    break;
                }
            }
        } catch(InputMismatchException ex) {
            System.out.print("========================================\n");
            System.out.println("Invalid Input"); 
            System.out.print("========================================\n");
            main(null);
        } catch(Exception ex) {
            System.out.print("========================================\n");
            System.out.println("Error"); 
            System.out.print("========================================\n");
            exit(0);
        }

        return temp;
    }
    public static int[] inputArray(int header, int trackSize, int size) {
        Scanner scan = new Scanner(System.in);
        int placeholder = 0;
        int[] temp = new int[size];  

        for(int i=0; i<size; i++)  
        {  
            try {
                System.out.print("Loc " + (i+1) + ": ");
                placeholder = scan.nextInt(); 

                if(placeholder < 0) {
                    System.out.print("========================================\n");
                    System.out.println("Invalid Input: Value is less than 0"); 
                    System.out.print("========================================\n");
                    i--;
                } else if(placeholder > (trackSize-1)) {
                    System.out.print("========================================\n");
                    System.out.println("Invalid Input: Value is greater than or equal to " + trackSize); 
                    System.out.print("========================================\n");
                    i--;
                } else {
                    temp[i] = placeholder; 
                }           
            } catch(InputMismatchException ex) {
                System.out.print("========================================\n");
                System.out.println("Invalid Input"); 
                System.out.print("========================================\n");
                main(null);
            } catch(Exception ex) {
                System.out.print("========================================\n");
                System.out.println("Error"); 
                System.out.print("========================================\n");
                exit(0);
            }
        }  

        return temp;
    }
    public static void printGraph(int header, int disk_size, int size, int[] array) {

        // Graph Line
        String dev = "Angelo Louis L. Hizon";

        int[] elements = new int[dev.length()-1];
        char[] ch = dev.toCharArray();
        for(int i=0; i<elements.length; i++){
            elements[i] = conversion(ch[i]);
        }

        for(int i=0; i<disk_size+5; i++){
          if(elements[5]!=111){
            elements[disk_size]=disk_size;
          }
              if(i%25==0){
                System.out.print("|");
              }else if(i%5==0){
                System.out.print("'");
              }
          }

        // Initial point within the graph
        System.out.println();
        if(header<5) {
            System.out.print("*("+header+")-initial position");
        } else {
            for(int i=5; i<=header; i++){
                if(i%5==0){
                    System.out.print(" ");
                } if(i==header) {
                    System.out.print("*("+header+")-initial position");
                }
            
            }
        }

        // Other points within the graph
        System.out.println();
        int elementCounter = 0;
        while(array[elementCounter]<5) {
            System.out.print("*("+array[elementCounter]+")");
            System.out.println();
            if(array.length-1==elementCounter){
                break;
            }

            elementCounter++;
        }
        if((array.length-1) != elementCounter){
            for(int i=5; i<disk_size+1; i++){
                if(i%5==0){
                    System.out.print(" ");
                } 
                 if(array.length==elementCounter) {
                    break;
                 } else if(i==array[elementCounter]) {
                     System.out.print("*("+array[elementCounter]+")");
                     System.out.println();
                     elementCounter++;
                     i = 0;
                } else {
                    while(array[elementCounter]<5){
                        System.out.print("*("+array[elementCounter]+")");
                        System.out.println();
                        elementCounter++;
                        if(array.length==elementCounter){
                            break;
                        }
                    }
                }
            }
        }
    }
    public static void printMenu(String[] options){
        System.out.print("========================================\n");
        for (String option : options){
            System.out.println(option);
        }
        System.out.print("Choose your option : ");
    }
    public static void result(int seek, int size) {
        double quotient = ((double)seek/size);

        System.out.println("Total Head Movement = " + seek + " Cylinders");
        // Limits the number of decimal places
        System.out.format("Average Seek Time = %.2f", quotient);
        System.out.print("\nSeek Sequence : ");
    }
    public static void repeat(int[] temp, int[] tempArray) {
        char choice = repeatInput();

        try {
            switch(choice) {
                case 'a':;
                case 'A': 
                    choice(temp[0], temp[1], temp[2], tempArray); break;
                case 'b':;    
                case 'B': 
                    main(null); break;
                case 'c':;
                case 'C': 
                    exit(0); break;
                default: throw new Exception();
            }
        } catch (Exception ex) {
            System.out.print("========================================\n");
            System.out.println("Invalid Input: Character input must range from A to C");
            System.out.print("========================================\n");
            repeat(temp, tempArray);
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
}