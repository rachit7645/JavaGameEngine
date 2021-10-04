package entities;

public class Inputs {

	private static boolean toMovePlayer = false;
	private static boolean toMoveCamera = false;
	private static int key;
	private static float cursorXPos = 0;
	private static float cursorYPos = 0;
	private static float oldCursorXPos;
	private static float oldCursorYPos;
	static float mouseScrollY = 0;

	public static boolean isToMovePlayer() {
		return toMovePlayer;
	}

	public static void setToMovePlayer(boolean toMovePlayer) {
		Inputs.toMovePlayer = toMovePlayer;
	}

	public static boolean isToMoveCamera() {
		return toMoveCamera;
	}

	public static void setToMoveCamera(boolean toMoveCamera) {
		Inputs.toMoveCamera = toMoveCamera;
	}

	public static int getKey() {
		return key;
	}

	public static void setKey(int key) {
		Inputs.key = key;
	}

	public static float getCursorXPos() {
		return cursorXPos;
	}

	public static void setCursorXPos(float cursorXPos) {
		Inputs.cursorXPos = cursorXPos;
	}

	public static float getCursorYPos() {
		return cursorYPos;
	}

	public static void setCursorYPos(float cursorYPos) {
		Inputs.cursorYPos = cursorYPos;
	}

	public static float getOldCursorXPos() {
		return oldCursorXPos;
	}

	public static void setOldCursorXPos(float oldCursorXPos) {
		Inputs.oldCursorXPos = oldCursorXPos;
	}

	public static float getOldCursorYPos() {
		return oldCursorYPos;
	}

	public static void setOldCursorYPos(float oldCursorYPos) {
		Inputs.oldCursorYPos = oldCursorYPos;
	}

	public static void setMouseScrollY(float mouseScrollY) {
		Inputs.mouseScrollY = mouseScrollY;
	}
}
