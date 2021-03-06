package sublist;

import java.util.ArrayList;

/**
 *
 * Exam 2. Tree methods.
 * 
 * @author Matt Boutell and TODO: You!
 * @param <T>
 */

/*
 * TODO: Directions: Implement the method below. See the paper for details.
 */
public class BinarySearchTree<T extends Comparable<T>> {

	BinaryNode root;

	final BinaryNode NULL_NODE = new BinaryNode(null);

	public BinarySearchTree() {
		root = NULL_NODE;
	}

	public ArrayList<T> subList(T low, T high) {
		ArrayList<T> nodes = new ArrayList<>();
		if (root != NULL_NODE) {
			this.root.subList(low, high, nodes);
			// return lowNode.right.subList(nodes, high);

		}
		return nodes;
	}

	// The next methods are used by the unit tests
	public void insert(T e) {
		root = root.insert(e);
	}

	/**
	 * Feel free to call from tests to use to verify the shapes of your trees
	 * while debugging. Just remove the calls you are done so the output isn't
	 * cluttered.
	 * 
	 * @return A string showing a traversal of the nodes where children are
	 *         indented so that the structure of the tree can be determined.
	 * 
	 */
	public String toIndentString() {
		return root.toIndentString("");
	}

	@Override
	public String toString() {
		return root.toString();
	}

	// /////////////// BinaryNode
	public class BinaryNode {

		public T data;
		public BinaryNode left;
		public BinaryNode right;

		// The rest of the methods are used by the unit tests and for debugging
		public BinaryNode(T element) {
			this.data = element;
			this.left = NULL_NODE;
			this.right = NULL_NODE;
		}

		public void subList(T low, T high, ArrayList<T> nodes) {
			if (this.left == NULL_NODE && this.right == NULL_NODE) {
				if (this.inRange(low, high)) {
					nodes.add(this.data);
				}
			} else if (this.left != NULL_NODE && this.right == NULL_NODE) {
				this.left.subList(low, high, nodes);
				if (this.inRange(low, high)) {
					nodes.add(this.data);

				}
			} else if (this.right != NULL_NODE && this.left == NULL_NODE) {
				if (this.inRange(low, high))
					nodes.add(this.data);
				this.right.subList(low, high, nodes);
			} else if (this.left != NULL_NODE && this.right != NULL_NODE) {
				this.left.subList(low, high, nodes);
				if(this.inRange(low, high))
					nodes.add(this.data);
				this.right.subList(low, high, nodes);
			}

			// }else
			// if(this.right !=NULL_NODE && this.left == NULL_NODE){
			// if()
			// }
		}

		public boolean inRange(T low, T high) {
			if (this.data.compareTo(low) >= 0 && this.data.compareTo(high) <= 0) {
				return true;
			}
			return false;
		}

		public BinaryNode insert(T e) {
			if (this == NULL_NODE) {
				return new BinaryNode(e);
			} else if (e.compareTo(data) < 0) {
				left = left.insert(e);
			} else if (e.compareTo(data) > 0) {
				right = right.insert(e);
			} else {
				// do nothing
			}
			return this;
		}

		@Override
		public String toString() {
			if (this == NULL_NODE) {
				return "";
			}
			return left.toString() + this.data + right.toString();
		}

		public String toIndentString(String indent) {
			if (this == NULL_NODE) {
				return indent + "NULL\n";
			}

			String myInfo = indent + String.format("%c\n", this.data);

			return myInfo + this.left.toIndentString(indent + "  ")
					+ this.right.toIndentString(indent + "  ");
		}
	}

}