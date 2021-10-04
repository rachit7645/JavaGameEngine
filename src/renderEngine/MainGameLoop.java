package renderEngine;

import entities.*;
import guis.GUIRenderer;
import guis.GUITexture;
import toolBox.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

	private long window;

	private Loader loader;
	private Random random;

	private List<Light> lights;
	private List<Terrain> terrains;
	private List<Entity> entities;
	private List<GUITexture> guis;

	private MasterRenderer renderer;
	private GUIRenderer guiRenderer;

	private WaterFrameBuffers buffers;

	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	private List<WaterTile> waters;

	private long startTime;
	private long currentTime;
	private long fps = 0;

	private static float delta;

	public MainGameLoop(long window) {
		this.window = window;

		GL.createCapabilities();

		loader = new Loader();
		random = new Random(69420);

		lights = new ArrayList<>();
		terrains = new ArrayList<>();
		entities = new ArrayList<>();
		guis = new ArrayList<>();
		waters = new ArrayList<>();

		renderer = new MasterRenderer(window, loader);
		guiRenderer = new GUIRenderer(loader);
		waterShader = new WaterShader();
		buffers = new WaterFrameBuffers(window);
		waterRenderer = new WaterRenderer(loader, waterShader, MasterRenderer.createProjectionMatrix(), buffers);
	}

	public void loop() {

		// Rawmodels go here

		RawModel model1 = OBJLoader.loadOBjModel("tree", loader);
		RawModel model4 = OBJLoader.loadOBjModel("lowPolyTree", loader);
		RawModel model5 = OBJLoader.loadOBjModel("person", loader);

		// Textures go here
		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree.png"));
		ModelTexture lowPolyTreeTexture = new ModelTexture(loader.loadTexture("lowPolyTree.png"));
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("playerTexture.png"));

		// Terrain texture stuff
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass.png"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud.png"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers.png"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path.png"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap.png"));

		// Transparency things

		// TexturedModels go here

		TexturedModel treeModel = new TexturedModel(model1, treeTexture);
		TexturedModel lowPolyTreeModel = new TexturedModel(model4, lowPolyTreeTexture);
		TexturedModel playerModel = new TexturedModel(model5, playerTexture);

		// Terrains go here

		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmapwater.png");
		Terrain currentTerrain;

		Player player = new Player(playerModel, new Vector3f(100, 0, -50), 0, 0, 0, 1f);
		Camera camera = new Camera(player);

		// Lights go here
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(1.0f, 1.0f, 1.0f)));
		terrains.add(terrain);

		// Generate random positions for trees and stuff

		for (int i = 0; i < 10; i++) {
			if (i % 3 == 0) {

				float treeXPos = random.nextFloat() * 200 - 100;
				float treeZPos = random.nextFloat() * -100;

				currentTerrain = Terrain.getCurrentTerrain(treeXPos, treeZPos, terrains);

				entities.add(new Entity(treeModel, new Vector3f(treeXPos, currentTerrain == null ? 0 : currentTerrain.getHeightOfTerrain(treeXPos, treeZPos),
						treeZPos), 0, 0, 0, 9f));

				float lowPolyTreeXPos = random.nextFloat() * 300 - 100;
				float lowPolyTreeZPos = random.nextFloat() * -150;

				currentTerrain = Terrain.getCurrentTerrain(lowPolyTreeXPos, lowPolyTreeZPos, terrains);

				entities.add(new Entity(lowPolyTreeModel, new Vector3f(lowPolyTreeXPos, currentTerrain == null ? 0 : currentTerrain.getHeightOfTerrain(
						lowPolyTreeXPos, lowPolyTreeZPos), lowPolyTreeZPos), 0, 0, 0, 0.9f));
			}
		}

		entities.add(player);

		// Set this so that the player is not floating

		currentTerrain = Terrain.getCurrentTerrain(player, terrains);
		player.setPosition(new Vector3f(75, 0, -75));
		Vector3f playerPosition = player.getPosition();
		playerPosition.y = currentTerrain.getHeightOfTerrain(playerPosition.x, playerPosition.z);

		waters.add(new WaterTile(75, -75, 9.5f));

		startTime = System.currentTimeMillis();

		// Main loop

		while (!GLFW.glfwWindowShouldClose(window)) {

			handleEvents(player, camera);

			drawWaterFBOS(camera);
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, 15));
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);

			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();

			calculateFPS();
			player.gravity(currentTerrain);
		}
		cleanUp();

	}

	private void drawWaterFBOS(Camera camera) {
		GL11.glEnable(GL11.GL_CLIP_PLANE0);

		buffers.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		renderer.renderScene(entities, terrains, lights, camera,
							 new Vector4f(0, 1, 0, -waters.get(0).getHeight()));
		camera.getPosition().y += distance;
		camera.invertPitch();

		buffers.bindRefractionFrameBuffer();
		renderer.renderScene(entities, terrains, lights, camera,
							 new Vector4f(0, -1, 0, waters.get(0).getHeight()));

		GL11.glDisable(GL11.GL_CLIP_PLANE0);
		buffers.unbindCurrentFrameBuffer(window);
	}

	private void handleEvents(Player player, Camera camera) {

		if (Inputs.isToMovePlayer()) {
			Terrain currentTerrain = Terrain.getCurrentTerrain(player, terrains);
			player.move(Inputs.getKey(), currentTerrain);
			camera.move();
			Inputs.setToMovePlayer(false);
		}

		if (Inputs.isToMoveCamera()) {
			camera.move();
			Inputs.setToMoveCamera(false);
		}

		if (MasterRenderer.getToResize()) {
			waterShader.loadProjectionMatrix(MasterRenderer.createProjectionMatrix());
		}

	}

	private void calculateFPS() {
		currentTime = System.currentTimeMillis();

		if (currentTime >= startTime + 1000) {

			System.out.print("\r");
			System.out.print("FPS: " + fps);
			fps = 0;
			delta = ((currentTime - startTime) / 1000f);
			startTime = currentTime;

		} else {
			fps++;
		}
	}

	private void cleanUp() {

		loader.cleanUp();
		renderer.cleanUp();
		waterShader.cleanUp();
		buffers.cleanUp();
		guiRenderer.cleanUp();
		GL.destroy();

	}

	public static float getDelta() {
		return delta;
	}
}
