package info.kgeorgiy.ja.nazarov.bank.internal;

import java.rmi.*;

public interface Account extends Remote {
    /** Returns account identifier. */
    String getId() throws RemoteException;

    /** Returns amount of money in the account. */
    Money getAmount() throws RemoteException;

    /** Sets amount of money in the account. */
    void setAmount(Money money) throws RemoteException;
}