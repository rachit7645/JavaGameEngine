package entities;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float pitch = 20;

	private Vector3f position;
	private float yaw = 0;
	private float roll = 0;

	private Vector2f cursorPos;
	private Vector2f oldCursorPos;

	private Player player;

	public Camera(Player player) {
		this.player = player;
		this.position = player.getPosition().add(0, 50, 0);
		this.cursorPos = new Vector2f(0, 0);
	}

	public void move() {

		checkInputs();
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float hDistance = calculateHorizontalDistance();
		float vDistance = calculateVerticalDistance();
		calculateCameraPosition(hDistance, vDistance);

	}

	void checkInputs() {
		oldCursorPos = cursorPos;
		cursorPos = Inputs.getCursorPos();
	}

	private void calculateCameraPosition(float hDistance, float vDistance) {

		Vector3f playerPosition = player.getPosition();
		float playerRotationY = player.getRotY();

		float theta = playerRotationY + angleAroundPlayer;
		float offsetX = (float) (hDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (hDistance * Math.cos(Math.toRadians(theta)));
		position.x = playerPosition.x - offsetX;
		position.z = playerPosition.z - offsetZ;
		position.y = vDistance;
		yaw = 180 - (playerRotationY + angleAroundPlayer);

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

	public void invertPitch() {
		pitch = -pitch;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
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

	private float getDX() {
		return cursorPos.x - oldCursorPos.x;
	}

	private float getDY() {
		return cursorPos.y - oldCursorPos.y;
	}

}
