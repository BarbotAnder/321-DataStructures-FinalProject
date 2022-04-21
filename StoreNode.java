// Our professor wanted each node to be stored on a full disk block right? my current implementation should do that (NODE_SIZE)
// But I didnt fully understand how he wanted it to work --Ander

//currently would have to be implemented in a for loop, is that necessary?

/* Needs to be implemented in BTree
 * make sure we set outputFileName = geneBankFileName (new param) + ".btree.data." + sequenceLength + "." + degree;
 * also ensure we set sequenceLength and degree
 */

 

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