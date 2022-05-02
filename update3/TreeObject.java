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
}
