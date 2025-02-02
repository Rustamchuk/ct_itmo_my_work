package info.kgeorgiy.ja.nazarov.i18n;

import java.text.SimpleDateFormat;
import java.util.*;

public class Report {
    private final Map<String, CategoryStatistics> statistics = new HashMap<>();

    public Report(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(String.valueOf(Utilities.CATEGORIES), locale);
        for (String key : bundle.keySet()) {
            statistics.put(bundle.getString(key), new CategoryStatistics());
        }
    }

    public void addStatistic(String category, List<CategoryContext> value) {
        if (statistics.containsKey(category)) {
            for (CategoryContext obj : value) {
                statistics.get(category).addValue(obj);
            }
        }
    }

    public CategoryStatistics getStatistic(String category) {
        return statistics.get(category);
    }

    public Set<String> getCategories() {
        return statistics.keySet();
    }
}

