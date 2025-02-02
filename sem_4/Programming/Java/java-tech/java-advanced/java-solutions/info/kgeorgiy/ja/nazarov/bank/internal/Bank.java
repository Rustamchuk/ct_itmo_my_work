package info.kgeorgiy.ja.nazarov.bank.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
    /**
     * Creates a new account with specified identifier if it does not already exist.
     * @param id account id
     * @return created or existing account.
     */
    Account createAccount(String id) throws RemoteException;

    /**
     * Returns account by identifier.
     * @param id account id
     * @return account with specified identifier or {@code null} if such account does not exist.
     */
    Account getAccount(String id) throws RemoteException;

    /**
     * Creates person by first, last name and passport.
     * @param firstName name
     * @param lastName surname
     * @param passportNumber passport of person
     * @return person.
     */
    Person createPerson(String firstName, String lastName, String passportNumber) throws RemoteException;

    /**
     * Gives remote kind of person.
     * @param passportNumber passport of person
     * @return remote person.
     */
    Person getRemotePerson(String passportNumber) throws RemoteException;

    /**
     * Gives local kind of person.
     * @param passportNumber passport of person
     * @return local person.
     */
    Person getLocalPerson(String passportNumber) throws RemoteException;
}
