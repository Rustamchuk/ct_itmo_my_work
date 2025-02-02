import java.util.Arrays;

public class ReverseOct {
	public static void main(String[] args) {
		String[][] inputStrings = new String[1][];
		Scanner scannerLines = new Scanner(System.in);
		String line = "";
		String number;
		int i = 0;
		int j = 0;

		while (scannerLines.hasNextWordArray()) {
			//System.out.println(line);
			if (i >= inputStrings.length) {
				inputStrings = Arrays.copyOf(inputStrings, i * 2);
			}
			inputStrings[i] = new String[1];
			String[] n = scannerLines.nextWordArray();
			while (j < n.length) {
				//System.out.print(number + " ");
				if (j >= inputStrings[i].length) {
					inputStrings[i] = Arrays.copyOf(inputStrings[i], j * 2);
				}
				inputStrings[i][j] = n[j];
				j++;
			}
			inputStrings[i] = Arrays.copyOf(inputStrings[i], j);
			i++;
			j = 0;
		}
		scannerLines.close();

		StringBuilder a = new StringBuilder();
		for (int n = inputStrings.length - 1; n >= 0; n--) {
			if (inputStrings[n] != null) {
				for (int k = inputStrings[n].length - 1; k >= 0; k--) {
					a.append(inputStrings[n][k]).append(" ");
				}
				if (n != 0)
					a.append(System.lineSeparator());
			}
		}
		System.out.println(a.toString());
	}
}