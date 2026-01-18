package nintendont.amongspirits.physics;

public class PhysicsLayers {
    public static final short NONE = 0;
    public static final short PLAYER = 1 << 0;  // 1
    public static final short ENEMY   = 1 << 1;  // 2
    public static final short WORLD  = 1 << 2;  // 4
    public static final short HITBOX = 1 << 3;  // 8

    // You can also create combined masks
    public static final short ALL = -1; // All bits set to 1
}
