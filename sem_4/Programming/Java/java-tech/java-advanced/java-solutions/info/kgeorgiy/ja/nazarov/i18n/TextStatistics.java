package info.kgeorgiy.ja.nazarov.i18n;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.*;
import java.util.*;
import java.util.regex.*;

public class TextStatistics {
    public static void main(String[] args) {
        if (args == null || args.length != 4 || args[0] == null || args[1] == null || args[2] == null || args[3] == null) {
            System.err.println("Usage: TextStatistics <text locale> <output locale> <input file> <output file>");
            return;
        }

        Locale textLocale = Locale.forLanguageTag(args[0]);
        Locale outputLocale = Locale.forLanguageTag(args[1]);
        File inputFile = new File(args[2]);
        File outputFile = new File(args[3]);

        process(textLocale, outputLocale, inputFile, outputFile);
    }

    public static void process(Locale textLocale, Locale outputLocale, File inputFile, File outputFile) {
        try {
            String text = readFile(inputFile);
            Report report = analyzeText(text, textLocale);
            writeReport(report, textLocale, outputLocale, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(Character.LINE_SEPARATOR);
            }
            return sb.toString();
        }
    }

    public static Report analyzeText(String text, Locale locale) throws ParseException {
        Report report = new Report(locale);
        Map<String, List<CategoryContext>> analysis = TextAnalyzer.analyzeText(text, locale);
        for (Map.Entry<String, List<CategoryContext>> entry : analysis.entrySet()) {
            report.addStatistic(entry.getKey(), entry.getValue());
        }
        return report;
    }

    private static void writeReport(Report report, Locale inputLocale, Locale outputLocale, File file) throws IOException {
        ResourceBundle bundleMessage = ResourceBundle.getBundle(String.valueOf(Utilities.MESSAGES), outputLocale);
        ResourceBundle bundleCategory = ResourceBundle.getBundle(String.valueOf(Utilities.CATEGORIES), inputLocale);
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.println(MessageFormat.format(bundleMessage.getString("analyzed_file"), file.getName()));
            writer.println(bundleMessage.getString("summary_statistics"));

            writer.println(MessageFormat.format(bundleMessage.getString("number_of_sentences"), report.getStatistic(bundleCategory.getString("sentences")).getCount()));
            writer.println(MessageFormat.format(bundleMessage.getString("number_of_words"), report.getStatistic(bundleCategory.getString("words")).getCount()));
            writer.println(MessageFormat.format(bundleMessage.getString("number_of_numbers"), report.getStatistic(bundleCategory.getString("numbers")).getCount()));
            writer.println(MessageFormat.format(bundleMessage.getString("number_of_currencies"), report.getStatistic(bundleCategory.getString("currencies")).getCount()));
            writer.println(MessageFormat.format(bundleMessage.getString("number_of_dates"), report.getStatistic(bundleCategory.getString("dates")).getCount()));

            writeCategoryStatistics(writer, bundleMessage, bundleCategory, "sentences", report.getStatistic(bundleCategory.getString("sentences")));
            writeCategoryStatistics(writer, bundleMessage, bundleCategory, "words", report.getStatistic(bundleCategory.getString("words")));
            writeCategoryStatistics(writer, bundleMessage, bundleCategory, "numbers", report.getStatistic(bundleCategory.getString("numbers")));
            writeCategoryStatistics(writer, bundleMessage, bundleCategory, "currencies", report.getStatistic(bundleCategory.getString("currencies")));
            writeCategoryStatistics(writer, bundleMessage, bundleCategory, "dates", report.getStatistic(bundleCategory.getString("dates")));
        }
    }

    private static void writeCategoryStatistics(PrintWriter writer, ResourceBundle bundle, ResourceBundle bundle2, String categoryKey, CategoryStatistics stats) {
        writer.println(bundle.getString("statistics_" + categoryKey));
        writer.println(MessageFormat.format(bundle.getString("number_of_" + categoryKey), stats.getCount(), stats.getUniqueCount()));
        writer.println(MessageFormat.format(bundle.getString("min_" + categoryKey), stats.getMinValue()));
        writer.println(MessageFormat.format(bundle.getString("max_" + categoryKey), stats.getMaxValue()));
        writer.println(MessageFormat.format(bundle.getString("min_length_" + categoryKey), stats.getMinLength(), stats.getMinValue()));
        writer.println(MessageFormat.format(bundle.getString("max_length_" + categoryKey), stats.getMaxLength(), stats.getMaxValue()));
        writer.println(MessageFormat.format(bundle.getString("average_length_" + categoryKey), stats.getAverageLength()));
    }
}
