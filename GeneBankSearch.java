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
		int cacheSize;
        int debugLevel; //don't understand need
		int degree; //don't understand need
		int sequenceLength;
        
		int argsLength = args.length;
		try {
			if(argsLength >= 4 && argsLength <= 6) {//valid args
                //cache bool
                if(args[0].equals("1")) {
                    //<cacheSize>
					if(args[5] != null) {
						cacheSize = Integer.parseInt(args[5]);
						if(cacheSize <= 0) {
							throw new Exception("ERROR: Invalid cache size value");
						
						}else { //cacheSize > 0
							//TODO: make a cache with cacheSize
						}
					}else { //args[5] == null
						throw new Exception("ERROR: No cache size value");
					}
					
				} else if(args[0].equals("0")) {
					cacheSize = 0;                          //TODO: check that this will not create a cache when creating the btree
					
				} else { //args[0] is not 1 or 0
					throw new Exception("ERROR: Invalid cache boolean");
				}
				
				//degree
				degree = Integer.parseInt(args[1]);
				if (degree < 0) {
					throw new Exception("ERROR: Invalid degree");
				}
				
                //BTreeFile
                bTreeFile = new File(args[2]);
                if(!bTreeFile.exists()){
                    throw new Exception("ERROR: bTreeFile does not exist");
                }
                
				//sequence length
				sequenceLength = Integer.parseInt(args[3]);
				if(sequenceLength <= 1 && sequenceLength > 31) {
					throw new Exception("ERROR: Invalid sequenceLenth size");
				} 

                //queryFile
                queryFile = new File(args[4]);
                if(!queryFile.exists()){
                    throw new Exception("ERROR: queryFile does not exist");
                }
			
				//<debugLevel>
                debugLevel = 0; //default
				if(args[6] != null) { // check arg 6 (which is optional)
					if(args[6].equals("1")) { 
						debugLevel = 1;
					}else if(!args[6].equals(0)) {//args[6] != 1 or 0
						throw new Exception("ERROR: Invalid debug level");
					}
				}

                //TODO: Add cache implementation

                reader = new FileIO(args[2]);
				Scanner fScan = new Scanner(queryFile);
                String dnaSequence;
                long sequence;
                int frequency;

                while(fScan.hasNextLine()){
                    dnaSequence = fScan.nextLine();
                    frequency = 0;
                    sequence = 0;
                    
                    for (int i = 0; i < sequenceLength; i++) {
                        sequence = sequence << 2;

                        switch (dnaSequence.charAt(i)) {
                        case 'a':
                            sequence = sequence | DNA.A;
                            break;
                        case 'c':
                            sequence = sequence | DNA.C;
                            break;
                        case 't':
                            sequence = sequence | DNA.T;
                            break;
                        case 'g':
                            sequence = sequence | DNA.G;
                            break;
                        }   
                    }
                    frequency = reader.Search(sequence);        //TODO: implement here or in FileIO.java

                    //sequence and frequncy are printed
                    System.out.println("original: " + dnaSequence + "\tfrequency: " + frequency);     //TODO: check sample query outputs
                }
                fScan.close();
				
			}else { //invald # args
				throw new Exception("ERROR: Invalid usage"); //args > 6 or args < 4
			}
		}catch(Exception e){
			System.out.println(e);
			System.out.println("USAGE: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length>\r\n"
					+ "[<cache size>] [<debug level>]");
		}
    }
}