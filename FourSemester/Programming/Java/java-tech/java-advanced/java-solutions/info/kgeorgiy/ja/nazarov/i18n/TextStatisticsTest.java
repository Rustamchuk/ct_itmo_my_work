package info.kgeorgiy.ja.nazarov.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TextStatisticsTest {
    @Test
    public void testAnalyzeText() throws IOException, ParseException {
        String text = "This is a test. 123. $45.67. 01/01/2020.";
        Locale locale = Locale.US;
        DataInfo dataInfo = new DataInfo(
                new CategoryStatistics(4, 4, "123.", "This is a test.", 4, 15, 9.25),
                new CategoryStatistics(4, 4, "a", "This", 1, 4, 2.75),
                new CategoryStatistics(1, 1, 123L, 123L, 3, 3, 3.0),
                new CategoryStatistics(1, 1, 45.67, 45.67, 6, 6, 6.0),
                new CategoryStatistics(1, 1, new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2020"), new SimpleDateFormat("MM/dd/yyyy").parse("01/01/2020"), 10, 10, 10.0)
        );
        checkTextStatistics(text, locale, dataInfo);
    }

    @Test
    public void testAnalyzeTextRussianLocale() throws IOException, ParseException {
        String text = "Это тест. 123. 45,67 ₽. 01.01.2020.";
        Locale locale = new Locale("ru", "RU");
        DataInfo dataInfo = new DataInfo(
                new CategoryStatistics(4, 4, "123.", "01.01.2020.", 4, 11, 8.0),
                new CategoryStatistics(2, 2, "Это", "тест", 3, 4, 3.5),
                new CategoryStatistics(1, 1, 123L, 123L, 3, 3, 3.0),
                new CategoryStatistics(1, 1, 45.67, 45.67, 7, 7, 7.0),
                new CategoryStatistics(1, 1, new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2020"), new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2020"), 10, 10, 10.0)
        );
        checkTextStatistics(text, locale, dataInfo);
    }

    @Test
    public void testAnalyzeTextNegativeRubles() throws IOException, ParseException {
        String text = "Это тест. -123. -45,67 ₽. 01.01.2020.";
        Locale locale = new Locale("ru", "RU");
        DataInfo dataInfo = new DataInfo(
                new CategoryStatistics(4, 4, "-123.", "01.01.2020.", 5, 11, 8.5),
                new CategoryStatistics(2, 2, "Это", "тест", 3, 4, 3.5),
                new CategoryStatistics(1, 1, -123L, -123L, 4, 4, 4.0),
                new CategoryStatistics(1, 1, -45.67, -45.67, 8, 8, 8.0),
                new CategoryStatistics(1, 1, new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2020"), new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2020"), 10, 10, 10.0)
        );
        checkTextStatistics(text, locale, dataInfo);
    }

    @Test
    public void testAnalyzeTextFrenchLocale() throws IOException, ParseException {
        String text = "Ceci est un test. 123. 55,55 € 45,67 €. 01/01/2020.";
        Locale locale = Locale.FRANCE;
        DataInfo dataInfo = new DataInfo(
                new CategoryStatistics(4, 4, "123.", "Ceci est un test.", 4, 17, 12.0),
                new CategoryStatistics(4, 4, "un", "Ceci", 2, 4, 3.25),
                new CategoryStatistics(1, 1, 123L, 123L, 3, 3, 3.0),
                new CategoryStatistics(2, 2, 45.67, 55.55, 7, 7, 7.0),
                new CategoryStatistics(1, 1, new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"), new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"), 10, 10, 10.0)
        );
        checkTextStatistics(text, locale, dataInfo);
    }

    private void checkTextStatistics(String text, Locale locale, DataInfo dataInfo) throws IOException, ParseException {
        Report report = TextStatistics.analyzeText(text, locale);
        Assertions.assertNotNull(report);

        ResourceBundle bundle = ResourceBundle.getBundle(Utilities.CATEGORIES.toString(), locale);

        checkCategoryStatistics(report, bundle, "sentences", dataInfo.getSentences());
        checkCategoryStatistics(report, bundle, "words", dataInfo.getWords());
        checkCategoryStatistics(report, bundle, "numbers", dataInfo.getNumbers());
        checkCategoryStatistics(report, bundle, "currencies", dataInfo.getCurrencies());
        checkCategoryStatistics(report, bundle, "dates", dataInfo.getDates());
    }

    private void checkCategoryStatistics(Report report, ResourceBundle bundle, String category, CategoryStatistics expected) {
        CategoryStatistics actual = report.getStatistic(bundle.getString(category));
        Assertions.assertEquals(expected.getCount(), actual.getCount());
        Assertions.assertEquals(expected.getUniqueCount(), actual.getUniqueCount());
        Assertions.assertEquals(expected.getMinValue(), actual.getMinValue());
        Assertions.assertEquals(expected.getMaxValue(), actual.getMaxValue());
        Assertions.assertEquals(expected.getMinLength(), actual.getMinLength());
        Assertions.assertEquals(expected.getMaxLength(), actual.getMaxLength());
        Assertions.assertEquals(expected.getTotalLen(), actual.getAverageLength());
    }
}
