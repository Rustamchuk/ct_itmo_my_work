package info.kgeorgiy.ja.nazarov.bank.internal;

import java.util.Map;

public class LocalPerson extends BasePerson {
    public LocalPerson(
            String firstName,
            String lastName,
            String passportNumber,
            Map<String, Account> accounts
    ) {
        super(firstName, lastName, passportNumber, accounts);
    }
}
