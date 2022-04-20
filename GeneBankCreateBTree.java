/*
 *
 * 
 * @args useCache degree geneBankFile seqLength cacheSize(optional) debugLevel(optional)
 * 
 * degree = 0 = optimal t
 * seqLength <= 31
 * debug = 1  = all DNA Strings stored in Filw dump using inorder transversal
 */

 public class GeneBankCreateBTree{
    private static final int BUFFER_SIZE = 4096

    public static void main(String args[]) {
        //what abt natively buffer stream?


        //write to binary Stream
        /*
        String outputFile = args[1];
        try (
            OutputStream outputStream = new FileOutputStream(outputFile);
        ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            long currentLong = -1;  //need conversion from string to long
            outputStream.write(buffer, 0, currentLong);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */


        //read from binary Stream
        /*
        String inputFile = args[0];
        try (
        InputStream inputStream = new FileInputStream(inputFile);
        ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                currentLong = bytesRead; //will need to use this as is to search and the string version of it for dump (I haven't checked example dump to confirm)
            }
    
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */
    }
 }