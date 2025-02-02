package info.kgeorgiy.ja.nazarov.bank.internal;

import java.util.Map;

public abstract class BasePerson implements Person {
    protected final String firstName;
    protected final String lastName;
    protected final String passportNumber;
    protected final Map<String, Account> accounts;

    protected BasePerson(
            String firstName,
            String lastName,
            String passportNumber,
            Map<String, Account> accounts
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportNumber = passportNumber;
        this.accounts = accounts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Account> getAccounts() {
        return accounts;
    }
}
