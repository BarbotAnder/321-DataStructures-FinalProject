import java.util.ArrayList;
public class BTreeNode {

	private long fileIndex; //identifer for each node
	private boolean leaf; //identifies if you can insert new key into node
	private int numKeys;
	private long parentNode;
	private ArrayList<TreeObject> keys;
	private ArrayList<Long> children;
	
	/**
	 * Create a new BTree ROOT Node
	 * @param fileIndex
	 * @param parentNode
	 * @param children
	 * @param leaf
	 * @param keys
	 */
	public BTreeNode() {
		numKeys = 0;
		fileIndex = 0;  //new file index are stored in Btree
		parentNode = -1;
		leaf = false;
		keys = new ArrayList<TreeObject>();
		children = new ArrayList<Long>();
	}

	/**
	 * Create a new BTree NOT ROOT Node (split node)
	 * @param fileIndex
	 * @param parentNode
	 * @param keys
	 * @param children
	 */
	public BTreeNode(long fileOffSet) {
		numKeys = 0;
		fileIndex = fileOffSet;
		parentNode = -1;
		leaf = false;
		keys = new ArrayList<TreeObject>();
		children = new ArrayList<Long>();	
	}

	//get the number of keys in the B-Tree Node.
	public int getNumKeys() {
		return numKeys;
	}

	//insertKey into node.
	public void insertKey(TreeObject obj, int i) {
		keys.add(i, obj);
		numKeys += 1;
	}

	//set Key of the node.
	public void setKey(TreeObject obj, int i) {
		keys.set(i,obj);
	}

	
	/**
	 * set number of keys in note //important for split
	 * @param numKeys amount of keys stored in node
	 */
	public void setNumKeys(int numKeys) {
		this.numKeys = numKeys;
	}

	/**
	set the key stored in Keys array
	@param index location of key in array
	*/
	public TreeObject getKey(int index ) {
		TreeObject key = keys.get(index);
		return key;
	}

	//get the number of children
	public int getNumChildren() {
		if(leaf){
			return 0;
		}
		return getNumKeys() + 1;
	}

	//set the children in Children array in the node
	public void setChild(long l, int index){
		children.set(index,l);
	}

    //get the children from Children array in the node
	public long getChild(int index){
		return children.get(index);
	}

	//insert the children in Children array in the node
	public void insertChild(long l, int index){
		children.add(index,l);
	}

    	//set the parent for current node
	public void setParent(long l) {
		parentNode = l;
	}

	//get the parent node of the BTree
	public long getParentNode() {
		return parentNode;
	}

	//get the Fileindex of the node
	public long getFileIndex() {
		return fileIndex;
	}

	//set the Fileindex of the node
	public void setFileIndex(long fileIndex) {
		this.fileIndex = fileIndex;
	}

	//set the leaf status of the node
	public void setLeaf(boolean leafStatus) {
		leaf = leafStatus;
	}

	//get the leaf status of the node
	public boolean getLeafStatus() {
		return leaf;
	}

	//Convert useful information to a string for debugging
	public String toString() {
		int numKeys = this.getNumKeys();
		int numChildren = this.getNumChildren();
		
		StringBuilder str = new StringBuilder("FileIndex: ");
		str.append(this.getFileIndex());
		str.append("\nNumKeys: ");
		str.append(numKeys);
		str.append("\nparentNode: ");
		str.append(this.getParentNode());
		str.append("\nleaf: ");
		str.append(this.getLeafStatus());
		str.append("\nKeys: ");
		
		//check for off by 1 error
		for (int i = 0; i <= numKeys; i++) {
			str.append(this.getKey(i));
			str.append(" ");
		}
		
		str.append("\nChildren");
		
		//check for off by 1 error
		for (int i = 0; i <= numChildren; i++) {
			str.append(this.getChild(i));
			str.append(" ");
		}	
		return str.toString();
	}
}
