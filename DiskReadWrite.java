import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * This class will convert all node Data, except fileIndex, to binary data and store it in the correct file, 
 * or will convert the binary data back into the correct node
 */

public class DiskReadWrite {
    private final int METADATA_SIZE = 8;      //2 ints
    private FileChannel file;
    private ByteBuffer buffer;
    private int nodeSize;

    //metadata
    private int degree;
    private int seqLen;



    public DiskReadWrite(File fileName) throws IOException {    // constructor, use this DiskReadWriter to store/read nodes using functions
        try {
            if (!fileName.exists()) {   //create file and write meta data
                fileName.createNewFile();
                RandomAccessFile dataFile = new RandomAccessFile(fileName, "rw");
                file = dataFile.getChannel();
                writeMetaData();
                readMetaData();
            } else {                    //read metadata
                RandomAccessFile dataFile = new RandomAccessFile(fileName, "rw");
                file = dataFile.getChannel();
                readMetaData();
            } 

            nodeSize = 13 + 12*degree; //bool, int, long, 12/treeObj, 8/childPointer
            buffer = ByteBuffer.allocateDirect(nodeSize);
    
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    public void diskWrite(BTreeNode node) throws IOException {
        file.position((node.getFileIndex()*nodeSize) + METADATA_SIZE);    //position of node in bytes
        buffer.clear();

        //put all node data into binary stream
        if(node.getLeafStatus){
            buffer.put((byte) 1)
        }else{
            buffer.put((byte) 0)
        }
        int n = node.getNumKeys;
        buffer.putInt(n);
        buffer.putLong(node.getParentNode);
        for(int i = 0; i < n; i++){
            TreeObject current = node.getKey(i);
            buffer.putLong(current.getDNA());
            buffer.putInt(current.getFrequency());         //TODO: needs to be implemented in TreeObject
        }
        if(!node.getLeafStatus()){
            for(int i = 0; i < n + 1; i++){
                buffer.putLong(node.getChild(i))
            }
        }

        //writes gathered information to file
        buffer.flip();
        file.write(buffer);
    }


    public BTreeNode diskRead(long fileIndex) throws IOException {
        file.position((fileIndex*nodeSize) + METADATA_SIZE);      //position of node in bytes
        buffer.clear();

        //reads a full buffer (which is nodeSize)    therefore reads a full node from file
        file.read(buffer);
        buffer.flip();

        //turn the data gathered in the binary stream into a BTreeNode
        BTreeNode node = new BTreeNode();  //node we will return
        node.setFileIndex(fileIndex);
        if(buffer.get() = 0){
            node.setLeaf(false); 
        }else{
            node.setLeaf(true); 
        }
	    int numKeys = buffer.getInt();
	    node.setParent(buffer.getLong());
        for(int i = 0; i < numKeys; i++){   //finds all keys stored
            TreeObject current = new TreeObject(buffer.getLong(), buffer.getInt());      //TODO: needs to be added in treeObject class
            node.insertKey(current, i);
        }
        if(!leaf){
            for(int i = 0; i < numKeys + 1; i++){
                node.insertChild(buffer.getLong());
            }
        }
        return node;
    }

    public void writeMetaData() throws IOException {
        file.position(0);
        
        //buffer of proper size ensures no wasted space
        ByteBuffer tmpbuffer = ByteBuffer.allocateDirect(METADATA_SIZE);
        tmpbuffer.clear();

        //tmpbuffer.put();    //TODO: add a million and a half things

        //writes metadata to file
        tmpbuffer.flip();
        file.write(tmpbuffer);
    }

    public void readMetaData() throws IOException {
        file.position(0);

        //buffer of proper size ensures only taking valid metadata
        ByteBuffer tmpbuffer = ByteBuffer.allocateDirect(METADATA_SIZE);
        tmpbuffer.clear();
        file.read(tmpbuffer);
        tmpbuffer.flip();

        // rootAddress = tmpbuffer.getLong();    //TODO: add a million and a half metadata pieces. (metadata gets stored in here.)
    }
    
    //planned completion 4/24
    public int Search(long key, fileChannel file){
        file.position(0);

        
        

    }
}