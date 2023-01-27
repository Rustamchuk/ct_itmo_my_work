import java.io.*;
import java.util.LinkedHashMap;

public class Wspp {
	public static void main(String[] args) {
		String inputFileName = args[0];
		String outputFileName = args[1];
		LinkedHashMap<String, IntList> unicWords = new LinkedHashMap<>();
		String currentWord = "";
		String line = "";
		int j = 0;
		
		try {
			Scanner scanFile = new Scanner(new FileReader(inputFileName));
			try {
				while (scanFile.hasNextOnlyWordArray()) {
					String[] words = scanFile.nextOnlyWordArray();
					for (String word : words) {
						currentWord = word.toLowerCase();
						j++;
						if (!unicWords.containsKey(currentWord)) {
							unicWords.put(currentWord, new IntList());
						}
						unicWords.get(currentWord).addWord(j);
					}
				}
			} finally {
				scanFile.close();
			}
			try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"))) {
				for (String key : unicWords.keySet()) {
					out.write(key + " " + (unicWords.get(key).getCount()));
					for (int pos : unicWords.get(key).getPos()) {
						out.write(" " + pos);
					}
					out.write("\n");
				}
			} catch (IOException e) {
				System.out.println("File error: " + e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found error: " + e.getMessage());
		}
	}
}