package info.kgeorgiy.ja.nazarov.bank.internal;

public abstract class BaseAccount implements Account{
    private final String id;
    private final Money money;

    public BaseAccount(String id, Money money) {
        this.id = id;
        this.money = money;
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
    public synchronized Money getAmount() {
        System.out.println("Getting amount of money for account " + id);
        return money;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setAmount(final Money money) {
        System.out.println("Setting amount of money for account " + id);
        this.money.setMoney(money.getAmount(), money.getPenny());
    }
}
