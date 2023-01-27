import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class G {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());
        int lenBank = Integer.toBinaryString(~1).length() + 1;
        Long[] elements = new Long[n];
        boolean finished = false;
        String[] elementsBin = new String[n];
        String[] inp = sc.nextLine().split(" ");

        for (int i = 0; i < n; i++) {
            elements[i] = Long.parseLong(inp[i]);
        }

        long target = Long.parseLong(sc.nextLine());
        String targetBin;
        StringBuilder curWord = new StringBuilder();
        String el;

        for (int i = 0; i < elements.length; i++) {
            el = Long.toBinaryString(elements[i]);
            curWord.append("0".repeat(Math.max(0, lenBank - el.length())));
            curWord.append(el);
            elementsBin[i] = curWord.toString();
            curWord.setLength(0);
        }

        el = Long.toBinaryString(target);
        curWord.append("0".repeat(Math.max(0, lenBank - el.length())));
        curWord.append(el);
        targetBin = curWord.toString();
        curWord.setLength(0);
        //Строим таблицу истинности
        int[][] truthTableArray = new int[lenBank][n];
        for (int i = 0; i < lenBank; i++) {
            for (int j = 0; j < n; j++) {
                truthTableArray[i][j] = elementsBin[j].charAt(i) - '0';
            }
        }
        List<int[]> truthTableList = Arrays.asList(truthTableArray);

        List<int[]> unicals = new ArrayList<>();
        List<Character> result = new ArrayList<>();
        //Убираем повторы. Ставим их результат
        int pos = 0;
        for (int i = 0; i < lenBank; i++) {
            boolean contain = false;
            for (int j = 0; j < unicals.size(); j++) {
                if (Arrays.equals(unicals.get(j), truthTableList.get(i))) {
                    pos = j;
                    contain = true;
                    break;
                }
            }
            if (contain) {
                //Если потеряли совпадение
                if (targetBin.charAt(i) != result.get(pos)) {
                    System.out.println("Impossible");
                    finished = true;
                    break;
                }
            } else {
                unicals.add(truthTableList.get(i));
                result.add(targetBin.charAt(i));
            }
        }
        //СКНФ образование формулы по нулям
        if (!finished) {
            boolean wasHere = false;
            for (int i = 0; i < unicals.size(); i++) {
                if (result.get(i) == '1') {
                    continue;
                }
                if (wasHere) {
                    curWord.append("&");
                }
                curWord.append("(");

                for (int j = 0; j < n; j++) {
                    if (j > 0) {
                        curWord.append("|");
                    }
                    if (unicals.get(i)[j] == 1) {
                        curWord.append("~");
                    }
                    curWord.append(j + 1);
                }
                curWord.append(")");

                wasHere = true;
            }
            if (curWord.isEmpty()) {
                //Если тождественная единица
                System.out.println("1|~1");
            } else {
                System.out.println(curWord.toString());
            }
        }
    }
}
