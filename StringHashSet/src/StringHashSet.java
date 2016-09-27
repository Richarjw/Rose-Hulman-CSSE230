import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * A hash set implementation for Strings. Cannot insert null into the set. Other
 * requirements are given with each method.
 *
 * @author Matt Boutell and <<TODO: your name here >>>. Created Oct 6, 2014.
 */
public class StringHashSet {

	// The initial size of the internal array.
	private static final int DEFAULT_CAPACITY = 5;
	private final Node NULL_NODE = new Node();
	// You'll want fields for the size (number of elements) and the internal
	// array of Nodes. I also added one for the capacity (the length
	// of the internal array).
	private int capacity = 0;
	private Node[] nodes = new Node[DEFAULT_CAPACITY];
	private int size = 0;
	private static final int PRIME = 31;
	private static final int LOAD_FACTOR = 2;
	private int modifications = 0;

	private class Node {
		// TODO: Implement this class . These are just linked-list style
		// nodes, so you will need at least fields for the data and a reference
		// to the next node, plus a constructor. You can choose to use a
		// NULL_NODE at the end, or not. I chose not to do so this time.
		String data;
		Node nextNode;

		public Node() {
			this.data = "";
			this.nextNode = null;
		}

		public Node(String data) {
			this.data = data;
			this.nextNode = NULL_NODE;
		}

		public boolean contains(String item) {
			Node current = this;
			while (current != null) {
				if (current.data.equals(item)) {
					return true;
				}
				current = current.nextNode;
			}
			return false;
		}
	}

