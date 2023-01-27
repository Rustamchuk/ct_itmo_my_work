import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NextSum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next();
        List<Integer> list = new ArrayList<>();
        int prev = 0;
        boolean ans = false;
        int res = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == '+' || a.charAt(i) == '=') {
                if (!ans) {
                    res = Integer.parseInt(a.substring(0, i));
                    ans = true;
                } else {
                    list.add(Integer.parseInt(a.substring(prev, i)));
                }
                prev = i + 1;
            }
        }
        list.add(Integer.parseInt(a.substring(prev, a.length())));

        if (list.size() == 1) {
            System.out.println("No solution");
        } else {
            list.set(list.size() - 1, list.get(list.size() - 1) - 1);
            list.set(list.size() - 2, list.get(list.size() - 2) + 1);
            if (list.get(list.size() - 2) > list.get(list.size() - 1)) {
                list.set(list.size() - 2, list.get(list.size() - 2) + list.get(list.size() - 1));
                list.remove(list.size() - 1);
            } else {
                while (list.get(list.size() - 2) * 2 <= list.get(list.size() - 1)) {
                    list.add(list.get(list.size() - 1) - list.get(list.size() - 2));
                    list.set(list.size() - 2, list.get(list.size() - 3));
                }
            }
            StringBuilder b = new StringBuilder();
            b.append(res).append("=");
            b.append(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                b.append("+").append(list.get(i));
            }
            System.out.println(b.toString());
        }
    }
}
