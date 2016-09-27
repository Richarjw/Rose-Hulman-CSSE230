import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

/**
 * This program runs various sorts and gathers timing information on them.
 *
 * @author <<Jack Richard and Adit Suvarna>> Created May 7, 2013.
 */
public class SortRunner {
	private static Random rand = new Random(17); // uses a fixed seed for
													// debugging. Can remove
													// later.
	private static int size = 300000;
	private static int shuffleFactor = size/10;
	/**
	 * Starts here.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// array size must be an int. You will need to use something much larger
//		int size = 100;

		// Each integer will have the range from [0, maxValue). If this is
		// significantly higher than size, you
		// will have small likelihood of getting duplicates.
		int maxValue = Integer.MAX_VALUE;

		// Test 1: Array of random values.
		System.out.println("Test 1: Array of Random Values\n");
		int[] randomIntArray = getRandomIntArray(size, maxValue);
		runAllSortsForOneArray(randomIntArray);

		// DONE: Tests 2-4
		// Generate the three other types of arrays (shuffled, almost sorted,
		// almost reverse sorted)
		// and run the sorts on those as well.
		
		//Test 2: Array of Shuffled Random Values.
		System.out.println("\nTest 2: Array of Shuffled Random values\n");
		int[] randomShuffle = getUniqueElementArray(size);
		runAllSortsForOneArray(randomShuffle);
		
		
		//Test 3: Array almost Sorted.
		System.out.println("\nTest 3: Array almost in Ascending Order\n");
		int[] almostSorted = getAlmostSortedArray(size);
		runAllSortsForOneArray(almostSorted);
		
		//Test 4: Array almost Reversed
		System.out.println("\nTest 4: Array almost in Descending Order\n");
		int [] almostReversed = getAlmostReverseArray(size);
		runAllSortsForOneArray(almostReversed);
	}

	/**
	 * 
	 * Runs all the specified sorts on the given array and outputs timing
	 * results on each.
	 *
	 * @param array
	 */
	private static void runAllSortsForOneArray(int[] array) {
		long startTime, elapsedTime;
		boolean isSorted = false;

		int[] sortedIntsUsingDefaultSort = array.clone();
		Integer[] sortedIntegersUsingDefaultSort = copyToIntegerArray(array);
		Integer[] sortedIntegersUsingHeapSort = sortedIntegersUsingDefaultSort
				.clone();
		Integer[] sortedIntegersUsingTreeSort = sortedIntegersUsingDefaultSort
				.clone();
		Integer[] sortedIntegersUsingSkipListSort = sortedIntegersUsingDefaultSort 
				.clone();
		int[] sortedIntsUsingQuickSort = array.clone();

		int size = array.length;

		// What is the default sort for ints? Read the javadoc.
		startTime = System.currentTimeMillis();
		Arrays.sort(sortedIntsUsingDefaultSort);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntsUsingDefaultSort);
		displayResults("int", "the default sort", elapsedTime, size, isSorted);

