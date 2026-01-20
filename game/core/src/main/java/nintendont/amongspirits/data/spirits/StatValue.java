package nintendont.amongspirits.data.spirits;

public class StatValue {
    protected int current;

    public StatValue() {
    }

    public StatValue(int current) {
        this.current = current;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current <= 0) {
            this.current = 0;
        } else {
            this.current = current;
        }
    }
}
