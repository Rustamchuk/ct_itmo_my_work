package game;

public abstract class DefaultPlayer {
    protected int m = 3;
    protected int n = 3;

    public void setMN(int m, int n) {
        this.m = m;
        this.n = n;
    }
}
