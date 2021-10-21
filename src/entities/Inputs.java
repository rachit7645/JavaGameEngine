package entities;

public class Inputs {

	private static boolean toMovePlayer = false;
	private static boolean toMoveCamera = false;
	private static boolean isWPressed = false;
	private static boolean isAPressed = false;
	private static boolean isSPressed = false;
	private static boolean isDPressed = false;
	private static boolean isSpacebarPressed = false;
	private static float cursorXPos = 0;
	private static float cursorYPos = 0;
	private static float oldCursorXPos;
	private static float oldCursorYPos;
	static float mouseScrollY = 0;

	public static boolean isIsWPressed() {
		return isWPressed;
	}

	public static void setIsWPressed(boolean isWPressed) {
		Inputs.isWPressed = isWPressed;
	}

	public static boolean isIsAPressed() {
		return isAPressed;
	}

	public static void setIsAPressed(boolean isAPressed) {
		Inputs.isAPressed = isAPressed;
	}

	public static boolean isIsSPressed() {
		return isSPressed;
	}

	public static void setIsSPressed(boolean isSPressed) {
		Inputs.isSPressed = isSPressed;
	}

	public static boolean isIsDPressed() {
		return isDPressed;
	}

	public static void setIsDPressed(boolean isDPressed) {
		Inputs.isDPressed = isDPressed;
	}

	public static boolean isIsSpacebarPressed() {
		return isSpacebarPressed;
	}

	public static void setIsSpacebarPressed(boolean isSpacebarPressed) {
		Inputs.isSpacebarPressed = isSpacebarPressed;
	}

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
