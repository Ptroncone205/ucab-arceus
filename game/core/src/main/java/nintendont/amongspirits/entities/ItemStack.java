package nintendont.amongspirits.entities;

import nintendont.amongspirits.entities.items.Item;

public class ItemStack {
    public Item item;
    public int count;

    public ItemStack (Item item){
        this.item = item;
        this.count = 1;
    }

    public ItemStack (Item item, int count){
        this.item = item;
        this.count = count;
    }

    public Item getItem (){
        return item;
    }

    public int getCount(){
        return count;
    }

    public void useItem(){
        //item.useItem();
        count--;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