		// What is the default sort for Integers (which are objects that wrap
		// ints)?
		startTime = System.currentTimeMillis();
		Arrays.sort(sortedIntegersUsingDefaultSort);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntegersUsingDefaultSort);
		displayResults("Integer", "the default sort", elapsedTime, size,
				isSorted);

		// Sort using the following methods, and time and verify each like done
		// above.
		// DONE: a simple sort that uses a TreeSet
		startTime = System.currentTimeMillis();
		sortedIntegersUsingTreeSort = treeSort(sortedIntegersUsingTreeSort);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntegersUsingTreeSort);
		displayResults("Integer", "Tree Sort", elapsedTime, size, isSorted);
		// DONE: your BinaryHeap sort
		BinaryHeap<Integer> heap = new BinaryHeap<>();
		startTime = System.currentTimeMillis();
		heap.sort(sortedIntegersUsingHeapSort);
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntegersUsingHeapSort);
		displayResults("Integer", "BinaryHeap Sort", elapsedTime, size, isSorted);
		// DONE: your implementation of quick sort
		startTime = System.currentTimeMillis();
		quickSort(sortedIntsUsingQuickSort, 0 , sortedIntsUsingQuickSort.length-1); 
		elapsedTime = (System.currentTimeMillis() - startTime);
		isSorted = verifySort(sortedIntsUsingQuickSort);
		displayResults("Int", "Quick Sort", elapsedTime, size, isSorted);
	}

	private static void displayResults(String typeName, String sortName,
			long elapsedTime, int size, boolean isSorted) {
		if (isSorted) {
			System.out.printf("Sorted %.1e %ss using %s in %d milliseconds\n",
					(double) size, typeName, sortName, elapsedTime);
		} else {
			System.out.println("ARRAY NOT SORTED");
		}
	}

	/**
	 * Checks in O(n) time if this array is sorted.
	 *
	 * @param a
	 *            An array to check to see if it is sorted.
	 */
	private static boolean verifySort(int[] a) {
		// DONE: implement this.
		for (int i = 1; i < a.length; i++) {// 1 loop through does O(n) time
			if (a[i - 1] > a[i])
				return false;
		}
		return true;
	}

	/**
	 * Checks in O(n) time if this array is sorted.
	 *
	 * @param a
	 *            An array to check to see if it is sorted.
	 */
	private static boolean verifySort(Integer[] a) {// Same as int[] for the
													// most part
		// DONE: implement this.
		for (int i = 1; i < a.length; i++) {
			if (a[i - 1] > a[i])
				return false;
		}
		return true;
	}

	/**
	 * Copies from an int array to an Integer array.
	 *
	 * @param randomIntArray
	 * @return A clone of the primitive int array, but with Integer objects.
	 */
	private static Integer[] copyToIntegerArray(int[] ints) {
		Integer[] integers = new Integer[ints.length];
		for (int i = 0; i < ints.length; i++) {
			integers[i] = ints[i];
		}
		return integers;
	}

	/**
	 * Creates and returns an array of random ints of the given size.
	 *
	 * @return An array of random ints.
	 */
	private static int[] getRandomIntArray(int size, int maxValue) {
		int[] a = new int[size];
		for (int i = 0; i < size; i++) {
			a[i] = rand.nextInt(maxValue);
		}
		return a;
	}

	/**
	 * Creates a shuffled random array. This method will implement the shuffle
	 * method built into java.
	 * 
	 * @param size
	 * @return An array of the ints from 0 to size-1, all shuffled
	 */
	private static int[] getUniqueElementArray(int size) {
		ArrayList<Integer> arg = new ArrayList<>();
		for(int i = 0; i< size-1; i++){
			arg.add(i);
		}
		Collections.shuffle(arg);
		int[] result = new int[arg.size()];
		for(int i = 0; i <result.length; i++){
			result[i] = arg.get(i);
		}
		return result;
	}
	/**
	 * 
	 * Creates an array that swaps a set amount of values to make the array almost sorted
	 *
	 * @param size
	 * @return
	 */
	private static int[] getAlmostSortedArray(int size){
		int[] arg = new int[size];
		for(int i = 0; i < size; i++){
			arg[i] = i;
		}
		for(int i = 0; i <shuffleFactor; i++){
			int a = rand.nextInt(size);
			int b = rand.nextInt(size);
			swap(arg,a,b);
		}
		return arg;
	}
	/**
	 * 
	 * Creates an array in descending order and swaps a certain amount of integers
	 * to make it almost sorted.
	 *
	 * @param size
	 * @return
	 */
	private static int[] getAlmostReverseArray(int size){
		int[] arg = new int[size];
		for(int i = 0; i < size; i++){
			arg[i] = size-i;
		}
		for(int i = 0; i <shuffleFactor; i++){
			int a = rand.nextInt(size);
			int b = rand.nextInt(size);
			swap(arg,a,b);
		}
		return arg;
	}
	private static void swap(int[] array, int a, int b){
		int temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	/**
	 * 
	 * quickSort is accomplished recursively by finding the median
	 * of the array, then quicksorting the left side followed by
	 * quicksorting the right side of the "pivot" point. there will
	 * be a helper method that finds the pivot point for each
	 * subarray.
	 *
	 * @param array
	 * @param start
	 * @param end
	 */
	private static void quickSort(int[] array, int start, int end){
		if(end<=start){//checks if smaller array is not just a single value or empty
			return;
		}
		int[] temp = array;
		int pivot = pivotFinder(temp,start,end);
		quickSort(temp,start,pivot-1);
		quickSort(temp,pivot+1,end);
		array = temp;
	}
	
	/**
	 * 
	 * pivotFinder does a lot of the busy work where it goes through the sub arrays and swaps the values when needed.
	 * If pivot is on the front, it will check the back and move towards the front and vice versa if the 
	 * pivot is on the back.
	 *
	 * @param array
	 * @param start
	 * @param end
	 * @return
	 */
	private static int pivotFinder(int[] array, int start, int end) {
		int pivotI = getMedian(array,start,end,(start+end)/2);
		swap(array,start,pivotI);
		int pivot = array[start];
		int front = start + 1;
		int back = end;
		while(true){
			while(front<array.length && array[front] <=pivot){
				front++; //moves front to the right if pivot value is still greater
			}
			while(back> 0 && array[back] > pivot){
				back--; // checks back side to back sure all values are greater than pivot value
			}
			if(front>=back)//front or back reached the other.
				break;
			
			swap(array,front,back);//Swap is needed for out of order values.
		}
		swap(array,start,back);//puts pivot back
		return back;//returns new pivot value
	}
	/**
	 * 
	 *This method sorts and finds the median value of an array of size 3.
	 *It will give us the initial pivot value for efficiency.
	 *
	 * @param array
	 * @param start
	 * @param end
	 * @param i
	 * @return
	 */
	private static int getMedian(int[] array, int start, int end, int i) {
		int[] arg = new int[3];
		arg[0] = array[start];
		arg[1] = array[end];
		arg[2] = array[i];
		Arrays.sort(arg);//sorts 3 values
		if(arg[1] == array[start])//checks which value is the center.
			return start;
		else if(arg[1] == array[end])
			return end;
		else
			return i;
	}

	/**
	 * 
	 * treeSort uses the treeSet given by java to sort a given array.
	 * Decided to return an array and set the input array to a new one.
	 * This avoided the issue of resizing. 
	 *
	 * @param array
	 */
	private static Integer[] treeSort(Integer[] array) {
		TreeSet<Integer> tree = new TreeSet<>();
		for (Integer element : array) {
			tree.add(element);
		}
		Iterator<Integer> iter = tree.iterator();
		Integer[] temp = new Integer[tree.size()];
		int location = 0;
		while(iter.hasNext()){
			temp[location] = iter.next();
			location++;
		}
		array = temp;
		return array;
	}
}
