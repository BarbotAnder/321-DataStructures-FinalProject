import java.io.*;

public class FileIO {
    private final long METADATA_SIZE = 8; // int, int (degree, seqLen)
    private final int NODE_SIZE;
    private long nextIndex;

    public final int degree;
    public final int seqLen;

    public RandomAccessFile file;
    
    // Create a file
    public FileIO(String fileName, int degree, int seqLen) throws IOException {
        file = new RandomAccessFile(fileName, "rw");
        file.writeInt(degree);
        file.writeInt(seqLen);
        this.degree = degree;
        this.seqLen = seqLen;
        NODE_SIZE = BTreeNode.serialSize(degree);
        nextIndex = 1;
    }

    // Load an existing file
    public FileIO(String fileName) throws IOException {
        file = new RandomAccessFile(fileName, "rw");
        this.degree = file.readInt();
        this.seqLen = file.readInt();
        NODE_SIZE = BTreeNode.serialSize(this.degree);
        nextIndex = ((file.length() - METADATA_SIZE) / NODE_SIZE) + 1;
    }

    // Get the next available fileIndex
    public long nextIndex() {
        long result = nextIndex;
        nextIndex += 1;
        return result;
    }

    public long maxIndex() throws IOException {
        return ((file.length() - METADATA_SIZE) / NODE_SIZE) + 1;
    }

    // Write a node to the file
    public void write(BTreeNode node) throws IOException {
        file.seek(METADATA_SIZE + (node.fileIndex * NODE_SIZE));
        file.write(node.serialize(degree, seqLen));
    }

    // Read a node from the specified location in the file
    public BTreeNode read(long fileIndex) throws IOException {
        byte[] buffer = new byte[NODE_SIZE];
        file.seek(METADATA_SIZE + (fileIndex * NODE_SIZE));
        file.read(buffer);
        return new BTreeNode(fileIndex, buffer, degree);
    }
}