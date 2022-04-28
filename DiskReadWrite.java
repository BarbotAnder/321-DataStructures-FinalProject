import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * This class will convert all node Data, except fileIndex, to binary data and store it in the correct file, 
 * or will convert the binary data back into the correct node
 * 
 * 
 * 
 * currently will have empty space at the back, could be implemented to leave space where it originates, i do not think it matters, it will be discussed w group
 */

public class DiskReadWrite {
    private final int METADATA_SIZE = 8;            //2 ints (degree, seqLen)
    private final int NODE_METADATA_SIZE = 13;      //bool, long, int (leaf, numKeys, parentPointer)
    private final int KEY_SIZE = 12;                //long, int (sequence, frequency)
    private final int CHILD_POINTER_SIZE = 8;       //long (childPointer)
    RandomAccessFile dataFile;
    private FileChannel file;
    private ByteBuffer buffer;
    private int nodeSize;

    //metadata
    private int degree;
    private int seqLen;



    public DiskReadWrite(File fileName) throws IOException {    //constructor, use this DiskReadWriter to store/read nodes using functions
        try {
            if (!fileName.exists()) {   //create file, write and read metadata
                fileName.createNewFile();
                dataFile = new RandomAccessFile(fileName, "rw");
                file = dataFile.getChannel();
                writeMetaData();
                readMetaData();
            } else {                    //read metadata
                dataFile = new RandomAccessFile(fileName, "rw");
                file = dataFile.getChannel();
                readMetaData();
            } 

            //initialize vars
            nodeSize = NODE_METADATA_SIZE + KEY_SIZE * (2 * degree - 1) + CHILD_POINTER_SIZE * (2 * degree); //(2t-1)treeObj, (2t)childPointer
            buffer = ByteBuffer.allocateDirect(nodeSize);
    
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    public void diskWrite(BTreeNode node) throws IOException {
        file.position((node.getFileIndex()*nodeSize) + METADATA_SIZE);    //position of the start of the node (in bytes)
        buffer.clear();

        //put all node data into binary stream
        if(node.getLeafStatus()){
            buffer.put((byte) 1);
        }else{
            buffer.put((byte) 0);
        }
        int n = node.getNumKeys();
        buffer.putInt(n);
        buffer.putLong(node.getParentNode());
        for(int i = 0; i < n; i++){
            TreeObject current = node.getKey(i);
            buffer.putLong(current.getDNA());
            buffer.putInt(current.getFrequency());
        }
        if(!node.getLeafStatus()){
            for(int i = 0; i < n + 1; i++){
                buffer.putLong(node.getChild(i));
            }
        }

        //writes gathered information to file
        buffer.flip();
        file.write(buffer);
    }


    public BTreeNode diskRead(long fileIndex) throws IOException {
        file.position((fileIndex*nodeSize) + METADATA_SIZE);      //position of the start of the node (in bytes)
        buffer.clear();

        //reads a full buffer (which is nodeSize)    therefore reads a full node from file
        file.read(buffer);
        buffer.flip();

        //turn the data gathered in the binary stream into a BTreeNode
        BTreeNode node = new BTreeNode();  //node we will return
        node.setFileIndex(fileIndex);
        byte leaf = buffer.get();
        if(leaf == 0){
            node.setLeaf(false); 
        }else{
            node.setLeaf(true); 
        }
	    int numKeys = buffer.getInt();
	    node.setParent(buffer.getLong());
        for(int i = 0; i < numKeys; i++){   //finds all keys stored
            TreeObject current = new TreeObject(buffer.getLong(), buffer.getInt());
            node.insertKey(current, i);
        }
        if(leaf == 0){
            for(int i = 0; i < numKeys + 1; i++){
                node.insertChild(buffer.getLong(), i);
            }
        }
        return node;
    }

    public void writeMetaData() throws IOException {
        file.position(0);
        
        //buffer of proper size ensures no wasted space
        ByteBuffer tmpBuffer = ByteBuffer.allocateDirect(METADATA_SIZE);
        tmpBuffer.clear();

        tmpBuffer.putInt(degree);
        tmpBuffer.putInt(seqLen);

        //writes metadata to file
        tmpBuffer.flip();
        file.write(tmpBuffer);
    }

    public void readMetaData() throws IOException {
        file.position(0);

        //buffer of proper size ensures only taking valid metadata
        ByteBuffer tmpBuffer = ByteBuffer.allocateDirect(METADATA_SIZE);
        tmpBuffer.clear();

        //read metadata from file
        file.read(tmpBuffer);
        tmpBuffer.flip();

        //turn the read data into understandable data & keep a record of it
        degree = tmpBuffer.getInt();
        seqLen = tmpBuffer.getInt();
    }
    
    //search through file for the provided sequence
    public int Search(long sequence) throws IOException{
        long low = 0;
        int mid = 0;
        long high = (file.size() - METADATA_SIZE) / nodeSize; //starts as nodeCount
        long pos;                                           //beginning of node we will be analyzing

        long currentSeq;
        while(low <= high){                                 //while not at the end of file
            mid = (int) (low + high) / 2;
            pos = (mid * nodeSize) + METADATA_SIZE + 1;  

            buffer.clear();                                 //restore buffer
            file.read(buffer, pos);             //read in a full node
            buffer.flip();
            
            buffer.get();           //get past leaf
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
        return -1;                                          //when sequence not found
    }
    
    public void setMetaData(int degree, int seqLen){
        this.degree = degree;
        this.seqLen = seqLen;
    }

    public void close() throws IOException{
        file.close();
        dataFile.close();
    }
}