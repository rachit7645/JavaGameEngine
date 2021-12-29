package entities;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

public class Inputs {

	private static long window;
	static float mouseScrollY = 0;
	private static boolean toMoveCamera = false;

	public static boolean isToMoveCamera() {
		return toMoveCamera;
	}

	public static void setToMoveCamera(boolean toMoveCamera) {
		Inputs.toMoveCamera = toMoveCamera;
	}

	public static void setWindow(long window) {
		Inputs.window = window;
	}

	public static boolean isPressed(int key) {
		int isPressed = GLFW.glfwGetKey(window, key);
		return isPressed > 0;
	}

	public static Vector2f getCursorPos() {

		DoubleBuffer cursorXPos = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer cursorYPos = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(window, cursorXPos, cursorYPos);
		return new Vector2f( (float) cursorXPos.get(0), (float) cursorYPos.get(0) );

	}

	public static void setMouseScrollY(float yOffset) {
		mouseScrollY = yOffset;
	}
}
