package info.kgeorgiy.ja.nazarov.i18n;

import java.text.*;
import java.util.*;
import java.util.regex.*;
import java.math.BigDecimal;

public class TextAnalyzer {
    public static List<CategoryContext> extractSentences(String text, Locale locale) {
        List<CategoryContext> sentences = new ArrayList<>();
        Matcher matcher = Pattern.compile(String.valueOf(Utilities.SENTENCE_PATTERN)).matcher(text);
        while (matcher.find()) {
            String sentence = matcher.group().trim();
            sentences.add(new CategoryContext(sentence, sentence));
        }
        return sentences;
    }

    public static List<CategoryContext> extractWords(String text, Locale locale) {
        BreakIterator iterator = BreakIterator.getWordInstance(locale);
        iterator.setText(text);
        List<CategoryContext> words = new ArrayList<>();
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String word = text.substring(start, end).trim();
            if (!word.isEmpty() && word.chars().allMatch(Character::isLetter)) {
                words.add(new CategoryContext(word, word));
            }
        }
        return words;
    }

    public static List<CategoryContext> extractNumbers(String text, Locale locale) throws ParseException {
        List<CategoryContext> numbers = new ArrayList<>();
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();
        char decimalSeparator = symbols.getDecimalSeparator();

        String datePattern = locale.equals(Utilities.LOCALE_RU) ?
                Utilities.DATE_PATTERN_RU.toString() : Utilities.DATE_PATTERN_EN.toString();
        text = text.replaceAll(datePattern, "");

        String currencyPattern = String.format(Utilities.CURRENCY_PATTERN.toString(), decimalSeparator, decimalSeparator);
        text = text.replaceAll(currencyPattern, "");

        Matcher matcher = Pattern.compile(String.valueOf(Utilities.NUMBER_PATTERN)).matcher(text);
        while (matcher.find()) {
            String numberStr = matcher.group();
            Number number = format.parse(numberStr);
            numbers.add(new CategoryContext(number, number));
        }
        return numbers;
    }

    public static List<CategoryContext> extractCurrencies(String text, Locale locale) throws ParseException {
        List<CategoryContext> currencies = new ArrayList<>();
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();
        char decimalSeparator = symbols.getDecimalSeparator();
        String currencySymbol = symbols.getCurrencySymbol();

        String currencyPattern = String.format(Utilities.CURRENCY_PATTERN.toString(), decimalSeparator, decimalSeparator);
        Matcher matcher = Pattern.compile(currencyPattern).matcher(text);

        while (matcher.find()) {
            Number a = format.parse(matcher.group().replace(" ", " "));
            double number = a.doubleValue();
            String formattedCurrency = format.format(number).replace(" ", " ");
            currencies.add(new CategoryContext(formattedCurrency, number));
        }
        return currencies;
    }

    public static List<CategoryContext> extractDates(String text, Locale locale) throws ParseException {
        List<CategoryContext> dates = new ArrayList<>();
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        String datePattern = locale.equals(Utilities.LOCALE_RU) ? "\\b\\d{1,2}\\.\\d{1,2}\\.\\d{2,4}\\b" : "\\b\\d{1,2}/\\d{1,2}/\\d{2,4}\\b";
        Matcher matcher = Pattern.compile(datePattern).matcher(text);

        while (matcher.find()) {
            Date date = format.parse(matcher.group());
            dates.add(new CategoryContext(date, date));
        }
        return dates;
    }

    public static Map<String, List<CategoryContext>> analyzeText(String text, Locale locale) throws ParseException {
        Map<String, List<CategoryContext>> report = new HashMap<>();
        ResourceBundle bundle = ResourceBundle.getBundle(String.valueOf(Utilities.CATEGORIES), locale);

        report.put(bundle.getString(Utilities.SENTENCES.toString()), extractSentences(text, locale));
        report.put(bundle.getString(Utilities.WORDS.toString()), extractWords(text, locale));
        report.put(bundle.getString(Utilities.CURRENCIES.toString()), extractCurrencies(text, locale));
        report.put(bundle.getString(Utilities.DATES.toString()), extractDates(text, locale));
        report.put(bundle.getString(Utilities.NUMBERS.toString()), extractNumbers(text, locale));

        return report;
    }
}
