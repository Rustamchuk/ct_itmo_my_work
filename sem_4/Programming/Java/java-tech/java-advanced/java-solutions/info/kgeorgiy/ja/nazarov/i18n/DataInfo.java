package info.kgeorgiy.ja.nazarov.i18n;

public class DataInfo {
    private final CategoryStatistics sentences;
    private final CategoryStatistics words;
    private final CategoryStatistics numbers;
    private final CategoryStatistics currencies;
    private final CategoryStatistics dates;

    public DataInfo(
            CategoryStatistics sentences,
            CategoryStatistics words,
            CategoryStatistics numbers,
            CategoryStatistics currencies,
            CategoryStatistics dates
    ) {
        this.sentences = sentences;
        this.words = words;
        this.numbers = numbers;
        this.currencies = currencies;
        this.dates = dates;
    }

    public CategoryStatistics getSentences() {
        return sentences;
    }

    public CategoryStatistics getWords() {
        return words;
    }

    public CategoryStatistics getNumbers() {
        return numbers;
    }

    public CategoryStatistics getCurrencies() {
        return currencies;
    }

    public CategoryStatistics getDates() {
        return dates;
    }
}
