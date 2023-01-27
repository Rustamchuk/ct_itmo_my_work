import java.util.*;

public class IntList {
	private int count;
	private List<Integer> positions = new ArrayList<Integer>();
	private List<String> lines = new ArrayList<String>();
	
	public void addWord(int pos) {
		count++;
		positions.add(pos);
	}
	
	public void addWord(int pos, int line) {
		count++;
		lines.add(line + ":" + pos);
	}
	
	public int getCount() {
		return count;
	}
	
	public List<Integer> getPos() {
		return positions;
	}
	
	public List<String> getLine() {
		return lines;
	}
}