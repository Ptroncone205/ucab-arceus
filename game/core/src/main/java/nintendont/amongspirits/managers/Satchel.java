package nintendont.amongspirits.managers;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.items.Item;

public class Satchel {
    private ArrayList<ItemStack> items;
    private final int SLOTS = 20;

    public Satchel (){
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item){
        if (items.size() >= SLOTS){
            return false;
        }

        for (int i = 0; i < items.size(); i++){
            if ((items.get(i).item).name.equals(item.name)){
                items.get(i).count++;
                return true;
            }
        }
        items.add(new ItemStack(item));
        return true;
    }

    public void removeItem(ItemStack item){
        items.remove(item);
    }

    public ArrayList<ItemStack> getItems(){
        return items;
    }

    public void useItem(ItemStack item){
        item.useItem();
        if (item.count == 0){
            items.remove(item);
        }
    }

    public void setItems(ArrayList<ItemStack> items){
        this.items = items;
    }

    public ItemStack getRandomItem() {
        return items.get(MathUtils.random(0, items.size() - 1));
    }

    public boolean hasYumenjiang() {
        return items.stream().anyMatch(i -> i.count == 1);
    }
}
