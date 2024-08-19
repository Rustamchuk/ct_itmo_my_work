package info.kgeorgiy.ja.nazarov.walk;

public class Walk {
    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.out.println("Wrong number of arguments!");
            return;
        }
        ProcessFiles.readWalkWrite(args[0], args[1]);
    }
}
