package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GUIRenderer;
import guis.GUITexture;
import modelLoader.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
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

	private static long startTime;
	private static long currentTime;
	private static float delta;
	private static long fps = 0;

	private static boolean toMovePlayer = false;
	private static boolean toMoveCamera = false;
	private static int key;

	public static void loop(long window) {

		GL.createCapabilities();

		Loader loader = new Loader();
		Random random = new Random(676452);

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

		Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap, "heightmapwater.png");
		Terrain currentTerrain;

		Player player = new Player(playerModel, new Vector3f(100,0,-50), 0, 0, 0, 1f);
		Camera camera = new Camera(player);

		// Lights go here

		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(1.0f, 1.0f, 1.0f)));

		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);

		List<Entity> entities = new ArrayList<Entity>();

		// Generate random positions for trees and stuff

		for(int i = 0; i < 20; i++) {
			if(i % 3 == 0){

				float treeXPos = random.nextFloat() * 200 - 100;
				float treeZPos = random.nextFloat() * -100;

				currentTerrain = Terrain.getCurrentTerrain(treeXPos, treeZPos, terrains);

				entities.add(new Entity(treeModel, new Vector3f(treeXPos, currentTerrain == null ? 0 : currentTerrain.getHeightOfTerrain(treeXPos, treeZPos),
						treeZPos), 0, 0, 0, 9f));

				float lowPolyTreeXPos = random.nextFloat() * 300 - 100;
				float lowPolyTreeZPos = random.nextFloat() * -150;

				currentTerrain = Terrain.getCurrentTerrain(lowPolyTreeXPos, lowPolyTreeZPos, terrains);

				entities.add(new Entity(lowPolyTreeModel, new Vector3f(lowPolyTreeXPos, currentTerrain == null ? 0 :  currentTerrain.getHeightOfTerrain(
						lowPolyTreeXPos, lowPolyTreeZPos), lowPolyTreeZPos), 0, 0, 0, 0.9f));
			}
		}

		entities.add(player);

		List<GUITexture> guis = new ArrayList<GUITexture>();

		MasterRenderer renderer = new MasterRenderer(window, loader);
		GUIRenderer guiRenderer = new GUIRenderer(loader);

		// Set this so that the player is not floating

		currentTerrain = Terrain.getCurrentTerrain(player, terrains);
		player.setPosition(new Vector3f(75, 0, -75));
		Vector3f playerPosition = player.getPosition();
		playerPosition.y = currentTerrain.getHeightOfTerrain(playerPosition.x, playerPosition.z);

		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, MasterRenderer.createProjectionMatrix());

		List<WaterTile> waters = new ArrayList<WaterTile>();
		waters.add(new WaterTile(75, -75, 9.5f));

		WaterFrameBuffers fbos = new WaterFrameBuffers(window);
		GUITexture gui = new GUITexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		guis.add(gui);

		startTime = System.currentTimeMillis();

		// Main loop

		while(!GLFW.glfwWindowShouldClose(window)) {

			if(toMovePlayer) {
				currentTerrain = Terrain.getCurrentTerrain(player, terrains);
				player.move(key, currentTerrain);
				camera.move();
				toMovePlayer = false;
			}

			if(toMoveCamera) {
				camera.move();
				toMoveCamera = false;
			}

			if(MasterRenderer.toResize) {
				waterShader.loadProjectionMatrix(MasterRenderer.createProjectionMatrix());
			}

			fbos.bindReflectionFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera);
			fbos.unbindCurrentFrameBuffer(window);

			renderer.renderScene(entities, terrains, lights, camera);
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);

			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();

			currentTime = System.currentTimeMillis();

			if(currentTime >= startTime + 1000) {

				System.out.print("\r");
				System.out.print("FPS: " + fps);
				fps = 0;
				delta = ((currentTime - startTime) / 1000f);
				startTime = currentTime;

			}
			else {
				fps++;
			}
			player.gravity(currentTerrain);
		}
		loader.cleanUp();
		renderer.cleanUp();
		waterShader.cleanUp();
		fbos.cleanUp();
		guiRenderer.cleanUp();

		GL.destroy();
	}

	public static float getDelta() {
		return delta;
	}

	public static void setToMovePlayer(boolean toMovePlayer) {
		MainGameLoop.toMovePlayer = toMovePlayer;
	}

	public static void setKey(int key) {
		MainGameLoop.key = key;
	}

	public static void setToMoveCamera(boolean toMoveCamera) {
		MainGameLoop.toMoveCamera = toMoveCamera;
	}

	public static boolean isToMoveCamera() {
		return toMoveCamera;
	}
}
