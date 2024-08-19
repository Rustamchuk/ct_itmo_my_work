package info.kgeorgiy.ja.nazarov.walk;

public class SumCounter {
    public static int jankinsHashFirstState(final byte[] txt, int hash, int len) {
        for (int i = 0; i < len; i++) {
            hash += txt[i] & 0xFF;
            hash += hash << 10;
            hash ^= hash >>> 6;
        }
        return hash;
    }

    public static int jankinsHashFinalState(int hash) {
        hash += hash << 3;
        hash ^= hash >>> 11;
        hash += hash << 15;
        return hash;
    }
}
