package info.kgeorgiy.ja.nazarov.bank.internal;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteAccount extends BaseAccount {
    public RemoteAccount(
            final String id,
            final Money money,
            final int port
    ) throws RemoteException {
        super(id, money);
        UnicastRemoteObject.exportObject(this, port);
    }
}
