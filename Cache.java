import java.util.ArrayDeque;
import java.util.Iterator;

public class Cache {
	private ArrayDeque<BTreeNode> cache;
	public final int SIZE;

	public Cache(int size) {
		cache = new ArrayDeque<BTreeNode>(size);
		SIZE = size;
	}
	
	// Get a node from the cache
	public BTreeNode get(long fileIndex) {
		for (BTreeNode node : cache) {
			if (node.fileIndex == fileIndex) {
				return node;
			}
		}
		return null;
	}
	
	// Add a node to the cache
	public void add(BTreeNode node) {
		if (SIZE == 0) { return; }
		if (cache.size() >= SIZE) {
			cache.removeLast();
		}
		cache.addFirst(node);
	}

	// Remove a node from the cache
	public void remove(long fileIndex) {
		Iterator<BTreeNode> iter = cache.iterator();
		while (iter.hasNext()) {
			if (iter.next().fileIndex == fileIndex) {
				iter.remove();
			}
		}
	}
	
	// Remove all nodes currently in the cache
	public void clearCache() {
		cache.clear();
	}
	
	// Check if the cache contains a node
	public boolean contains(long fileIndex) {
		return get(fileIndex) != null;
	}

	// Print the fileIndexes of nodes stored in the cache
	public void printCache() {
		for (BTreeNode node : cache) {
			System.out.println(node.fileIndex);
		}
	}
}
