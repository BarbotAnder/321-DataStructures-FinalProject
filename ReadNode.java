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
        */
    }
}