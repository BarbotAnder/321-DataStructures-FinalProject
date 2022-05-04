import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

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
						//optimal degree calculations
						//NODE_METADATA_SIZE + KEY_SIZE * (2 * degree - 1) + CHILD_POINTER_SIZE * (2 * degree) <= 4096
						// 8 + 12(2*degree + 1) + 8(2*degree - 1) <= 4096
						// 8 + 24degree + 12 + 16degree - 8 <= 4096
						// 12 + 40degree <= 4096
						// 40degree <= 4084
						// degree = 102
						
						degree = 102;
					}					
				} else { //the degree is not valid
					throw new Exception("ERROR: Invalid degree");//throw an error to stop program
				}
				
				
				//sequence length
				sequenceLength = Integer.parseInt(args[3]);
				if(sequenceLength <= 1 && sequenceLength > 31) { // length is over 31 
					throw new Exception("ERROR: Invalid sequenceLenth size");//throw an error to stop program
				} 
			
				
				
				//gbk file last because this is where we create our BTree
				File file = new File(args[2]);
				if(file.exists() && file.isFile()) {
					//TODO: delete print statements
					System.out.println("Degree: " + degree + "\nCacheSize: " + cacheSize + "\nsequenceLength: " + sequenceLength);
					System.out.println("File: " + args[2]);
					
					btree = new BTree("test", 0, degree, sequenceLength);
					readFile(file, sequenceLength, btree);
					
					
					
					
				} else { //file is not valid
					throw new FileNotFoundException();//throw an error to stop program
				}
				
				//check for the last argument
				if(argsLength == 6) { // check arg 6 (which is optional) then make sure it is valid
					if(args[5].equals("1")) { 
						
						//creates a dump file
						dump(btree,args[2] + ".btree.dump." + args[3]);
						
					}else if(args[5].equals("0")) { 
						
						//no dump file so print in console
						btree.dump();
						
					}else {
						throw new Exception("ERROR: Invalid debug level"); //throw an error to stop program
						//error not valid debug level
					}
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


    //THIS IS THE FUNCTION TO PARSE A GIVEN FILE
	//TODO: COPY PASTE PARSING FILE ON GIT HUB WHEN READY
	private static void readFile(File file, int length, BTree btree) throws IOException {
		int letters; //used for keeping track of size of subsequence
		boolean exit;
		boolean endOfFile = false; // makes sure if we are at the end of the file
		int x = 0; //used for next line tracking
		char letter; //keeps track of the current data
		boolean letterHadBefore = false;
		int sequenceSize = length;  
		try {
			
			Scanner scan = new Scanner(file);
			String nextLine = scan.nextLine();
			String currentLine;
			
			while(!nextLine.equals("ORIGIN") && !endOfFile) { //searches for the first origin line	
				if(scan.hasNextLine()) {
					nextLine = scan.nextLine();
				} else {
					endOfFile = true;
				}
			}
			while(!endOfFile) {
				exit = false;
				letters = 0;
				nextLine = scan.nextLine(); 
				while(!nextLine.equals("//")) {  
					currentLine = nextLine;
					nextLine = scan.nextLine(); //lineByLine is on the very first line of sequences
					
					for(int i = 0; i<currentLine.length(); i++) {
						if(exit) { //this is for the very last case where the very last char will not be filled in
							letter = 'w'; //so it sets it to a not valid character so it won't go through
						}else {
						letter = currentLine.charAt(i);
						}
						byte num = letterCheck(letter);
						if(num >= 0) { //is the starting value for the subsequence valid
	
							long subSequence = 0x0;
							subSequence |= (num & 0x3); // puts last two bits into the long
							subSequence = (subSequence << 2); //shifts over for the next bit to be added
							letters++;
							int additions = 0; // additions reset to 0 for every new i index (represents everytime we hit a ' '
							for(int j = 1; j<sequenceSize; j++) {
								if(currentLine.length()-1 >= (i+j+additions)) { // currentLine >= the char index remain in currentLine else go nextLine
									letter = currentLine.charAt(i+j+additions); // additions is if we have passed over any spaces
									num = letterCheck(letter);
									if(num >= 0) {
	
										subSequence |= (num & 0x3); // puts last two bits into the long
										subSequence = (subSequence << 2); //shifts over for the next bit to be added									letters++;
										letters++;
										if(letters == sequenceSize) { //if we have reached the selected size
											// this will be where you add the long to the binary tree
											//you will have to shift right by one bit no mater what because 
											// we have been shifting by two every time\
											subSequence = subSequence >> 1;
											btree.insert(subSequence); //insert into btree
	
											letters = 0;
										}
									}else if(letter == ' ') { //when the char is in the space
										additions++; //add one
										letter = currentLine.charAt(i+j+additions); //remakes the letter to the very next one
										num = letterCheck(letter);
	
										// add char to long and shift
										subSequence |= (num & 0x3); // puts last two bits into the long
										subSequence = (subSequence << 2); //shifts over for the next bit to be added
										letters++;
										if(letters == sequenceSize) {  //if we have reached the selected size
											subSequence = subSequence >> 1;
											btree.insert(subSequence); //insert into btree
											
											letters = 0;
										}
									}else if(letter == 'n') { 
										if(letters != 0) { // if we have letters in our sequence else do nothing
											// we want to do nothing so we
											//can remake a new long with a vaild length
											
											letters = 0;
											j = sequenceSize; // resets to start a new subsequence
										}
									}
								}else { //go into next line for last few spots of the sequence
									if(!letterHadBefore) { //this checks if we have already been inside the next line for the same sequence
										x = 0;
									}
									if(!nextLine.equals("//")) { //this checks if our nextline does not equal the end of our parsing
										letter = nextLine.charAt(++x); //if we already been through next line once it will take the char after the previous one
										while(letter != 'a' && letter != 't' && letter != 'c' && letter != 'g') { //finds the first letter
											x++;
											letter = nextLine.charAt(x);
										}
										letterHadBefore = true; // sets the we have now been through the next Line
										num = letterCheck(letter);
										// add char to long and shift
	
										subSequence |= (num & 0x3); // puts last two bits into the long
										subSequence = (subSequence << 2); //shifts over for the next bit to be added
										letters++;
										if(letters == sequenceSize) { //if we have reached the selected size
											subSequence = subSequence >> 1;
										    btree.insert(subSequence); //insert into btree
	
											letters = 0;
											letterHadBefore = false;
										}
									}else {
										exit = true; //for the very last case, if we didn't do this the letter would remain the same and would add on
									}
								}
							}
						}
					}	
				}
				while(!nextLine.equals("ORIGIN") && !endOfFile) { //searches for next origin if there is one	
					if(scan.hasNextLine()) {
						nextLine = scan.nextLine();
					} else {
						endOfFile = true;
					}
				}
			}
			scan.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static byte letterCheck(char letter) {
		switch(letter) {
		case 'a':
			return 0;
		case 'c':
			return 1;
		case 't':
			return 3;
		case 'g':
			return 2;
		default:
			return -1; //if its not a valid letter we want
		}			
	}
	
	/**
	 * @param: btree object
	 * @param: filename the dump file
	**/
	public static void dump(BTree tree, String filename) throws IOException {
		PrintStream ps;
		try {
			ps = new PrintStream(filename);
			PrintStream stdout = System.out;
			tree.dumpStream(0, ps);
			System.setOut(ps);
	    	System.setOut(stdout);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
