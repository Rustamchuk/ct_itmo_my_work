import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class D {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cnt = Integer.parseInt(sc.nextLine());
        List<String> input = new ArrayList<String>();
        List<String> elements = new ArrayList<String>();
        List<String> finished = new ArrayList<String>();
        int number = cnt;
        int last = 0;

        int ones = 0;
        for (int i = 0; i < Math.pow(2, cnt); i++) {
            if (sc.hasNextLine()) {
                input.add(sc.nextLine());

                if (input.get(i).charAt(input.get(i).length() - 1) == '1') {
                    ones--;
                } else {
                    ones++;
                }
            }
        }
        if (ones == Math.pow(2, cnt)) {
            ones = -1;
        } else {
            ones = 1;
        }

        for (int i = 0; i < Math.pow(2, cnt); i++) {
            String cur = input.get(i);
            elements.clear();
            for (int j = 0; j < cnt; j++) {
                elements.add(String.valueOf(cur.charAt(j)));
            }
            int ans = cur.charAt(cur.length() - 1) - '0';
            if ((ans == 1 && ones >= 0) || (ans == 0 && ones < 0)) {
                for (int j = 0; j < cnt; j++) {
                    int el = cur.charAt(j) - '0';
                    if ((el == 0 && ones >= 0) || (el == 1 && ones < 0)) {
                        number++;
                        finished.add("1 " + (j + 1));
                        elements.set(j, number + "");
                    } else {
                        elements.set(j, (j + 1) + "");
                    }

                    if (j > 0) {
                        number++;
                        if (ones >= 0) {
                            finished.add("2 " + elements.get(j - 1) + " " + elements.get(j));
                        } else {
                            finished.add("3 " + elements.get(j - 1) + " " + elements.get(j));
                        }
                        elements.set(j, number + "");
                    }
                }
                if (i > 0 && last != 0) {
                    if (ones >= 0) {
                        finished.add("3 " + last + " " + number);
                    } else {
                        finished.add("2 " + last + " " + number);
                    }
                    number++;
                }
                last = number;
            }
        }
        System.out.println(number);
        for (String a : finished) {
            System.out.println(a);
        }
    }
}
