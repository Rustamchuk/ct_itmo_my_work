package info.kgeorgiy.ja.nazarov.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Utilities {
    DATE_PATTERN_RU("\\b\\d{1,2}\\.\\d{1,2}\\.\\d{2,4}\\b"),
    DATE_PATTERN_EN("\\b\\d{1,2}/\\d{1,2}/\\d{2,4}\\b"),
    CURRENCY_PATTERN("-?\\p{Sc}\\s*\\d+(\\%c\\d{1,2})?|-?\\d+(\\%c\\d{1,2})?\\s*\\p{Sc}"),
    SENTENCE_PATTERN("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)"),
    NUMBER_PATTERN("-?\\b\\d+\\b"),
    SENTENCES("sentences"),
    WORDS("words"),
    NUMBERS("numbers"),
    CURRENCIES("currencies"),
    DATES("dates"),
    PROPERTIES("info.kgeorgiy.ja.nazarov.i18n."),
    CATEGORIES(PROPERTIES + "categories"),
    MESSAGES(PROPERTIES + "messages");

    private final String pattern;
    public static final Locale LOCALE_RU = new Locale("ru", "RU");

    Utilities(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return pattern;
    }
}
