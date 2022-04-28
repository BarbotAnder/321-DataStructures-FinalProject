public class TreeObject {
    long DNA = -1;
    int frequency = 0;

    public TreeObject(long DNA) {
        this.DNA = DNA;
    }
    public TreeObject(long DNA, int frequency) {
        this.DNA = DNA;
        this.frequency = frequency;
    }
    public long getDNA() {
        return DNA;
    }
    public void increase_frequency(){
        //How many times a given DNA sequence occurs
        frequency++;
    }
    public String counter(int numChar){
        //numChar: the number of chars given in a DNA sequence.
        return null;
    }
    public int getKeyValue() {
        return 0;
    }
    public void incrementFrequency() {

    }
    public int getFrequency() {
        return frequency;
    }
}
