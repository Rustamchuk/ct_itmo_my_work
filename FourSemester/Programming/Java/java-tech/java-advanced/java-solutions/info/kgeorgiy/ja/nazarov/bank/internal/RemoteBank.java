package info.kgeorgiy.ja.nazarov.bank.internal;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class RemoteBank implements Bank {
    private final int port;
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();

    public RemoteBank(final int port) {
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account createAccount(final String id) throws RemoteException {
        System.out.println("Creating account " + id);
        final Account account = new RemoteAccount(id, new Money(0, 0), port);

        if (accounts.putIfAbsent(id, account) == null) {
            String[] parts = id.split(":");
            String passportNumber = parts[0];
            Person person = persons.get(passportNumber);
            if (person != null) {
                person.getAccounts().put(id, account);
            }
            return account;
        } else {
            return getAccount(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Account getAccount(final String id) {
        System.out.println("Retrieving account " + id);
        return accounts.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person createPerson(
            String firstName,
            String lastName,
            String passportNumber
    ) throws RemoteException {
        Person person = new RemotePerson(firstName, lastName, passportNumber, new ConcurrentHashMap<>(), port);
        if (persons.putIfAbsent(passportNumber, person) == null) {
            return person;
        } else {
            return getRemotePerson(passportNumber);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person getRemotePerson(String passportNumber) {
        return persons.get(passportNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Person getLocalPerson(String passportNumber) throws RemoteException {
        Person person = persons.get(passportNumber);
        if (person == null) {
            return null;
        }
        return new LocalPerson(
                person.getFirstName(),
                person.getLastName(),
                person.getPassportNumber(),
                person.getAccounts().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Account account = entry.getValue();
                            try {
                                return new LocalAccount(account.getId(), new Money(
                                        account.getAmount().getAmount(),
                                        account.getAmount().getPenny())
                                );
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ))
        );
    }
}
