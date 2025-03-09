package twilight.cat;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;
    private Model model;
    private ModelInstance modelInstance;
    private Environment environment;

    private Main game;

    public GameScreen(Main game) {
        this.game = game;
    }


    private boolean[] keys = new boolean[256]; // Array to track key states
    @Override
    public void show() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(5f, 5f, 700f);  // Move camera further back
        camera.lookAt(5f, 5f, 0f);         // Point to the model's origin
        camera.near = 0.1f;
        camera.far = 1000f; // maximumm rendering distanct
        camera.update();

        modelBatch = new ModelBatch();

        // Create environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        // Create a simple cube model
//        ModelBuilder modelBuilder = new ModelBuilder();
//        model = modelBuilder.createBox(2f, 2f, 2f,
//                new Material(ColorAttribute.createDiffuse(0.3f, 0.6f, 0.9f, 1f)),
//                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        model = new G3dModelLoader(new JsonReader()).loadModel(Gdx.files.internal("drop_pack/fish_full_color.g3dj"));
        modelInstance = new ModelInstance(model);
        modelInstance.transform.setToTranslation(0f,0f,0f);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                keys[keycode] = true; // Key is pressed
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                keys[keycode] = false; // Key is released
                return true;
            }
        });
        System.out.println(Gdx.files.internal("drop_pack/fish_full_color.g3dj").file().getAbsolutePath());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        float moveSpeed = 100f * delta;  // Adjust speed for smooth movement
        float rotateSpeed = 30f * delta;  // Adjust speed for smooth rotation

        // Move camera based on key states
        if (keys[Input.Keys.UP]) camera.translate(0f, 0f, -moveSpeed);      // Move forward
        if (keys[Input.Keys.DOWN]) camera.translate(0f, 0f, moveSpeed);     // Move backward
        if (keys[Input.Keys.LEFT]) camera.translate(-moveSpeed, 0f, 0f);    // Move left
        if (keys[Input.Keys.RIGHT]) camera.translate(moveSpeed, 0f, 0f);    // Move right
        if (keys[Input.Keys.TAB]) camera.rotateAround(Vector3.Zero, Vector3.Y, rotateSpeed);  // Rotate view

        camera.update();

        modelBatch.begin(camera);
        modelBatch.render(modelInstance, environment);
        modelBatch.end();
    }

    private void handleInput(float delta) {
//        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            playerPosition.x -= PLAYER_SPEED * delta;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            playerPosition.x += PLAYER_SPEED * delta;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//            playerPosition.y += PLAYER_SPEED * delta;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//            playerPosition.y -= PLAYER_SPEED * delta;
//        }
    }

    @Override
    public void resize(int width, int height) {
//        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        modelBatch.dispose();
        model.dispose();
    }
}
