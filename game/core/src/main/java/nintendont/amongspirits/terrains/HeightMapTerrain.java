package nintendont.amongspirits.terrains;

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
import nintendont.amongspirits.Const;
import nintendont.amongspirits.terrains.attributes.TerrainFloatAttribute;
import nintendont.amongspirits.terrains.attributes.TerrainMaterialAttribute;
import nintendont.amongspirits.terrains.attributes.TerrainTextureAttribute;

import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
/**Tenemos aqui presente una obra magna de la ingenieria,
 * Aristoteles decia que era la divina comedia de la programacion
 * Fue forjado con el sudor y sangre de un lobo solitario, despues de sufrir
 * por incontables horas ante la inexistente documentacion de bullet
 * quiero agradecer a Xoppa, por haber creado una clase que se encarga de conseguir
 * UV y normals del heightmap, y gracias por el algoritmo de obtener los colores de los pixeles
 * a pesar de que lo tuve que modificar (gracias gemini) y sacar de la clase porque sino no funciona
 * gracias a jamestkhan por hacer la generacion del modelo
 * y por encima de todo gracias a kirby luka gato2 le miau miau por existir
 */
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
        this.scale = scale;
        Pixmap pixmap = new Pixmap(Gdx.files.internal(heightMapPath));
        width = pixmap.getWidth();
        length = pixmap.getHeight();
        heightData = new float[width * length];

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

        createModel();
        modelInstance = new ModelInstance(model);

        body = createRigidBody();
        body.setRestitution(0.5f);
        body.setFriction(1f);
        body.setUserValue(Const.PF_GROUND);

        terrainShape.setLocalScaling(new Vector3(scale, 1f, scale));
    }

    private void createModel() {
        ModelBuilder modelBuilder = new ModelBuilder();


        field = new HeightField(true, heightData, width, length, true,
            Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		field.corner00.set(-(width - 1) * scale/2f, -maxHeight * 1/2f, -(length - 1) * scale/2f);
		field.corner10.set( (width - 1) * scale/2f, -maxHeight * 1/2f, -(length - 1) * scale/2f);
		field.corner01.set(-(width - 1) * scale/2f, -maxHeight * 1/2f,  (length - 1) * scale/2f);
		field.corner11.set( (width - 1) * scale/2f, -maxHeight * 1/2f,  (length - 1) * scale/2f);
		field.magnitude.set(0f, 1f, 0f);
		field.update();

        modelBuilder.begin();

        // model = modelBuilder.end();
        // modelInstance = new ModelInstance(model);

        // material atrtibutes setup
        Material material = new Material();//modelInstance.materials.get(0);

        TerrainTextureAttribute baseAttribute = TerrainTextureAttribute.createDiffuseBase(getMipMapTexture("textures/grass1.png"));
        TerrainTextureAttribute terrainSlopeTexture = TerrainTextureAttribute.createDiffuseSlope(getMipMapTexture("textures/cliff.png"));
        TerrainTextureAttribute terrainHeightTexture = TerrainTextureAttribute.createDiffuseHeight(getMipMapTexture("textures/dirt.png"));

        baseAttribute.scaleU = 5f;
        baseAttribute.scaleV = 5f;
        TerrainFloatAttribute slope = TerrainFloatAttribute.createMinSlope(0.85f);

        TerrainMaterial terrainMaterial = new TerrainMaterial();
        terrainMaterial.set(baseAttribute);
        terrainMaterial.set(terrainSlopeTexture);
        terrainMaterial.set(terrainHeightTexture);
        terrainMaterial.set(slope);

        material.set(TerrainMaterialAttribute.createTerrainMaterialAttribute(terrainMaterial));

        texture = new Texture(Gdx.files.internal("textures/grass1.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        material.set(PBRTextureAttribute.createBaseColorTexture(texture));

        modelBuilder.part("terrain", field.mesh, GL20.GL_TRIANGLES, material);

        model = modelBuilder.end();
        modelInstance = new ModelInstance(model);


    }

    private Texture getMipMapTexture(String path) {
        Texture texture = new Texture(Gdx.files.internal(path), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        return texture;
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
        body.dispose();
        terrainShape.dispose();
        model.dispose();
        texture.dispose();
    }
}
