import java.io.*;
import java.util.TreeMap;

public class WordStatWords {
	public static void main(String[] args) {
		String inputFileName = args[0];
		String outputFileName = args[1];
		TreeMap<String, Integer> unicWords = new TreeMap<>();
		char symbol;
		String avolveChars = "'";
		int j = -1;
		String currentWord = "";
		String line = "";
		
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName), "UTF-8"))) {
			while ((line = in.readLine()) != null) {
				line = line.toLowerCase() + " ";
				for (int i = 0; i < line.length(); i++) {
					symbol = line.charAt(i);
					if (Character.isLetter(symbol) 
									|| Character.DASH_PUNCTUATION == Character.getType(symbol) 
												|| avolveChars.contains(symbol + "")) {
						if (j == -1) {
							j = i;
						}
					} else if (j != -1) {
						currentWord = line.substring(j, i);
						if (unicWords.containsKey(currentWord)) {
							unicWords.replace(currentWord, unicWords.get(currentWord) + 1);
						} else {
							unicWords.put(currentWord, 1);
						}
						j = -1;
					}
				}
			}
			try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), "UTF-8"))) {
				for (String key : unicWords.keySet()) {
					out.write(key + " " + (unicWords.get(key)) + "\n");
				}
			} catch (IOException e) {
			System.out.println("File error: " + e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("File error: " + e.getMessage());
		}
	}
}