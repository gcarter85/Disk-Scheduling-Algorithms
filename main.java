import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class node {
	// Distance from head to track number
	int distance = 0;
	
	// Has track been accessed or not
	boolean accessed = false;
}

public class main {
	
	static int disk_size = 1024;
	
	public static void CSCAN(Scanner scan, int count) {
		int head = 0;
		int mid = 0;
		double time = 0.0;
		int distance;
		int track = 0;
		int div = 0;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		ArrayList<Double> timeArr = new ArrayList<Double>();
		
		// add values to the arraylist
		for (int i = 0; i < count; i++) {
			arr.add(scan.nextInt());
			timeArr.add(0.0);
		}
		
		// Create right and left sides
		Vector<Integer> leftofleft = new Vector<Integer>();
		Vector<Integer> right = new Vector<Integer>();
		Vector<Integer> left = new Vector<Integer>();
		
		// Add values to right and left sides
		while (!arr.isEmpty()) {
			if (arr.get(0) < head) {
				left.add(arr.get(0));
				arr.remove(0);
			} else {
				right.add(arr.get(0));
				arr.remove(0);
			}
		}
		
		// sort the two
		Collections.sort(left);
		Collections.sort(right);
		
		// iterate until all values are used
		while (!right.isEmpty() || !left.isEmpty()) {
			// go through the right side first
			while (!right.isEmpty()) {
				// Get new track
				track = right.get(0);
				right.remove(0);
				
				// Check distance and add time
				distance = Math.abs(track - head);
				for (int i = 0; i < timeArr.size(); i++) {
					timeArr.set(i, (timeArr.get(i) + (6.2 + (distance * 0.15))));
				}
				time += timeArr.get(0);
				timeArr.remove(0);
				
				// Track is now new head
				head = track;
				div++;
				
				// Add new value into the queue
				if (scan.hasNext()) {
					arr.add(scan.nextInt());
					timeArr.add(0.0);
					if(arr.get(0) < head) {
						left.add(arr.get(0));
						arr.remove(0);
						Collections.sort(left);
					} else {
						right.add(arr.get(0));
						arr.remove(0);
						Collections.sort(right);
					}
				}
			}
			
			if (!left.isEmpty()) {
				mid = left.get(left.size() - 1);
			}
			
			// go through left side now
			while (!left.isEmpty()) {
				// get new track
				track = left.get(0);
				left.remove(0);
				
				// Check distance and add time
				distance = Math.abs(track - head);
				for (int i = 0; i < timeArr.size(); i++) {
					timeArr.set(i, (timeArr.get(i) + (6.2 + (distance * 0.15))));
				}
				time += timeArr.get(0);
				timeArr.remove(0);
				
				// Track is now new head
				head = track;
				div++;
				
				// Add new value into the queue
				if (scan.hasNext()) {
					arr.add(scan.nextInt());
					timeArr.add(0.0);
					if(arr.get(0) < mid && arr.get(0) >= head) {
						left.add(arr.get(0));
						arr.remove(0);
						Collections.sort(left);
					} else if (arr.get(0) > mid){
						right.add(arr.get(0));
						arr.remove(0);
						Collections.sort(right);
					} else {
						leftofleft.add(arr.get(0));
						arr.remove(0);
					}
				}
			}
			while (!leftofleft.isEmpty()) {
				if (leftofleft.get(0) < head) {
					left.add(leftofleft.get(0));
					leftofleft.remove(0);
				} else {
					right.add(leftofleft.get(0));
					leftofleft.remove(0);
				}
			}
			Collections.sort(left);
			Collections.sort(right);
		}
		System.out.println("Average: " + (time / div));
	}
	
