package nintendont.amongspirits.data.spirits;

public class RangedStatValue extends StatValue {
    private int max;

    public RangedStatValue(){}
    public RangedStatValue(int current, int max) {
        super(current);
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    @Override
    public void setCurrent(int current) {
        if (current >= max) {
            this.current = max;
        } else {
            super.setCurrent(current);
        }
    }

    public float getRatio() {
        return (float) current / max;
    }

    public boolean isEmpty() {
        return current <= 0;
    }

    public boolean isFull() {
        return current >= max;
    }
}
