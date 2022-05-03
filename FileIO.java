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

    /* by Ander, not fully corrected, could be implemented in GeneBankSearch.java
    
    
    public int Search(long sequence) throws IOException{
        int low = 0;
        int mid = 0;
        int high = (int) (file.length() - METADATA_SIZE) / NODE_SIZE; //starts as nodeCount
        long pos;                                           //beginning of node we will be analyzing (in bytes)

        byte[] buffer = new byte[NODE_SIZE];
        long currentSeq;
        while(low <= high){                                 //while not at the end of file
            mid = (int) (low + high) / 2;
            pos = ((mid -1 ) * NODE_SIZE) + METADATA_SIZE + 1;    //keep +1? TODO: Check (determined by whether read(byte[] reads pos into byte[0], or pos+1 into byte[0])

            file.seek(pos);
            file.read(buffer);             //read in a full node
            
            
            buffer.           //get past leaf
            int numKeys = buffer.getInt();
            buffer.getLong();       //get past parentPointer

            long currentLow = 0;                                //lowest value in current node
            long currentHigh = 0;                               //highest value in current node
            for(int i = 0; i < numKeys; i++){               //for all keys stored
                currentSeq = buffer.getLong();              
                if(i == 0){
                    currentLow = currentSeq;
                }else if(i == numKeys - 1){
                    currentHigh = currentSeq;
                }
                if(currentSeq == sequence){                 //correct sequence found
                    return buffer.getInt();                 //return correct frequency
                }
                buffer.getInt();     //get past frequency
            }
            if(currentHigh > sequence){
                high = mid - 1;                             
            }else if (currentLow < sequence){
                low = mid + 1;
            }
            System.out.println(".");
        }
        return -1;                                           //when sequence not found
    }
    */
}
