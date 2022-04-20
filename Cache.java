import java.util.LinkedList;

public class Cache {
	
	private LinkedList<BTreeNode> cache;
	private final int SIZE;
	public Cache(int size) {
		cache = new LinkedList<BTreeNode>();
		this.SIZE = size;
	}
	
	public BTreeNode getObject(long index) {
		for (BTreeNode node : cache) {
			if (node.getFileIndex() == index) {
				return node;
			}
		}
		return null;
	}
	
	//add node to the cache
	public void addObject(BTreeNode o) {
		cache.addFirst(o);
	}
	
	//remove node from the cache
	public void removeObject(BTreeNode o) {
		cache.remove(o);
	}
	
	//add node to beginning of cache and remove all other instances of it
	public void addObjectFirst(BTreeNode o) {
		if (contains(o)) {
			removeObject(o);
			addObject(o);
		} else {
			cache.addFirst(o);
			if (cache.size() > SIZE) {
				cache.removeLast();	
			}
			
		}
		
	}
	
	//remove the last node in the cache
	public void removeObjectLast() {
		cache.removeLast();
	}
	
	//purge all nodes currently in the cache
	public void clearCache() {
		for (int i = 0; i < SIZE; i++) {
			cache.remove(i);
		}
		
	}
	
	/**
	 * search the cache for a node
	 * @param index fileOffset of the node
	 * true if cache contains specified node
	 */
	public boolean contains(long index) {
		for (BTreeNode node : cache) {
			if (node.getFileIndex() == index) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * search the cache for a node
	 * @param o node to look for
	 * return true if cache contains specified node
	 */
	public boolean contains(BTreeNode o) {
		return cache.contains(o);
	}
	
	/**
	 * get the cache as a LinkedList
	 * @return LinkedList of nodes
	 */
	public LinkedList<BTreeNode> toList() {
		return cache;
	}
	
	//get the cache as an array
	public BTreeNode[] toArray() {
		BTreeNode[] cacheArray = (BTreeNode[]) cache.toArray();
		return cacheArray;
	}
	
	public int getSize() {;
		return cache.size();
	}

	//print the fileOffSet of nodes stored in cache
	public void printCache(){
		
		for (BTreeNode node : cache) {
		System.out.println(node.getFileIndex());
		}
	}

}