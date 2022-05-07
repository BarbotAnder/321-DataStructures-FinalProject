import java.nio.ByteBuffer;
import java.util.ArrayList;

public class BTreeNode {
	public long fileIndex;
	public boolean isLeaf;
	public long parentIndex;

	public ArrayList<TreeObject> keys;
	public ArrayList<Long> children;

	private static int NODE_METADATA_SIZE = 13;      // bool, int, long (isLeaf, numKeys, parentIndex)
    private static int KEY_SIZE = 12;                // long, int (sequence, frequency)
    private static int CHILD_INDEX_SIZE = 8;         // long (fileIndex)

	public BTreeNode(long fileIndex) {
		this.fileIndex = fileIndex;
		isLeaf = true;
		parentIndex = -1;
		keys = new ArrayList<TreeObject>();
		children = new ArrayList<Long>();
	}

	// Deserialize node from byte array
	public BTreeNode(long fileIndex, byte[] bytes, int degree) {
		this.fileIndex = fileIndex;
		keys = new ArrayList<TreeObject>();
		children = new ArrayList<Long>();
		
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		isLeaf = (buffer.get() != 0);
		int numKeys = buffer.getInt();
		parentIndex = buffer.getLong();

		for (int i = 0; i < 2 * degree - 1; i++) {
			if (i < numKeys) {
				long sequence = buffer.getLong();
				int frequency = buffer.getInt();
				keys.add(new TreeObject(sequence, frequency));
			} else {
				buffer.getLong();
				buffer.getInt();
			}
		}

		for (int i = 0; i < 2 * degree; i++) {
			if (i < numKeys) {
				long childIndex = buffer.getLong();
				children.add(childIndex);
			} else {
				//buffer.getLong();
				return;
			}
		}
	}

	// Return the next node that could contain the sequence
	public long next(long sequence) {
		for (int i = 0; i < keys.size(); i++) {
			if (sequence < keys.get(i).sequence) {
				return children.get(i - 1);
			}
		}
		return children.get(children.size() - 1);
	}

	// Get the number of children
	public int getNumChildren() {
		if (isLeaf) {
			return 0;
		}
		return children.size();
	}

	// Serialize the node into a byte array
	public byte[] serialize(int degree, int seqLen) {
		int size = serialSize(degree);
		ByteBuffer buffer = ByteBuffer.allocate(size);
		
		if (isLeaf) {
			buffer.put((byte) 1);
		} else {
			buffer.put((byte) 0);
		}

		buffer.putInt(keys.size());
		buffer.putLong(parentIndex);
		
		for (int i = 0; i < 2 * degree - 1; i++) {
			if (i < keys.size()) {
				buffer.putLong(keys.get(i).sequence);
				buffer.putInt(keys.get(i).frequency);
			} else {
				buffer.putLong(0);
				buffer.putInt(0);
			}
		}

		for (int i = 0; i < 2 * degree; i++) {
			if (i < children.size()) {
				buffer.putLong(children.get(i));
			} else {
				buffer.putLong(0);
			}
		}

		return buffer.array();
	}

	// Calculate how many bytes are needed to serialize a node of a given degree
	public static int serialSize(int degree) {
		return NODE_METADATA_SIZE + KEY_SIZE * (2 * degree - 1) + CHILD_INDEX_SIZE * (2 * degree);
	}

	public void debug() {
		System.out.println("fileIndex: " + fileIndex);
		System.out.println("isLeaf: " + isLeaf);
		System.out.println("parentIndex: " + parentIndex);
		System.out.print("Keys: ");
		for (TreeObject key : keys) {
			System.out.print(key.sequence + ", ");
		}

		System.out.print("\nChildren: ");
		for (Long child : children) {
			System.out.print(child + ", ");
		}
		System.out.println("\n");
	}
}
