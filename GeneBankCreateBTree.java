import java.io.File;
import java.io.FileNotFoundException;

public class GeneBankCreateBTree {

	public GeneBankCreateBTree() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		int argsLength = args.length; //doing this so i dont have to call args.length a bunch
		try {
			if(argsLength >= 4 && argsLength <= 6) {
				//first check the first 4 maditory arguments
				//NOTE: if with cache args[4] is also maditory
				
				if(args[0].equals("1")) { // checks either 1 or 0
					
					if(argsLength == 5) {//implement cache by checking first if we have a size for it
						
						int cacheSize = Integer.parseInt(args[4]);
						if(cacheSize <= 0) { // we will test sizes 100 and 500
							throw new Exception("ERROR: Invalid cache size value"); //throw an error to stop program b/c its invalid
						
						}else {
							//valid cache size so implemet cache
							//NOTE we will want to store the cache after we finish to use as a parameter for search algo
						}
					}else { //there is no arguement for the size
						throw new Exception("ERROR: No cache size value"); //throw an error to stop program b/c its invalid
					}
					
				} else if(args[0].equals("0")) { //do not implement cache

				} else { //args[0] is not 1 or 0
					throw new Exception("ERROR: Invalid cache value"); //throw an error to stop program basically
				}
				
				//degree
				int degree = Integer.parseInt(args[1]);
				if(degree >= 0 && degree < 4096) { 
					if(degree == 0) {
						// code for chooseing optimum degree
					}
					System.out.println(degree);	
					
				} else { //the degree is not valid
					throw new Exception("ERROR: Invalid degree");//throw an error to stop program
				}
				
				//gbk file
				File file = new File(args[2]);
				if(file.exists() && file.isFile()) { //file exits
					System.out.println("valid file");			

				} else { //file is not valid
					throw new FileNotFoundException();//throw an error to stop program
				}
				
				
				//sequence length
				int sequenceLength = Integer.parseInt(args[3]);
				if(sequenceLength > 0 && sequenceLength < 32) { // 1 to 31 (inclusive)
					System.out.println(sequenceLength);			
				} else {
					//throw error maybe?
					throw new Exception("ERROR: Invalid sequenceLenth size");//throw an error to stop program
				}
			}
			
			//check for the last 
			if(argsLength == 6) { // has 6 arguments (which is now optional) so make sure it is valid
				if(args[5].equals("1")) { //creates a dump file
					
				}else if(args[5].equals("0")) { //no dump file just print in console
					
				}else {
					throw new Exception("ERROR: Invalid debug level"); //throw an error to stop program
					//error not valid debug level
				}
			
			} else {
				throw new Exception("ERROR: Invalid usage"); //throw an error to stop program 
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(Exception e){
			System.out.println(e);
			System.out.println("USAGE: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length>\r\n"
					+ "[<cache size>] [<debug level>]");;
		}
	}
}


//TODO: COPY PASTE PARSING FILE ON GIT HUB WHEN READY
