package entities;

import org.joml.Vector3f;
import renderEngine.MainGameLoop;

public class Camera implements Keys{

	private static float cursorXPos = 0;
	private static float cursorYPos = 0;
	private static float oldCursorXPos;
	private static float oldCursorYPos;
	private static float mouseScrollY = 0;

	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	private float pitch = 20;

	private static boolean isMouseButton1Down = false;

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
		calculatePitch();
		if(MainGameLoop.isToMoveCamera())
			calculateAngleAroundPlayer();
		float hDistance = calculateHorizontalDistance();
		float vDistance = calculateVerticalDistance();
		calculateCameraPosition(hDistance, vDistance);

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
		float zoomLevel = mouseScrollY * 10f;
		distanceFromPlayer -= zoomLevel;
		mouseScrollY = 0;
	}

	private void calculatePitch() {
		if(isMouseButton1Down) {
			float pitchChange = getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}

	private void calculateAngleAroundPlayer() {
		float angleChange = getDX() * 0.3f;
		angleAroundPlayer -= angleChange;
	}

	public static float getCursorXPos() {
		return cursorXPos;
	}

	public static void setCursorXPos(float cursorXPos) {
		Camera.cursorXPos = cursorXPos;
	}

	public static float getCursorYPos() {
		return cursorYPos;
	}

	public static void setCursorYPos(float cursorYPos) {
		Camera.cursorYPos = cursorYPos;
	}

	public static float getOldCursorXPos() {
		return oldCursorXPos;
	}

	public static void setOldCursorXPos(float oldCursorXPos) {
		Camera.oldCursorXPos = oldCursorXPos;
	}

	public static float getOldCursorYPos() {
		return oldCursorYPos;
	}

	public static void setOldCursorYPos(float oldCursorYPos) {
		Camera.oldCursorYPos = oldCursorYPos;
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
		return getCursorXPos() - getOldCursorXPos();
	}

	private float getDY() {
		return getCursorYPos() - getOldCursorYPos();
	}

	public static void setMouseScrollY(float mouseScrollY) {
		Camera.mouseScrollY = mouseScrollY;
	}

	public static void setIsMouseButton1Down(boolean isMouseButton1Down) {
		Camera.isMouseButton1Down = isMouseButton1Down;
	}
}
