package adaptiveHuffman.tree;

public class Node {
	
	public Node parent = null;
	public Node left = null;
	public Node right = null;
	
	protected boolean isNYT = false;
	protected boolean isLeaf = false;
	
	private int weight;
	private int index;
	private int value;
	
	/**
	 * Konstruktor unutrasnjeg cvora
	 * @param parent
	 * @param left
	 * @param right
	 * @param weight
	 * @param index
	 */
	public Node(Node parent, Node left, Node right, int weight, int index) {
		this.parent = parent;
		this.weight = weight;
		this.index = index;
	}
	
	/**
	 * NYT Node konstruktor.
	 * NYT samo zna parent node
	 * nema decu i tezina mu je uvek 0
	 * i indeks 0.
	 * 
	 * @param parent
	 */
	public Node(Node parent) {
		this.parent = parent;
		this.weight = 0;
		this.index = 0;
		this.isNYT = true;
	}
	
	/**
	 * Konstruktor lista.
	 * Novi list uvek ima tezinu i indeks 1.
	 * 
	 * @param parent
	 * @param value
	 */
	public Node(Node parent, int value) {
		this.parent = parent;
		this.weight = 1;
		this.index = 1;
		this.value = value;
		this.isLeaf = true;
		this.isNYT = false;
	}
	
	public boolean isLeaf() {
		return this.isLeaf;
	}
	
	public boolean isNYT() {
		return this.isNYT;
	}

	/**
	 *
	 * vraca indeks,tezinu i poruku o vrsti čvora
	 * vraca vrednost ako je list.
	 */
	public String toString() {
		if(this.isLeaf) {
			return " index: "+this.index+" weight: "+this.weight+" value: "+this.value+" AM LEAF";
		}
		else if(this.isNYT) {
			return " index: "+this.index+" weight: "+this.weight+" AM NYT";
		}
		else {
			return " index: "+this.index+" weight: "+this.weight+" AM INTERNAL";
		}
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getWeight() {
		return this.weight;
	}
	
	public void increment() {
		this.weight++;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getValue() {
		return this.value;
	}

}