	public static void SSTF(Scanner scan, int count) {
		// Create node array for the different nodes
		ArrayList<node> diff = new ArrayList<node>();
		
		// Create initial values
		int head = 0;
		double time = 0.0;
		double total = 0.0;
		int div = 0;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		ArrayList<Double> timeArr = new ArrayList<Double>();
		
		// Add values from the list and time values to arraylist
		for (int i = 0; i < count; i++) {
			arr.add(scan.nextInt());
			timeArr.add(0.0);
		}
		// create nodes needed
		for (int i = 0; i < count; i++) {
			diff.add(new node());
		}
		
		// go through until no more nodes
		while (!arr.isEmpty()) {
			// calculate distances
			calcDiff(arr, head, diff);
			
			// find minimum distance
			int index = min(diff);

			// set to true so it won't be accessed again
			diff.get(index).accessed = true;
			
			// calculate time
			time = (6.2 + (diff.get(index).distance * 0.15));
			diff.remove(index);
			
			// add time to all values sitting in queue
			for (int j = div; j < timeArr.size(); j++) {
				timeArr.set(j, (timeArr.get(j) + time));
			}
			
			// add to total time
			total += timeArr.get(div);
			
			// increment all values needed
			if (scan.hasNext()) {
				arr.add(scan.nextInt());
				timeArr.add(0.0);
				diff.add(new node());
			}
			head = arr.get(index);
			arr.remove(index);
			div++;
		}
		
		// Print the average
		System.out.println("Average: " + (total / div));
	}
	
	public static void calcDiff(ArrayList<Integer>queue, int head, ArrayList<node> diff) {
		// calculate the distance for all values
		for (int i = 0; i < diff.size(); i++) {
			diff.get(i).distance = Math.abs(queue.get(i) - head);
		}
	}
	
	public static int min(ArrayList<node> diff) {
		int result = -1;
		int min = Integer.MAX_VALUE;
		
		// check to find the value at the minimum distance from node
		for (int i = 0; i < diff.size(); i++) {
			if (!diff.get(i).accessed && min > diff.get(i).distance) {
				min = diff.get(i).distance;
				result = i;
			}
		}
		
		return result;
	}
	
	public static void FIFO(Scanner scan, int count) {
		// Initializes values
		int head = 0;
		double time = 0.0;
		int distance, track;
		int div = 0;
		ArrayList<Integer> arr = new ArrayList<Integer>();
		ArrayList<Double> timeArr = new ArrayList<Double>();
		
		// Add initial queue to array
		for (int i = 0; i < count; i++) {
			arr.add(scan.nextInt());
			timeArr.add(0.0);
		}
		
		// Run until all values are read through
		while (!arr.isEmpty()) {
			// Set track end
			track = arr.get(0);
			arr.remove(0);
			
			// Check distance and add time
			distance = Math.abs(track - head);
			
			// Add times to all in queue
			for (int j = div; j < timeArr.size(); j++) {
				timeArr.set(j, (timeArr.get(j) + (distance * .15) + 6.2));
			}
			time += timeArr.get(div);
			
			// track is now new head
			head = track;
			
			if (scan.hasNext()) {
				arr.add(scan.nextInt());
				timeArr.add(0.0);
			}
			div++;
		}

		System.out.println("Average: " + (time / div));
	}
	
	public static void main(String[] args) {
		// Ensure there is a queue size greater than 0
		if (Integer.parseInt(args[1]) <= 0 ) {
			System.out.println("Must have a Queue Size greater than 0.");
		} else {
			try {
				// Read in file and create scanner for file
				File inFile = new File(args[2]);
				Scanner scan = new Scanner(inFile);
				int count = Integer.parseInt(args[1]);
				
				// Run the correct algorithm
				if (args[0].equalsIgnoreCase("C-SCAN")) {
					CSCAN(scan, count);
				} else if (args[0].equalsIgnoreCase("SSTF")) {
					SSTF(scan, count);
				} else if (args[0].equalsIgnoreCase("FIFO")) {
					FIFO(scan, count);
				} else {
					System.out.println("Must select Algorithm type: C-SCAN, SSTF, or FIFO");
				}
			} catch (FileNotFoundException e) {
				System.out.println("Must include a working input file.");
				e.printStackTrace();
			}
		}
	}

}
