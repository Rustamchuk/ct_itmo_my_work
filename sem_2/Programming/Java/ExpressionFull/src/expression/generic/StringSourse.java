package expression.generic;

public class StringSourse implements CharSourse {
    private final String data;
    private int pos;

    public StringSourse(String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    public void back() {
        pos--;
    }

    public boolean isUnicWord(String word) {
        boolean unic = pos > 1 && (Character.isWhitespace(data.charAt(pos - 2)) || data.charAt(pos - 2) == ')');
        for (int i = 1; i < word.length(); i++) {
            if (!hasNext() || next() != word.charAt(i)) {
                return false;
            }
        }
        if (!unic) unic = pos < data.length() && (Character.isWhitespace(data.charAt(pos)) || data.charAt(pos) == '(');
        return unic;
    }

    @Override
    public String errorMessage() {
        return data;
    }
}
