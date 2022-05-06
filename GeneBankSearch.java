/*
 *
 * 
 * @args useCache bTreeFile queryFile cacheSize(optional) debugLevel(optional)
 * 
 * all strings in queryFile need to match seqLength of bTreeFile
 * 
 */

import java.io.File;
import java.util.Scanner;

//will pull from DiskReadWrite Search
public class GeneBankSearch{

    public static void main(String[] args) {
		
		FileIO reader;
        File bTreeFile;
        File queryFile;
        BTree btree;

		int cacheSize;
        int debugLevel; //don't understand need
		int degree; //don't understand need
		int sequenceLength;
        
		int argsLength = args.length;
		try {
			if(argsLength >= 3 && argsLength <= 5) {//valid args
                //cache bool
                if(args[0].equals("1")) {
                    //<cacheSize>
					if(args[3] != null) {
						cacheSize = Integer.parseInt(args[3]);
						if(cacheSize <= 0) {
							throw new Exception("ERROR: Invalid cache size value");
						} // cache is implemented in BTree
					}else { //args[3] == null
						throw new Exception("ERROR: No cache size value");
					}
				} else if(args[0].equals("0")) {
					cacheSize = 0;
				} else { //args[0] is not 1 or 0
					throw new Exception("ERROR: Invalid cache boolean");
				}
				
                //gets BTreeFile
                bTreeFile = new File(args[1]);
                if(!bTreeFile.exists()){
                    throw new Exception("ERROR: bTreeFile does not exist");
                }else{
                    btree = new BTree(args[1], cacheSize);
                }

                //queryFile
                queryFile = new File(args[2]);
                if(!queryFile.exists()){
                    throw new Exception("ERROR: queryFile does not exist");
                }
                
				//debugLevel
                debugLevel = 0; //default
				if(args.length > 3 && args[3] != null) { // check arg 6 (which is optional)
					if(args[3].equals("1")) { 
						debugLevel = 1;
					}else if(!args[3].equals("0")) {//args[6] != 1 or 0
						throw new Exception("ERROR: Invalid debug level");
					}
				}
				
                reader = new FileIO(args[1]);
				Scanner fScan = new Scanner(queryFile);
                String dnaSequence;
                long sequence;
                int frequency;
                sequenceLength = btree.getSeqLen();
                while(fScan.hasNextLine()){
                    dnaSequence = fScan.nextLine();
                    dnaSequence = dnaSequence.toLowerCase();
                    frequency = 0;
                    sequence = 0;
                    
                    
                    for (int i = 0; i < sequenceLength; i++) {
                        sequence = sequence << 2;

                        switch (dnaSequence.charAt(i)) {
                        case 'a':
                        	sequence = sequence << 2; // shift left because its already 0
                            break;
                        case 'c':
                        	sequence |= 1;
                        	sequence = sequence << 2; // shift left for the next value
                        	break;
                        case 't': 
                        	sequence |= 3;
                        	sequence = sequence << 2; // shift left for the next value
                        	break;
                        case 'g': 
                        	sequence |= 2;
                        	sequence = sequence << 2; // shift left for the next value
                        	break;
                        }   
                    }
                    sequence = sequence << 1; //shift by 1 for first index to be 0
                    
                    frequency = btree.get(sequence);
                    System.out.println(dnaSequence + ": " + frequency);
                }
                fScan.close();
				
			}else { //invald # args
				throw new Exception("ERROR: Invalid usage"); //args > 6 or args < 4
			}
		}catch(Exception e){
			System.out.println(e);
			System.out.println("USAGE: java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file>\r\n"
					+ "[<cache size>] [<debug level>]");
		}
    }   
    
}
