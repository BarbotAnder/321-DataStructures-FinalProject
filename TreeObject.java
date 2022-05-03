public class TreeObject {
    public long sequence;
    public int frequency;

    public TreeObject(long sequence) {
        this.sequence = sequence;
		this.frequency = 1;
    }

    public TreeObject(long sequence, int frequency) {
        this.sequence = sequence;
		this.frequency = frequency;
    }

    public String toString() {
        return String.valueOf(sequence);
    }
	
    public String toString(int sequenceLength) {
    	long testseq = sequence;
    	testseq = sequence >> 1;
    	String stringSeq = "";
    	byte numcheck = 0;
    		for(int i = 2*sequenceLength - 1; i>=0; i -= 2) {
    			numcheck = 0;
    			numcheck |= (testseq >> (i-1));
    			switch(numcheck) {
    		    case 0:
    		    	stringSeq = stringSeq.concat("a");
    		    	break;
    		    case 1:
    		    	stringSeq = stringSeq.concat("c");
    		    	break;
    		    case 2:
    		    	stringSeq = stringSeq.concat("g");
    		    	break;
    		    case 3:
    		    	stringSeq = stringSeq.concat("t");
    		    	break;
    		   }
    			testseq &= ~(1<<i);
    			testseq &= ~(1<<i-1);
    		}
    				
    	return stringSeq;
    }
}
