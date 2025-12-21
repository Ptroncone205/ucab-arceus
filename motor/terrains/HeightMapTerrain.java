package com.motor.terrains;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btHeightfieldTerrainShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;
import com.badlogic.gdx.utils.Disposable;

import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;

public class HeightMapTerrain implements Disposable{
    private Model model;
    private ModelInstance modelInstance;
    private btRigidBody body;
    private btHeightfieldTerrainShape terrainShape;
    private int width, length;
    private float maxHeight;
    private float[] heightData;
    private HeightField field;
    private final float scale;

    private Texture texture;

    public HeightMapTerrain(String heightMapPath, float maxHeight, float scale) {
        this.maxHeight = maxHeight;
        Pixmap pixmap = new Pixmap(Gdx.files.internal(heightMapPath));
        width = pixmap.getWidth();
        length = pixmap.getHeight();
        heightData = new float[width * length];
        this.scale = scale;

        // Obtener alturas porque sino el heightfield no me funciona no quiero saber por que
        for (int z = 0; z < length; z++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixmap.getPixel(x, z);
                int r = (pixel & 0xff000000) >>> 24;
                float height = (r / 255f) * maxHeight;
                heightData[z * width + x] = height;
            }
        }
        pixmap.dispose();
        
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(heightData.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer heightBuffer = byteBuffer.asFloatBuffer();
        heightBuffer.put(heightData);
        heightBuffer.position(0);

        terrainShape = new btHeightfieldTerrainShape(
            width, 
            length, 
            heightBuffer, 
            1f, 
            0f, 
            maxHeight, 
            1, 
            false);
        
        model = createModel();
        modelInstance = new ModelInstance(model);
        
        body = createRigidBody();
        body.setRestitution(0.5f);
        body.setFriction(1f);

        terrainShape.setLocalScaling(new Vector3(scale, 1f, scale));
    }

    private Model createModel() {
        ModelBuilder modelBuilder = new ModelBuilder();

        
        field = new HeightField(true, heightData, width, length, true,
            Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		field.corner00.set(-(width - 1) * scale/2f, -maxHeight * scale/2f, -(length - 1) * scale/2f);
		field.corner10.set( (width - 1) * scale/2f, -maxHeight * scale/2f, -(length - 1) * scale/2f);
		field.corner01.set(-(width - 1) * scale/2f, -maxHeight * scale/2f,  (length - 1) * scale/2f);
		field.corner11.set( (width - 1) * scale/2f, -maxHeight * scale/2f,  (length - 1) * scale/2f);
		field.magnitude.set(0f, 1f, 0f);
		field.update();
        
        Material material = new Material();
        texture = new Texture(Gdx.files.internal("textures/grass1.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        material.set(PBRTextureAttribute.createBaseColorTexture(texture));
        
        modelBuilder.begin();
        modelBuilder.part("terrain", field.mesh, GL20.GL_TRIANGLES, material);
        return modelBuilder.end();
    }

    private btRigidBody createRigidBody() {
        btDefaultMotionState motionState = new btDefaultMotionState();
        float mass = 0f;
        Vector3 localInertia = new Vector3(0, 0, 0);
        return new btRigidBody(mass, motionState, terrainShape, localInertia);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public btRigidBody getRigidBody() {
        return body;
    }

    @Override
    public void dispose() {
        texture.dispose();
        body.dispose();
        terrainShape.dispose();
        model.dispose();
    }
}