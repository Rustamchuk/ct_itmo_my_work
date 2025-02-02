package info.kgeorgiy.ja.nazarov.bank;

import info.kgeorgiy.ja.nazarov.bank.internal.Bank;
import info.kgeorgiy.ja.nazarov.bank.internal.Person;
import info.kgeorgiy.ja.nazarov.bank.internal.Account;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class Client {
    /**
     * Utility class.
     */
    private static final String DEFAULT_NAME = "Rustam";
    private static final String DEFAULT_SURNAME = "Nazarov";
    private static final String DEFAULT_PASSPORT = "123456789";
    private static final String DEFAULT_ID = "default";
    private static final int DEFAULT_AMOUNT = 0;

    private Client() {
    }

    public static void main(final String... args) throws RemoteException {
        final Bank bank;
        try {
            bank = (Bank) Naming.lookup("//localhost/bank");
        } catch (final NotBoundException e) {
            System.out.println("Bank is not bound");
            return;
        } catch (final MalformedURLException e) {
            System.out.println("Bank URL is invalid");
            return;
        }

        final String firstName = getArgumentOrDefault(args, 0, DEFAULT_NAME);
        final String lastName = getArgumentOrDefault(args, 1, DEFAULT_SURNAME);
        final String passportNumber = getArgumentOrDefault(args, 2, DEFAULT_PASSPORT);
        final String subId = getArgumentOrDefault(args, 3, DEFAULT_ID);
        final int amountChange = getArgumentOrDefault(args, 4, DEFAULT_AMOUNT);

        Person person = bank.getRemotePerson(passportNumber);
        if (person == null) {
            System.out.println("Creating person");
            person = bank.createPerson(firstName, lastName, passportNumber);
        } else {
            System.out.println("Person already exists");
        }

        System.out.println("Person: " + person.getFirstName() + " " + person.getLastName() +
                ", Passport: " + person.getPassportNumber());

        Account account = bank.getAccount(subId);
        if (account == null) {
            System.out.println("Creating account");
            account = bank.createAccount(subId);
        } else {
            System.out.println("Account already exists");
        }

        account.setAmount(amountChange);

        System.out.println("Account id: " + account.getId());
        System.out.println("Money: " + account.getAmount());
        System.out.println("Adding money");
        System.out.println("Money: " + account.getAmount());
    }

    private static String getArgumentOrDefault(String[] args, int index, String defaultValue) {
        return args.length > index ? args[index] : defaultValue;
    }

    private static int getArgumentOrDefault(String[] args, int index, int defaultValue) {
        return args.length > index ? Integer.parseInt(args[index]) : defaultValue;
    }
}
