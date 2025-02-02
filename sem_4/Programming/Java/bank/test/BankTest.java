package info.kgeorgiy.ja.nazarov.bank.test;

import info.kgeorgiy.ja.nazarov.bank.internal.Account;
import info.kgeorgiy.ja.nazarov.bank.internal.Bank;
import info.kgeorgiy.ja.nazarov.bank.internal.Person;
import info.kgeorgiy.ja.nazarov.bank.internal.RemoteBank;
import org.junit.jupiter.api.*;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class BankTest {
    private static final int BANK_PORT = 8088;
    private static final int REGISTRY_PORT = 1099;
    private static final int THREAD_COUNT = 10;
    private static Registry registry;
    private static Bank bank;

    @BeforeAll
    public static void initialize() throws RemoteException {
        registry = LocateRegistry.createRegistry(REGISTRY_PORT);

        bank = new RemoteBank(BANK_PORT);
        UnicastRemoteObject.exportObject(bank, BANK_PORT);
        registry.rebind("bank", bank);
    }

    @AfterAll
    public static void terminate() {
        try {
            UnicastRemoteObject.unexportObject(registry, true);
            registry.unbind("bank");
            UnicastRemoteObject.unexportObject(bank, true);
        } catch (NotBoundException | RemoteException ignored) {
        }
    }

    @Test
    public void postAccount() throws RemoteException {
        String id = TestUtils.randomString();
        int amount = TestUtils.randomInt();
        Account account = bank.createAccount(id);
        account.setAmount(amount);

        Assertions.assertNotNull(account);
        Assertions.assertEquals(id, account.getId());
        Assertions.assertEquals(amount, account.getAmount());
    }

    @Test
    public void postGetAccount() throws RemoteException {
        String id = TestUtils.randomString();
        Account created = bank.createAccount(id);
        Account got = bank.getAccount(id);

        Assertions.assertEquals(got, created);

        created.setAmount(TestUtils.randomInt());
        Assertions.assertEquals(got, created);

        got.setAmount(TestUtils.randomInt());
        Assertions.assertEquals(got, created);
    }

    @Test
    public void noAccount() throws RemoteException {
        Assertions.assertNull(bank.getAccount(TestUtils.randomString()));
    }

    @Test
    public void postPersonAccount() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        String accountId = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        Account account = bank.createAccount(accountId);
        Account gotAccountBank = bank.getAccount(accountId);
        Account gotAccountPerson = person.getAccounts().get(accountId);

        Assertions.assertNotNull(account);
        Assertions.assertNotNull(gotAccountBank);
        Assertions.assertNotNull(gotAccountPerson);
        Assertions.assertEquals(account, gotAccountBank);
        Assertions.assertEquals(account, gotAccountPerson);
    }

    @Test
    public void postAccountsPerson() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        String accountId1 = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        String accountId2 = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());

        Account account1 = bank.createAccount(accountId1);
        Account account2 = bank.createAccount(accountId2);

        Assertions.assertNotNull(account1);
        Assertions.assertNotNull(account2);
        Assertions.assertNotEquals(account1, account2);
        Assertions.assertEquals(account1, person.getAccounts().get(accountId1));
        Assertions.assertEquals(account2, person.getAccounts().get(accountId2));
    }

    @Test
    public void postGetMultiplePersons() throws RemoteException {
        Person person1 = TestUtils.createRandomPerson(bank);
        Person person2 = TestUtils.createRandomPerson(bank);

        Assertions.assertNotNull(person1);
        Assertions.assertNotNull(person2);
        Assertions.assertEquals(person1, bank.getRemotePerson(person1.getPassportNumber()));
        Assertions.assertEquals(person2, bank.getRemotePerson(person2.getPassportNumber()));
    }

    @Test
    public void postGetLocalPerson() throws RemoteException {
        Person created = TestUtils.createRandomPerson(bank);
        Person local = bank.getLocalPerson(created.getPassportNumber());

        Assertions.assertNotNull(created);
        Assertions.assertNotNull(local);
        Assertions.assertEquals(created.getFirstName(), local.getFirstName());
        Assertions.assertEquals(created.getLastName(), local.getLastName());
        Assertions.assertEquals(created.getPassportNumber(), local.getPassportNumber());
    }

    @Test
    public void postGetRemotePerson() throws RemoteException {
        Person created = TestUtils.createRandomPerson(bank);
        Person got = bank.getRemotePerson(created.getPassportNumber());

        Assertions.assertNotNull(created);
        Assertions.assertNotNull(got);
        Assertions.assertEquals(created, got);
    }

    @Test
    public void noRemotePerson() throws RemoteException {
        Assertions.assertNull(bank.getRemotePerson(TestUtils.randomString()));
    }

    @Test
    public void noLocalPerson() throws RemoteException {
        Assertions.assertNull(bank.getLocalPerson(TestUtils.randomString()));
    }

    @Test
    public void setAmount() throws RemoteException {
        String id = TestUtils.randomString();
        int amount = TestUtils.randomInt();
        Account account = bank.createAccount(id);
        account.setAmount(amount);

        Assertions.assertEquals(amount, account.getAmount());
    }

    @Test
    public void checkBalanceAfterMultipleOperations() throws RemoteException {
        String id = TestUtils.randomString();
        Account account = bank.createAccount(id);
        int initialAmount = TestUtils.randomInt();
        account.setAmount(initialAmount);

        int deposit = TestUtils.randomInt();
        account.setAmount(account.getAmount() + deposit);

        int withdrawal = TestUtils.randomInt();
        account.setAmount(account.getAmount() - withdrawal);

        Assertions.assertEquals(initialAmount + deposit - withdrawal, account.getAmount());
    }

    @Test
    public void setAmountLocal() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        int amount1 = TestUtils.randomInt();
        int amount2 = TestUtils.randomInt();
        String accountId = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        Account account = bank.createAccount(accountId);

        account.setAmount(amount1);
        Person local = bank.getLocalPerson(person.getPassportNumber());

        Assertions.assertEquals(amount1, TestUtils.getAmount(local, accountId));

        local.getAccounts().get(accountId).setAmount(amount2);

        Person remote2 = bank.getRemotePerson(person.getPassportNumber());
        Person local2 = bank.getLocalPerson(person.getPassportNumber());

        Assertions.assertEquals(amount1, TestUtils.getAmount(person, accountId));
        Assertions.assertEquals(amount1, TestUtils.getAmount(remote2, accountId));
        Assertions.assertEquals(amount2, TestUtils.getAmount(local, accountId));
        Assertions.assertEquals(amount1, TestUtils.getAmount(local2, accountId));
    }

    @Test
    public void checkLocalAndRemoteAccess() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        String accountId = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        Account account = bank.createAccount(accountId);
        int amount = TestUtils.randomInt();
        account.setAmount(amount);

        Person remotePerson = bank.getRemotePerson(person.getPassportNumber());
        Person localPerson = bank.getLocalPerson(person.getPassportNumber());

        Assertions.assertEquals(amount, TestUtils.getAmount(remotePerson, accountId));
        Assertions.assertEquals(amount, TestUtils.getAmount(localPerson, accountId));
    }

    @Test
    public void setAmountRemote() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        int amount1 = TestUtils.randomInt();
        int amount2 = TestUtils.randomInt();
        String accountId = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        Account account = bank.createAccount(accountId);

        account.setAmount(amount1);
        Person remote = bank.getRemotePerson(person.getPassportNumber());
        Person local = bank.getLocalPerson(person.getPassportNumber());

        Assertions.assertEquals(account.getAmount(), TestUtils.getAmount(remote, accountId));

        account.setAmount(amount2);

        Person remote2 = bank.getRemotePerson(person.getPassportNumber());
        Person local2 = bank.getLocalPerson(person.getPassportNumber());

        Assertions.assertEquals(amount2, TestUtils.getAmount(remote, accountId));
        Assertions.assertEquals(amount2, TestUtils.getAmount(remote2, accountId));
        Assertions.assertEquals(amount1, TestUtils.getAmount(local, accountId));
        Assertions.assertEquals(amount2, TestUtils.getAmount(local2, accountId));
    }

    @Test
    public void deliverAmount() throws RemoteException {
        String id1 = TestUtils.randomString();
        String id2 = TestUtils.randomString();
        Account account1 = bank.createAccount(id1);
        Account account2 = bank.createAccount(id2);
        int initialAmount = TestUtils.randomInt();
        account1.setAmount(initialAmount);

        int transferAmount = TestUtils.randomInt();
        account1.setAmount(account1.getAmount() - transferAmount);
        account2.setAmount(account2.getAmount() + transferAmount);

        Assertions.assertEquals(initialAmount - transferAmount, account1.getAmount());
        Assertions.assertEquals(transferAmount, account2.getAmount());
    }

    @Test
    public void concurrentPostAccount() throws InterruptedException {
        int threadCount = THREAD_COUNT
                ;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    String id = TestUtils.randomString();
                    Account account = bank.createAccount(id);
                    Assertions.assertNotNull(account);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    public void concurrentChangeAmount() throws RemoteException, InterruptedException {
        String id = TestUtils.randomString();
        Account account = bank.createAccount(id);
        int initialAmount = TestUtils.randomInt();
        account.setAmount(initialAmount);

        int threadCount = THREAD_COUNT;
        int increment = TestUtils.randomInt();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    synchronized (account) {
                        account.setAmount(account.getAmount() + increment);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        Assertions.assertEquals(initialAmount + increment * threadCount, account.getAmount());
    }

    @Test
    public void testConcurrentCreateAndGetLocalPerson() throws InterruptedException {
        int threadCount = THREAD_COUNT;
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    Person person = TestUtils.createRandomPerson(bank);
                    Assertions.assertNotNull(person);
                    Person localPerson = bank.getLocalPerson(person.getPassportNumber());
                    Assertions.assertNotNull(localPerson);
                    Assertions.assertEquals(person.getFirstName(), localPerson.getFirstName());
                    Assertions.assertEquals(person.getLastName(), localPerson.getLastName());
                    Assertions.assertEquals(person.getPassportNumber(), localPerson.getPassportNumber());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}