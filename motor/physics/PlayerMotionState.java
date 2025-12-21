package com.motor.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class PlayerMotionState extends MotionState {

    public PlayerMotionState(Matrix4 transform) {
        super(transform);
    }

    @Override
    public void setWorldTransform(Matrix4 worldTrans) {
        Vector3 pos = new Vector3();
        worldTrans.getTranslation(pos);
        transform.setTranslation(pos);
    }
    
}
