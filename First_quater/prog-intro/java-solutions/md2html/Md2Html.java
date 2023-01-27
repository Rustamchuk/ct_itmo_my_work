package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Md2Html {
    public static void main(String[] args) {
        String inputName = args[0];
        String outputName = args[1];
//        String inputName = "src/md2html/input.txt";
//        String outputName = "src/md2html/output.txt";

        String line;
        StringBuilder curLine = new StringBuilder();

        boolean[] saw = new boolean[] {false, false, false, false, false, false, false};
        int[] poses = new int[7];

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputName), StandardCharsets.UTF_8));
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName), StandardCharsets.UTF_8));
            line = fileReader.readLine();
            while (line != null) {
                curLine.setLength(0);
                while (line.isEmpty()) {
                    line = fileReader.readLine();
                }
                while (!line.isEmpty()) {
                    if (curLine.length() != 0) {
                        curLine.append(System.lineSeparator());
                    }
                    curLine.append(line);
                    line = fileReader.readLine();
                    if (line == null)
                        break;
                }
                int j = 0;
                int cnt = 0;
                while (!Character.isWhitespace(curLine.charAt(j))) {
                    if (curLine.charAt(j) == '#') { cnt++; }
                    else { cnt = 0; break;}
                    j++;
                }
                String startEnd;

                if (cnt != 0) {
                    startEnd = "h" + cnt;
                    curLine.delete(0, cnt + 1);
                } else {
                    startEnd = "p";
                }
                curLine.insert(0,"<" + startEnd + ">");
                curLine.append("</").append(startEnd).append(">");
                saw = new boolean[] {false, false, false, false, false, false, false};
                for (int i = startEnd.length() + 2; i < curLine.length() - startEnd.length() - 3; i++) {
                    if (Character.isLetter(curLine.charAt(i)) || Character.isDigit(curLine.charAt(i))
                            || Character.isWhitespace(curLine.charAt(i)))
                        continue;
                    switch (curLine.charAt(i)) {
                        case '>' -> {
                            curLine.delete(i, i + 1);
                            curLine.insert(i, "&gt;");
                        }
                        case '<' -> {
                            curLine.delete(i, i + 1);
                            curLine.insert(i, "&lt;");
                        }
                        case '&' -> {
                            curLine.delete(i, i + 1);
                            curLine.insert(i, "&amp;");
                        }
                        case '\\' -> {
                            curLine.delete(i, i + 1);
                        }
                        case '*' -> {
                            if (i + 1 < curLine.length()) {
                                if (curLine.charAt(i + 1) == '*') {
                                    saw[0] = !saw[0];
                                    if (saw[0]) {
                                        poses[0] = i;
                                        i += 1;
                                    } else {
                                        curLine.delete(i, i + 2);
                                        curLine.insert(i, "</strong>");
                                        curLine.delete(poses[0], poses[0] + 2);
                                        curLine.insert(poses[0], "<strong>");
                                        i += 14;
                                    }
                                    continue;
                                }
                            }
                            saw[2] = !saw[2];
                            if (saw[2]) {
                                poses[2] = i;
                            } else {
                                curLine.delete(i, i + 1);
                                curLine.insert(i, "</em>");
                                curLine.delete(poses[2], poses[2] + 1);
                                curLine.insert(poses[2], "<em>");
                                i += 7;
                            }
                        }
                        case '_' -> {
                            if (i + 1 < curLine.length()) {
                                if (curLine.charAt(i + 1) == '_') {
                                    saw[1] = !saw[1];
                                    if (saw[1]) {
                                        poses[1] = i;
                                        i += 1;
                                    } else {
                                        curLine.delete(i, i + 2);
                                        curLine.insert(i, "</strong>");
                                        curLine.delete(poses[1], poses[1] + 2);
                                        curLine.insert(poses[1], "<strong>");
                                        i += 14;
                                    }
                                    continue;
                                }
                            }
                            saw[3] = !saw[3];
                            if (saw[3]) {
                                poses[3] = i;
                            } else {
                                curLine.delete(i, i + 1);
                                curLine.insert(i, "</em>");
                                curLine.delete(poses[3], poses[3] + 1);
                                curLine.insert(poses[3], "<em>");
                                i += 7;
                            }
                        }
                        case '-' -> {
                            if (i + 1 < curLine.length()) {
                                if (curLine.charAt(i + 1) == '-') {
                                    saw[4] = !saw[4];
                                    if (saw[4]) {
                                        poses[4] = i;
                                        i += 1;
                                    } else {
                                        curLine.delete(i, i + 2);
                                        curLine.insert(i, "</s>");
                                        curLine.delete(poses[4], poses[4] + 2);
                                        curLine.insert(poses[4], "<s>");
                                        i += 4;
                                    }
                                }
                            }
                        }
                        case '`' -> {
                            saw[5] = !saw[5];
                            if (saw[5]) {
                                poses[5] = i;
                            } else {
                                curLine.delete(i, i + 1);
                                curLine.insert(i, "</code>");
                                curLine.delete(poses[5], poses[5] + 1);
                                curLine.insert(poses[5], "<code>");
                                i += 11;
                            }
                        }
                        case '~' -> {
                            saw[6] = !saw[6];
                            if (saw[6]) {
                                poses[6] = i;
                            } else {
                                curLine.delete(i, i + 1);
                                curLine.insert(i, "</mark>");
                                curLine.delete(poses[6], poses[6] + 1);
                                curLine.insert(poses[6], "<mark>");
                                i += 11;
                            }
                        }
                    }
                }
                writer.write(curLine.toString());
                writer.newLine();
            }
            writer.close();
            fileReader.close();
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
