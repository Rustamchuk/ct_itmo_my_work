package info.kgeorgiy.ja.nazarov.bank.internal;

public class Money {
    private long amount;
    private int penny;

    public Money(long amount, int penny) {
        setMoney(amount, penny);
    }

    public void setMoney(long amount, int penny) {
        this.amount = amount + penny / 100;
        this.penny = penny % 100;
    }

    public void deposit(final Money money) {
        this.penny += money.penny;
        this.amount += money.amount + this.penny / 100;
        this.penny %= 100;
    }

    public long getAmount() {
        return amount;
    }

    public int getPenny() {
        return penny;
    }
}
