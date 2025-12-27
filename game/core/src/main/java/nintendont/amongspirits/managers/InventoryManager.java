package nintendont.amongspirits.managers;

import java.util.ArrayList;

import nintendont.amongspirits.entities.ItemStack;
import nintendont.amongspirits.entities.items.Item;

public class InventoryManager {
    private ArrayList<ItemStack> items;
    private int slots = 20;

    public InventoryManager (){
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item){
        if (items.size() >= slots){
            return false;
        }

        // for (int i = 0; i < items.size(); i++){
        //     if ((items.get(i).item).getClass().equals(item.getClass())){
        //         items.get(i).count++;
        //         return true;
        //     }
        // }
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
}
