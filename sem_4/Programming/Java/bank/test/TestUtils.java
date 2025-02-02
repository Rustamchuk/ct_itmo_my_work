package info.kgeorgiy.ja.nazarov.bank.test;

import info.kgeorgiy.ja.nazarov.bank.internal.Bank;
import info.kgeorgiy.ja.nazarov.bank.internal.Person;

import java.rmi.RemoteException;
import java.util.Random;

public class TestUtils {
    protected static final Random RANDOM = new Random(1234567890123456789L);

    public static String formatAccountId(String passport, String subId) {
        return passport + ":" + subId;
    }

    public static String randomString() {
        return String.valueOf(RANDOM.nextInt());
    }

    public static int randomInt() {
        return RANDOM.nextInt();
    }

    public static int getAmount(Person person, String accountId) throws RemoteException {
        return person.getAccounts().get(accountId).getAmount();
    }

    public static Person createRandomPerson(final Bank bank) throws RemoteException {
        return bank.createPerson(randomString(), randomString(), randomString());
    }
}
