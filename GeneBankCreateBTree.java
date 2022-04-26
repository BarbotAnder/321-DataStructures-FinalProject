import java.io.File;
import java.io.FileNotFoundException;

public class GeneBankCreateBTree {

	

	public static void main(String[] args) {
		
		BTree btree;
		int cacheSize; //will change if we want a cache
		int degree;
		int sequenceLength;
		
		int argsLength = args.length; //doing this so i dont have to call args.length a bunch
		try {
			if(argsLength >= 4 && argsLength <= 6) {//first check the first 4 maditory arguments
				
				if(args[0].equals("1")) { //checks for cache NOTE: if with cache args[4] is also maditory
					
					if(argsLength == 5) {//since we want cache check size for cache
						cacheSize = Integer.parseInt(args[4]);
						if(cacheSize <= 0) { // we will test sizes 100 and 500
							throw new Exception("ERROR: Invalid cache size value"); //throw an error to stop program b/c its invalid
						
						}else {
							//implementing btree later covers making the cache with cacheSize
						}
					}else { //there is no arguement for the size
						throw new Exception("ERROR: No cache size value");
					}
					
				} else if(args[0].equals("0")) { //do not implement cache
					cacheSize = 0; //this will not create a cache when creating the btree
					
				} else { //args[0] is not 1 or 0
					throw new Exception("ERROR: Invalid cache value"); //throw an error to stop program basically
				}
				
				//degree
				degree = Integer.parseInt(args[1]);
				if(degree >= 0 && degree < 4096) { 
					if(degree == 0) {
						
						// code for chooseing optimum degree
					
					}
					//System.out.println(degree);	
					
				} else { //the degree is not valid
					throw new Exception("ERROR: Invalid degree");//throw an error to stop program
				}
				
				
				//sequence length
				sequenceLength = Integer.parseInt(args[3]);
				if(sequenceLength <= 1 && sequenceLength > 31) { // length is over 31 
					throw new Exception("ERROR: Invalid sequenceLenth size");//throw an error to stop program
				} 
			
				//check for the last argument
				if(argsLength == 6) { // check arg 6 (which is optional) then make sure it is valid
					if(args[5].equals("1")) { 
						
						//creates a dump file
						
					}else if(args[5].equals("0")) { 
						
						//no dump file (print in console?)
						
					}else {
						throw new Exception("ERROR: Invalid debug level"); //throw an error to stop program
						//error not valid debug level
					}
				}
				
				//gbk file last because this is where we create our BTree
				File file = new File(args[2]);
				if(file.exists() && file.isFile()) {
					System.out.println("Degree: " + degree + "\nCacheSize: " + cacheSize + "\nsequenceLength: " + sequenceLength);
					System.out.println("File: " + args[2]);
					//btree = new BTree(degree, cacheSize, sequenceLength, "test");
					//readFile(file, sequenceLength, btree);
					
					
					
				} else { //file is not valid
					throw new FileNotFoundException();//throw an error to stop program
				}
				
				
			}else { //invald # args
				throw new Exception("ERROR: Invalid usage"); //args > 6 and args < 4
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
