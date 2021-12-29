package entities;

import models.TexturedModel;
import org.joml.Vector3f;
import renderEngine.MainGameLoop;
import terrains.Terrain;

public class Player extends Entity {

	private static final float RUN_SPEED = 2;
	private static final float TURN_SPEED = 5;
	private static final float JUMP_SPEED = 1f;
	private static final float GRAVITY = -0.42f;
	private static final float JUMP_HEIGHT = 9;

	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float verticalSpeed = 0;

	private boolean isJumpFinished = false;

	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}

	public void move(Terrain currentTerrain) {

		checkInput(currentTerrain);

		super.increaseRotation(0, currentTurnSpeed * MainGameLoop.getDelta(), 0);

		float distance = currentSpeed * MainGameLoop.getDelta();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);
		gravity(currentTerrain);

	}

	private void checkInput(Terrain currentTerrain) {

		if (Inputs.isIsWPressed())
			this.currentSpeed = RUN_SPEED;
		else if (Inputs.isIsSPressed())
			this.currentSpeed = -RUN_SPEED;
		else
			this.currentSpeed = 0;

		if (Inputs.isIsDPressed())
			this.currentTurnSpeed = -TURN_SPEED;
		else if (Inputs.isIsAPressed())
			this.currentTurnSpeed = TURN_SPEED;
		else
			this.currentTurnSpeed = 0;

		if (Inputs.isIsSpacebarPressed())
			jump(currentTerrain);
		else
			isJumpFinished = false;

	}

	private void jump(Terrain currentTerrain) {

		if (isJumpFinished)
			return;

		float terrainHeight;
		if (currentTerrain == null) terrainHeight = 0;
		else terrainHeight = currentTerrain.getHeightOfTerrain(getPosition().x, getPosition().z);

		if (super.getPosition().y > terrainHeight + JUMP_HEIGHT) isJumpFinished = true;
		else super.increasePosition(0, verticalSpeed = JUMP_SPEED, 0);

	}

	public void gravity(Terrain currentTerrain) {
		float terrainHeight;

		if (currentTerrain == null)
			terrainHeight = 0;
		else
			terrainHeight = currentTerrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);

		if (super.getPosition().y > terrainHeight)
			super.increasePosition(0, GRAVITY * MainGameLoop.getDelta(), 0);
		if (super.getPosition().y < terrainHeight)
			super.getPosition().y = terrainHeight;
	}
}
