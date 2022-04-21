import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/*
 * This class will convert all node Data, except fileIndex, to binary data and store it in the correct file, 
 * or will convert the binary data back into the correct node
 */

public class DiskReadWrite {
    private int METADATA_SIZE = 8;
    private long nextDiskAddress = METADATA_SIZE;
    private FileChannel file;
    private ByteBuffer buffer;
    private int nodeSize;

    private long rootAddress = METADATA_SIZE; // offset to the root node
    //private Node root; 

    public DiskReadWrite(File fileName) throws IOException {    // constructor, use this DiskReadWriter to store/read nodes using functions
        nodeSize = 4096;
        buffer = ByteBuffer.allocateDirect(nodeSize);

        try {
            if (!fileName.exists()) {   //create file and write meta data
                fileName.createNewFile();
                RandomAccessFile dataFile = new RandomAccessFile(fileName, "rw");
                file = dataFile.getChannel();
                writeMetaData();    //TODO
            } else { //
                RandomAccessFile dataFile = new RandomAccessFile(fileName, "rw");
                file = dataFile.getChannel();
                //readMetaData();
                //root = diskRead(rootAddress);
            } 
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

        // rootAddress = tmpbuffer.getLong();    //TODO: add a million and a half metadata pieces. (simplifying all metadata into one object would make this easy to return.)
    }
}
























/*this is a mess lmao

// Our professor wanted each node to be stored on a full disk block right? my current implementation should do that (NODE_SIZE)
// But I didnt fully understand how he wanted it to work --Ander

//currently would have to be implemented in a for loop, is that necessary?

/* Needs to be implemented in BTree
 * make sure we set outputFileName = geneBankFileName (new param) + ".btree.data." + sequenceLength + "." + degree;
 * also ensure we set sequenceLength and degree
 *

 

public void StoreNode(BTreeNode node){
    private static final int NODE_SIZE = 4096;    //size of disk block in bytes,
    File outputFile = new File(outputFileName);     

    try (){
        if(node.isRoot){    //obviously not correct
            outputFile.createNewFile();
            //TODO: lots more
        }
    
    byte[] nodeData = new byte[NODE_SIZE];
    //TODO: convert full node into nodeData

    OutputStream outputStream = new FileOutputStream(outputFile);
    outputStream.write(nodeData); //will always write entire byte[]

    } catch (IOException e) {
        e.printStackTrace();
    }
}



// Our professor wanted each node to be stored on a full disk block right? my current implementation should do that (NODE_SIZE)
// But I didnt fully understand how he wanted it to work --Ander

// Needs to be implemented in BTree

public Node ReadNode(BTreeNode node){   //i believe this is possible since it is merely asking for a pointer?
    private static final int NODE_SIZE = 4096

    public static void main(String args[]) {
        File inputFile = new File(inputFileName);

        try{
            InputStream inputStream = new FileInputStream(inputFile);
            byte[] nodeData = new byte[NODE_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(nodeData)) != -1) {
                // nodeData contains all the data from the next node on every iteration
                // will need to use this as is to search and the string version of it for dump (I haven't checked example dump to confirm)
                
            }
    
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
*/