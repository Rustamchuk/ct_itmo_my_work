import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Scanner {
	private Reader inputSystem;
	private String inputString;
	private StringBuilder curLine = new StringBuilder();
	private int curInt;
	private String currentWord;
	private int stoppedIndexString;
	private int stoppedIndexChar;
	private int stoppedIndex;
	private int capacity;
	private boolean foundInt;
	private boolean foundWord;
	private char[] reader = new char[256];

	private int[] intArray = new int[1];
	private String[] wordArray = new String[1];

	private StringBuilder curDigit = new StringBuilder();

	private boolean lenSyp = System.lineSeparator().length() == 2;

	public Scanner(InputStream input) {
		inputSystem = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
	}

	public Scanner(Reader reader) {
		inputSystem = reader;
	}

	public Scanner(String input) {
		inputString = input;
	}

	public void close() {
		try {
			inputSystem.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean hasNextLine() {
		readLine();
		return !curLine.isEmpty();
	}

	public boolean hasNextInt() {
		if (!prepareWordInt()) {
			return false;
		}
		searchIntOrWord(false, true);
		return foundInt;
	}

	public boolean hasNextIntArray() {
		searchIntArray();
		return foundInt;
	}

	public boolean hasNextWord(boolean withInt) {
		if (!prepareWordInt()) {
			return false;
		}
		searchIntOrWord(true, withInt);
		return foundWord;
	}

	public boolean hasNextWordArray() {
		searchWordArray();
		return foundWord;
	}

	public boolean hasNextOnlyWordArray() {
		searchOnlyWordArray();
		return foundWord;
	}

	private boolean prepareWordInt() {
		if (inputSystem != null) {
			stoppedIndexString = 0;
			readLine();
			return true;
		} else if (inputString != null) {
			if (curLine.isEmpty()) {
				curLine.append(inputString + " ");
			}
			return true;
		} else {
			System.out.println("Unknown input!");
			return false;
		}
	}

	public String nextLine() {
		if (curLine.isEmpty()) {
			readLine();
		}
		String nextString = curLine.toString();
		curLine.setLength(0);
		return nextString;
	}

	public int nextInt() {
		if (!foundInt) {
			searchIntOrWord(false, true);
		}
		int nextNumber = curInt;
		foundInt = false;
		return nextNumber;
	}

	public int[] nextIntArray() {
		if (!foundInt) {
			searchIntArray();
		}
		foundInt = false;
		return intArray;
	}


	public String nextWord(boolean withInt) {
		if (!foundWord) {
			searchIntOrWord(true, withInt);
		}
		String nextWord = currentWord;
		foundWord = false;
		return nextWord;
	}

	public String[] nextWordArray() {
		if (!foundWord) {
			searchWordArray();
		}
		foundWord = false;
		return wordArray;
	}

	public String[] nextOnlyWordArray() {
		if (!foundWord) {
			searchOnlyWordArray();
		}
		foundWord = false;
		return wordArray;
	}

	private void readLine() {
		try {
            boolean foundLine = false;
            boolean wasRead = false;
            int checkRead;

            if (stoppedIndex >= capacity) {
                capacity = inputSystem.read(reader);
                //System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
                stoppedIndex = 0;
                if (capacity == -1 || capacity == 0) {
                    return;
                }
            }

            while (stoppedIndex < reader.length) {
                if (checkNextSeparator('\n') || checkNextSeparator('\r')) {
					if (checkNextSeparator('\r')) {
						nextChar();
						if (stoppedIndex >= capacity) {
							capacity = inputSystem.read(reader);
							//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
							stoppedIndex = 0;
							if (capacity == -1 || capacity == 0) {
								return;
							}
						}
						if (checkNextSeparator('\n')) {
							nextChar();
						}
					} else {
						nextChar();
					}
					foundLine = true;
					wasRead = true;
					if (curLine.isEmpty()) {
						curLine.append(" ");
					}
					return;
				}
                if (reader[stoppedIndex] != 0) {
                    wasRead = true;
                    curLine.append(reader[stoppedIndex]);
                    nextChar();
                } else {
                    return;
                }
				if (stoppedIndex >= capacity) {
					capacity = inputSystem.read(reader);
					//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
					stoppedIndex = 0;
					if (capacity == -1 || capacity == 0) {
						return;
					}
				}
                //System.out.println(curLine.toString());
            }

            if (!wasRead) {
                //System.out.println(">>" + stoppedIndex + "<<");
                return;
            }

            if (!foundLine) {
                //System.out.print(curLine.toString() + "!!!!!!!!!!");
                readLine();
            }
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void searchIntArray() {
		try {
			boolean wasRead = false;
			int checkRead;
			int ind = 0;
			intArray = new int[1];

			if (stoppedIndex >= capacity) {
				capacity = inputSystem.read(reader);
				//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
				stoppedIndex = 0;
				if (capacity == -1) {
					return;
				}
			}

			while (stoppedIndex < reader.length) {
				if (checkNextSeparator('\n') || checkNextSeparator('\r')) {
					nextChar();
					if (lenSyp) {
						if (stoppedIndex >= capacity) {
							capacity = inputSystem.read(reader);
							//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
							stoppedIndex = 0;
							if (capacity == -1) {
								return;
							}
						}
						if (checkNextSeparator('\n')) {
							nextChar();
						}
					}
					if (!wasRead) {
						intArray = new int[0];
						foundInt = true;
						return;
					}

					if (ind >= intArray.length) {
						intArray = Arrays.copyOf(intArray, ind * 2);
					}
					try {
						intArray[ind++] = Integer.parseInt(curDigit.toString());
					} catch (NumberFormatException e) {
						foundInt = false;
					}
					intArray = Arrays.copyOf(intArray, ind);
					curDigit.setLength(0);
					foundInt = true;
					return;
				}
				if (reader[stoppedIndex] != 0) {
					wasRead = true;
					if (checkInt(reader[stoppedIndex])) {
						curDigit.append(reader[stoppedIndex]);
					} else if (!curDigit.isEmpty()) {
						if (ind >= intArray.length) {
							intArray = Arrays.copyOf(intArray, ind * 2);
						}
						try {
							intArray[ind++] = Integer.parseInt(curDigit.toString());
						} catch (NumberFormatException e) {
							foundInt = false;
						}
						curDigit.setLength(0);
					}
					nextChar();
				} else {
					return;
				}
				if (stoppedIndex >= capacity) {
					capacity = inputSystem.read(reader);
					//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
					stoppedIndex = 0;
					if (capacity == -1) {
						return;
					}
				}
				//System.out.println(curLine.toString());
			}

			if (!wasRead) {
				//System.out.println(">>" + stoppedIndex + "<<");
				return;
			}

			if (!foundInt) {
				//System.out.print(curLine.toString() + "!!!!!!!!!!");
				readLine();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void searchWordArray() {
		try {
			boolean wasRead = false;
			int checkRead;
			int ind = 0;
			wordArray = new String[1];

			if (stoppedIndex >= capacity) {
				capacity = inputSystem.read(reader);
				//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
				stoppedIndex = 0;
				if (capacity == -1) {
					return;
				}
			}

			while (stoppedIndex < reader.length) {
				if (checkNextSeparator('\n') || checkNextSeparator('\r')) {
					nextChar();
					if (lenSyp) {
						if (stoppedIndex >= capacity) {
							capacity = inputSystem.read(reader);
							//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
							stoppedIndex = 0;
							if (capacity == -1) {
								return;
							}
						}
						if (checkNextSeparator('\n')) {
							nextChar();
						}
					}
					if (!wasRead) {
						wordArray = new String[0];
						foundWord = true;
						return;
					}

					if (ind >= wordArray.length) {
						wordArray = Arrays.copyOf(wordArray, ind * 2);
					}
					try {
						wordArray[ind++] = curDigit.toString();
					} catch (NumberFormatException e) {
						foundInt = false;
					}
					wordArray = Arrays.copyOf(wordArray, ind);
					curDigit.setLength(0);
					foundWord = true;
					return;
				}
				if (reader[stoppedIndex] != 0) {
					wasRead = true;
					if (checkInt(reader[stoppedIndex])) {
						curDigit.append(reader[stoppedIndex]);
					} else if (!curDigit.isEmpty()) {
						if (ind >= wordArray.length) {
							wordArray = Arrays.copyOf(wordArray, ind * 2);
						}
						wordArray[ind++] = curDigit.toString();
						curDigit.setLength(0);
					}
					nextChar();
				} else {
					return;
				}
				if (stoppedIndex >= capacity) {
					capacity = inputSystem.read(reader);
					//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
					stoppedIndex = 0;
					if (capacity == -1) {
						return;
					}
				}
				//System.out.println(curLine.toString());
			}

			if (!wasRead) {
				//System.out.println(">>" + stoppedIndex + "<<");
				return;
			}

			if (!foundWord) {
				//System.out.print(curLine.toString() + "!!!!!!!!!!");
				readLine();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void searchOnlyWordArray() {
		try {
			boolean wasRead = false;
			int checkRead;
			int ind = 0;
			wordArray = new String[1];

			if (stoppedIndex >= capacity) {
				capacity = inputSystem.read(reader);
				//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
				stoppedIndex = 0;
				if (capacity == -1) {
					if (!curDigit.isEmpty()) {
						wordArray[ind++] = curDigit.toString();
						curDigit.setLength(0);
					}
					return;
				}
			}

			while (stoppedIndex < reader.length) {
				if (checkNextSeparator('\n') || checkNextSeparator('\r')) {
					nextChar();
					if (lenSyp) {
						if (stoppedIndex >= capacity) {
							capacity = inputSystem.read(reader);
							//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
							stoppedIndex = 0;
							if (capacity == -1) {
								return;
							}
						}
						if (checkNextSeparator('\n')) {
							nextChar();
						}
					}
					if (!wasRead) {
						wordArray = new String[0];
						foundWord = true;
						return;
					}
					if (!curDigit.isEmpty()) {
						if (ind >= wordArray.length) {
							wordArray = Arrays.copyOf(wordArray, ind * 2);
						}
						try {
							wordArray[ind++] = curDigit.toString();
						} catch (NumberFormatException e) {
							foundInt = false;
						}
					}
					wordArray = Arrays.copyOf(wordArray, ind);
					curDigit.setLength(0);
					foundWord = true;
					return;
				}
				if (reader[stoppedIndex] != 0) {
					wasRead = true;
					if (checkWord(reader[stoppedIndex])) {
						curDigit.append(reader[stoppedIndex]);
					} else if (!curDigit.isEmpty()) {
						if (ind >= wordArray.length) {
							wordArray = Arrays.copyOf(wordArray, ind * 2);
						}
						wordArray[ind++] = curDigit.toString();
						curDigit.setLength(0);
					}
					nextChar();
				} else {
					return;
				}
				if (stoppedIndex >= capacity) {
					capacity = inputSystem.read(reader);
					//System.out.println(">>" + reader[0] + "<<" + ">>" + reader[reader.length - 1] + "<<");
					stoppedIndex = 0;
					if (capacity == -1) {
						if (!curDigit.isEmpty()) {
							wordArray[ind++] = curDigit.toString();
							curDigit.setLength(0);
						}
						return;
					}
				}
				//System.out.println(curLine.toString());
			}

			if (!wasRead) {
				//System.out.println(">>" + stoppedIndex + "<<");
				return;
			}

			if (!foundWord) {
				//System.out.print(curLine.toString() + "!!!!!!!!!!");
				readLine();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void searchIntOrWord(boolean word, boolean number) {
		boolean wrote = false;
		for (int i = stoppedIndexString; i < curLine.length(); i++) {
			if ((word && checkWord(curLine.charAt(i))) || (number && checkInt(curLine.charAt(i)))) {
				if (!wrote) {
					stoppedIndexString = i;
					wrote = true;
				}
			} else if (wrote) {
				if (number && !word) {
					try {
						curInt = Integer.parseInt(curLine.substring(stoppedIndexString, i));
						stoppedIndexString = i;
						foundInt = true;
					} catch (NumberFormatException e) {
						foundInt = false;
					}
				} else {
					currentWord = curLine.toString().substring(stoppedIndexString, i);
					stoppedIndexString = i;
					foundWord = true;
				}
				return;
			}
		}
	}

	private boolean checkWord(char symbol) {
		return Character.isLetter(symbol)
				|| Character.DASH_PUNCTUATION == Character.getType(symbol)
				|| "'".contains(symbol + "");
	}

	private boolean checkInt(char symbol) {
		String avolveForInt = "1234567890+-";
		return avolveForInt.contains(symbol + "");
	}

	private void nextChar() {
		reader[stoppedIndex] = 0;
		stoppedIndex++;
	}

	private boolean checkNextSeparator(char symbol) {
		if (stoppedIndex < reader.length) {
			return reader[stoppedIndex] == symbol;
		}
		return true;
	}
}
