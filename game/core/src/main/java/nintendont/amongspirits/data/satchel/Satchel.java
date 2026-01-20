package nintendont.amongspirits.data.satchel;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;

public class Satchel {
    private ArrayList<ItemStack> items;
    private final int SLOTS = 20;

    public Satchel (){
        this.items = new ArrayList<>();
    }

    public ArrayList<ItemStack> getItems(){
        return items;
    }

    public void setItems(ArrayList<ItemStack> items){
        this.items = items;
    }

    public boolean addItem(Item item){
        if (items.size() >= SLOTS){
            return false;
        }

        for (int i = 0; i < items.size(); i++){
            if ((items.get(i).getItem()).getName().equals(item.getName())){
                items.get(i).increase();
                return true;
            }
        }
        items.add(new ItemStack(item));
        return true;
    }

    public void removeItem(ItemStack item){
        items.remove(item);
    }

    public void useItem(ItemStack item){
        item.decrease();
        if (item.getCount() == 0){
            items.remove(item);
        }
    }

    public ItemStack getRandomItem() {
        return items.get(MathUtils.random(0, items.size() - 1));
    }
}
