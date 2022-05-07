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
	
	public String toString(int len) {
		long seq = this.sequence;
		String result = "";

		for (int i = 0; i < len; i++) {
			int c = (int) (seq & 3);
			switch (c) {
				case 0:
					result = "a" + result;
					break;
				
				case 3:
					result = "t" + result;
					break;

				case 1:
					result = "c" + result;
					break;

				case 2:
					result = "g" + result;
					break;

				default:
					return null;
			}

			seq = seq >> 2;
		}

		return result;
	}
}
