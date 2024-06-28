package adaptiveHuffman.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Tree {

	public Node root;
	public Node NYT; // Current NYT node.

	// Easily access a node based on its value.
	private Map<Integer, Node> seen = new HashMap<Integer,Node>();
	// Keep nodes in order based on weight.
	private List<Node> order = new ArrayList<Node>();

	public Tree() {
		this.root = new Node(null);
		this.NYT = root;
		order.add(root);
	}
	public void insertInto(Integer value) {
		// Deal with value that exists in tree first.
		if(seen.containsKey(value)) {
			Node toUpdate = seen.get(value);
			updateTree(toUpdate);
		}
		else {
			Node parent = giveBirth(value);
			updateTree(parent);
		}
	}
	public boolean contains(Integer value) {
		if(seen.containsKey(value)) {
			return true;
		}
		else {
			return false;
		}
	}
	public int getCode(Integer c, boolean seen, ArrayList<Boolean> buffer) {
		int length = 0;
		if(!seen) { // Return NYT code
			if(NYT == root) {
				return length; // Nothing in tree;
			}
			else {
				length = generateCode(NYT,buffer);
			}
		}
		else {
			length = generateCode(this.seen.get(c),buffer);
		}
		return length;
	}
	public boolean isEmpty() {
		return root == NYT;
	}
	public void printTree(boolean breadthFirst) {
		if(breadthFirst) {
			printTreeBreadth(root);
		}
		else {
			printTreeDepth(root);
		}
	}
	private Node giveBirth(int value) {
		Node newNYT = new Node(NYT);
		Node leaf = new Node(NYT, value);
		seen.put(value, leaf); // Add new value to seen.
		order.add(0,leaf);
		order.add(0,newNYT);

		Node oldNYT = NYT;
		NYT.isNYT = false; // Update the current NYT so that it is now internal node.
		NYT.left = newNYT; // Set children.
		NYT.right = leaf;
		NYT = newNYT; // Turn NYT pointer into the new NYT node.

		updateNodeIndices();
		return oldNYT;
	}
	private void updateTree(Node node) {
		while(node != root) {
			if(maxInWeightClass(node))  {
				Node toSwap = findHighestIndexWeight(node);
				swap(toSwap,node);

			}
			node.increment(); // Increment node weight.
			node = node.parent;
		}
		node.increment();
		node.setIndex(order.indexOf(node));
	}
	private boolean maxInWeightClass(Node node) {
		int index = order.indexOf(node);
		int weight = node.getWeight();
		for(int i = index+1; i < order.size(); i++) {
			Node next = order.get(i);
			if(next != node.parent && next.getWeight() == weight) {
				return true;
			}
			else if(next != node.parent && next.getWeight() > weight){
				return false;
			}
		}
		return false;
	}
	private Node findHighestIndexWeight(Node node) {
		Node next;
		int index = node.getIndex() + 1;
		int weight = node.getWeight();
		while((next = order.get(index)).getWeight() == weight) {
			index++;
		}
		next = order.get(--index); // Overshot correct index, need to decrement.
		return next;
	}
	private void swap(Node newNodePosition, Node oldNodeGettingSwapped) {
		int newIndex = newNodePosition.getIndex();
		int oldIndex = oldNodeGettingSwapped.getIndex();

		Node oldParent = oldNodeGettingSwapped.parent;
		Node newParent = newNodePosition.parent;

		boolean oldNodeWasOnRight, newNodePositionOnRight;
		oldNodeWasOnRight = newNodePositionOnRight = false;

		if(newNodePosition.parent.right == newNodePosition) {
			newNodePositionOnRight = true;
		}
		if(oldNodeGettingSwapped.parent.right == oldNodeGettingSwapped) {
			oldNodeWasOnRight = true;
		}
		if(newNodePositionOnRight) {
			newParent.right = oldNodeGettingSwapped;
		}
		else{
			newParent.left = oldNodeGettingSwapped;
		}
		if(oldNodeWasOnRight) {
			oldParent.right = newNodePosition;
		}
		else {
			oldParent.left = newNodePosition;
		}
		oldNodeGettingSwapped.parent = newParent;
		newNodePosition.parent = oldParent;
		order.set(newIndex, oldNodeGettingSwapped);
		order.set(oldIndex, newNodePosition);
		updateNodeIndices();
	}
	private void updateNodeIndices() {
		for(int i = 0; i < order.size(); i++) {
			Node node = order.get(i);
			node.setIndex(order.indexOf(node));
		}
	}
	private int generateCode(Node in, ArrayList<Boolean> buffer) {
		Node node = in;
		Node parent;
		int length = 0;
		while(node.parent != null) {
			parent = node.parent;
			if(parent.left == node) {
				buffer.add(false);
				length++;
			}
			else {
				buffer.add(true);
				length++;
			}
			node = parent;
		}
		return length;
	}
	private void printTreeDepth(Node node){
		if(node.isNYT) {
			System.out.println(node);
		}
		else if(node.isLeaf) {
			System.out.println(node);
		}
		else { // Node is an internal node.
			System.out.println(node);
			printTreeDepth(node.left);
			printTreeDepth(node.right);
		}
	}
	private void printTreeBreadth(Node root) {
		Queue<Node> queue = new LinkedList<Node>() ;
		if (root == null)
			return;
		queue.clear();
		queue.add(root);
		while(!queue.isEmpty()){
			Node node = queue.remove();
			System.out.println(node);
			if(node.right != null) queue.add(node.right);
			if(node.left != null) queue.add(node.left);
		}

	}

}

