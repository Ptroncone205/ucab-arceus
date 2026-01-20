package nintendont.amongspirits.data.satchel;

public class ItemStack {
    private Item item;
    private int count;

    public ItemStack (Item item) {
        this.item = item;
        this.count = 1;
    }

    public ItemStack (Item item, int count) {
        this.item = item;
        this.count = count;
    }

    public Item getItem (){
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCount(){
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increase() {
        count++;
    }

    public void decrease() {
        count--;
    }
}