	/**
	 * Creates a Hash Set with the default capacity.
	 */
	public StringHashSet() {
		// Recall that using this as a method calls another constructor
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates a Hash Set with the given capacity.
	 */
	public StringHashSet(int initialCapacity) {
		initialize(initialCapacity);
	}

	private void initialize(int initialCapacity) {
		// DONE: Set the capacity to the given capacity, and initialize the
		// other fields.
		// Why did we pull this out into a separate method? Perhaps another
		// method needs to initialize the hash set as well? (Hint)
		this.capacity = initialCapacity;
		this.nodes = new Node[initialCapacity];
		this.size = 0;
		this.modifications = 0;
	}

	/**
	 * Calculates the hash code for Strings, using the x=31*x + y pattern.
	 * Follow the specification in the String.hashCode() method in the Java API.
	 * Note that the hashcode can overflow the max integer, so it can be
	 * negative. Later, in another method, you'll need to account for a negative
	 * hashcode by adding Integer.MAX_VALUE before you mod by the capacity
	 * (table size) to get the index.
	 *
	 * This method is NOT the place to calculate the index though.
	 *
	 * @param item
	 * @return The hash code for this String
	 */
	public static int stringHashCode(String item) {
		// TODO: Write this.
		int value = 0;
		value = value + item.charAt(0);
		for (int i = 1; i < item.length(); i++) {
			value = PRIME * value + item.charAt(i);//use x= 31*x + y
		}
		return value;
	}

	public int index(String item) {
		int value = stringHashCode(item);
		if (value > 0) {
			return value % this.capacity;
		}
		return (Integer.MAX_VALUE + value) % this.capacity;
	}

	/**
	 * Adds a new node if it is not there already. If there is a collision, then
	 * add a new node to the -front- of the linked list.
	 * 
	 * Must operate in amortized O(1) time, assuming a good hashcode function.
	 *
	 * If the number of nodes in the hash table would be over double the
	 * capacity (that is, lambda > 2) as a result of adding this item, then
	 * first double the capacity and then rehash all the current items into the
	 * double-size table.
	 *
	 * @param item
	 * @return true if the item was successfully added (that is, if that hash
	 *         table was modified as a result of this call), false otherwise.
	 */
	public boolean add(String item) {
		// DONE: Write this
		if (this.contains(item))
			return false;
		this.size++;
		this.modifications++;
		int index = this.index(item);
		if (this.nodes[index] != NULL_NODE) {
			Node newNode = new Node(item);
			newNode.nextNode = this.nodes[index];
			this.nodes[index] = newNode;
		} else {
			this.nodes[index] = new Node(item);
		}
		if (this.size > this.capacity * LOAD_FACTOR) {
			this.rehash();
		}
		return true;

	}

	/*
	 * This method deals with when I need to double the size of the array
	 * because the size has gotten larger than the load factor*capacity.
	 */
	private void rehash() {
		Node[] newSet = new Node[this.capacity * 2];
		Iterator<String> iter = this.iterator();
		while (iter.hasNext()) {
			String currentString = iter.next();
			int value = stringHashCode(currentString);
			if (value < 0)
				value += Integer.MAX_VALUE;
			int index = value % (2 * this.capacity);
			if (newSet[index] == null) {
				newSet[index] = new Node(currentString);
			} else {
				Node newNode = new Node(currentString);// This adds the current
														// spot
				newNode.nextNode = newSet[index];// to the next spot of the new
													// Node
				newSet[index] = newNode;// then sets the new node to the first
										// spot
			}
		}
		this.capacity = this.capacity * 2;//doubles capacity after done adding all.
		this.nodes = newSet;
	}

	public boolean empty(int index) {
		return this.nodes[index] == null;// checks if the location is empty
	}

	/**
	 * Prints an array value on each line. Each line will be an array index
	 * followed by a colon and a list of Node data values, ending in null. For
	 * example, inserting the strings in the testAddSmallCollisions example
	 * gives "0: shalom hola null 1 bonjour null 2 caio hello null 3 null 4 hi
	 * null". Use a StringBuilder, so you can build the string in O(n) time.
	 * (Repeatedly concatenating n strings onto a growing string gives O(n^2)
	 * time)
	 * 
	 * @return A slightly-formatted string, mostly used for debugging
	 */
	public String toRawString() {
		// DONE: Write this
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.capacity; i++) {
			builder.append(i);//append syntax
			builder.append(':');
			if (!this.empty(i)) {
				Node current = this.nodes[i];
				while (current != null) {
					builder.append(' ');
					if (current.data != null)//With while loop i look for all 
						builder.append(current.data);//nodes at index
					current = current.nextNode;
				}
			}
			builder.append(' ');
			builder.append("null");
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * 
	 * Checks if the given item is in the hash table.
	 * 
	 * Must operate in O(1) time, assuming a good hashcode function.
	 *
	 * @param item
	 * @return True if and only if the item is in the hash table.
	 */
	public boolean contains(String item) {
		// DONE: Write this
		int index = this.index(item);
		return !(this.nodes[index] == null) && this.nodes[index].contains(item);
	}

	/**
	 * Returns the number of items added to the hash table. Must operate in O(1)
	 * time.
	 *
	 * @return The number of items in the hash table.
	 */
	public int size() {
		// DONE: Write this
		return this.size;
	}

	/**
	 * @return True iff the hash table contains no items.
	 */
	public boolean isEmpty() {
		// DONE: Write this
		return (this.size == 0);
	}

	/**
	 * Removes all the items from the hash table. Resets the capacity to the
	 * DEFAULT_CAPACITY
	 */
	public void clear() {
		// DONE: Write this. Should take 1 line if you read carefully above.
		int count = this.modifications;
		this.initialize(DEFAULT_CAPACITY);
		this.modifications = count + 1;
	}

	/**
	 * Removes the given item from the hash table if it is there. You do NOT
	 * need to resize down if the load factor decreases below the threshold.
	 * 
	 * @param item
	 * @return True If the item was in the hash table (or equivalently, if the
	 *         table changed as a result).
	 */
	public boolean remove(String item) {
		// DONE: Write this.
		if (!this.contains(item)) {
			return false;
		}
		int index = this.index(item);
		Node currentNode = this.nodes[index];
		this.size--;
		this.modifications++;
		if (currentNode.data.equals(item)) {
			if (currentNode.nextNode != NULL_NODE) {
				this.nodes[index] = currentNode.nextNode;
			} else {
				this.nodes[index] = null;
			}
			return true;
		}
		while (true) {
			if (currentNode.nextNode.data.equals(item)) {//I kept having an issue here,but fixed
				if (currentNode.nextNode.nextNode != null)//it by realizing i was checking if
					currentNode.nextNode = currentNode.nextNode.nextNode;//a node equaled a string
				else
					currentNode.nextNode = NULL_NODE;
				return true;
			}
			currentNode = currentNode.nextNode;
		}
		
	}

	/**
	 * Adds all the items from the given collection to the hash table.
	 *
	 * @param collection
	 * @return True if the hash table is modified in any way.
	 */
	public boolean addAll(Collection<String> collection) {
		// DONE: Write this.
		Boolean addedAll = false;
		for(String current:collection){
			boolean temp = this.add(current);
			if(!addedAll){
				addedAll= temp;
			}
		}
		return addedAll;
	}

	/**
	 * 
	 * Challenge Feature: Returns an iterator over the set. Return the items in
	 * any order that you can do efficiently. Should throw a
	 * NoSuchElementException if there are no more items and next(0 is called.
	 * Should throw a ConcurrentModificationException if next() is called and
	 * the set has been mutated since the iterator was created.
	 *
	 * @return an iterator.
	 */
	public Iterator<String> iterator() {
		return new HashIterator();
	}

	private class HashIterator implements Iterator<String> {
		private int currentIndex;
		private Node current;
		private boolean nextPossible;
		private int count;
		private int mod;

		public HashIterator() {
			this.mod = StringHashSet.this.modifications;
			this.count = 0;
			if (!StringHashSet.this.isEmpty()) {
				this.nextPossible = true;
				int index = 0;
				while (StringHashSet.this.empty(index)) {
					index++;
				}
				this.currentIndex = index;
				this.current = StringHashSet.this.nodes[index];
			} else {
				this.currentIndex = 0;
				this.current = null;
				this.nextPossible = false;
			}
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub.
			return this.count < StringHashSet.this.size;
		}

		@Override
		public String next() {
			// TODO Auto-generated method stub.
			if (this.mod != StringHashSet.this.modifications)
				throw new ConcurrentModificationException();
			if (!this.nextPossible)
				throw new NoSuchElementException();
			this.count++;
			String currentString = this.current.data;
			if (this.current.nextNode != null)
				this.current = this.current.nextNode;
			else {
				if (this.hasNext()) {
					this.currentIndex++;
					while (StringHashSet.this.empty(this.currentIndex)) {
						this.currentIndex++;
					}
					this.current = StringHashSet.this.nodes[this.currentIndex];
				} else {
					this.nextPossible = false;
				}
			}
			return currentString;
		}

	}

	// Challenge Feature: If you have an iterator, this is easy. Use a
	// StringBuilder, so you can build the string in O(n) time. (Repeatedly
	// concatenating n strings onto a string gives O(n^2) time)
	// Format it like any other Collection's toString (like [a,b,c])
	@Override
	public String toString() {
		Iterator<String> iter = this.iterator();
		StringBuilder string = new StringBuilder();
		string.append('[');
		for(int i = 0;i<this.size;i++){
			string.append(iter.next());
			string.append(", ");
		}
		string.delete(string.length()-2,string.length());//deletes last ", " 
		string.append("]");
		return string.toString();
		
	}
}
