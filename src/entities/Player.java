package entities;

import models.TexturedModel;
import org.joml.Vector3f;
import renderEngine.MainGameLoop;
import terrains.Terrain;

public class Player extends Entity implements Keys{

	private static final float RUN_SPEED = 5;
	private static final float TURN_SPEED = 15;
	private static final float GRAVITY = -0.42f;
	private static final float JUMP_HEIGHT = 14;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float verticalSpeed = 0;

	private boolean toMove = true;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(int key, Terrain currentTerrain) {

		if (toMove) {
			checkInput(key, currentTerrain);

			super.increaseRotation(0, currentTurnSpeed * MainGameLoop.getDelta(), 0);

			float distance = currentSpeed * MainGameLoop.getDelta();
			float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
			float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
			super.increasePosition(dx, 0, dz);
			this.gravity(currentTerrain);
			toMove = false;
		}else{
			toMove = true;
		}
	}

	private void checkInput(int key,Terrain currentTerrain) {

		if (key == KEY_W)
			this.currentSpeed = RUN_SPEED;
		else if (key == KEY_S)
			this.currentSpeed = -RUN_SPEED;
		else
			this.currentSpeed = 0;

		if(key == KEY_D)
			this.currentTurnSpeed = -TURN_SPEED;
		else if (key == KEY_A)
			this.currentTurnSpeed = TURN_SPEED;
		else
			this.currentTurnSpeed = 0;

		if(key == KEY_SPACEBAR)
			jump(currentTerrain);

	}

	private void jump(Terrain currentTerrain) {

		float terrainHeight;

		if(currentTerrain == null)
			terrainHeight = 0;
		else
			terrainHeight = currentTerrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

		if(super.getPosition().y <= terrainHeight)
			verticalSpeed = JUMP_HEIGHT;
		super.increasePosition(0, verticalSpeed, 0);
	}

	public void gravity(Terrain currentTerrain) {
		float terrainHeight;
		verticalSpeed = 0;

		if(currentTerrain == null)
			terrainHeight = 0;
		else
			terrainHeight = currentTerrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

		if(super.getPosition().y > terrainHeight)
			super.increasePosition(0, GRAVITY * MainGameLoop.getDelta(), 0);
		if(super.getPosition().y < terrainHeight)
			super.getPosition().y = terrainHeight;
	}
}
