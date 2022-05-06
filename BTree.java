import java.io.IOException;
import java.io.PrintStream;
import java.util.Stack;

public class BTree {
    private final int degree;
    private final int seqLen;

    private BTreeNode root;
    private Cache cache;
    private FileIO file;
    
    // Create a BTree in the specified file.
    public BTree(String fileName, int cacheSize, int degree, int seqLen) throws IOException {
        this.file = new FileIO(fileName, degree, seqLen);
        this.cache = new Cache(cacheSize);
        this.degree = degree;
        this.seqLen = seqLen;
        this.root = new BTreeNode(0);
        this.root.keys.add(new TreeObject(-1));
    }

    // Load a BTree from the specified file.
    public BTree(String fileName, int cacheSize) throws IOException {
        this.file = new FileIO(fileName);
        this.cache = new Cache(cacheSize);
        this.degree = this.file.degree;
        this.seqLen = this.file.seqLen;
        this.root = this.file.read(0);
    }

    public void insert(long sequence) throws IOException {
        // Refer to https://webdocs.cs.ualberta.ca/~holte/T26/ins-b-tree.html

        // 1. Find the target leaf node
        long fileIndex = search(sequence);

        // 2. Add the value to the node
        BTreeNode node = loadNode(fileIndex);
        for (int i = 0; i < node.keys.size(); i++) {
            if (node.keys.get(i).sequence == sequence) {
                node.keys.get(i).frequency++;
                saveNode(node);
                return;

            } else if (sequence < node.keys.get(i).sequence) {
                node.keys.add(i, new TreeObject(sequence));
                saveNode(node);
                split(node);
                return;
            }
        }
        
        // If we got all the way to the end without finding a spot, just add value to end of
        node.keys.add(new TreeObject(sequence));
        saveNode(node);
        split(node);
    }

    // Splits the node at fileIndex if needed
    private void split(BTreeNode node) throws IOException {
        if (node.keys.size() < degree * 2) {
            return;
        }

        // The old node becomes the left node
        // The last (M-1)/2 values move to the new right node
        // The middle value gets added to the parent node
        
        BTreeNode right = new BTreeNode(file.nextIndex());
        right.isLeaf = node.isLeaf;
        right.parentIndex = node.parentIndex;

        right.keys.addAll(node.keys.subList(degree, node.keys.size()));
        node.keys.subList(degree, node.keys.size()).clear();

        if (!right.isLeaf) {
            right.children.addAll(node.children.subList(degree, node.children.size()));
            node.children.subList(degree, node.children.size()).clear();
        }

        // Update parentIndex on children of the right node
        for (Long childIndex : right.children) {
            BTreeNode child = loadNode(childIndex);
            child.parentIndex = right.fileIndex;
            saveNode(child);
        }

        // If node is the root
        if (node.parentIndex == -1) {
            root = new BTreeNode(0);
            root.isLeaf = false;
            right.parentIndex = 0;
            node.parentIndex = 0;
            node.fileIndex = file.nextIndex();

            // Update parentIndex on children of the left node
            for (Long childIndex : node.children) {
                BTreeNode child = loadNode(childIndex);
                child.parentIndex = node.fileIndex;
                saveNode(child);
            }

            long middle = right.keys.get(0).sequence;
            root.keys.add(new TreeObject(-1));
            root.keys.add(new TreeObject(middle));

            root.children.add(node.fileIndex);
            root.children.add(right.fileIndex);

            saveNode(node);
            saveNode(right);
            saveNode(root);
        } else {
            BTreeNode parent = loadNode(node.parentIndex);
            long middle = right.keys.get(0).sequence;

            int loc = parent.children.indexOf(node.fileIndex);
            parent.children.add(loc + 1, right.fileIndex);
            parent.keys.add(loc + 1, new TreeObject(middle));
            
            saveNode(node);
            saveNode(right);
            saveNode(parent);

            split(parent);
        }
    }

    public int get(long sequence) throws IOException {
        long targetNode = search(sequence);
        BTreeNode target = loadNode(targetNode);

        for (int i = 0; i < target.keys.size(); i++) {
            if (target.keys.get(i).sequence == sequence) {
                return target.keys.get(i).frequency;
            }
        }

        return 0;
    }

    public void flush() throws IOException {
        file.write(root);
    }

    // Find the fileIndex of the node that contains a given sequence
    private long search(long sequence) throws IOException {
        return searchInner(sequence, root);
    }

    private long searchInner(long sequence, BTreeNode node) throws IOException {
        if (!node.isLeaf) {
            long nextFileIndex = node.next(sequence);
            BTreeNode nextNode = loadNode(nextFileIndex);
            return searchInner(sequence, nextNode);
        } else {
            return node.fileIndex;
        }
    }

    private void fileDump() throws IOException {  //just for debugging
        System.out.println("DUMP ----------");
        for (long i = 0; i < file.maxIndex() - 1; i++) {
            BTreeNode node = loadNode(i);
            node.debug();
        }
    }

    public void dump() throws IOException {
        dumpInner(0);
    }

    private void dumpInner(long fileIndex) throws IOException {
        BTreeNode node = loadNode(fileIndex);

        if (node.isLeaf) {
            for (TreeObject obj : node.keys) {
                // If the value isn't the sentinal value
                if (obj.sequence != -1) {
                    System.out.println(obj.toString(seqLen) + ": " + obj.frequency);
                }
            }
        } else {
            for (Long child : node.children) {
                dumpInner(child);
            }
        }
    }

    //This uses and prints to a dump file
    public void dumpStream(long fileIndex, PrintStream stream) throws IOException {
        BTreeNode node = loadNode(fileIndex);

        if (node.isLeaf) {
            for (TreeObject obj : node.keys) {
                // If the value isn't the sentinal value
                if (obj.sequence != -1) {
                	
                    stream.append(obj.toString(seqLen) + ": " + obj.frequency + "\n");
                }
            }
        } else {
            for (Long child : node.children) {
                dumpStream(child, stream);
            }
        }
    }
    
    private BTreeNode loadNode(long fileIndex) throws IOException {
        if (fileIndex == 0) {
            return root;
        }

        // Attempt to get the node from the cache
        BTreeNode node = cache.get(fileIndex);

        if (node == null) {
            node = file.read(fileIndex);
            cache.add(node);
        }

        return node;
    }

    private void saveNode(BTreeNode node) throws IOException {
        // Remove outdated node from cache
        cache.remove(node.fileIndex);

        // Write to disk
        file.write(node);
    }
    public int getSeqLen() {
    	return seqLen;
    }

    private void inOrder(BTreeNode node){
        if (node == null){
            return;
        }else{
            BTreeNode current = node;
        }

        int numChildren = node.getNumChildren();
        Stack<BTreeNode> s = new Stack<BTreeNode>();

        for(int i = numChildren; i>(numChildren/2); i--){
            //s.push(node.children[i]);
        }
        s.push(node);
        for(int i = numChildren/2; i >= 0; i--){
            //s.push(node.children[i]);
        }
        //pop next off stack, repeat....?
    }
}
