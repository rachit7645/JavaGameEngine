package entities;

import org.joml.Vector3f;

public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float pitch = 20;

	private Vector3f position;
	private float yaw = 0;
	private float roll = 0;

	private Player player;

	public Camera(Player player) {
		this.player = player;
		this.position = player.getPosition().add(0, 50, 0);
	}

	public void move() {

		calculateZoom();
		if(Inputs.isToMoveCamera()) {
			calculatePitch();
			calculateAngleAroundPlayer();
		}
		float hDistance = calculateHorizontalDistance();
		float vDistance = calculateVerticalDistance();
		calculateCameraPosition(hDistance, vDistance);

	}

	public void invertPitch() {
		pitch = -pitch;
	}

	private void calculateCameraPosition(float hDistance, float vDistance) {

		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (hDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (hDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		if(player.getPosition().y == 0)
			position.y = player.getPosition().y + vDistance;
		else
			position.y = vDistance;
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);

	}

	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}

	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}

	private void calculateZoom() {
		float zoomLevel = Inputs.mouseScrollY * 10f;
		distanceFromPlayer -= zoomLevel;
		Inputs.mouseScrollY = 0;
	}

	private void calculatePitch() {
		float pitchChange = getDY() * 0.1f;
		pitch -= pitchChange;
	}

	private void calculateAngleAroundPlayer() {
		float angleChange = getDX() * 0.3f;
		angleAroundPlayer -= angleChange;
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	private float getDX() {
		return Inputs.getCursorXPos() - Inputs.getOldCursorXPos();
	}

	private float getDY() {
		return Inputs.getCursorYPos() - Inputs.getOldCursorYPos();
	}

}
