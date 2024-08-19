package info.kgeorgiy.ja.nazarov.bank.internal;

public abstract class BaseAccount implements Account{
    private final String id;
    private int amount;

    public BaseAccount(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized int getAmount() {
        System.out.println("Getting amount of money for account " + id);
        return amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setAmount(final int amount) {
        System.out.println("Setting amount of money for account " + id);
        this.amount = amount;
    }
}
