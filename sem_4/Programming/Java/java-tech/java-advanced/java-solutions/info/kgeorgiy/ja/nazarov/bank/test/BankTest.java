package info.kgeorgiy.ja.nazarov.bank.test;

import info.kgeorgiy.ja.nazarov.bank.internal.*;
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
        Money amount = TestUtils.createMoney();
        Account account = bank.createAccount(id);
        account.setAmount(amount);

        Assertions.assertNotNull(account);
        Assertions.assertEquals(id, account.getId());
        TestUtils.assertEqualMoney(amount, account.getAmount());
    }

    @Test
    public void postGetAccount() throws RemoteException {
        String id = TestUtils.randomString();
        Account created = bank.createAccount(id);
        Account got = bank.getAccount(id);

        Assertions.assertEquals(got, created);

        created.setAmount(TestUtils.createMoney());
        Assertions.assertEquals(got, created);

        got.setAmount(TestUtils.createMoney());
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
        Money amount = TestUtils.createMoney();
        Account account = bank.createAccount(id);
        account.setAmount(amount);

        TestUtils.assertEqualMoney(amount, account.getAmount());
    }

    @Test
    public void checkBalanceAfterMultipleOperations() throws RemoteException {
        String id = TestUtils.randomString();
        Account account = bank.createAccount(id);
        Money initialAmount = TestUtils.createMoney();
        account.setAmount(initialAmount);

        Money deposit = TestUtils.createMoney();
        initialAmount.deposit(deposit);
        account.setAmount(initialAmount);

        Money withdrawal = TestUtils.createMoney(true);
        initialAmount.deposit(withdrawal);
        account.setAmount(initialAmount);

        TestUtils.assertEqualMoney(initialAmount, account.getAmount());
    }

    @Test
    public void setAmountLocal() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        Money amount1 = TestUtils.createMoney();
        Money amount2 = TestUtils.createMoney();
        String accountId = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        Account account = bank.createAccount(accountId);

        account.setAmount(amount1);
        Person local = bank.getLocalPerson(person.getPassportNumber());

        TestUtils.assertEqualMoney(amount1, TestUtils.getAmount(local, accountId));

        local.getAccounts().get(accountId).setAmount(amount2);

        Person remote2 = bank.getRemotePerson(person.getPassportNumber());
        Person local2 = bank.getLocalPerson(person.getPassportNumber());

        TestUtils.assertEqualMoney(amount1, TestUtils.getAmount(person, accountId));
        TestUtils.assertEqualMoney(amount1, TestUtils.getAmount(remote2, accountId));
        TestUtils.assertEqualMoney(amount2, TestUtils.getAmount(local, accountId));
        TestUtils.assertEqualMoney(amount1, TestUtils.getAmount(local2, accountId));
    }

    @Test
    public void checkLocalAndRemoteAccess() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        String accountId = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        Account account = bank.createAccount(accountId);
        Money amount = TestUtils.createMoney();
        account.setAmount(amount);

        Person remotePerson = bank.getRemotePerson(person.getPassportNumber());
        Person localPerson = bank.getLocalPerson(person.getPassportNumber());

        TestUtils.assertEqualMoney(amount, TestUtils.getAmount(remotePerson, accountId));
        TestUtils.assertEqualMoney(amount, TestUtils.getAmount(localPerson, accountId));
    }

    @Test
    public void setAmountRemote() throws RemoteException {
        Person person = TestUtils.createRandomPerson(bank);
        Money amount1 = TestUtils.createMoney();
        Money amount2 = TestUtils.createMoney();
        String accountId = TestUtils.formatAccountId(person.getPassportNumber(), TestUtils.randomString());
        Account account = bank.createAccount(accountId);

        account.setAmount(amount1);
        Person remote = bank.getRemotePerson(person.getPassportNumber());
        Person local = bank.getLocalPerson(person.getPassportNumber());

        Assertions.assertEquals(account.getAmount(), TestUtils.getAmount(remote, accountId));

        account.setAmount(amount2);

        Person remote2 = bank.getRemotePerson(person.getPassportNumber());
        Person local2 = bank.getLocalPerson(person.getPassportNumber());

        TestUtils.assertEqualMoney(amount2, TestUtils.getAmount(remote, accountId));
        TestUtils.assertEqualMoney(amount2, TestUtils.getAmount(remote2, accountId));
        TestUtils.assertEqualMoney(amount1, TestUtils.getAmount(local, accountId));
        TestUtils.assertEqualMoney(amount2, TestUtils.getAmount(local2, accountId));
    }

    @Test
    public void deliverAmount() throws RemoteException {
        String id1 = TestUtils.randomString();
        String id2 = TestUtils.randomString();
        Account account1 = bank.createAccount(id1);
        Account account2 = bank.createAccount(id2);
        Money initialAmount = TestUtils.createMoney();
        account1.setAmount(initialAmount);

        Money transferAmount = TestUtils.createMoney();
        account1.getAmount().deposit(new Money(-transferAmount.getAmount(), -transferAmount.getPenny()));
        account2.getAmount().deposit(new Money(transferAmount.getAmount(), transferAmount.getPenny()));
        initialAmount.deposit(new Money(-transferAmount.getAmount(), -transferAmount.getPenny()));

        TestUtils.assertEqualMoney(initialAmount, account1.getAmount());
        TestUtils.assertEqualMoney(transferAmount, account2.getAmount());
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
        Money initialAmount = TestUtils.createMoney();
        account.setAmount(initialAmount);

        int threadCount = THREAD_COUNT;
        Money increment = TestUtils.createMoney();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                try {
                    synchronized (account) {
                        account.getAmount().deposit(increment);
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

        for (int i = 0; i < threadCount; i++) {
            initialAmount.setMoney(initialAmount.getAmount() + increment.getAmount(),
                    initialAmount.getPenny() + increment.getPenny());
        }

        TestUtils.assertEqualMoney(initialAmount, account.getAmount());
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

    @Test
    public void testMoneyCounter() {
        Money init = new Money(100, 101);
        Money deposit = new Money(100, 99);
        init.deposit(deposit);

        TestUtils.assertEqualMoney(new Money(202, 0), init);
    }
}