package nintendont.amongspirits.data.spirits;

public class SpiritMove{
    private String id, name, description;
    private SpiritMoveCategory category;
    private int basePower, baseAccuracy, basePowerPoints, baseProbability;

    public SpiritMove(String id, String name, String description, SpiritMoveCategory category, int basePower, int baseAccuracy, int basePowerPoints, int baseProbability) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.basePower = basePower;
        this.baseAccuracy = baseAccuracy;
        this.basePowerPoints = basePowerPoints;
        this.baseProbability = baseProbability;
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public SpiritMoveCategory getCategory(){
        return category;
    }

    public int getBasePower(){
        return basePower;
    }

    public int getBaseAccuracy(){
        return baseAccuracy;
    }

    public int getBasePowerPoints(){
        return basePowerPoints;
    }

    public int getBaseProbability(){
        return baseProbability;
    }
}
