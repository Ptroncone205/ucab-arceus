package nintendont.amongspirits;

public class Const {
    //todo
    public static float CAMERA_DEFAULT_PITCH = 0f;
    public static float CAMERA_MIN_PITCH = 1f;
    public static float CAMERA_MAX_PITCH = 80f;
    public static float CAMERA_ZOOM_LEVEL_FACTOR = 0.5f;
    public static float CAMERA_ANGLE_AROUND_PLAYER_FACTOR = 0.2f;
    public static float CAMERA_MIN_DISTANCE_FROM_PLAYER = 4;
    public static float CAMERA_PITCH_FACTOR = 0.3f;
    public static float MAX_STEP_HEIGHT = 30f;

    public static short PF_PLAYER = 1 << 0;
    public static short PF_ITEM   = 1 << 1;
    public static short PF_GROUND = 1 << 2;
}
