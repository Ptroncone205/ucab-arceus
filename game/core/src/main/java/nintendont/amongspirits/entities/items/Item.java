package nintendont.amongspirits.entities.items;

import java.time.format.TextStyle;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import nintendont.amongspirits.Const;
import nintendont.amongspirits.entities.Entity;
import nintendont.amongspirits.physics.MotionState;

import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.scene.Scene;
// TODO entity
public class Item extends Entity{
    public static final float PICKUP_DIST = 3f;

    public final int id;
    public final String name;
    public final String desc;
    public final boolean isMaterial;
    public Texture icon;
    // sprite para el inventario(?)
    public Scene scene;
    public Vector3 pos;

    public Item (int id, String name, String desc, boolean isMaterial){
        this.icon = null;
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.isMaterial = isMaterial;
    }

    public Item(JsonValue data){
        this.id = data.getInt("id");
        this.name = data.getString("name");
        this.desc = data.getString("desc");
        this.isMaterial = data.getBoolean("isMaterial");
        Pixmap tmp = new Pixmap(Gdx.files.internal(data.getString("icon")));
        this.icon = new Texture(tmp);
        tmp.dispose();
        System.out.println(this.name + " created: " + Gdx.files.internal(data.getString("icon")));
    }

    public void create(Vector3 pos, float width, float height, float depth){
        this.pos = pos;
        ModelBuilder mb = new ModelBuilder();
        mb.begin();

        Material material = new Material();
        material.set(PBRColorAttribute.createBaseColorFactor(Color.RED));
        MeshPartBuilder builder = mb.part(name + id, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        BoxShapeBuilder.build(builder, pos.x, pos.y, pos.z, width, height, depth);

        modelInstance = new ModelInstance(mb.end());
        scene = new Scene(modelInstance);

        btBoxShape shape = new btBoxShape(new Vector3(width/2, height/2, depth/2));
        btRigidBody.btRigidBodyConstructionInfo info = new btRigidBody.btRigidBodyConstructionInfo(0, null, shape, Vector3.Zero);
        body = new btRigidBody(info);
        body.setWorldTransform(modelInstance.transform);
        body.setUserValue(Const.PF_ITEM);

    }

    public Scene getScene(){
        return scene;
    }

    public String getName(){
        return name;
    }

    public boolean isMaterial() {
        return isMaterial;
    }

    public btRigidBody getBody(){
        return body;
    }

    public int getID() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public Texture getIcon() {
        return icon;
    }

    public void useItem(Entity target){
        System.out.println("item used on" + target.toString());
    }

    @Override
    public void dispose(){
        super.dispose();
        this.icon.dispose();
    }
}
