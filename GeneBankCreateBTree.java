import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class GeneBankCreateBTree {
	public static void printUsage() {
		System.out.println("USAGE: java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
	}

	public static void main(String[] args) {
		BTree btree;
		int cacheSize;
		int degree;
		int sequenceLength;
		int debugLevel = 0;

		// did we get the correct number of arguments
		if (args.length < 4 || args.length > 6) {
			printUsage();
			return;
		}

		String fileName = args[2];

		// cache argument
		if (args[0].equals("0")) {
			cacheSize = 0;
		} else if (args[0].equals("1") && args.length >= 5) {
			cacheSize = Integer.parseInt(args[4]);
		} else {
			System.out.println("ERROR: Invalid cache size");
			printUsage();
			return;
		}

		// degree argument
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
		} else { // the degree is not valid
			System.out.println("ERROR: Invalid degree");
			printUsage();
			return;
		}

		// sequence argument
		sequenceLength = Integer.parseInt(args[3]);
		if(sequenceLength <= 1 || sequenceLength > 31) {
			System.out.println("ERROR: Invalid sequence lenth");
			printUsage();
			return;
		}

		// debug level argument
		if(args.length == 6) {
			if(args[5].equals("0")) {
				debugLevel = 0;
				
			} else if(args[5].equals("1")) { 
				debugLevel = 1;
				
			} else {
				System.out.println("ERROR: Invalid debug level");
				printUsage();
				return;
			}
		}

		try {
			File file = new File(fileName);
			if(file.exists() && file.isFile()) {
				btree = new BTree(fileName + ".btree.data." + sequenceLength + "." + degree, cacheSize, degree, sequenceLength);
				loadFile(file, btree);

				if (debugLevel == 0) {

				} else if (debugLevel == 1) {
					// creates a dump file
					dump(btree, "dump");
				}
				
			} else { // file is not valid
				throw new FileNotFoundException();
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadFile(File file, BTree btree) throws IOException {
		Scanner scan = new Scanner(file);
		boolean isDNA = false;
		String fullSequence = new String();

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.contains("ORIGIN")) {
				isDNA = true;
				continue;
			}

			if (isDNA) {
				if (line.contains("//")) {
					for (int i = 0; i < fullSequence.length() - btree.seqLen; i++) {
						long converted = seqToLong(fullSequence.substring(i, i + btree.seqLen));
						if (converted != -1) {
							btree.insert(converted);
						}
					}

					isDNA = false;
					fullSequence = "";
					continue;
				}

				fullSequence += line.substring(10).replaceAll("\\s", "");
			}
		}

		scan.close();
	}

	private static long seqToLong(String seq) {
		long result = 0;
		for (char c : seq.toLowerCase().toCharArray()) {
			switch (c) {
				case 'a':
					result = result << 2;
					break;
				
				case 't':
					result = result << 2;
					result |= 3;
					break;

				case 'c':
					result = result << 2;
					result |= 1;
					break;

				case 'g':
					result = result << 2;
					result |= 2;
					break;

				default:
					return -1;
			}
		}
		return result;
	}
	
	/**
	 * @param: btree object
	 * @param: filename the dump file
	**/
	public static void dump(BTree tree, String filename) throws IOException {
		PrintStream ps;
		try {
			ps = new PrintStream(filename);
			tree.dumpStream(0, ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
