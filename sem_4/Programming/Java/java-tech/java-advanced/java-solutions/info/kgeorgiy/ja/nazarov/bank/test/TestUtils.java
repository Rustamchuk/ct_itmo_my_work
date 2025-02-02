package info.kgeorgiy.ja.nazarov.bank.test;

import info.kgeorgiy.ja.nazarov.bank.internal.Bank;
import info.kgeorgiy.ja.nazarov.bank.internal.Money;
import info.kgeorgiy.ja.nazarov.bank.internal.Person;
import org.junit.jupiter.api.Assertions;

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

    public static Money getAmount(Person person, String accountId) throws RemoteException {
        return person.getAccounts().get(accountId).getAmount();
    }

    public static Money createMoney() {
        return createMoney(false);
    }


    public static Money createMoney(boolean negate) {
        int amount = randomInt() * (negate ? -1 : 1);
        int penny = randomInt() * (negate ? -1 : 1);
        return new Money(amount, penny);
    }

    public static void assertEqualMoney(Money a, Money b) {
        Assertions.assertEquals(a.getAmount(), b.getAmount());
        Assertions.assertEquals(a.getPenny(), b.getPenny());
    }

    public static Person createRandomPerson(final Bank bank) throws RemoteException {
        return bank.createPerson(randomString(), randomString(), randomString());
    }
}
