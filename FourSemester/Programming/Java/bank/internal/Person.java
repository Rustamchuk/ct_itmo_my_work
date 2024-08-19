package info.kgeorgiy.ja.nazarov.bank.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.util.Map;

public interface Person extends Remote, Serializable {
    /**
     * Gives person name.
     * @return name type String.
     */
    String getFirstName() throws RemoteException;

    /**
     * Gives person surname.
     * @return surname type String.
     */
    String getLastName() throws RemoteException;

    /**
     * Gives person passport.
     * @return passport type String.
     */
    String getPassportNumber() throws RemoteException;

    /**
     * Gives person accounts.
     * @return accounts type map.
     */
    Map<String, Account> getAccounts() throws RemoteException;
}
