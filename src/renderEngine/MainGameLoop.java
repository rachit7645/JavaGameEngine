package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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

		RawModel model1 = OBJLoader.loadOBjModel("tree", loader);
		RawModel model2 = OBJLoader.loadOBjModel("grassModel", loader);
		RawModel model3 = OBJLoader.loadOBjModel("fern", loader);
		RawModel model4 = OBJLoader.loadOBjModel("lowPolyTree", loader);
		RawModel model5 = OBJLoader.loadOBjModel("person", loader);

		ModelTexture treeTexture = new ModelTexture(loader.loadTexture("tree.png"));
		ModelTexture grassTexture = new ModelTexture(loader.loadTexture("grassTexture.png"));
		ModelTexture fernTexture = new ModelTexture(loader.loadTexture("fern.png"));
		ModelTexture lowPolyTreeTexture = new ModelTexture(loader.loadTexture("lowPolyTree.png"));
		ModelTexture playerTexture = new ModelTexture(loader.loadTexture("playerTexture.png"));

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass.png"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud.png"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers.png"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path.png"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap.png"));

		grassTexture.setHasTransparency(true);
		grassTexture.setUseFakeLighting(true);
		fernTexture.setHasTransparency(true);
		fernTexture.setUseFakeLighting(true);

		TexturedModel treeModel = new TexturedModel(model1, treeTexture);
		TexturedModel grassModel = new TexturedModel(model2, grassTexture);
		TexturedModel fernModel = new TexturedModel(model3, fernTexture);
		TexturedModel lowPolyTreeModel = new TexturedModel(model4, lowPolyTreeTexture);
		TexturedModel playerModel = new TexturedModel(model5, playerTexture);

		Terrain terrain = new Terrain(0,-1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1,-1, loader, texturePack, blendMap);
		Terrain terrain3 = new Terrain(0,0, loader, texturePack, blendMap);
		Terrain terrain4 = new Terrain(-1,0, loader, texturePack, blendMap);

		Player player = new Player(playerModel, new Vector3f(100,0,-50), 0, 0, 0, 1f);

		Light light = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));
		Camera camera = new Camera(player);

		List<Entity> entities = new ArrayList<Entity>();

		for(int i = 0; i < 400; i++) {
			if(i % 7 == 0) {
				entities.add(new Entity(grassModel, new Vector3f(random.nextFloat() * 400 - 200, 0,
						random.nextFloat() * -400), 0, 0, 0, 1.8f));
			}
			if(i % 3 == 0){
				entities.add(new Entity(treeModel, new Vector3f(random.nextFloat() * 800 - 400, 0,
						random.nextFloat() * -600), 0, 0, 0, 9f));
				entities.add(new Entity(lowPolyTreeModel, new Vector3f(random.nextFloat() * 900 - 400, 0,
						random.nextFloat() * -700), 0, 0, 0, 0.9f));
			}
			if(i % 5 == 0) {
				entities.add(new Entity(fernModel, new Vector3f(random.nextFloat() * 400 - 200, 0,
						random.nextFloat() * -400), 0, random.nextFloat() * 360, 0, 0.9f));
			}
		}

		entities.add(player);

		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);

		MasterRenderer renderer = new MasterRenderer(window);

		startTime = System.currentTimeMillis();

		while(!GLFW.glfwWindowShouldClose(window)) {

			if(toMovePlayer) {
				player.move(key);
				camera.move();
				toMovePlayer = false;
			}

			if(toMoveCamera) {
				camera.move();
				toMoveCamera = false;
			}

			for(Entity entity : entities)
				renderer.processEntity(entity);
			for(Terrain currentTerrain : terrains) {
				renderer.processTerrain(currentTerrain);
			}

			renderer.render(light, camera);

			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();

			currentTime = System.currentTimeMillis();

			if(currentTime >= startTime + 1000) {
				System.out.println("FPS: " + fps);
				fps = 0;
				delta = ((currentTime - startTime) / 1000f);
				startTime = currentTime;
			}else{
				fps++;
			}
			player.gravity();
		}
		loader.cleanUp();
		renderer.cleanUp();

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
