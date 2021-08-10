package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyBoxRenderer;
import skybox.SkyboxShader;
import terrains.Terrain;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MasterRenderer {

	private long window;

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;

	private static final float RED = 184.0f/255.0f;
	private static final float BLUE = 238.0f/255.0f;
	private static final float GREEN = 213.0f/255.0f;

	public static boolean toResize;

	private StaticShader shader = new StaticShader();
	private EntityRenderer entityRenderer = new EntityRenderer(shader);

	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer = new TerrainRenderer(terrainShader);

	private SkyboxShader skyboxShader = new SkyboxShader();
	private SkyBoxRenderer skyBoxRenderer;

	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();

	public MasterRenderer(long window, Loader loader) {
		this.window = window;

		skyBoxRenderer = new SkyBoxRenderer(loader, skyboxShader);

		IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetFramebufferSize(window, width, height);

		Matrix4f projectionMatrix = createProjectionMatrix();

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();

		terrainShader.start();
		terrainShader.loadProjectionMatrix(projectionMatrix);
		terrainShader.connectTextureUnits();
		terrainShader.stop();

		skyboxShader.start();
		skyboxShader.loadProjectionMatrix(projectionMatrix);
		skyboxShader.stop();

		GL11.glViewport(0, 0, width.get(0), height.get(0));

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL14.GL_MULTISAMPLE);
		enableCulling();
	};

	public void renderScene(List<Entity> entities, List<Terrain> terrains,
							List<Light> lights, Camera camera, Vector4f clipPlane) {
		for(Entity entity : entities)
			processEntity(entity);
		for(Terrain cTerrain : terrains) {
			processTerrain(cTerrain);
		}

		render(lights, camera, clipPlane);
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {

		if(toResize) {
			IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
			GLFW.glfwGetFramebufferSize(window, width, height);

			Matrix4f projectionMatrix = createProjectionMatrix();

			shader.loadProjectionMatrix(projectionMatrix);
			terrainShader.loadProjectionMatrix(projectionMatrix);
			skyboxShader.loadProjectionMatrix(projectionMatrix);

			GL11.glViewport(0, 0, width.get(0), height.get(0));
			toResize = false;
		}

		prepare();

		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);

		entityRenderer.render(entities);
		shader.stop();

		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);

		terrainRenderer.render(terrains);
		terrainShader.stop();

		skyBoxRenderer.render(camera, RED, GREEN, BLUE);

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

	public static Matrix4f createProjectionMatrix() {

		return new Matrix4f().perspective(FOV, 16f / 9f, NEAR_PLANE, FAR_PLANE);

	}

	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	public static void setToResize(boolean toResize) {
		MasterRenderer.toResize = toResize;
	}

	public static boolean getToResize() {
		return toResize;
	}

	public void prepare() {
		GL11.glClearColor(RED, GREEN, BLUE, 1.0f);
		GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

}
