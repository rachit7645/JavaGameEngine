package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private static final float RED = 184.0f/255.0f;
    private static final float BLUE = 238.0f/255.0f;
    private static final float GREEN = 213.0f/255.0f;;
    private static boolean toResize;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer = new EntityRenderer(shader);

    private TerrainShader terrainShader = new TerrainShader();
    private TerrainRenderer terrainRenderer = new TerrainRenderer(terrainShader);

    private long window;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<Terrain>();

    public MasterRenderer(long window) {
        this.window = window;

        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, width, height);

        shader.start();
        shader.loadProjectionMatrix(createProjectionMatrix(width, height));
        shader.stop();

        terrainShader.start();
        terrainShader.loadProjectionMatrix(createProjectionMatrix(width,height));
        terrainShader.connectTextureUnits();
        terrainShader.stop();

        GL11.glViewport(0, 0, width.get(0), height.get(0));

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        enableCulling();
    };

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(Light light, Camera camera) {

        if(toResize) {
            IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
            GLFW.glfwGetFramebufferSize(window, width, height);

            shader.loadProjectionMatrix(createProjectionMatrix(width, height));
            terrainShader.loadProjectionMatrix(createProjectionMatrix(width,height));

            GL11.glViewport(0, 0, width.get(0), height.get(0));
            toResize = false;
        }

        prepare();

        shader.start();
        shader.loadSkyColor(RED, GREEN, BLUE);
        shader.loadLight(light);
        shader.loadViewMatrix(camera);

        entityRenderer.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.loadSkyColor(RED, GREEN, BLUE);
        terrainShader.loadLight(light);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();

        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        if(batch != null) {
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public Matrix4f createProjectionMatrix(IntBuffer x, IntBuffer y) {

        float aspectRatio = (float) (x.get(0) / y.get(0));
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE)) / frustum_length);
        projectionMatrix.m23(-1f);
        projectionMatrix.m32(-(2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33(0);

        return projectionMatrix;

    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    public static void setToResize(boolean toResize) {
        MasterRenderer.toResize = toResize;
    }

    public void prepare() {
        GL11.glClearColor(RED, GREEN, BLUE, 1.0f);
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

}
