package entities;

public class Inputs {

	private static boolean toMovePlayer = false;
	private static boolean toMoveCamera = false;
	private static int key;

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
}
