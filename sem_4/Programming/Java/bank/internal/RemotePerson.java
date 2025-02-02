package info.kgeorgiy.ja.nazarov.bank.internal;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Map;

public class RemotePerson extends BasePerson {
    public RemotePerson(
            String firstName,
            String lastName,
            String passportNumber,
            Map<String, Account> accounts,
            final int port
    ) throws RemoteException {
        super(firstName, lastName, passportNumber, accounts);
        UnicastRemoteObject.exportObject(this, port);
    }
}
