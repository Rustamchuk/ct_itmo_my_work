package info.kgeorgiy.ja.nazarov.i18n;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CategoryStatistics {
    private int count = 0;
    private final Set<Object> uniqueValues = new HashSet<>();
    private int uniqueSize;
    private Object minValue = null;
    private Object maxValue = null;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;
    private double totalLength = 0;

    public CategoryStatistics(int count, int uniqCnt, Object minValue, Object maxValue, int minLength, int maxLength, double totalLength) {
        this.count = count;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.totalLength = totalLength;
        this.uniqueSize = uniqCnt;
    }

    public CategoryStatistics() {
    }

    public void addValue(CategoryContext valueContext) {
        count++;
        Object value = valueContext.value;
        Object compareValue = valueContext.compareValue;
        uniqueValues.add(value);
        if (minValue == null || compare(compareValue, minValue) < 0) {
            minValue = compareValue;
        }
        if (maxValue == null || compare(compareValue, maxValue) > 0) {
            maxValue = compareValue;
        }
        int length = value.toString().length();
        if (value instanceof Date) {
            length = new SimpleDateFormat("MM/dd/yyyy").format((Date) value).length();
        }
        if (length < minLength) {
            minLength = length;
        }
        if (length > maxLength) {
            maxLength = length;
        }
        totalLength += length;
    }


    @SuppressWarnings("unchecked")
    private int compare(Object o1, Object o2) {
        if (o1 instanceof String && o2 instanceof String) {
            return Integer.compare(o1.toString().length(), o2.toString().length());
        }
        if (o1 instanceof Number && o2 instanceof Number) {
            double d1 = ((Number) o1).doubleValue();
            double d2 = ((Number) o2).doubleValue();
            return Double.compare(d1, d2);
        }
        if (o1 instanceof Comparable && o2 instanceof Comparable) {
            return ((Comparable) o1).compareTo(o2);
        }
        return o1.toString().compareTo(o2.toString());
    }

    public int getCount() {
        return count;
    }

    public int getUniqueCount() {
        return Math.max(uniqueValues.size(), uniqueSize);
    }

    public Object getMinValue() {
        return minValue;
    }

    public Object getMaxValue() {
        return maxValue;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public double getAverageLength() {
        return count == 0 ? 0 : totalLength / count;
    }

    public double getTotalLen() {
        return totalLength;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUniqueSize(int uniqueSize) {
        this.uniqueSize = uniqueSize;
    }

    public void setMinValue(Object minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(Object maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setTotalLength(double totalLength) {
        this.totalLength = totalLength;
    }
}
