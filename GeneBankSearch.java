/*
 *
 * 
 * @args useCache bTreeFile queryFile cacheSize(optional) debugLevel(optional)
 * 
 * all strings in queryFile need to match seqLength of bTreeFile
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//will pull from DiskReadWrite Search
public class GeneBankSearch{
    public static void printUsage() {
		System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
	}

    public static void main(String[] args) {
		int cacheSize;
		int debugLevel = 0;

		// did we get the correct number of arguments
		if (args.length < 3 || args.length > 5) {
			printUsage();
			return;
		}

		String bTreeFileName = args[1];
        String queryFileName = args[2];

		// cache argument
		if (args[0].equals("0")) {
			cacheSize = 0;
		} else if (args[0].equals("1") && args.length >= 4) {
			cacheSize = Integer.parseInt(args[3]);
		} else {
			System.out.println("ERROR: Invalid cache size");
			printUsage();
			return;
		}

		// debug level argument
		if(args.length == 5) {
			if(args[4].equals("0")) {
				debugLevel = 0;
				
			} else {
				System.out.println("ERROR: Invalid debug level");
				printUsage();
				return;
			}
		}

		try {
			File queryFile = new File(queryFileName);
			if(queryFile.exists() && queryFile.isFile()) {
				BTree btree = new BTree(bTreeFileName, cacheSize);
				Scanner scan = new Scanner(queryFile);

                while (scan.hasNextLine()) {
                    String line = scan.nextLine().toLowerCase();
                    int freq = btree.get(seqToLong(line));
                    System.out.println(line + ": " + freq);
                }
				
			} else { // file is not valid
				throw new FileNotFoundException();
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
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
}
